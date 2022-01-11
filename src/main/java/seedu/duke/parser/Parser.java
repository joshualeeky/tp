package seedu.duke.parser;

import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.Storage;
import seedu.duke.Ui;

import java.time.LocalDate;
import java.util.logging.Level;

public class Parser extends CommandHandler {

    private static final int SPLIT_COMMAND_FROM_INFO_LENGTH = 2;
    private static final int INPUT_COMMAND = 0;
    private static final int INPUT_INFO = 1;

    private static final String QUIT_COMMAND = "quit";
    private static final String CLOSE_COMMAND = "close";

    /**
     * Parses the user-entered command and additional information/flags.
     *
     * @param userInput the {@link String} containing the user input
     * @return whether the program should continue running after processing the given user input
     */
    public static boolean parseUserInput(String userInput) {

        String[] rawInput = userInput.split(" ", SPLIT_COMMAND_FROM_INFO_LENGTH);
        String inputCommand = rawInput[INPUT_COMMAND].toLowerCase();
        String inputParams = null;

        if (rawInput.length == SPLIT_COMMAND_FROM_INFO_LENGTH) {
            inputParams = rawInput[INPUT_INFO];
        }

        try {
            if (inputCommand.equals(QUIT_COMMAND)) {
                Ui.goodBye();
                return false;
            } else if (inputCommand.equals(CLOSE_COMMAND)) {
                Storage.closeTrip();
                return true;
            } else if (inputCommand.equals(HELP_COMMAND)) {
                Ui.displayHelp();
                return true;
            } else if (!checkValidCommand(inputCommand)) {
                Storage.getLogger().log(Level.WARNING, "invalid user input");
                Ui.printUnknownCommandError();
                return true;
            } else if (Storage.getListOfTrips().isEmpty() && !inputCommand.equals(CREATE_COMMAND)) {
                Storage.getLogger().log(Level.WARNING, "No trip created yet");
                Ui.printNoTripError();
                return true;
            } else {
                handleValidCommands(inputCommand, inputParams);
                return true;
            }
        } catch (NullPointerException e) {
            Ui.printNoOpenTripError();
            return true;
        } catch (ForceCancelException e) {
            Ui.printForceCancelled();
            return true;
        }
    }

    private static final String CREATE_COMMAND = "create";
    private static final String EDIT_COMMAND = "edit";
    private static final String OPEN_COMMAND = "open";
    private static final String SUMMARY_COMMAND = "summary";
    private static final String VIEW_COMMAND = "view";
    private static final String DELETE_COMMAND = "delete";
    private static final String LIST_COMMAND = "list";
    private static final String EXPENSE_COMMAND = "expense";
    private static final String AMOUNT_COMMAND = "amount";
    private static final String HELP_COMMAND = "help";
    private static final String PEOPLE_COMMAND = "people";
    private static final String OPTIMIZE_COMMAND = "optimize";

    /**
     * Handles commands entered by the user that are confirmed as valid, and redirects to the appropriate method
     * for further updates.
     *
     * @param inputCommand Valid command executed by the user.
     * @param inputParams  Additional information appended to the command by the user
     *                     (inputParams are not checked and may not be valid).
     *
     * @see Parser#parseUserInput(String)
     */
    private static void handleValidCommands(String inputCommand, String inputParams) throws ForceCancelException {

        switch (inputCommand) {
        case CREATE_COMMAND:
            handleCreateTrip(inputParams);
            break;

        case EDIT_COMMAND:
            handleEditTrip(inputParams);
            break;

        case OPEN_COMMAND:
            handleOpenTrip(inputParams);
            break;

        case SUMMARY_COMMAND:
            handleTripSummary(inputParams);
            break;

        case VIEW_COMMAND:
            handleViewTrip(inputParams);
            break;

        case DELETE_COMMAND:
            handleDelete(inputParams);
            break;

        case LIST_COMMAND:
            handleList();
            break;

        case EXPENSE_COMMAND:
            handleCreateExpense(inputParams);
            break;

        case AMOUNT_COMMAND:
            handleAmount(inputParams);
            break;

        case PEOPLE_COMMAND:
            handlePeople();
            break;

        case OPTIMIZE_COMMAND:
            handleOptimize();
            break;

        default:
            Ui.printUnknownCommandError();
        }
    }

    //@@author lixiyuan416

    /**
     * Helper function to get user to confirm y/n.
     *
     * @return true if user agrees, false otherwise
     */
    public static boolean getUserToConfirm() {
        boolean isValidInput = false;
        boolean doesUserAgree = false;
        while (!isValidInput) {
            String userReply = Storage.getScanner().nextLine();
            if (userReply.strip().equalsIgnoreCase(Ui.USER_CONTINUE)) {
                isValidInput = true;
                doesUserAgree = true;
            } else if (userReply.strip().equalsIgnoreCase(Ui.USER_QUIT)) {
                isValidInput = true;
            } else {
                System.out.println("Enter y/n");
            }
        }
        return doesUserAgree;
    }
    //@@author

    private static boolean checkValidCommand(String inputCommand) {
        return Storage.getValidCommands().contains(inputCommand);
    }

    //@@author yeezao
    public static boolean doesUserWantToForceCancel(String userInput) {
        return userInput.equals("-cancel");
    }

    private static final int DAYDD = 0;
    private static final int MONTHMM = 1;
    private static final int YEARYYYY = 2;

    /**
     * Checks if the user-entered date is a date that actually exists.
     *
     * @param dateInString the entire dd-mm-yyyy date as a single string.
     * @return true if the date actually exists.
     */
    public static boolean doesDateReallyExist(String dateInString) {
        String[] dateSplitUp = dateInString.split("-");
        int day;
        int month;
        int year;
        try {
            day = Integer.parseInt(dateSplitUp[DAYDD]);
            month = Integer.parseInt(dateSplitUp[MONTHMM]);
            year = Integer.parseInt(dateSplitUp[YEARYYYY]);
        } catch (NumberFormatException e) {
            return false;
        }

        //definitely an invalid date
        if (day < 1 || day > 31 || month < 1 || month > 12) {
            return false;
        }
        //for months with 30 days
        if ((month < 7 && month % 2 == 0) || (month >= 8 && month % 2 == 1)) {
            if (day > 30) {
                return false;
            }
        }
        //leap year checks
        if (month == 2) {
            LocalDate leapYearCheck = LocalDate.of(year, 1, 1);
            if (!leapYearCheck.isLeapYear() && day > 28) {
                return false;
            } else {
                return (!leapYearCheck.isLeapYear() || day <= 29);
            }
        }
        return true;
    }

}
