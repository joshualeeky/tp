package seedu.duke;

import seedu.duke.parser.Parser;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Duke {

    private static boolean isProgramRunning = true;

    /**
     * Prints the welcome message and initializes {@link Scanner}, {@link Logger}, and {@link Gson},
     * and reads the user's save file.
     */
    private static void beginWelcome() {

        Ui.printWelcome();

        Scanner in = new Scanner(System.in);
        Storage.setScanner(in);
        Logger logger = Logger.getLogger("ProgramLogger");
        //Temporary disable logger
        logger.setLevel(Level.OFF);
        Storage.setLogger(logger);

        FileStorage.initializeGson();
        Storage.readFromFile(Storage.FILE_PATH);

    }

    /**
     * Reads and returns user input with leading and trailing whitespaces removed.
     *
     * @param in {@link Scanner} to read user input
     * @return user input with leading and trailing whitespaces removed
     */
    private static String readUserInput(Scanner in) {
        Ui.printPendingCommand();
        return in.nextLine().strip();
    }

    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {

        beginWelcome();

        while (isProgramRunning) {
            isProgramRunning = Parser.parseUserInput(readUserInput(Storage.getScanner()));
            try {
                Storage.writeToFile(Storage.FILE_PATH);
            } catch (IOException e) {
                Ui.printCouldNotSaveMessage();
                //e.printStackTrace();
            }
        }

    }

}
