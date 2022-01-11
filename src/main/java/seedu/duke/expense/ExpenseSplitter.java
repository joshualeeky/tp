package seedu.duke.expense;

import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.parser.Parser;
import seedu.duke.Person;
import seedu.duke.Storage;
import seedu.duke.Ui;

import java.util.HashMap;

public interface ExpenseSplitter {
    double EPSILON = 0.001;

    //@@author joshualeeky

    /**
     * If there is only one person that is part of the expense, the function automatically assigns the amount spent
     * on the expense to the person in the expense.
     * @param expense Expense that is being added into the current trip.
     * @param person Person who is part of the trip.
     */
    static void updateOnePersonSpending(Expense expense, Person person) {
        person.setMoneyOwed(person, expense.getAmountSpent());
        expense.setPayer(person);
        expense.setAmountSplit(person, expense.getAmountSpent());
    }

    /**
     * Updates the spending of each individual who is entered into the expense.
     * @param expense Expense that is being added.
     * @throws ForceCancelException Cancel the creation of the expense anytime an input is required by the user.
     */
    static void updateIndividualSpending(Expense expense) throws ForceCancelException {
        Person payer = getValidPersonInExpenseFromString(expense);
        expense.setPayer(payer);
        HashMap<Person, Double> amountBeingPaid = new HashMap<>();
        Ui.equalSplitPrompt();
        double total = 0.0;
        for (Person person : expense.getPersonsList()) {
            double amountRemaining = expense.getAmountSpent() - total;
            if (amountRemaining < EPSILON) {
                assignZeroToRemaining(expense, amountBeingPaid, payer);
                return;
            } else if (checkLastPersonInExpense(expense, person)) {
                assignRemainder(person, payer, amountRemaining, expense, amountBeingPaid);
                return;
            } else {
                amountBeingPaid = assignNormal(person, payer, amountRemaining, total, amountBeingPaid, expense);
            }
            if (amountBeingPaid != null) {
                total += amountBeingPaid.get(person);
            } else {
                return;
            }
        }
    }

    /**
     * Helper function for updateIndividualSpending(). Updates the amountBeingPaid HashMap while checking if the amount
     * entered is higher than the amount spent for the expense as well as checking if the value entered is valid.
     * @param person the current Person who is being assigned a value
     * @param payer the Person who paid for the Expense
     * @param amountRemaining the amount of money left to be assigned
     * @param total the total amount of money that has been assigned
     * @param amountBeingPaid HashMap of the amount each Person is assigned
     * @param expense the expense that is being added to the current Trip.
     * @return updated amountBeingPaid HashMap.
     * @throws ForceCancelException if the user chooses to cancel the operation.
     */
    private static HashMap<Person, Double> assignNormal(Person person, Person payer, double amountRemaining,
            double total, HashMap<Person, Double> amountBeingPaid, Expense expense) throws ForceCancelException {
        Ui.printHowMuchDidPersonSpend(person.getName(), amountRemaining);
        String amountString = Ui.receiveUserInput();
        if (checkAssignEqual(amountBeingPaid, amountString)) {
            assignEqualAmounts(payer, expense, amountBeingPaid);
            return null;
        } else {
            try {
                double amount = Double.parseDouble(amountString);
                amount = Storage.formatForeignMoneyDouble(amount);
                total += amount;
                if (total > expense.getAmountSpent() || amount < 0) {
                    Ui.printIncorrectAmount(expense.getAmountSpent());
                    updateIndividualSpending(expense);
                    return null;
                } else {
                    amountBeingPaid.put(person, amount);
                    return amountBeingPaid;
                }
            } catch (NumberFormatException e) {
                Ui.argNotNumber();
                return assignNormal(person, payer, amountRemaining, total, amountBeingPaid, expense);
            }
        }
    }

    /**
     * Helper function for updateIndividualSpending(). checks if the user input is 'equal' to indicate that the user
     * would like to split the expense equally among the people involved in the expense. Also checks to ensure that
     * the command being entered is before the user enters any amount for any person.
     * @param amountBeingPaid Hashmap containing the people who the user has assigned values to.
     * @param amountString the user input.
     * @return true or false boolean value to indicate if the user has used 'equal'.
     */
    private static boolean checkAssignEqual(HashMap<Person, Double> amountBeingPaid, String amountString) {
        return amountString.equalsIgnoreCase("equal") && amountBeingPaid.isEmpty();
    }

    /**
     * Helper function for updateIndividualSpending(). Checks if a Person is the last to be assigned an amount
     * in the expense.
     * @param expense Expense that is being checked.
     * @param person Person to be checked.
     * @return true or false boolean value to indicate if it is the last person in the expense.
     */
    private static boolean checkLastPersonInExpense(Expense expense, Person person) {
        return expense.getPersonsList().indexOf(person) == expense.getPersonsList().size() - 1;
    }

    /**
     * Helper function for updateIndividualSpending(). Assigns the rest of the Persons the amount 0 if the amount
     * for the expense has been fully paid for already. Requires user confirmation.
     * @param expense Expense that is being added to the current trip.
     * @param amountBeingPaid HashMap containing the people who the user has assigned values to.
     * @param payer Person who is paying for the expense.
     * @throws ForceCancelException Cancels the creation of the expense in the event the user wishes to
     *     stop creating the expense.
     */
    private static void assignZeroToRemaining(Expense expense, HashMap<Person, Double> amountBeingPaid, Person payer)
            throws ForceCancelException {
        Ui.askUserToConfirm();
        if (Parser.getUserToConfirm()) {
            for (Person person : expense.getPersonsList()) {
                if (!amountBeingPaid.containsKey(person)) {
                    amountBeingPaid.put(person, 0d);
                }
            }
            assignAmounts(payer, expense,amountBeingPaid);
        } else {
            Ui.printErrorWithInitialAmount();
            updateIndividualSpending(expense);
        }
    }

    /**
     * Helper function for updateIndividualSpending(). Assigns the remaining amount of the expense to the last person
     * in the expense list after receiving approval from the user.
     * @param person the Person who is to be assigned the amount.
     * @param payer the Person who paid for the expense.
     * @param amountRemaining the amount to be assigned to the last person.
     * @param expense the expense that is being added.
     * @param amountBeingPaid the HashMap containing how much each Person spent on the expense.
     * @throws ForceCancelException allows the user to cancel the command.
     */
    private static void assignRemainder(Person person, Person payer, double amountRemaining, Expense expense,
                                        HashMap<Person, Double> amountBeingPaid) throws ForceCancelException {
        Ui.askAutoAssignRemainder(person, amountRemaining);
        if (Parser.getUserToConfirm()) {
            amountBeingPaid.put(person, Storage.formatForeignMoneyDouble(amountRemaining));
            assignAmounts(payer, expense, amountBeingPaid);
        } else {
            Ui.printErrorWithInitialAmount();
            updateIndividualSpending(expense);
        }
    }

    /**
     * Helper function for updateIndividualSpending(). Checks the input String from the user to ensure that the name
     * entered is of a person who is part of the Expense.
     * @param expense Expense that is being checked.
     * @return Person who is associated to the String that was input by the user, if the user input is invalid and the
     *     String is not of a name of a Person in the Expense, the function will ask the user for an input again.
     * @throws ForceCancelException Allows the user to cancel anytime there is an input required by the user.
     */
    private static Person getValidPersonInExpenseFromString(Expense expense) throws ForceCancelException {
        Ui.printGetPersonPaid();
        String name = Ui.receiveUserInput();
        for (Person person : expense.getPersonsList()) {
            if (name.equalsIgnoreCase(person.getName())) {
                return person;
            }
        }
        Ui.printPersonNotInExpense();
        Ui.printPeopleInvolved(expense.getPersonsList());
        return getValidPersonInExpenseFromString(expense);
    }

    /**
     * Helper function for updateIndividualSpending(). Divides the amount spent on the expense equally among all the
     * participants in the Expense. If there is a deficit or surplus, the payer will bear the extra or loss.
     * @param payer Person who is paying for the Expense.
     * @param expense Expense that is being added to the current Trip.
     * @param amountBeingPaid The amount that is being paid for the Expense.
     */
    private static void assignEqualAmounts(Person payer, Expense expense, HashMap<Person, Double> amountBeingPaid)
            throws ForceCancelException {
        double total = 0.0;
        double amount = Storage.formatForeignMoneyDouble(expense.getAmountSpent() / expense.getPersonsList().size());
        for (Person people : expense.getPersonsList()) {
            amountBeingPaid.put(people, amount);
            total += amount;
        }
        if (total != expense.getAmountSpent()) {
            double payerAmount = amountBeingPaid.get(payer) + (expense.getAmountSpent() - total);
            amountBeingPaid.put(payer, payerAmount);
        }
        assignAmounts(payer, expense, amountBeingPaid);
    }

    /**
     * Helper function for updateIndividualSpending(). Stores the value that each Person is assigned in their own Person
     * class to be used for other functions, also stores the value each Person owes the payer of the Expense.
     * @param payer Person who paid for the Expense.
     * @param expense The Expense that is being added to the current Trip.
     * @param amountBeingPaid HashMap containing the people who the user has assigned values to.
     */
    private static void assignAmounts(Person payer, Expense expense, HashMap<Person, Double> amountBeingPaid) {
        for (Person person : expense.getPersonsList()) {
            if (person == payer) {
                person.setMoneyOwed(person, amountBeingPaid.get(person));
                expense.setAmountSplit(person, amountBeingPaid.get(person));
                continue;
            }
            payer.setMoneyOwed(person, amountBeingPaid.get(person));
            person.setMoneyOwed(payer, -amountBeingPaid.get(person));
            person.setMoneyOwed(person, amountBeingPaid.get(person));
            expense.setAmountSplit(person, amountBeingPaid.get(person));
        }
    }
}
