package org.kr1v.noteblockrecorder.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonWriter {
    static List<Note> notes = new ArrayList<>();
    static List<Layer> layers = new ArrayList<>();
    static List<CustomInstruments> custom_instruments = Instruments.getCustomInstruments();
    public static void main(int highestlayer) {
        // Create some sample notes
//        notes.add(new Note(0, 6, 1, 36, 100, 0, 1));
//        notes.add(new Note(0, 1, 0, 43, 20, -70, 1));
//        notes.add(new Note(0, 2, 0, 48, 20, -70, 1));


        // Create a header
        Header header = new Header(
                notes.getLast().getTick() + 1, // length
                5, // file_version
                16, // vani_inst
                highestlayer, // height
                "kr1v", // name
                "kr1v", // author
                "kr1v", // orig_author
                "Written with a mod by kr1v", // description
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
        // Create the JSON structure
        JsonStructure jsonStructure = new JsonStructure(header, notes, layers, custom_instruments);

        // Create Gson instance
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Pretty print for better readability

        // Convert the JsonStructure object to a JSON string
        String json = gson.toJson(jsonStructure);
        System.out.println("hey do the thing i sasked you");
        // Write the JSON string to a file
        try (FileWriter writer = new FileWriter("notes.json")) {
            System.out.println("written to: ");
            writer.write(json);
        } catch (IOException e) {
            System.out.println("uck");
            e.printStackTrace();
        }
    }
}