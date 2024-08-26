package org.kr1v.noteblockrecorder.client;

import java.util.List;

// Class to represent the entire JSON structure
public class JsonStructure {
    private Header header;
    private List<Note> notes;
    private List<Layer> layers;
    private List<CustomInstruments> custom_instruments;

    // Constructor
    public JsonStructure(Header header, List<Note> notes, List<Layer> layers, List<CustomInstruments> custom_instruments) {
        this.header = header;
        this.notes = notes;
        this.layers = layers;
        this.custom_instruments = custom_instruments;
    }

    // Getters (if needed)
    public Header getHeader() { return header; }
    public List<Note> getNotes() { return notes; }
    public List<Layer> getLayers() { return layers; }
    public List<CustomInstruments> getCustom_instruments() { return custom_instruments;}
}
