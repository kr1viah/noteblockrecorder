package org.kr1v.noteblockrecorder.client;

// Class to represent a single note
public class Note {
    private int tick;
    private int layer;
    private int inst;
    private int key;
    private int vel;
    private int pan;
    private int pitch;

    // Constructor
    public Note(int tick, int layer, int inst, int key, int vel, int pan, int pitch) {
        this.tick = tick;
        this.layer = layer;
        this.inst = inst;
        this.key = key;
        this.vel = vel;
        this.pan = pan;
        this.pitch = pitch;
    }

    // Getters and setters (if needed)
    public int getTick() { return tick; }
    public int getLayer() { return layer; }
    public int getInst() { return inst; }
    public int getKey() { return key; }
    public int getVel() { return vel; }
    public int getPan() { return pan; }
    public int getPitch() { return pitch; }
}

