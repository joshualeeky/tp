package seedu.duke.trip;

import seedu.duke.Person;
import seedu.duke.Storage;
import seedu.duke.Ui;
import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.expense.Expense;
import seedu.duke.parser.Parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.logging.Level;

public interface FilterFinder {

    DateTimeFormatter inputPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    //@@author lixiyuan416

    /**
     * Filters expenses inside a trip by date.
     * See parent method {@link Trip#getFilteredExpenses(String, String)}
     *
     * @param listOfCurrentExpenses expense list of a trip
     * @param expenseAttribute date to search for
     * @throws ForceCancelException when user inputs command to cancel the view filter operation
     */
    static void findMatchingDateExpenses(ArrayList<Expense> listOfCurrentExpenses, String expenseAttribute)
            throws ForceCancelException {
        boolean areThereExpenses = false;
        String inputDate = expenseAttribute;
        while (!isDateValid(inputDate)) {
            inputDate = Ui.receiveUserInput();
        }
        LocalDate dateToFind = LocalDate.parse(inputDate, inputPattern);
        for (Expense e : listOfCurrentExpenses) {
            if (e.getDate().isEqual(dateToFind)) {
                int index = listOfCurrentExpenses.indexOf(e);
                Ui.printFilteredExpenses(e, index);
                areThereExpenses = true;
            }
        }
        if (!areThereExpenses) {
            Ui.printNoMatchingExpenseError();
        }
    }
    //@@author

    //@@author leeyikai

    /**
     * Finds the {@link Expense} that has the same {@link Person} name as {@param expenseAttribute} and prints
     * the expense. If there are no matching {@link Expense}, an error message will be printed.
     *
     * @param listOfCurrentExpenses {@link ArrayList} containing the list of expenses currently in the trip.
     * @param expenseAttribute @{@link String} containing the name of the {@link Person} that we want to find.
     */
    static void findMatchingPayerExpenses(ArrayList<Expense> listOfCurrentExpenses, String expenseAttribute) {
        boolean areThereExpenses = false;
        for (Expense e : listOfCurrentExpenses) {
            if (e.getPayer().getName().equalsIgnoreCase(expenseAttribute)) {
                int index = listOfCurrentExpenses.indexOf(e);
                Ui.printFilteredExpenses(e, index);
                areThereExpenses = true;
            }
        }
        if (!areThereExpenses) {
            Ui.printNoMatchingExpenseError();
        }
    }

    /**
     * Finds the {@link Expense} that contains the description as {@param expenseAttribute} and prints it out.
     * If there are no matching {@link Expense}, an error message will be printed.
     *
     * @param listOfCurrentExpenses {@link ArrayList} containing the list of expenses currently in the trip.
     * @param expenseAttribute @{@link String} containing the description that we want to find.
     */
    static void findMatchingDescriptionExpenses(ArrayList<Expense> listOfCurrentExpenses,
                                                        String expenseAttribute) {
        boolean areThereExpenses = false;
        String descriptionToLowerCase;
        String attributeToLowerCase = expenseAttribute.toLowerCase();
        for (Expense e : listOfCurrentExpenses) {
            descriptionToLowerCase = e.getDescription().toLowerCase();
            if (descriptionToLowerCase.contains(attributeToLowerCase)) {
                int index = listOfCurrentExpenses.indexOf(e);
                Ui.printFilteredExpenses(e, index);
                areThereExpenses = true;
            }
        }
        if (!areThereExpenses) {
            Ui.printNoMatchingExpenseError();
        }
    }

    /**
     * Finds the {@link Expense} that contains the category as {@param expenseAttribute} and prints it out.
     * If there are no matching {@link Expense}, an error message will be printed.
     *
     * @param listOfCurrentExpenses {@link ArrayList} containing the list of expenses currently in the trip.
     * @param expenseAttribute @{@link String} containing the category that we want to find.
     */
    static void findMatchingCategoryExpenses(ArrayList<Expense> listOfCurrentExpenses,
                                                     String expenseAttribute) {
        boolean areThereExpenses = false;
        for (Expense e : listOfCurrentExpenses) {
            if (e.getCategory().equalsIgnoreCase(expenseAttribute)) {
                int index = listOfCurrentExpenses.indexOf(e);
                Ui.printFilteredExpenses(e, index);
                areThereExpenses = true;
            }
        }
        if (!areThereExpenses) {
            Ui.printNoMatchingExpenseError();
        }
    }
    //@@author

    //@@author lixiyuan416

    /**
     * Filters expenses inside a trip by person.
     * See parent method {@link Trip#getFilteredExpenses(String, String)}
     *
     * @param listOfCurrentExpenses list of expenses of a trip
     * @param personToSearchFor name of person
     */
    static void findMatchingPersonExpenses(ArrayList<Expense> listOfCurrentExpenses,
                                                   String personToSearchFor) {
        boolean areThereExpenses = false;
        for (Expense e : listOfCurrentExpenses) {
            boolean isExpenseToBeAdded = false;
            ArrayList<Person> personList = e.getPersonsList();
            for (Person p : personList) {
                if (p.getName().equalsIgnoreCase(personToSearchFor)) {
                    isExpenseToBeAdded = true;
                    break;
                }
            }
            if (isExpenseToBeAdded) {
                int index = listOfCurrentExpenses.indexOf(e);
                Ui.printFilteredExpenses(e, index);
                areThereExpenses = true;
            }
        }
        if (!areThereExpenses) {
            Ui.printNoMatchingExpenseError();
        }
    }

    private static boolean isDateValid(String inputDate) {
        try {
            if (Parser.doesDateReallyExist(inputDate)) {
                LocalDate.parse(inputDate, inputPattern);
                return true;
            }
            return false;
        } catch (DateTimeParseException e) {
            Storage.getLogger().log(Level.INFO, "Invalid date format entered");
            Ui.viewFilterDateFormatInvalid();
            return false;
        }
    }
}
