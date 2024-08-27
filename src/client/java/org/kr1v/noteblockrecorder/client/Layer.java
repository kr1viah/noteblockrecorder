package org.kr1v.noteblockrecorder.client;

public class Layer {
    private String name;
    private boolean lock;
    private int volume;
    private int pan;
    public Layer() {
        this.name = "";
        this.lock = false;
        this.volume = 100;
        this.pan = 0;
    }
}
