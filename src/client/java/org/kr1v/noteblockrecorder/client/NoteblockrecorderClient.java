package org.kr1v.noteblockrecorder.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;

public class NoteblockrecorderClient implements ClientModInitializer {
    private int tick = 0;
    private int currentlayer = 0;
    public  int highestlayer = 0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            @Nullable ClientPlayerEntity player = client.player;
            if (player != null) {
                tick = player.age;
            }
            if (currentlayer > highestlayer) {
                highestlayer = currentlayer;
            }
            currentlayer = 0;
        });

        NoteblockrecorderClient.PlaySoundS2CPacketCallback.EVENT.register((packet) -> {
            int inst = Instruments.main(packet.getSound());
            if (inst == -1) {
                System.out.println("aborted");
                System.out.println("key: " + packet.getSound().getKey());
                System.out.println();
                return ActionResult.PASS;
            }
            // int vel = (int) (packet.getVolume() * 1/3);
            int vel = (int) (Math.pow(packet.getVolume() * 1/3, 0.5) * 100);

            int key = Pitch.key(packet.getPitch());
            int correctedpitch = Pitch.pitch(packet.getPitch());

            Note note = new Note(tick, currentlayer, inst, key, vel, 0, correctedpitch);
            JsonWriter.notes.add(note);
            currentlayer++;

            return ActionResult.PASS;
        });
        ClientPlayConnectionEvents.DISCONNECT.register(this::onPlayerLeave);
    }
    private void onPlayerLeave(ClientPlayNetworkHandler handler, MinecraftClient server) {
        JsonWriter.main(highestlayer);
        highestlayer = 0;
    }
    public interface PlaySoundS2CPacketCallback {
        Event<NoteblockrecorderClient.PlaySoundS2CPacketCallback> EVENT = EventFactory.createArrayBacked(NoteblockrecorderClient.PlaySoundS2CPacketCallback.class,
                (listeners) -> (packet) -> {
                    for (NoteblockrecorderClient.PlaySoundS2CPacketCallback listener : listeners) {
                        ActionResult result = listener.interact(packet);

                        if(result != ActionResult.PASS) {
                            return result;
                        }
                    }

                    return ActionResult.PASS;
                });

        ActionResult interact(PlaySoundS2CPacket packet);
    }
}
