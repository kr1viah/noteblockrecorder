package org.kr1v.noteblockrecorder.client;

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;

import java.util.List;

public class QuantizedBatch {
	public final List<PlaySoundS2CPacket> notes;
	public final long step;
	public QuantizedBatch(List<PlaySoundS2CPacket> notes, long step) {
		this.notes = notes;
		this.step  = step;
	}
}
