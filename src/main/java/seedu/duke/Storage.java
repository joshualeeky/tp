package seedu.duke;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.trip.Trip;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage {

    public static final String FILE_PATH = "trips.json";
    public static final String LAST_INTERACTED = "last";
    private static ArrayList<Trip> listOfTrips = new ArrayList<>();
    private static Trip openTrip = null;
    private static Trip lastTrip = null;

    private static Scanner scanner;
    private static Logger logger;

    //@@author joshualeeky
    private static final ArrayList<String> validCommands = new ArrayList<>(
            Arrays.asList("create", "edit", "view", "open", "list", "summary",
                    "delete", "expense", "quit", "help", "amount", "people", "close", "optimize"));

    private static final HashMap<String, String[]> availableCurrency = new HashMap<>() {{
            put("USD", new String[]{"$", "%.02f"});
            put("SGD", new String[]{"$", "%.02f"});
            put("AUD", new String[]{"$", "%.02f"});
            put("CAD", new String[]{"$", "%.02f"});
            put("NZD", new String[]{"$", "%.02f"});
            put("EUR", new String[]{"", "%.02f"}); //€
            put("GBP", new String[]{"", "%.02f"}); //£
            put("MYR", new String[]{"RM", "%.02f"});
            put("HKD", new String[]{"$", "%.02f"});
            put("THB", new String[]{"", "%.02f"}); //฿
            put("RUB", new String[]{"", "%.02f"}); //₽
            put("ZAR", new String[]{"R", "%.02f"});
            put("TRY", new String[]{"", "%.02f"}); //₺
            put("BRL", new String[]{"R$", "%.02f"});
            put("DKK", new String[]{"Kr.", "%.02f"});
            put("PLN", new String[]{"", "%.02f"}); //zł
            put("ILS", new String[]{"", "%.02f"}); //₪
            put("SAR", new String[]{"SR", "%.02f"});
            put("CNY", new String[]{"", "%.0f"}); //¥
            put("JPY", new String[]{"", "%.0f"}); //¥
            put("KRW", new String[]{"", "%.0f"}); //₩
            put("IDR", new String[]{"Rp", "%.0f"});
            put("INR", new String[]{"Rs", "%.0f"});
            put("CHF", new String[]{"SFr.", "%.0f"});
            put("SEK", new String[]{"kr", "%.0f"});
            put("NOK", new String[]{"kr", "%.0f"});
            put("MXN", new String[]{"$", "%.0f"});
            put("TWD", new String[]{"NT$", "%.0f"});
            put("HUF", new String[]{"Ft", "%.0f"});
            put("CZK", new String[]{"Kc", "%.0f"});
            put("CLP", new String[]{"$", "%.0f"});
            put("PHP", new String[]{"", "%.0f"}); //₱
            put("AED", new String[]{"", "%.0f"}); //د.إ
            put("COP", new String[]{"$", "%.0f"});
            put("RON", new String[]{"lei", "%.0f"});
        }};

    public static HashMap<String, String[]> getAvailableCurrency() {
        return availableCurrency;
    }

    public static double formatForeignMoneyDouble(double money) throws ForceCancelException {
        return Double.parseDouble(String.format(Storage.getOpenTrip().getForeignCurrencyFormat(), money));
    }

    public static double formatRepaymentMoneyDouble(double money) throws ForceCancelException {
        return Double.parseDouble(String.format(Storage.getOpenTrip().getRepaymentCurrencyFormat(), money));
    }

    //@@author

    //@@author yeezao
    /**
     * Serializes the {@link Storage#listOfTrips} into a JSON String using {@link Gson}
     * to be written to the save file.
     *
     * @throws IOException if {@link FileStorage#writeToFile(String, String)} fails
     *
     * @see FileStorage#writeToFile(String, String)
     */
    public static void writeToFile(String filePath) throws IOException {
        String jsonString = FileStorage.getGson().toJson(listOfTrips);
        FileStorage.writeToFile(jsonString, filePath);
    }

    /**
     * Parsers the JSON string returned from {@link FileStorage#readFromFile(String)}
     * to populate the {@link Storage#listOfTrips}.
     *
     * @see FileStorage#readFromFile(String)
     */
    public static void readFromFile(String filePath) {
        try {
            String jsonString = FileStorage.readFromFile(filePath);
            Type tripType = new TypeToken<ArrayList<Trip>>(){}.getType();
            listOfTrips = FileStorage.getGson().fromJson(jsonString, tripType);
            Ui.printFileLoadedSuccessfully();
        } catch (JsonParseException e) {
            Ui.printJsonParseError();
            askOverwriteOrClose();
        } catch (NoSuchElementException e) {
            Ui.printEmptyFileWarning();
        } catch (FileNotFoundException e) {
            Ui.printFileNotFoundError();
            createNewFile(FILE_PATH);
        }
    }

    private static final int EXIT_ERROR_CODE = 1;

    /**
     * Creates a new blank file at the specified file path ({@link Storage#FILE_PATH}).
     *
     * @see FileStorage#newBlankFile(String)
     */
    public static void createNewFile(String filePath) {
        try {
            FileStorage.newBlankFile(filePath);
            Ui.newFileSuccessfullyCreated();
        } catch (IOException ex) {
            Ui.printCreateFileFailure();
            System.exit(EXIT_ERROR_CODE);
        }
    }

    /**
     * If {@link Storage#readFromFile(String)} throws a {@link JsonParseException}, asks the user whether to overwrite
     * the corrupted file or close the program.
     *
     * @see Storage#createNewFile(String)
     */
    private static void askOverwriteOrClose() {

        while (true) {
            Ui.printJsonParseUserInputPrompt();
            String input = scanner.nextLine().strip();
            if (input.contains(Ui.USER_QUIT)) {
                Ui.goodBye();
                Storage.getLogger().log(Level.WARNING, "JSON Parse failed, user requests program end");
                System.exit(EXIT_ERROR_CODE);
                return;
            } else if (input.contains(Ui.USER_CONTINUE)) {
                createNewFile(FILE_PATH);
                return;
            }
        }
    }


    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        Storage.scanner = scanner;
    }
    //@@author

    public static ArrayList<String> getValidCommands() {
        return validCommands;
    }

    //@@author yeezao
    /**
     * Gets the currently open trip. If no trip is open, asks the user to enter a trip index to open that trip.
     *
     * @return the currently opened trip
     */
    public static Trip getOpenTrip() throws ForceCancelException {
        if (openTrip == null) {
            Ui.printNoOpenTripError();
            promptUserForValidTrip();
            Ui.printOpenTripMessage(openTrip);
        }
        lastTrip = openTrip;
        return openTrip;
    }

    /**
     * If the user enters an invalid trip number, asks the user to re-enter a valid trip number.
     *
     * @see Storage#getOpenTrip()
     */
    private static void promptUserForValidTrip() throws ForceCancelException {
        try {
            System.out.print("Please enter the trip you would like to open: ");
            String input = Ui.receiveUserInput();
            int tripIndex = Integer.parseInt(input) - 1;
            setOpenTrip(tripIndex);
        } catch (NumberFormatException e) {
            Ui.argNotNumber();
            Ui.promptForTripIndex();
            promptUserForValidTrip();
        }
    }

    /**
     * Checks if there is an open trip or not.
     * @return true if there is an open trip
     */
    public static boolean checkOpenTrip() {
        return openTrip != null;
    }

    /**
     * Opens the trip at the specified <code>tripIndex</code>, and sets that trip as the last modified trip.
     * @param tripIndex index of the trip inside {@link Storage#listOfTrips} to be opened
     */
    public static void setOpenTrip(int tripIndex) {
        openTrip = listOfTrips.get(tripIndex);
        lastTrip = openTrip;
    }

    /**
     * Closes the currently active trip, sets it as the last trip,  and sets the open trip as null.
     */
    public static void closeTrip() {
        Trip tripToBeClosed = openTrip;
        openTrip = null;
        lastTrip = tripToBeClosed;
        Ui.printTripClosed(tripToBeClosed);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Storage.logger = logger;
    }

    public static ArrayList<Trip> getListOfTrips() {
        return listOfTrips;
    }

    public static Trip getLastTrip() {
        return lastTrip;
    }

    public static void setLastTrip(Trip lastTrip) {
        Storage.lastTrip = lastTrip;
    }

    public static void setListOfTrips(ArrayList<Trip> listOfTrips) {
        Storage.listOfTrips = listOfTrips;
    }

}
