package org.kr1v.noteblockrecorder.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.ActionResult;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class NoteblockrecorderClient implements ClientModInitializer {
    public static ArrayList<Batch> noteBatches = new ArrayList<>();
    public static final int TIME_BETWEEN_BATCHES_THRESHOLD = 20; // in milliseconds
    public static int highestLayer = 0;
    @Override
    public void onInitializeClient() {
        PlaySoundS2CPacketCallback.EVENT.register((packet) -> {
            Instant timestamp = Instant.now();
            if (noteBatches.isEmpty() ||
                    Duration.between(noteBatches.getLast().timeStamp, timestamp).toMillis()
                            >= TIME_BETWEEN_BATCHES_THRESHOLD
                ) {
                if (!noteBatches.isEmpty()) System.out.println(Long.toString(noteBatches.size()) + ": time " + Duration.between(noteBatches.getLast().timeStamp, timestamp).toMillis() + "ms");
                noteBatches.add(new Batch(timestamp));
            }
            noteBatches.getLast().notes.add(packet);
            if (noteBatches.getLast().notes.size() > highestLayer) {
                highestLayer = noteBatches.getLast().notes.size();
            }
            return ActionResult.PASS;
        });
        ClientPlayConnectionEvents.DISCONNECT.register(this::onPlayerLeave);
    }
    private void onPlayerLeave(ClientPlayNetworkHandler handler, MinecraftClient server) {
        if (noteBatches.isEmpty()) return;

        long quantum = detectQuantumNoisy(noteBatches);

        Instant t0 = noteBatches.getFirst().timeStamp;

        // build a quantized list
        List<QuantizedBatch> quantized = new ArrayList<>(noteBatches.size());
        for (Batch b : noteBatches) {
            long deltaMs = Duration.between(t0, b.timeStamp).toMillis();
            long step    = Math.round((double)deltaMs / quantum);
            quantized.add(new QuantizedBatch(b.notes, step));
        }
        File out = new File(MinecraftClient.getInstance().runDirectory, "recording.nbs");
	    try {
		    SaveAsNbs.write(out, quantized, quantum);
	    } catch (IOException e) {
		    throw new RuntimeException(e);
	    }

	    noteBatches = new ArrayList<>();
        highestLayer = 0;
    }
    public static long detectQuantumNoisy(List<Batch> batches) {
        if (batches.size() < 2) return 0;
        // 1) collect deltas
        List<Long> deltas = new ArrayList<>();
        for (int i = 1; i < batches.size(); i++) {
            long dt = Duration
                    .between(batches.get(i-1).timeStamp,
                            batches.get(i  ).timeStamp)
                    .toMillis();
            deltas.add(dt);
        }
        long minDt = Collections.min(deltas);
        long maxDt = Collections.max(deltas);
        int maxK  = (int)Math.ceil((double)maxDt / minDt);
        // 2) tally candidates
        Map<Long, Integer> freq = new HashMap<>();
        for (long dt : deltas) {
            for (int k = 1; k <= maxK && k <= 10; k++) {
                long cand = Math.round((double)dt / k);
                // ignore nonsense quanta
                if (cand < 5 || cand > 200) continue;
                freq.merge(cand, 1, Integer::sum);
            }
        }
        // 3) choose the best
        return freq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0L);
    }

}
