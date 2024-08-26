package org.kr1v.noteblockrecorder.client;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

import java.util.List;
import java.util.HashSet;
import java.util.Objects;

public class Instruments {
    static List<CustomInstruments> customSounds = new java.util.ArrayList<>();

    public static int main(RegistryEntry<SoundEvent> instrument) {
        if (instrument.value().getId().getPath().contains("block.note_block.harp")) {
            return 0;
        } else if (instrument.value().getId().getPath().contains("block.note_block.bass")) {
            return 1;
        } else if (instrument.value().getId().getPath().contains("block.note_block.basedrum")) {
            return 2;
        } else if (instrument.value().getId().getPath().contains("block.note_block.snare")) {
            return 3;
        } else if (instrument.value().getId().getPath().contains("block.note_block.hat")) {
            return 4;
        } else if (instrument.value().getId().getPath().contains("block.note_block.guitar")) {
            return 5;
        } else if (instrument.value().getId().getPath().contains("block.note_block.flute")) {
            return 6;
        } else if (instrument.value().getId().getPath().contains("block.note_block.bell")) {
            return 7;
        } else if (instrument.value().getId().getPath().contains("block.note_block.chime")) {
            return 8;
        } else if (instrument.value().getId().getPath().contains("block.note_block.xylophone")) {
            return 9;
        } else if (instrument.value().getId().getPath().contains("block.note_block.iron_xylophone")) {
            return 10;
        } else if (instrument.value().getId().getPath().contains("block.note_block.cow_bell")) {
            return 11;
        } else if (instrument.value().getId().getPath().contains("block.note_block.didgeridoo")) {
            return 12;
        } else if (instrument.value().getId().getPath().contains("block.note_block.bit")) {
            return 13;
        } else if (instrument.value().getId().getPath().contains("block.note_block.banjo")) {
            return 14;
        } else if (instrument.value().getId().getPath().contains("block.note_block.pling")) {
            return 15;
        }

        HashSet<String> seenSounds = new HashSet<>();
        System.out.println(instrument.value().getId().getPath());
        int soundId = instrument.value().getId().hashCode();
        System.out.println("translationKey: " + instrument.value().getId().toTranslationKey());
        System.out.println("translationKeyShort: " + instrument.value().getId().toShortTranslationKey());
        System.out.println("type: " + instrument.getType());
        CustomInstruments thingToBeAdded = new CustomInstruments(instrument.value().getId().getPath(), soundId);
        customSounds.add(thingToBeAdded);
        for (int i = 0; i < customSounds.size(); i++) {
            if (Objects.equals(customSounds.get(i).getName(), thingToBeAdded.getName())) {
                soundId = i + 16;
                System.out.println("this ones it: " + i);
                break;
            }
            else {
                System.out.println("this ones not it: " + i);
            }
        }
        customSounds.removeLast();
        thingToBeAdded.sound_id = Integer.toString(soundId);
        customSounds.add(thingToBeAdded);
        customSounds.removeIf(thingToBeAdded2 -> !seenSounds.add(String.valueOf(thingToBeAdded2.sound_id)));

        System.out.println("soundId: " + soundId);
        return soundId;
    }
    public static List<CustomInstruments> getCustomInstruments() {
        return customSounds;
    }
}