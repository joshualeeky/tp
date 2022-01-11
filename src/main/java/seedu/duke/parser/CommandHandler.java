package seedu.duke.parser;

import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.exceptions.InvalidAmountException;
import seedu.duke.exceptions.NoExpensesException;
import seedu.duke.exceptions.SameNameException;
import seedu.duke.exceptions.TripNotOpenException;
import seedu.duke.Storage;
import seedu.duke.Ui;

import static seedu.duke.Storage.LAST_INTERACTED;

abstract class CommandHandler extends CommandExecutor {

    //@@author yeezao
    /**
     * Confirms that the user entered paramaters, and calls {@link Parser#executeCreateTrip(String)}.
     *
     * @param inputParams attributes of the trip to be created.
     */
    protected static void handleCreateTrip(String inputParams) throws ForceCancelException {
        try {
            assert inputParams != null;
            executeCreateTrip(inputParams);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            Ui.printCreateFormatError();
        } catch (SameNameException e) {
            Ui.sameNameInTripError();
        }
    }

    protected static void handleEditTrip(String inputParams) throws ForceCancelException {
        try {
            assert inputParams != null;
            executeEditTrip(inputParams);
        } catch (NumberFormatException | IndexOutOfBoundsException | NullPointerException e) {
            Ui.printEditFormatError();
        }
    }

    protected static void handleOpenTrip(String inputParams) throws ForceCancelException {
        try {
            assert inputParams != null;
            executeOpen(inputParams);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            Ui.printSingleUnknownTripIndexError();
            System.out.println();
        } catch (NullPointerException e) {
            Ui.emptyArgForOpenCommand();
        }
    }
    //@@author

    protected static void handleTripSummary(String inputParams) throws ForceCancelException {
        try {
            executeSummary(inputParams);
        } catch (ArrayIndexOutOfBoundsException e) {
            Ui.printUnknownTripIndexError();
        }
    }

    protected static void handleViewTrip(String inputParams) throws ForceCancelException {
        try {
            executeView(inputParams);
        } catch (ArrayIndexOutOfBoundsException e) {
            Ui.printFilterFormatError();
        }
    }

    //@@author yeezao
    protected static void handleDelete(String inputParams) throws ForceCancelException {
        try {
            assert inputParams != null;
            executeDelete(inputParams);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            if (Storage.checkOpenTrip()) {
                Ui.printUnknownExpenseIndexError();
            } else {
                Ui.printUnknownTripIndexError();
            }
        } catch (IndexOutOfBoundsException e) {
            if (Storage.checkOpenTrip()) {
                if (inputParams.equalsIgnoreCase(LAST_INTERACTED)) {
                    Ui.noRecentExpenseError();
                } else {
                    Ui.printUnknownExpenseIndexError();
                }
            } else {
                if (inputParams.equalsIgnoreCase(LAST_INTERACTED)) {
                    Ui.printNoLastTripError();
                } else {
                    Ui.printUnknownTripIndexError();
                }
            }
        } catch (NullPointerException e) {
            if (!Storage.checkOpenTrip()) {
                Ui.emptyArgForDeleteTripCommand();
            } else {
                Ui.emptyArgForDeleteExpenseCommand();
            }
        }
    }
    //@@author

    protected static void handleList() throws ForceCancelException {
        executeList();
    }

    /**
     * Confirms that the user had entered parameters for creating a new expense, and redirects to
     * {@link Parser#executeCreateExpense(String)} to create the expense.
     *
     * @param inputParams attributes of expense to be created.
     */
    protected static void handleCreateExpense(String inputParams) throws ForceCancelException {
        try {
            assert inputParams != null;
            executeCreateExpense(inputParams);
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException e) {
            Ui.printExpenseFormatError();
        }
    }

    protected static void handleAmount(String inputParams) throws ForceCancelException {
        try {
            executeAmount(inputParams);
        } catch (NullPointerException e) {
            Ui.invalidAmountFormat();
        }
    }


    /**
     * Prints out the list of people in the trip if trip is open.
     * Otherwise, informs the user no trip open.
     */
    protected static void handlePeople() throws ForceCancelException {
        try {
            executePeople();
        } catch (TripNotOpenException e) {
            Ui.printNoOpenTripError();
        }
    }

    protected static void handleOptimize() throws ForceCancelException {
        try {
            executeOptimize();
        } catch (NoExpensesException e) {
            Ui.printNoExpensesError();
        }
    }
}

