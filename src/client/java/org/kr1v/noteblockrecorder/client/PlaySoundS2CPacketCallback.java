package org.kr1v.noteblockrecorder.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.util.ActionResult;

public interface PlaySoundS2CPacketCallback {
	Event<PlaySoundS2CPacketCallback> EVENT = EventFactory.createArrayBacked(PlaySoundS2CPacketCallback.class,
			(listeners) -> (packet) -> {
				for (PlaySoundS2CPacketCallback listener : listeners) {
					ActionResult result = listener.interact(packet);

					if (result != ActionResult.PASS) {
						return result;
					}
				}

				return ActionResult.PASS;
			});
	ActionResult interact(PlaySoundS2CPacket packet);
}