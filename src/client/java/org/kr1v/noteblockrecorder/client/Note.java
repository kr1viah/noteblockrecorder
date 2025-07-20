package org.kr1v.noteblockrecorder.client;

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;

public class Note {
	public PlaySoundS2CPacket packet;
	public int tick;
	public int layer;
	public Note(PlaySoundS2CPacket packet, int tick, int layer) {
		this.packet = packet;
		this.tick = tick;
		this.layer = layer;
	}
}
