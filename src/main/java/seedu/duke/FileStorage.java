//@@author yeezao

package seedu.duke;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.Level;

public class FileStorage {

    private static Gson gson;

    /**
     * Gets the {@link FileWriter} from {@link FileStorage#initializeFileWriter(String)}, and writes the
     * JSON String to the file.
     *
     * @param jsonString parsed JSON string containing all data from the program.
     * @throws IOException if writing to file fails.
     */
    public static void writeToFile(String jsonString, String filePath) throws IOException {
        Storage.getLogger().log(Level.INFO, "starting write to save file");
        FileWriter fileWriter = initializeFileWriter(filePath);
        fileWriter.write(jsonString);
        fileWriter.close();
    }

    /**
     * Reads the raw JSON String from the indicated save file.
     *
     * @param filePath path of the JSON file to be read from.
     * @return JSON String from the file.
     * @throws FileNotFoundException if there is no file corresponding to the <code>filePath</code>.
     */
    public static String readFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        return scanner.nextLine();
    }

    /**
     * Creates a new blank file at the given <code>filePath</code>.
     *
     * @param filePath path location to create the file at
     * @throws IOException if file creation fails (thrown from {@link FileWriter}).
     */
    public static void newBlankFile(String filePath) throws IOException {
        FileWriter fileWriter = initializeFileWriter(filePath);
        fileWriter.close();
    }

    /**
     * Initializes a new instance of {@link FileWriter} with the given <code>filePath</code>.
     *
     * @param filePath path location to create the file at.
     * @return instance of {@link FileWriter}.
     * @throws IOException if the FileWriter could not be created.
     *
     * @see FileStorage#newBlankFile(String)
     * @see FileStorage#writeToFile(String, String)
     */
    private static FileWriter initializeFileWriter(String filePath) throws IOException {
        return new FileWriter(filePath);
    }

    /**
     * Registers the custom serializers and deserializers for {@link LocalDate} type, and creates an
     * instance of {@link Gson} stored in {@link FileStorage}.
     */
    public static void initializeGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        FileStorage.gson = gsonBuilder.create();
    }

    public static Gson getGson() {
        return gson;
    }

    private static class LocalDateSerializer implements JsonSerializer<LocalDate> {

        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    private static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String dateInString = json.getAsJsonPrimitive().getAsString();
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateInString, pattern);
        }
    }

}
