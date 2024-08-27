package org.kr1v.noteblockrecorder.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonWriter {
    static List<Note> notes = new ArrayList<>();
    static List<Layer> layers = new ArrayList<>();
    static List<CustomInstruments> custom_instruments = Instruments.getCustomInstruments();
    static String name = "name";
    public static void main(int highestlayer) {

        assert MinecraftClient.getInstance().player != null;
        if (MinecraftClient.getInstance().player.getName() != null) {
            name = MinecraftClient.getInstance().player.getName().getString();
        }
        // Create a header
        Header header = new Header(
                notes.getLast().getTick() + 1, // length
                5, // file_version
                16, // vani_inst
                highestlayer, // height
                name, // name
                name, // author
                name, // orig_author
                "Made with Note Block Recorder", // description
                20.0, // tempo
                false, // auto_save
                18, // auto_save_time
                4, // time_sign
                0, // minutes_spent
                0, // left_clicks
                0, // right_clicks
                0, // block_added
                0, // block_removed
                "", // import_name
                false, // loop
                0, // loop_max
                0 // loop_start
        );
        for (int i = 0; i < highestlayer; i++) {
            layers.add(new Layer());
        }
        JsonStructure jsonStructure = new JsonStructure(header, notes, layers, custom_instruments);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonStructure);

        try (FileWriter writer = new FileWriter("notes.json")) {
            System.out.println("written");
            writer.write(json);
        } catch (IOException e) {
            System.out.println("uck");
            e.printStackTrace();
        }
    }
}