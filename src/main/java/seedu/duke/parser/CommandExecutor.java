package seedu.duke.parser;

import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.exceptions.NoExpensesException;
import seedu.duke.exceptions.SameNameException;
import seedu.duke.exceptions.TripNotOpenException;
import seedu.duke.trip.Trip;
import seedu.duke.Storage;
import seedu.duke.Ui;
import seedu.duke.Person;
import seedu.duke.expense.Expense;

import java.util.ArrayList;

import static seedu.duke.Storage.LAST_INTERACTED;

abstract class CommandExecutor extends PaymentOptimizer implements ExpenseSummarizer {
    private static final int EDIT_ATTR_COUNT = 2;
    private static final int ATTRIBUTE_DATA = 1;
    private static final int EDIT_ATTRIBUTE = 0;
    private static final int EDIT_INDEX = 0;
    private static final String EDIT_LOCATION = "-location";
    private static final String EDIT_DATE = "-date";
    private static final String EDIT_EXRATE = "-exchangerate";
    private static final String EDIT_FORCUR = "-forcur";
    private static final String EDIT_HOMECUR = "-homecur";
    private static final int NEW_TRIP_ATTRIBUTES_COUNT = 6;
    private static final int NUMBER_OF_PARAMETERS = 3;
    private static final int INDEX_OF_SECOND_COMMAND = 0;
    private static final int INDEX_OF_CATEGORY = 1;
    private static final int INDEX_OF_EXPENSE_ATTRIBUTE = 2;
    private static final String LAST = "last";
    private static final String FILTER = "filter";

    //@@author yeezao
    /**
     * Creates a new instance of {@link Trip}.
     *
     * @param attributesInString attributes of the trip to be added (in a single {@link String}), before being parsed.
     *
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     * @throws IndexOutOfBoundsException if the user has not entered sufficient attributes to create a new trip.
     * @throws SameNameException if the user enters multiple persons with the same name.
     */
    protected static void executeCreateTrip(String attributesInString)
            throws ForceCancelException, IndexOutOfBoundsException, SameNameException {
        String[] newTripInfo = attributesInString.split("/", NEW_TRIP_ATTRIBUTES_COUNT);
        if (newTripInfo.length < NEW_TRIP_ATTRIBUTES_COUNT) {
            throw new IndexOutOfBoundsException();
        }
        Trip newTrip = new Trip(newTripInfo);
        if (!isTripADuplicate(newTrip)) {
            addNewTripToList(newTrip);
        } else {
            if (doesUserWantToAddDuplicateTrip()) {
                addNewTripToList(newTrip);
            }
        }

    }

    /**
     * Adds a newly-created {@link Trip} to the <code>listOfTrips</code>.
     *
     * @param newTrip instance of a newly-created {@link Trip}.
     */
    private static void addNewTripToList(Trip newTrip) {
        Storage.getListOfTrips().add(newTrip);
        Ui.newTripSuccessfullyCreated(newTrip);
        Storage.setLastTrip(newTrip);
    }

    /**
     * Asks if the user wants to proceed with adding a trip that has been detected as a duplicate.
     *
     * @return true if the user still wants to add the trip
     * @throws ForceCancelException if the user does not want to add the trip
     */
    private static boolean doesUserWantToAddDuplicateTrip() throws ForceCancelException {
        Ui.duplicateTripWarning();
        while (true) {
            String userOption = Ui.receiveUserInput();
            if (userOption.contains(Ui.USER_CONTINUE)) {
                return true;
            } else if (userOption.contains(Ui.USER_QUIT)) {
                Ui.printForceCancelled();
                return false;
            }
        }
    }

    /**
     * Checks if the trip might be a duplicate of an already existing trip.
     * The following attributes are checked: date, exchange rate, location, currency.
     *
     * @param newTrip instance of newly-created {@link Trip} object
     * @return true if the trip is detected as a possible duplicate
     */
    public static boolean isTripADuplicate(Trip newTrip) {
        for (Trip tripToCompare : Storage.getListOfTrips()) {
            if (tripToCompare.getDateOfTrip().equals(newTrip.getDateOfTrip())
                    && tripToCompare.getExchangeRate() == newTrip.getExchangeRate()
                    && tripToCompare.getForeignCurrency().equalsIgnoreCase(newTrip.getForeignCurrency())
                    && tripToCompare.getLocation().equalsIgnoreCase(newTrip.getLocation())) {
                return true;
            }
        }
        return false;
    }



    /**
     * Gets the trip to be edited and edits the specified attributes of the trip.
     *
     * @param inputDescription - user input of trip index and trip attributes to edit.
     *
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     *
     * @see Parser#editTripWithIndex(String)
     * @see Parser#editTripPerAttribute(Trip, String)
     *
     */
    protected static void executeEditTrip(String inputDescription) throws ForceCancelException {
        String[] tripToEditInfo = inputDescription.split(" ", EDIT_ATTR_COUNT);
        String attributesToEdit = tripToEditInfo[ATTRIBUTE_DATA];
        Trip tripToEdit;
        if (tripToEditInfo[EDIT_INDEX].equals(LAST_INTERACTED)) {
            tripToEdit = Storage.getLastTrip();
            if (tripToEdit == null) {
                Ui.printNoLastTripError();
                return;
            }
        } else {
            tripToEdit = editTripWithIndex(tripToEditInfo[EDIT_INDEX].strip());
        }
        editTripPerAttribute(tripToEdit, attributesToEdit);
    }

    /**
     * Sets the user-specified trip as opened. Requires that the {@code listOfTrips} has at least one open trip.
     *
     * @param indexAsString index of trip to open, as a {@link String} to be parsed.
     *
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    protected static void executeOpen(String indexAsString) throws ForceCancelException {
        //assumes that listOfTrips have at least 1 trip
        int indexToGet = Integer.parseInt(indexAsString) - 1;
        if (Storage.checkOpenTrip()) {
            Ui.printTripClosed(Storage.getOpenTrip());
        }
        Storage.setOpenTrip(indexToGet);
        Ui.printOpenTripMessage(Storage.getOpenTrip());
    }
    //@@author

    //@@author itsleeqian
    /**
     * Prints out the summary of expenses of an individual or everyone.
     * @param inputParams the individual to view. Can also be null to print everyone.
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    protected static void executeSummary(String inputParams) throws ForceCancelException {
        Trip currentTrip = Storage.getOpenTrip();
        if (inputParams == null) {
            //list everybody's expense summary
            for (Person p : currentTrip.getListOfPersons()) {
                ExpenseSummarizer.getIndividualExpenseSummary(p);
                System.out.println();
            }
        } else {
            //list only 1 person, if exists
            try {
                //returns null if no such person
                Person personToView = getValidPersonInTripFromString(inputParams, currentTrip);
                if (personToView != null) {
                    ExpenseSummarizer.getIndividualExpenseSummary(personToView);
                } else {
                    Ui.printNoPersonFound(inputParams);
                    Ui.printSummaryFormatError();
                }

            } catch (IndexOutOfBoundsException e) {
                Ui.printNoExpensesError();
            }
        }
    }
    //@@author

    //@@author leeyikai

    /**
     * Checks to see which expenses user wants to see and calls the appropriate method.
     * @param inputParams contains the information that determines what expenses user wants to see
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    protected static void executeView(String inputParams) throws ForceCancelException {
        Trip openTrip = Storage.getOpenTrip();
        if (inputParams == null) {
            openTrip.viewAllExpenses();
        } else if (inputParams.equalsIgnoreCase(LAST_INTERACTED)) {
            if (openTrip.getLastExpense() == null) {
                Ui.noRecentExpenseError();
            } else {
                System.out.println(openTrip.getLastExpense());
            }
        } else {
            String[] paramString = inputParams.split(" ", NUMBER_OF_PARAMETERS);
            String secondCommand = paramString[INDEX_OF_SECOND_COMMAND];
            String expenseCategory = null;
            String expenseAttribute = null;
            if (!isNumeric(secondCommand)) {
                expenseCategory = paramString[INDEX_OF_CATEGORY];
                expenseAttribute = paramString[INDEX_OF_EXPENSE_ATTRIBUTE];
            }
            if (secondCommand.equalsIgnoreCase(FILTER)) {
                try {
                    openTrip.getFilteredExpenses(expenseCategory, expenseAttribute);
                } catch (IndexOutOfBoundsException e) {
                    Ui.printNoExpensesError();
                }
            } else if (isNumeric(secondCommand)) {
                try {
                    int index = Integer.parseInt(secondCommand);
                    System.out.println(openTrip.getExpenseAtIndex(index));
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    Ui.printUnknownExpenseIndexError();
                }

            }
        }
    }
    //@@author

    //@@author yeezao
    /**
     * Checks whether to delete trip or delete expense (by determining if a trip is open),
     * and calls the appropriate method.
     *
     * @param inputParams attributes of trip to be deleted (if valid, this should be the trip/expense index)
     *
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     *
     * @see Parser#executeDeleteTrip(int)
     * @see Parser#executeDeleteExpense(int)
     */
    protected static void executeDelete(String inputParams) throws ForceCancelException {
        int index;
        if (Storage.checkOpenTrip()) {
            Trip currTrip = Storage.getOpenTrip();
            if (inputParams.equalsIgnoreCase(LAST_INTERACTED)) {
                index = currTrip.getListOfExpenses().indexOf(currTrip.getLastExpense());
            } else {
                index = Integer.parseInt(inputParams) - 1;
            }
            executeDeleteExpense(index);
        } else {
            if (inputParams.equalsIgnoreCase(LAST_INTERACTED)) {
                index = Storage.getListOfTrips().indexOf(Storage.getLastTrip());
            } else {
                index = Integer.parseInt(inputParams) - 1;
            }
            executeDeleteTrip(index);
        }
    }
    //@@author

    //@@author itsleeqian
    /**
     * Lists either trips or expenses depending on if a trip is open or not.
     * @throws ForceCancelException allows the user to cancel an operation when an input is required
     */
    protected static void executeList() throws ForceCancelException {
        if (!Storage.checkOpenTrip()) {
            Ui.printAllTrips();
        } else {
            Ui.printExpensesInList(Storage.getOpenTrip().getListOfExpenses());
        }
    }
    //@@author

    //@@author joshualeeky

    /**
     * Creates an Expense object in the current opened trip.
     * @param inputDescription the input of the user.
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    protected static void executeCreateExpense(String inputDescription) throws ForceCancelException {
        Trip currTrip = Storage.getOpenTrip();
        assert Storage.checkOpenTrip();
        Expense newExpense = new Expense(inputDescription);
        currTrip.addExpense(newExpense);
        currTrip.setLastExpense(newExpense);
        Ui.printExpenseAddedSuccess();
    }

    /**
     * Prints how much a Person object owe other Person object and/or how much other Person objects owe the Person
     * object.
     *
     * @param inputParams the input of the user.
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    protected static void executeAmount(String inputParams) throws ForceCancelException {
        Trip trip = Storage.getOpenTrip();
        Person toBeChecked = getValidPersonInTripFromString(inputParams, trip);
        if (toBeChecked == null) {
            Ui.invalidArgForAmount();
        } else {
            Ui.printAmount(toBeChecked, trip);
        }
    }

    //@@author

    //@@author itsleeqian
    /**
     * Lists the people involved in a trip.
     * @throws TripNotOpenException cannot list people if no trip is open.
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    protected static void executePeople() throws TripNotOpenException, ForceCancelException {
        Trip currTrip = Storage.getOpenTrip();
        if (Storage.checkOpenTrip()) {
            System.out.println("These are the people involved in this trip:");
            Ui.printListOfPeople(currTrip.getListOfPersons());
        } else {
            throw new TripNotOpenException();
        }
    }
    //@@author

    //@@author leeyikai

    /**
     * Check that there are expenses in the current open trip. If there is, execute the optimization method.
     *
     * @throws NoExpensesException stops the optimize command when there is no expenses available to optimize.
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    protected static void executeOptimize() throws NoExpensesException, ForceCancelException {
        if (Storage.getOpenTrip().getListOfExpenses().size() > 0) {
            checkForOptimization();
        } else {
            throw new NoExpensesException();
        }
    }
    //@@author

    //@@author joshualeeky
    private static void executeDeleteExpense(int expenseIndex) throws ForceCancelException {
        Trip currentTrip = Storage.getOpenTrip();
        Expense expenseToDelete = currentTrip.getListOfExpenses().get(expenseIndex);
        double expenseAmount = expenseToDelete.getAmountSpent();
        correctBalances(expenseToDelete);
        currentTrip.removeExpense(expenseIndex);
        Ui.printDeleteExpenseSuccessful(expenseAmount);
        currentTrip.setLastExpense(null);
    }

    //@@author yeezao
    /**
     * Deletes a trip from the <code>listOfTrips</code>.
     *
     * @param tripIndex index of Trip to be applied to <code>listOfTrips</code>
     */
    private static void executeDeleteTrip(int tripIndex) {
        ArrayList<Trip> listOfTrips = Storage.getListOfTrips();
        Trip tripToDelete = listOfTrips.get(tripIndex);
        listOfTrips.remove(tripIndex);
        Ui.printDeleteTripSuccessful(tripToDelete);
        Storage.setLastTrip(null);
    }


    /**
     * Parses the user input to determine which attributes to edit,
     * and calls the relevant setters to edit those attributes.
     *
     * @param tripToEdit user-specified trip to be edited
     * @param attributeToEdit String of all attributes to be added and their new values
     *
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    private static void editTripPerAttribute(Trip tripToEdit, String attributeToEdit) throws ForceCancelException {
        String[] splitCommandAndData = attributeToEdit.split(" ", EDIT_ATTR_COUNT);
        String data = splitCommandAndData[ATTRIBUTE_DATA];
        switch (splitCommandAndData[EDIT_ATTRIBUTE]) {
        case EDIT_LOCATION:
            String originalLocation = tripToEdit.getLocation();
            tripToEdit.setLocation(data);
            Ui.changeLocationSuccessful(tripToEdit, originalLocation);
            break;
        case EDIT_DATE:
            String originalDate = tripToEdit.getDateOfTripString();
            tripToEdit.setDateOfTrip(data);
            Ui.changeDateSuccessful(tripToEdit, originalDate);
            break;
        case EDIT_EXRATE:
            double originalExRate = tripToEdit.getExchangeRate();
            tripToEdit.setExchangeRate(data);
            Ui.changeExchangeRateSuccessful(tripToEdit, originalExRate);
            break;
        case EDIT_HOMECUR:
            String originalHomeCurrency = tripToEdit.getRepaymentCurrency();
            tripToEdit.setRepaymentCurrency(data);
            Ui.changeHomeCurrencySuccessful(tripToEdit, originalHomeCurrency);
            break;
        case EDIT_FORCUR:
            String originalForeignCurrency = tripToEdit.getForeignCurrency();
            tripToEdit.setForeignCurrency(data);
            Ui.changeForeignCurrencySuccessful(tripToEdit, originalForeignCurrency);
            break;
        default:
            System.out.println(splitCommandAndData[EDIT_ATTRIBUTE] + " was not recognised. "
                    + "Please try again after this process is complete");
        }
    }

    /**
     * Gets the trip to be edited from the user-entered index.
     *
     * @param tripIndexInString index of trip to be edited, as a {@link String} to be parsed.
     * @return the {@link Trip} object to be edited.
     */
    private static Trip editTripWithIndex(String tripIndexInString) {
        int indexToEdit = Integer.parseInt(tripIndexInString) - 1;
        Trip tripToEdit = Storage.getListOfTrips().get(indexToEdit);
        Storage.setLastTrip(tripToEdit);
        return tripToEdit;
    }
    //@@author

    public static boolean isNumeric(String secondCommand) {
        try {
            Integer.parseInt(secondCommand);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //@@author joshualeeky
    private static void correctBalances(Expense expense) {
        Person payer = expense.getPayer();
        for (Person person : expense.getPersonsList()) {
            if (person == payer) {
                payer.setMoneyOwed(payer, -expense.getAmountSplit().get(person.getName()));
                continue;
            }
            payer.setMoneyOwed(person, -expense.getAmountSplit().get(person.getName()));
            person.setMoneyOwed(payer, expense.getAmountSplit().get(person.getName()));
            person.setMoneyOwed(person, -expense.getAmountSplit().get(person.getName()));
        }
    }

    private static Person getValidPersonInTripFromString(String name, Trip trip) {
        for (Person person : trip.getListOfPersons()) {
            if (name.equalsIgnoreCase(person.getName())) {
                return person;
            }
        }
        return null;
    }
    //@@author

    //@author leeyikai

    /**
     * Gets the necessary information and carry out the optimized payment function. When finished optimizing,
     * this method will call the appropriate method in {@link Ui} and print the optimized transactions out.
     *
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    private static void checkForOptimization() throws ForceCancelException {
        Trip trip = Storage.getOpenTrip();
        PaymentOptimizer.optimizePayments(trip);
        Ui.printOptimizedAmounts();
    }
    //@@author
}
