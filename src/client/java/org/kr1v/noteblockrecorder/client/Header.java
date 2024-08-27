package org.kr1v.noteblockrecorder.client;

public class Header {
    private final int length;
    private final int file_version;
    private final int vani_inst;
    private final int height;
    private final String name;
    private final String author;
    private final String orig_author;
    private final String description;
    private final double tempo;
    private final boolean auto_save;
    private final int auto_save_time;
    private final int time_sign;
    private final int minutes_spent;
    private final int left_clicks;
    private final int right_clicks;
    private final int block_added;
    private final int block_removed;
    private final String import_name;
    private final boolean loop;
    private final int loop_max;
    private final int loop_start;

    // Constructor
    public Header(int length, int file_version, int vani_inst, int height, String name, String author,
                  String orig_author, String description, double tempo, boolean auto_save, int auto_save_time,
                  int time_sign, int minutes_spent, int left_clicks, int right_clicks, int block_added,
                  int block_removed, String import_name, boolean loop, int loop_max, int loop_start) {
        this.length = length;
        this.file_version = file_version;
        this.vani_inst = vani_inst;
        this.height = height;
        this.name = name;
        this.author = author;
        this.orig_author = orig_author;
        this.description = description;
        this.tempo = tempo;
        this.auto_save = auto_save;
        this.auto_save_time = auto_save_time;
        this.time_sign = time_sign;
        this.minutes_spent = minutes_spent;
        this.left_clicks = left_clicks;
        this.right_clicks = right_clicks;
        this.block_added = block_added;
        this.block_removed = block_removed;
        this.import_name = import_name;
        this.loop = loop;
        this.loop_max = loop_max;
        this.loop_start = loop_start;
    }
    public int getLength() { return length; }
    public int getFileVersion() { return file_version; }
    public int getVaniInst() { return vani_inst; }
    public int getHeight() { return height; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public String getOrigAuthor() { return orig_author; }
    public String getDescription() { return description; }
    public double getTempo() { return tempo; }
    public boolean isAutoSave() { return auto_save; }
    public int getAutoSaveTime() { return auto_save_time; }
    public int getTimeSign() { return time_sign; }
    public int getMinutesSpent() { return minutes_spent; }
    public int getLeftClicks() { return left_clicks; }
    public int getRightClicks() { return right_clicks; }
    public int getBlockAdded() { return block_added; }
    public int getBlockRemoved() { return block_removed; }
    public String getImportName() { return import_name; }
    public boolean isLoop() { return loop; }
    public int getLoopMax() { return loop_max; }
    public int getLoopStart() { return loop_start; }
}
