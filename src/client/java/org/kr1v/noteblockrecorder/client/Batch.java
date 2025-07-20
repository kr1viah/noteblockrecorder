package org.kr1v.noteblockrecorder.client;

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;

import java.time.Instant;
import java.util.ArrayList;

public class Batch {
	public ArrayList<PlaySoundS2CPacket> notes = new ArrayList<>();
	public Instant timeStamp;
	public Batch(Instant timeStamp) {
		this.timeStamp = timeStamp;
	}
}
