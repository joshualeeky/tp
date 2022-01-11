package seedu.duke.parser;

import seedu.duke.Person;
import seedu.duke.Storage;
import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.trip.Trip;
import seedu.duke.Ui;
import seedu.duke.expense.Expense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//@@author itsleeqian
public interface ExpenseSummarizer {

    /**
     * Prints the summary of expenses in a trip for a Person.
     * @param person The person to print for.
     * @throws ForceCancelException allows the user to cancel an operation when an input is required.
     */
    static void getIndividualExpenseSummary(Person person) throws ForceCancelException {
        double currentAmount; //amount paid for current expense
        double totalAmountSpent = 0;
        double totalAmountSpentInLocalCurrency = 0;
        Trip currTrip = Storage.getOpenTrip();
        ArrayList<Expense> listOfExpenses = currTrip.getListOfExpenses();
        int expensesInvolved = 0; //num of expenses involved
        HashMap<String, Double> categoriesSplit = new HashMap<>(); //contains the amount spent in each category
        for (Expense e : listOfExpenses) {
            if (containsPerson(e.getPersonsList(), person.getName())) {
                currentAmount = e.getAmountSplit().get(person.getName());
                String currentCategory = e.getCategory();
                totalAmountSpent += currentAmount;
                expensesInvolved++;
                //the following if else is to update the category/amtSpent hashmap
                if (!categoriesSplit.containsKey(currentCategory)) {
                    categoriesSplit.put(currentCategory, currentAmount);
                } else {
                    double updatedValue = categoriesSplit.get(currentCategory) + currentAmount;
                    categoriesSplit.put(currentCategory, updatedValue);
                }
            }
        }
        totalAmountSpentInLocalCurrency = roundToLocal(totalAmountSpentInLocalCurrency, currTrip, categoriesSplit);
        System.out.println(person + " has spent "
                + Ui.stringForeignMoney(totalAmountSpent)
                + " (" + currTrip.getRepaymentCurrency() + " "
                + currTrip.getRepaymentCurrencySymbol()
                + String.format(currTrip.getRepaymentCurrencyFormat(), totalAmountSpentInLocalCurrency) + ") on "
                + expensesInvolved
                + " expenses on the following categories:");
        for (Map.Entry<String, Double> set : categoriesSplit.entrySet()) {
            System.out.println(set.getKey() + ": " + Ui.stringForeignMoney(set.getValue())
                    + " (" + Ui.stringRepaymentMoney(set.getValue()) + ")");
        }
    }

    /**
     * Helper method for getIndividualExpenseSummary() method.
     * Returns the rounded and formatted total repayment amount spent.
     * @param totalAmount the amount before rounding
     * @param currTrip the Trip the user is in/computing
     * @param categoriesSplit the HashMap containing the category and the amount spent on said category
     * @return a rounded and formatted value for amount spent in local currency
     */
    private static double roundToLocal(double totalAmount, Trip currTrip, HashMap<String, Double> categoriesSplit)
            throws ForceCancelException {
        for (Map.Entry<String, Double> set : categoriesSplit.entrySet()) {
            totalAmount += Storage.formatRepaymentMoneyDouble(
                    set.getValue() / currTrip.getExchangeRate());
        }
        return totalAmount;
    }

    /**
     * Returns true if personsList contains a person with a specific name.
     * This is to replace the list.contains() method due to bugs with json deserialization.
     *
     * @param personsList list of persons to check
     * @param name the name to check for
     * @return true if personsList contains a person with a specific name
     */
    private static boolean containsPerson(ArrayList<Person> personsList, String name) {
        for (Person person : personsList) {
            if (person.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    //@@author
}
