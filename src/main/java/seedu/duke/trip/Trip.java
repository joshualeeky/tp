package seedu.duke.trip;

import seedu.duke.Person;
import seedu.duke.Storage;
import seedu.duke.Ui;
import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.exceptions.SameNameException;
import seedu.duke.expense.Expense;
import seedu.duke.parser.Parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.ArrayList;

import static seedu.duke.parser.Parser.isNumeric;

public class Trip implements FilterFinder {

    private LocalDate dateOfTrip;
    private ArrayList<Expense> listOfExpenses = new ArrayList<>();
    private ArrayList<Person> listOfPersons = new ArrayList<>();
    private double exchangeRate;
    private String foreignCurrency;
    private String foreignCurrencyFormat;
    private String foreignCurrencySymbol;
    private String repaymentCurrency = "SGD";
    private String repaymentCurrencyFormat = "%.02f";
    private String repaymentCurrencySymbol = "$";
    private String location;
    private Expense lastExpense = null;
    private static final int ISO_LENGTH = 3;
    private static final String CATEGORY = "category";
    private static final String DESCRIPTION = "description";
    private static final String PAYER = "payer";
    private static final String PERSON = "person";
    private static final String DATE = "date";

    //@@author yeezao
    /**
     * Legacy constructor for Trip. Used as stub for testing.
     */
    public Trip() {
        //empty constructor
    }

    private static final int LOCATION_STRING = 1;
    private static final int DATE_STRING = 2;
    private static final int FORCUR_STRING = 3;
    private static final int EXRATE_STRING = 4;
    private static final int PERSONS_STRING = 5;

    /**
     * Non-empty {@link Trip} constructor. Reads in a String array and processes it to set attributes for a given Trip.
     *
     * @param newTripInfo array containing one attribute in each element
     */
    public Trip(String[] newTripInfo) throws ForceCancelException, SameNameException {
        assert newTripInfo.length == 6;
        setLocation(newTripInfo[LOCATION_STRING].strip());
        setDateOfTrip(newTripInfo[DATE_STRING].strip());
        setForeignCurrency(newTripInfo[FORCUR_STRING].strip().toUpperCase());
        setExchangeRate(newTripInfo[EXRATE_STRING].strip());
        setListOfPersons(splitPeople(newTripInfo[PERSONS_STRING]));
    }

    //@@author leeyikai
    public void getFilteredExpenses(String expenseCategory, String expenseAttribute) {

        if (listOfExpenses.isEmpty()) {
            Ui.printNoExpensesError();
            return;
        }
        try {
            switch (expenseCategory) {
            case CATEGORY:
                FilterFinder.findMatchingCategoryExpenses(listOfExpenses, expenseAttribute);
                break;
            case DESCRIPTION:
                FilterFinder.findMatchingDescriptionExpenses(listOfExpenses, expenseAttribute);
                break;
            case PAYER:
                FilterFinder.findMatchingPayerExpenses(listOfExpenses, expenseAttribute);
                break;
            case PERSON:
                FilterFinder.findMatchingPersonExpenses(listOfExpenses, expenseAttribute);
                break;
            case DATE:
                FilterFinder.findMatchingDateExpenses(listOfExpenses, expenseAttribute);
                break;
            default:
                Ui.printInvalidFilterError();
                break;
            }

        } catch (IndexOutOfBoundsException e) {
            Ui.printFilterFormatError();
        } catch (ForceCancelException e) {
            Ui.printForceCancelled();
        }

    }

    //@@author yeezao
    public LocalDate getDateOfTrip() {
        return dateOfTrip;
    }

    /**
     * Returns the {@link LocalDate} object as a formatted string (with the format dd MMMM yy).
     *
     * @return the formatted date as a {@link String}.
     */
    public String getDateOfTripString() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return getDateOfTrip().format(pattern);
    }

    /**
     * Parses a user input (in {@link String}) into a {@link LocalDate}.
     *
     * @param dateOfTrip user-entered date of trip as a String
     */
    public void setDateOfTrip(String dateOfTrip) throws ForceCancelException {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate date = LocalDate.parse(dateOfTrip, pattern);
            if (Parser.doesDateReallyExist(dateOfTrip)) {
                if (date.isBefore(LocalDate.EPOCH)) {
                    dateInvalid();
                } else {
                    this.dateOfTrip = date;
                }
            } else {
                dateInvalid();
            }
        } catch (DateTimeParseException e) {
            Ui.printDateTimeFormatError();
            userInputDateError();
        }
    }

    private void dateInvalid() throws ForceCancelException {
        Ui.dateInvalidError();
        userInputDateError();
    }

    private void userInputDateError() throws ForceCancelException {
        String newInput = Ui.receiveUserInput();
        setDateOfTrip(newInput);
    }


    public double getExchangeRate() {
        return exchangeRate;
    }


    /**
     * Parses an exchange rate entered by the user (as a {@link String}) into a {@link Double}.
     *
     * @param exchangeRateString user-entered exchange rate (as a String)
     */
    public void setExchangeRate(String exchangeRateString) throws ForceCancelException {
        try {
            this.exchangeRate = Double.parseDouble(exchangeRateString);
        } catch (NumberFormatException e) {
            Ui.printExchangeRateFormatError();
            String userInput = Ui.receiveUserInput();
            setExchangeRate(userInput);
        }
    }

    //@@author itsleeqian
    public void setForeignCurrency(String foreignCurrency) throws ForceCancelException {
        try {
            if (isNumeric(foreignCurrency) || foreignCurrency.length() != ISO_LENGTH) {
                throw new NumberFormatException();
            }
            this.foreignCurrency = foreignCurrency;
            setForeignCurrencyFormat(this.foreignCurrency);
            setForeignCurrencySymbol(this.foreignCurrency);
        } catch (NumberFormatException e) {
            Ui.printIsoFormatError();
            String userInput = Ui.receiveUserInput();
            setForeignCurrency(userInput);
        }
    }

    //@@author joshualeeky
    public String getForeignCurrency() {
        return foreignCurrency;
    }

    private void setForeignCurrencyFormat(String input) {
        if (Storage.getAvailableCurrency().containsKey(input)) {
            this.foreignCurrencyFormat = Storage.getAvailableCurrency().get(input)[1];
        } else {
            this.foreignCurrencyFormat = "%.02f";
        }
    }

    private void setForeignCurrencySymbol(String input) {
        if (Storage.getAvailableCurrency().containsKey(input)) {
            this.foreignCurrencySymbol = Storage.getAvailableCurrency().get(input)[0];
        } else {
            this.foreignCurrencySymbol = "";
        }
    }

    public String getForeignCurrencyFormat() {
        return foreignCurrencyFormat;
    }

    public String getForeignCurrencySymbol() {
        return foreignCurrencySymbol;
    }

    public String getRepaymentCurrency() {
        return repaymentCurrency;
    }

    //@@author itsleeqian
    public void setRepaymentCurrency(String repaymentCurrency) throws ForceCancelException {
        try {
            if (isNumeric(repaymentCurrency) || repaymentCurrency.length() != ISO_LENGTH) {
                throw new NumberFormatException();
            }
            this.repaymentCurrency = repaymentCurrency.toUpperCase();
            setRepaymentCurrencyFormat(this.repaymentCurrency);
            setRepaymentCurrencySymbol(this.repaymentCurrency);
        } catch (NumberFormatException e) {
            Ui.printIsoFormatError();
            String userInput = Ui.receiveUserInput();
            setRepaymentCurrency(userInput);
        }
    }

    private void setRepaymentCurrencyFormat(String input) {
        if (Storage.getAvailableCurrency().containsKey(input)) {
            this.repaymentCurrencyFormat = Storage.getAvailableCurrency().get(input)[1];
        } else {
            this.repaymentCurrencyFormat = "%.02f";
        }
    }

    public String getRepaymentCurrencyFormat() {
        return repaymentCurrencyFormat;
    }

    public String getRepaymentCurrencySymbol() {
        return repaymentCurrencySymbol;
    }

    private void setRepaymentCurrencySymbol(String input) {
        if (Storage.getAvailableCurrency().containsKey(input)) {
            this.repaymentCurrencySymbol = Storage.getAvailableCurrency().get(input)[0];
        } else {
            this.repaymentCurrencySymbol = "";
        }
    }
    //@@author

    //@@author yeezao
    public String getLocation() {
        return this.location;
    }


    public void setListOfPersons(ArrayList<Person> listOfPersons) throws ForceCancelException, SameNameException {
        if (listOfPersons.isEmpty()) {
            Ui.noPersonsAdded();
            String userInput = Ui.receiveUserInput();
            setListOfPersons(splitPeople(userInput));
            return;
        }
        this.listOfPersons = listOfPersons;
    }

    public ArrayList<Person> getListOfPersons() {
        return listOfPersons;
    }

    public void setLocation(String location) throws ForceCancelException {
        if (location.isBlank()) {
            Ui.locationIsBlank();
            setLocation(Ui.receiveUserInput());
        } else {
            this.location = location;
        }
    }

    public Expense getLastExpense() {
        return lastExpense;
    }

    public void setLastExpense(Expense lastExpense) {
        this.lastExpense = lastExpense;
    }

    public void setPersonName(int indexOfPerson, String newName) {
        Person personToEdit = listOfPersons.get(indexOfPerson);
        personToEdit.setName(newName);
    }
    //@@author

    public void addExpense(Expense expense) {
        listOfExpenses.add(expense);
        listOfExpenses.sort(Comparator.comparing(Expense::getDate));
    }

    public ArrayList<Expense> getListOfExpenses() {
        return listOfExpenses;
    }

    public void removeExpense(Integer i) {
        listOfExpenses.remove(listOfExpenses.get(i));
    }

    public void viewAllExpenses() {
        if (listOfExpenses.isEmpty()) {
            Ui.printNoExpensesError();
        } else {
            System.out.println("List of all Expenses in detail: ");
            for (Expense expense : listOfExpenses) {
                System.out.print(listOfExpenses.indexOf(expense) + 1 + ". ");
                Ui.printExpenseDetails(expense);
            }
        }
    }


    public Expense getExpenseAtIndex(Integer index) {
        return listOfExpenses.get(index - 1);
    }


    //@@author joshualeeky

    /**
     * Splits the user-entered {@link String} of people involved in a trip into a String array.
     *
     * @param peopleChained String of all persons involved in the trip
     * @return {@link String} array, each element of the array being a person involved in the trip
     */
    private ArrayList<Person> splitPeople(String peopleChained) throws SameNameException {
        ArrayList<String> listOfPeopleNames = new ArrayList<>();
        ArrayList<String> listOfPeopleNamesUpperCased = new ArrayList<>();
        ArrayList<Person> listOfPeople = new ArrayList<>();
        for (String personName : peopleChained.split(",")) {
            if (listOfPeopleNamesUpperCased.contains(personName.strip().toUpperCase())) {
                throw new SameNameException();
            } else if (!personName.isBlank()) {
                listOfPeopleNames.add(personName.strip());
                listOfPeopleNamesUpperCased.add(personName.strip().toUpperCase());
            }
        }
        for (String name : listOfPeopleNames) {
            Person person = new Person(name);
            listOfPeople.add(person);
        }
        for (Person person : listOfPeople) {
            for (Person personToAdd : listOfPeople) {
                person.getMoneyOwed().put(personToAdd.getName(), 0.0);
            }
        }
        return listOfPeople;
    }
}
