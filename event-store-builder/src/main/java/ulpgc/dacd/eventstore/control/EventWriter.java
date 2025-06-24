package ulpgc.dacd.eventstore.control;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class EventWriter {
    private static final Logger logger = Logger.getLogger(EventWriter.class.getName());

    public static void saveEvent(String topic, JsonObject json) {
        String timestamp = json.get("ts").getAsString();
        String source = json.get("ss").getAsString();
        String date = timestamp.substring(0, 10).replace("-", "");

        String dirPath = "eventstore/" + topic + "/" + source;
        new File(dirPath).mkdirs();

        String filePath = dirPath + "/" + date + ".events";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(json.toString());
            writer.newLine();
            logger.info("Saved event to: " + filePath);
        } catch (IOException e) {
            logger.severe("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
