package seedu.duke.expense;

import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.exceptions.InvalidAmountException;
import seedu.duke.Person;
import seedu.duke.Storage;
import seedu.duke.Ui;
import seedu.duke.parser.Parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class Expense implements ExpenseSplitter {
    private double amountSpent;
    private String description;
    private ArrayList<Person> personsList;
    private String category;
    private LocalDate date;
    private Person payer;
    private HashMap<String, Double> amountSplit = new HashMap<>();
    private static final DateTimeFormatter inputPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter outputPattern = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final int NUMBER_OF_INPUTS = 3;
    private static final int EXPENSE_INDEX = 0;
    private static final int CATEGORY_INDEX = 1;
    private static final int PERSONS_INDEX = 2;
    private static final int PERSONS_AND_DESCRIPTION_INDEX = 2;
    private static final int INDEX_OF_PERSON = 0;

    /**
     * Legacy constructor for Expense. Used as stub for testing. Does not include parsing.
     *
     * @param amountSpent amount spent in the expense.
     * @param category category which is expense is being spent in.
     * @param personsList list of people that were involved in the expense.
     * @param description description of expense.
     */
    //@@author lixiyuan416
    public Expense(double amountSpent, String description, ArrayList<Person> personsList,
                   String category, LocalDate date, Person payer, HashMap<String, Double> amountSplit) {
        this.amountSpent = amountSpent;
        this.description = description;
        this.category = category;
        this.date = date;
        this.payer = payer;
        this.amountSplit = amountSplit;
        this.personsList = personsList;
    }

    /**
     * Constructor for {@link Expense} class - contains parsing and amount assignment.
     *
     * @param inputDescription String of user input to be parsed and assigned to expense attributes
     */
    public Expense(String inputDescription) throws ForceCancelException {


        String[] expenseInfo = inputDescription.split(" ", NUMBER_OF_INPUTS);
        setAmountSpent(expenseInfo[EXPENSE_INDEX]);
        setCategory(expenseInfo[CATEGORY_INDEX].toLowerCase());
        this.description = getDescriptionParse(expenseInfo[PERSONS_AND_DESCRIPTION_INDEX]);
        this.personsList = checkValidPersons(expenseInfo[PERSONS_INDEX]);
        this.date = promptDate();
        if (personsList.size() == 1) {
            ExpenseSplitter.updateOnePersonSpending(this, personsList.get(INDEX_OF_PERSON));
        } else {
            ExpenseSplitter.updateIndividualSpending(this);
        }
    }

    //@@author joshualeeky

    /**
     * Extracts the description of the expense from the user input.
     *
     * @param userInput the string that the user enters.
     * @return description of the expense.
     */
    private static String getDescriptionParse(String userInput) {
        return userInput.split("/")[1].strip();
    }

    /**
     * Obtains a list of Person objects from a string of names separated by a comma. Also checks if there are repeated
     * names that were entered in the expense.
     *
     * @param userInput the string that the user enters.
     * @return ArrayList containing the Person objects included in the expense.
     */
    private static ArrayList<Person> checkValidPersons(String userInput) throws ForceCancelException {
        String[] listOfPeople = userInput.split("/")[0].split(",");
        ArrayList<String> listOfPeopleNamesUpperCase = new ArrayList<>();
        ArrayList<Person> validListOfPeople = new ArrayList<>();
        ArrayList<String> invalidListOfPeople = new ArrayList<>();
        if (listOfPeople.length == 1 && listOfPeople[0].strip().equalsIgnoreCase("-all")) {
            return Storage.getOpenTrip().getListOfPersons();
        }
        boolean isThereRepeatedName = false;
        for (String name : listOfPeople) {
            boolean isValidPerson = false;
            for (Person person : Storage.getOpenTrip().getListOfPersons()) {
                if (name.strip().equalsIgnoreCase(person.getName())) {
                    validListOfPeople.add(person);
                    isValidPerson = true;
                    break;
                }
            }
            if (listOfPeopleNamesUpperCase.contains(name.strip().toUpperCase())) {
                isThereRepeatedName = true;
            } else if (!isValidPerson) {
                invalidListOfPeople.add(name.strip());
            }
            listOfPeopleNamesUpperCase.add(name.strip().toUpperCase());
        }
        if (!invalidListOfPeople.isEmpty()) {
            Ui.printInvalidPeople(invalidListOfPeople);
            String newUserInput = Ui.receiveUserInput();
            return checkValidPersons(newUserInput);
        } else if (isThereRepeatedName) {
            Ui.sameNameInExpenseError();
            String newUserInput = Ui.receiveUserInput();
            return checkValidPersons(newUserInput);
        }
        return validListOfPeople;
    }


    //@@author lixiyuan416

    /**
     * Prompts user for date.
     *
     * @return today's date if user input is an empty string, otherwise keeps prompting user until a valid date is given
     */
    public LocalDate promptDate() throws ForceCancelException {
        Ui.expensePromptDate();
        String inputDate = Ui.receiveUserInput();
        while (!isDateValid(inputDate)) {
            inputDate = Ui.receiveUserInput();
            Storage.getLogger().log(Level.INFO, "Invalid date format entered");
        }
        if (inputDate.isEmpty()) {
            return LocalDate.now();
        }
        return LocalDate.parse(inputDate, inputPattern);
    }

    private Boolean isDateValid(String date) {
        if (date.isEmpty()) {
            return true;
        }
        try {
            LocalDate.parse(date, inputPattern);
            //Additional check for weird dates like feb 31
            if (!Parser.doesDateReallyExist(date)) {
                Ui.expenseDateInvalid();
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            Ui.expenseDateInvalid();
            return false;
        }
    }


    @Override
    public String toString() {
        return ("\t" + this.getDescription()
                + System.lineSeparator()
                + "\t" + "Date: " + this.getStringDate()
                + System.lineSeparator()
                + "\t" + "Amount Spent: " + Ui.stringForeignMoney(this.getAmountSpent())
                + System.lineSeparator()
                + "\t" + "People involved:"
                + System.lineSeparator()
                + getPersonExpense()
                + "\t" + "Payer: " + this.getPayer()
                + System.lineSeparator()
                + "\t" + "Category: " + this.category)
                + System.lineSeparator();
    }
    //@@author

    public String getPersonExpense() {
        StringBuilder returnString = new StringBuilder();
        String name;
        String formattedSpace = "\t";
        for (Person p : personsList) {
            name = p.getName();
            returnString.append(formattedSpace);
            returnString.append(formattedSpace);
            returnString.append(personsList.indexOf(p) + 1).append(") ");
            returnString.append(name).append(", ");
            returnString.append(Ui.stringForeignMoney(getAmountSplit().get(name)));
            returnString.append(System.lineSeparator());
        }
        return returnString.toString();
    }

    //@@author itsleeqian
    public void setAmountSpent(String amount) throws ForceCancelException {
        try {
            this.amountSpent = Double.parseDouble(amount);
            if (this.amountSpent <= 0) {
                throw new InvalidAmountException();
            }
            this.amountSpent = Double.parseDouble(amount);
            this.amountSpent = Storage.formatForeignMoneyDouble(this.amountSpent);
        } catch (InvalidAmountException e) {
            Ui.printInvalidAmountError();
            String newInput = Ui.receiveUserInput();
            setAmountSpent(newInput);
        }
    }
    //@@author

    public String getDescription() {
        return description;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date, inputPattern);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStringDate() {
        return date.format(outputPattern);
    }

    public ArrayList<Person> getPersonsList() {
        return personsList;
    }

    public double getAmountSpent() {
        return amountSpent;
    }


    public void setPayer(Person person) {
        this.payer = person;
    }

    public Person getPayer() {
        return payer;
    }


    public void setAmountSplit(Person person, double amount) {
        amountSplit.put(person.getName(), amount);
    }

    public HashMap<String, Double> getAmountSplit() {
        return amountSplit;
    }

}
