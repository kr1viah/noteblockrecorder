package org.kr1v.noteblockrecorder.client;

import it.unimi.dsi.fastutil.bytes.ByteArrays;
import net.minecraft.sound.SoundEvent;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SaveAsNbs {
	static ArrayList<String> customInstruments = new ArrayList<>();

	/**
	 * Write out the given quantized batches to a .nbs file.
	 * @param outFile   the target .nbs file
	 * @param quantized list of batches with their step index (tick) and notes
	 * @param quantumMs the duration of one tick in milliseconds
	 * @throws IOException on IO error
	 */
	public static void write(File outFile, List<QuantizedBatch> quantized, long quantumMs) throws IOException {
		if (quantized.isEmpty()) return;

		int lengthTicks = quantized.stream()
				.mapToInt(q -> (int) q.step)
				.max().orElse(0) + 1;

		int layerCount = quantized.stream()
				.mapToInt(q -> q.notes.size())
				.max().orElse(0) + 2;

		int tempoScaled = (int) Math.round((1000.0 / quantumMs) * 100);

		try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(outFile))) {
			writeLEShort(dout, (short) 0);
			dout.writeByte(5);
			dout.writeByte(16);
			writeLEShort(dout, (short) lengthTicks);
			writeLEShort(dout, (short) layerCount);

			writeString(dout, "");
			writeString(dout, "");
			writeString(dout, "");
			writeString(dout, "");

			writeLEShort(dout, (short) tempoScaled);
			dout.writeByte(105);
			dout.writeByte(105);
			dout.writeByte(4);

			writeLEInt(dout, 1);
			writeLEInt(dout, 1);
			writeLEInt(dout, 1);
			writeLEInt(dout, 1);
			writeLEInt(dout, 1);

			writeString(dout, "");
			dout.writeByte(0);
			dout.writeByte(0);
			writeLEShort(dout, (short) 0);

			ArrayList<Note> notes = new ArrayList<>();

			for (QuantizedBatch q : quantized) {
				long tick = q.step;
				for (int i = 0; i < q.notes.size(); i++) {
					notes.add(new Note(q.notes.get(i), (int)tick, i));
				}
			}
			int tick = -1;
			int layer = -1;
			Note fstNote = notes.getFirst();

			for (Note note : notes) {
				if (tick != note.tick) {
					if (note != fstNote) {
						writeLEShort(dout, (short) 0);
						layer = -1;
					}
					writeLEShort(dout, (short) (note.tick - tick));
					tick = note.tick;
				}
				if (layer != note.layer) {
					writeLEShort(dout, (short) (note.layer - layer));
					layer = note.layer;
					float y = (float) (12 * (Math.log(note.packet.getPitch()) / Math.log(2)) + 44);
					int key = Math.round(y);

					float fractionalPart = y - key;

					int pitch = Math.round(fractionalPart * 100);

					if (pitch == 50) {
						pitch = -50;
						key += 1;
					}
					dout.writeByte(mapInstrument(note.packet.getSound().value()));
					dout.writeByte(key);
					dout.writeByte(Math.round(note.packet.getVolume() * 100));
					dout.writeByte(100);
					writeLEShort(dout, (short) pitch);
				}
			}
			writeLEInt(dout, 0);

			for (int i = 0; i <= layerCount; i++) {
				writeString(dout, ""); // layer name
				dout.writeByte(0);     // locked = false
				dout.writeByte(100);   // volume = 100%
				dout.writeByte(100);   // stereo = center
			}

			if (customInstruments.isEmpty()) {
				dout.writeByte(0);
			} else {
				dout.writeByte(customInstruments.size());
				for (String instrument : customInstruments) {
					writeString(dout, instrument);
					writeString(dout, instrument+".ogg");
					dout.writeByte(45);
					dout.writeByte(0);

				}
			}

			dout.flush();
		} catch (Exception e) {
			System.out.println("help\n" + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private static void writeString(DataOutputStream dout, String s) throws IOException {
		byte[] data = s.getBytes(StandardCharsets.UTF_8);
		writeLEInt(dout, data.length);
		dout.write(data);
	}

	private static void writeLEInt(DataOutputStream dout, int i) throws IOException {
		byte[] data = ByteBuffer
				.allocate(Integer.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.putInt(i)
				.array();
		ByteArrays.reverse(data);
		for (byte byt : data) {
			dout.writeByte(byt);
		}
	}

	// one of these is wrong

	private static void writeLEShort(DataOutputStream dout, short i) throws IOException {
		byte[] data = ByteBuffer
			.allocate(Short.BYTES)
			.order(ByteOrder.LITTLE_ENDIAN)
			.putShort(i)
			.array();
		dout.write(data);
	}

	private static byte mapInstrument(SoundEvent sound) {
		String p = sound.getId().getPath();
		if (p.startsWith("block.note_block.harp"))           return 0;
		if (p.startsWith("block.note_block.bass"))           return 1;
		if (p.startsWith("block.note_block.basedrum"))       return 2;
		if (p.startsWith("block.note_block.snare"))          return 3;
		if (p.startsWith("block.note_block.hat"))            return 4;
		if (p.startsWith("block.note_block.guitar"))         return 5;
		if (p.startsWith("block.note_block.flute"))          return 6;
		if (p.startsWith("block.note_block.bell"))           return 7;
		if (p.startsWith("block.note_block.chime"))          return 8;
		if (p.startsWith("block.note_block.xylophone"))      return 9;
		if (p.startsWith("block.note_block.iron_xylophone")) return 10;
		if (p.startsWith("block.note_block.cow_bell"))       return 11;
		if (p.startsWith("block.note_block.didgeridoo"))     return 12;
		if (p.startsWith("block.note_block.bit"))            return 13;
		if (p.startsWith("block.note_block.banjo"))          return 14;
		if (p.startsWith("block.note_block.pling"))          return 15;
		if (customInstruments.contains(p)) {
			return (byte)(customInstruments.indexOf(p)+16);
		}
		customInstruments.add(p);
		System.out.println(p);
		return (byte)customInstruments.indexOf(p);
	}
}
