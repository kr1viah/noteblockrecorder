package org.kr1v.noteblockrecorder.client;

public class CustomInstruments {
    private String name;
    private String filePath;
    private int pitch;
    private boolean pressKeys;
    public String  sound_id;

    // Constructor
    public CustomInstruments(String name, int sound_id) {
        this.name = name;
        this.filePath = name + ".ogg";
        this.pitch = 45;
        this.pressKeys = false;
        this.sound_id = Integer.toString(sound_id);
    }
    public String getName() { return name; }
}
