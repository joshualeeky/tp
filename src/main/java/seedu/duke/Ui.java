package seedu.duke;

import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.expense.Expense;
import seedu.duke.parser.Parser;
import seedu.duke.trip.Trip;

import java.util.ArrayList;
import java.util.HashMap;

public class Ui {

    public static final String USER_CONTINUE = "y";
    public static final String USER_QUIT = "n";

    public static void printOptimizedAmounts() throws ForceCancelException {
        boolean isAllPaid = true;
        Trip openTrip = Storage.getOpenTrip();
        ArrayList<Person> listOfPersons = openTrip.getListOfPersons();
        HashMap<String, Double> currentHashMap;
        String nameOfPersonPaying;
        String nameOfPersonReceiving;
        Double amountOwed;
        for (Person personPaying : listOfPersons) {
            for (Person personReceiving : listOfPersons) {
                nameOfPersonPaying = personPaying.getName();
                nameOfPersonReceiving = personReceiving.getName();
                currentHashMap = personPaying.getOptimizedMoneyOwed();
                amountOwed = currentHashMap.get(nameOfPersonReceiving);
                if (!personPaying.equals(personReceiving) && amountOwed < 0) {
                    if (isAllPaid) {
                        System.out.println("Here are the optimized payment transactions:");
                    }
                    System.out.println(nameOfPersonPaying + " needs to pay "
                            + stringForeignMoney(-amountOwed)
                            + " (" + stringRepaymentMoney(-amountOwed) + ")"
                            + " to " + personReceiving);
                    isAllPaid = false;
                }
            }
        }
        if (isAllPaid) {
            System.out.println("All are paid! :)");
        }
    }

    public static String receiveUserInput() throws ForceCancelException {
        String userInput = Storage.getScanner().nextLine().strip();
        if (Parser.doesUserWantToForceCancel(userInput)) {
            throw new ForceCancelException();
        }
        return userInput;
    }

    public static void printPendingCommand() {
        System.out.print("Enter your command: ");
    }

    public static void printWelcome() {
        String logo = System.lineSeparator()
                + "    ____              __  ___     ____             __  " + System.lineSeparator()
                + "   / __ \\____ ___  __/  |/  ___  / __ )____ ______/ /__" + System.lineSeparator()
                + "  / /_/ / __ `/ / / / /|_/ / _ \\/ __  / __ `/ ___/ //_/" + System.lineSeparator()
                + " / ____/ /_/ / /_/ / /  / /  __/ /_/ / /_/ / /__/ ,<   " + System.lineSeparator()
                + "/_/    \\__,_/\\__, /_/  /_/\\___/_____/\\__,_/\\___/_/|_|  " + System.lineSeparator()
                + "            /____/                                     " + System.lineSeparator();
        System.out.println("Welcome to" + logo);
    }

    public static void goodBye() {
        System.out.println("Exiting the program now. Goodbye!");
    }

    public static void newTripSuccessfullyCreated(Trip newTrip) {
        System.out.println("Your trip to " + newTrip.getLocation() + " on "
                + newTrip.getDateOfTripString() + " has been successfully added!");
    }


    //@@author joshualeeky
    public static String stringForeignMoney(double val) {
        try {
            return Storage.getOpenTrip().getForeignCurrency() + " "
                    + Storage.getOpenTrip().getForeignCurrencySymbol()
                    + String.format(Storage.getOpenTrip().getForeignCurrencyFormat(), val);
        } catch (ForceCancelException e) {
            printForceCancelled();
            return null;
        }
    }

    public static String stringRepaymentMoney(double val) {
        try {
            return Storage.getOpenTrip().getRepaymentCurrency() + " "
                    + Storage.getOpenTrip().getRepaymentCurrencySymbol()
                    + String.format(Storage.getOpenTrip().getRepaymentCurrencyFormat(),
                    val / Storage.getOpenTrip().getExchangeRate());
        } catch (ForceCancelException e) {
            printForceCancelled();
            return null;
        }
    }

    public static void askAutoAssignRemainder(Person person, double remainder) {
        System.out.print("Assign the remaining " + stringForeignMoney(remainder)
                + " to " + person.getName() + "? (y/n): ");
    }

    //@@author


    public static void printListOfPeople(ArrayList<Person> people) {
        for (Person person : people) {
            System.out.println("\t" + person.getName());
        }
    }

    public static void printExpenseDetails(Expense e) {
        System.out.println(e);
    }


    public static void printFilteredExpenses(Expense e, int index) {
        System.out.println((index + 1) + ". " + e);
    }

    public static void printExpenseAddedSuccess() {
        System.out.println("Your expense has been added successfully!");
    }

    public static void printExpensesInList(ArrayList<Expense> listOfExpenses) {
        if (listOfExpenses.isEmpty()) {
            printNoExpensesError();
            return;
        }
        System.out.println("List of Expenses: ");
        for (int i = 0; i < listOfExpenses.size(); i++) {
            System.out.print("\t");
            System.out.println(i + 1 + ". "
                    + listOfExpenses.get(i).getDescription() + " | "
                    + listOfExpenses.get(i).getStringDate());
        }
    }

    public static void printOpenTripMessage(Trip trip) {
        System.out.println("You have opened the following trip: "
                + System.lineSeparator()
                + trip.getLocation() + " | " + trip.getDateOfTripString());
        System.out.println();
    }

    public static void printCreateFormatError() {
        System.out.println("Please format your inputs as follows: "
                + System.lineSeparator()
                + "create /[place] /[date DD-MM-YYYY] /[currency ISO] /[exchange rate] /[persons-in-trip].");
        System.out.println("Separate person-in-trip with commas");
    }

    public static void printExpenseFormatError() {
        System.out.println("Please format your inputs as follows: "
                + System.lineSeparator()
                + "expense [amount] [category] [people] /[description].");
    }

    public static void printEditFormatError() {
        System.out.println("Please format your inputs as follows: "
                + System.lineSeparator()
                + "edit [trip num] [attribute] [new value]"
                + System.lineSeparator()
                + "attributes: -location, -date, -exchangerate, -forcur, -homecur"
                + System.lineSeparator());
    }


    public static void printFilterFormatError() {
        System.out.println("Please format your inputs as follows: "
                + System.lineSeparator()
                + "view filter [category, description, payer, person, date] [search keyword]");
    }


    public static void printExchangeRateFormatError() {
        System.out.print("Please re-enter your exchange rate as a decimal number (e.g. 1.32): ");
    }

    public static void printInvalidAmountError() {
        System.out.print("Please re-enter your expense amount as a positive number (i.e > 0): ");
    }

    public static void printDateTimeFormatError() {
        System.out.print("The entered date is invalid. Please enter the date again: ");
    }

    public static void dateInvalidError() {
        System.out.println("Sorry, the date you entered is invalid. Please enter the date again: ");
    }

    public static void printIsoFormatError() {
        System.out.print("Please re-enter your currency ISO (e.g. JPY, USD): ");
    }

    public static void printUnknownCommandError() {
        System.out.println("Sorry, we didn't recognize your entry. Please try again, or enter help "
                + "to learn more.");
    }

    public static void printSingleUnknownTripIndexError() {
        System.out.println("Invalid trip number, try again");
        System.out.println("Syntax: open [trip number]");
        System.out.println("--------------------------");
        printAllTrips();
        System.out.println("--------------------------");
    }

    public static void printUnknownTripIndexError() {
        System.out.println("Sorry, no such trip number exists. Please check your trip number and try again.");
    }

    public static void printUnknownExpenseIndexError() {
        System.out.println("Sorry, no such expense number exists. Please check your expense number and try again.");
    }

    public static void printNoTripError() {
        System.out.println("You have not created a trip yet. Please create a trip using the keyword 'create'.");
    }

    public static void printDeleteTripSuccessful(Trip tripDeleted) {
        System.out.println("Your trip to " + tripDeleted.getLocation() + " on "
                + tripDeleted.getDateOfTripString() + " has been successfully removed.");
    }

    public static void printDeleteExpenseSuccessful(Double expenseAmount) {
        System.out.println("Your expense of " + stringForeignMoney(expenseAmount) + " has been successfully removed.");
    }

    public static void printNoExpensesError() {
        System.out.println("There are no expenses in your trip, please add an expense using the keyword 'expense'.");
    }

    public static void printNoPersonFound(String string) {
        System.out.println("There are no persons with the name of [" + string + "] in this trip.");
    }

    public static void printSummaryFormatError() {
        System.out.println("Please format your inputs as follows: "
                + System.lineSeparator()
                + "\"summary\" or \"summary [person name]\".");
    }

    public static void printNoMatchingExpenseError() {
        System.out.println("No matching expenses found.");
    }

    public static void printNoOpenTripError() {
        System.out.println("You have not opened any trip yet. Please open a trip to proceed further.");
        printAllTrips();
    }

    //@@author itsleeqian
    public static void printAllTrips() {
        System.out.println("List of Trips: ");
        ArrayList<Trip> listOfTrips = Storage.getListOfTrips();
        for (int i = 0; i < listOfTrips.size(); i++) {
            System.out.print("\t");
            System.out.println(i + 1 + ". "
                    + listOfTrips.get(i).getLocation() + " | "
                    + listOfTrips.get(i).getDateOfTripString());
        }
    }
    //@@author

    public static void emptyArgForOpenCommand() {
        System.out.println();
        System.out.println("Which trip to open?");
        System.out.println("Syntax: open [trip number]");
        System.out.println("--------------------------");
        printAllTrips();
        System.out.println("--------------------------");

    }

    public static void argNotNumber() {
        System.out.println("Input is not a number");
    }

    public static void promptForTripIndex() {
        System.out.print("The number you entered is not valid, ");
    }

    public static void emptyArgForDeleteTripCommand() {
        System.out.println();
        System.out.println("Which trip to delete?");
        System.out.println("Syntax: delete [trip number]");
        System.out.println("---------------------------");
        printAllTrips();
        System.out.println("---------------------------");
    }

    public static void emptyArgForDeleteExpenseCommand() throws ForceCancelException {
        System.out.println();
        System.out.println("Which expense to delete?");
        System.out.println("Syntax: delete [expense number]");
        System.out.println("---------------------------");
        printExpensesInList(Storage.getOpenTrip().getListOfExpenses());
        System.out.println("---------------------------");
    }

    public static void invalidAmountFormat() {
        System.out.println("The syntax for amount you have entered is invalid. "
                + "Please format as follows: amount [person].");
    }

    public static void invalidArgForAmount() throws ForceCancelException {
        Trip currTrip = Storage.getOpenTrip();
        System.out.println("The person you entered is not in the opened trip.");
        System.out.println("These are the people involved in this trip:");
        Ui.printListOfPeople(currTrip.getListOfPersons());
        System.out.println();
    }


    public static void printGetPersonPaid() {
        System.out.print("Who paid for the expense?: ");
    }

    public static void printHowMuchDidPersonSpend(String name, double amountRemaining) {
        System.out.print("There is " + stringForeignMoney(amountRemaining) + " left to be assigned."
                + " How much did " + name + " spend?: ");
    }

    public static void printPersonNotInExpense() {
        System.out.println("The person you entered is not in the expense, please try again.");
    }

    //@@author joshualeeky
    public static void printAmount(Person person, Trip trip) {
        System.out.println(person.getName() + " spent "
                + stringForeignMoney(person.getMoneyOwed().get(person.getName()))
                + " (" + stringRepaymentMoney(person.getMoneyOwed().get(person.getName())) + ") on the trip so far");

        for (Person otherPerson : trip.getListOfPersons()) {
            if (otherPerson != person) {
                if (person.getMoneyOwed().get(otherPerson.getName()) > 0) {
                    System.out.println(otherPerson.getName() + " owes "
                            + stringForeignMoney(person.getMoneyOwed().get(otherPerson.getName()))
                            + " (" + stringRepaymentMoney(person.getMoneyOwed().get(otherPerson.getName())) + ")"
                            + " to " + person.getName());
                } else if (person.getMoneyOwed().get(otherPerson.getName()) < 0) {
                    System.out.println(person.getName() + " owes "
                            + stringForeignMoney(-person.getMoneyOwed().get(otherPerson.getName()))
                            + " (" + stringRepaymentMoney(-person.getMoneyOwed().get(otherPerson.getName())) + ")"
                            + " to " + otherPerson.getName());
                } else {
                    System.out.println(person.getName() + " does not owe anything to " + otherPerson.getName());
                }
            }
        }
    }
    //@@author

    public static void printIncorrectAmount(double amount) {
        System.out.println("The amount you have entered is not possible. The total "
                + "of the expense should equal " + stringForeignMoney(amount));
    }

    public static void printPeopleInvolved(ArrayList<Person> personArrayList) {
        System.out.println("These are the people who are involved in the expense: ");
        printListOfPeople(personArrayList);
    }

    //@@author lixiyuan416
    public static void displayHelp() {
        if (Storage.getListOfTrips().isEmpty()) {
            System.out.println("You have no trips! Create one to get started!");
            System.out.println();
            System.out.println("Syntax: create /[location] /[date dd-mm-yyyy] "
                    + "/[foreign-currency-ISO-code] /[exchange-rate] /[persons-in-trip]");
            System.out.println("\t Separate each person-in-trip with commas");
            System.out.println();
            System.out.println("quit: exit the program");
            System.out.println();
        } else if (!Storage.checkOpenTrip()) {
            System.out.println("You have trips, but have not opened any");
            System.out.println();
            System.out.println("list: list all your trips");
            System.out.println("open [trip number]: Open a trip");
            System.out.println("delete [trip number]: Delete a trip");
            System.out.println();
            System.out.println("create /[location] /[date dd-mm-yyyy] "
                    + "/[foreign-currency-ISO-code] /[exchange-rate] /[persons-in-trip]: Creates a trip");
            System.out.println("Separate persons-in-trip with commas");
            System.out.println();
            System.out.println("edit [trip num] [attribute] [new value]: edit trip attributes");
            System.out.println("\tattributes: -location, -date, -exchangerate, -forcur, -homecur");
            System.out.println("\tNote the hyphen in the attribute");
            System.out.println("\tlast can be used for [trip num]");
            System.out.println();
            System.out.println("quit: exit the program");
            System.out.println();
        } else {
            System.out.println("You are inside a trip. Here is a list of what you can do:");
            System.out.println();
            System.out.println("\texpense: creates an expense");
            System.out.println("\t\texpense [amount] [category] [people] /[description]");
            System.out.println("\t\t Separate each person with a comma");
            System.out.println();
            System.out.println("\tlist: List all expenses of the trip");
            System.out.println();
            System.out.println("\tpeople: List of persons in the trip");
            System.out.println();
            System.out.println("\tdelete [expense num]: delete an expense");
            System.out.println("\t\t\"delete last\" to delete last expense");
            System.out.println();
            System.out.println("\tview filter [options] [search keyword]: list filtered expenses.");
            System.out.println("\t\tfilter options: [category, description, payer, person, date]");
            System.out.println();
            System.out.println("\tview -[index]: View expense in detail");
            System.out.println("\t\tViews all expenses if index not provided. \"view last\" to view last expense");
            System.out.println();
            System.out.println("\tsummary -[name]: Shows all much a person has spent on this trip in total.");
            System.out.println("\t\tDisplays summary for everyone in the trip if index not provided");
            System.out.println();
            System.out.println("\toptimize: Displays who-pays-who instructions to "
                    + "settle expense repayment at the end of the trip");
            System.out.println("\tamount [person]: Displays who-pays-who instructions for one person, unoptimized");
            System.out.println();
            System.out.println("\tclose: Closes the current trip");
            System.out.println("\topen [trip num]: Closes the current trip, opens another trip");
            System.out.println();
            System.out.println("\tYou can also create or edit a trip, "
                    + "but it's recommended to close the current trip first");
            System.out.println("\tquit: exit the program");
            System.out.println();
        }
    }
    //@@author

    public static void printInvalidFilterError() {
        System.out.println("Please filter using the following valid filter attributes: "
                + System.lineSeparator()
                + "[category], [description], [payer], [person]");
    }

    public static void printFileNotFoundError() {
        System.out.println("No preloaded data found! We have created a file for you.");
    }

    public static void printJsonParseError() {
        System.out.println("We couldn't read your save file. It may be corrupted, "
                + "or may have been wrongly modified outside the program.");
        System.out.println("To overwrite your current save file and start with a new save file, enter 'y'. "
                + "Otherwise, enter 'n' to exit the program.");
        System.out.println("IMPORTANT: if you start with a new save file, your previous data will be erased. "
                + "This operation is irreversible.");
    }

    public static void printJsonParseUserInputPrompt() {
        System.out.print("Would you like to overwrite your save file? (y/n): ");
    }

    public static void printErrorWithInitialAmount() {
        System.out.println("Please check the amount you entered for the expense or "
                + "the amount you allocated to each person again.");
    }

    public static void sameNameInTripError() {
        System.out.println("You have entered people with the same name, please recreate the trip ensuring there are no "
                + "repeated names for the trip.");
    }

    public static void sameNameInExpenseError() {
        System.out.println("You have entered people with the same name.");
        System.out.println("Please reenter the names of the participants of the expense, "
                + "ensuring there are no repeats:");
    }

    public static void printNoLastTripError() {
        System.out.println("You may have deleted the most recently modified trip. "
                + "Please try again with the trip number of the trip you wish to edit.");
    }

    public static void printCreateFileFailure() {
        System.out.println("The save file could not be created. Exiting the program now...");
    }

    public static void newFileSuccessfullyCreated() {
        System.out.println("A new save file has been created!");
    }

    public static void printEmptyFileWarning() {
        System.out.println("A save file was found, but it is empty.");
        System.out.println("If you wish to recover the contents of your save file, please exit the program now.");
        System.out.println("Otherwise, you may continue to use the program.");
    }

    public static void printInvalidPeople(ArrayList<String> names) throws ForceCancelException {
        final Trip currTrip = Storage.getOpenTrip();
        for (String name : names) {
            if (names.indexOf(name) == names.size() - 1) {
                System.out.print(name);
            } else if (names.indexOf(name) == names.size() - 2) {
                System.out.print(name + " and ");
            } else if (names.indexOf(name) < names.size() - 2) {
                System.out.print(name + ", ");
            }
        }
        if (names.size() == 1) {
            System.out.print(" is ");
        } else {
            System.out.print(" are ");
        }
        System.out.println("not part of the trip.");
        System.out.println("These are the names of the people who are part of the trip:");
        printListOfPeople(currTrip.getListOfPersons());
        System.out.println("Please enter the names of the people who are involved in this expense again, "
                + "separated by a comma:");
    }

    public static void printTripClosed(Trip trip) {
        System.out.println("You have closed the following trip:"
                + System.lineSeparator()
                + trip.getLocation() + " | " + trip.getDateOfTripString());
    }

    //@@author lixiyuan416
    public static void equalSplitPrompt() {
        System.out.println("Enter \"equal\" if expense is to be evenly split, enter individual spending otherwise");
    }

    public static void askUserToConfirm() {
        System.out.print("There will be people involved that don't need to pay, are you sure? (y/n): ");
    }

    public static void expenseDateInvalid() {
        System.out.println("\tPlease enter date as DD-MM-YYYY, or enter nothing to use today's date");
    }

    public static void expensePromptDate() {
        System.out.println("Enter date of expense:");
        System.out.println("\tPress enter to use today's date");
    }

    public static void noRecentExpenseError() {
        System.out.println("You have not recently added an expense.");
    }

    public static void viewFilterDateFormatInvalid() {
        System.out.println("\tPlease enter date as DD-MM-YYYY");
    }
    //@@author

    public static void changeForeignCurrencySuccessful(Trip tripToEdit, String original) {
        System.out.println("Your foreign spending currency has been changed from "
                + original + " to " + tripToEdit.getForeignCurrency() + ".");
    }

    public static void changeHomeCurrencySuccessful(Trip tripToEdit, String original) {
        System.out.println("Your home currency has been changed from "
                + original + " to " + tripToEdit.getRepaymentCurrency() + ".");
    }

    public static void changeExchangeRateSuccessful(Trip tripToEdit, double original) {
        System.out.println("The exchange rate has been changed from "
                + original + " to " + tripToEdit.getExchangeRate() + ".");
    }

    public static void changeDateSuccessful(Trip tripToEdit, String original) {
        System.out.println("The date of your trip has been changed from "
                + original + " to " + tripToEdit.getDateOfTripString() + ".");
    }

    public static void changeLocationSuccessful(Trip tripToEdit, String original) {
        System.out.println("The location of your trip has been changed from "
                + original + " to " + tripToEdit.getLocation() + ".");
    }

    public static void printCouldNotSaveMessage() {
        System.out.println("Sorry, there was an error saving your data. We'll try to save your data again"
                + "the next time you enter a command.");
    }

    public static void printFileLoadedSuccessfully() {
        System.out.println();
        System.out.println("Your saved data was successfully loaded!");
        System.out.println();
    }

    public static void printForceCancelled() {
        System.out.println("You have chosen to cancel this operation.");
    }

    public static void locationIsBlank() {
        System.out.println("No location was entered. Please enter your trip location: ");
    }

    public static void noPersonsAdded() {
        System.out.println("No persons were added to this trip. Please enter the names of the people in this trip: ");
    }

    public static void duplicateTripWarning() {
        System.out.println("A trip with similar information may already exist. Please confirm if you wish to proceed"
                + " with creating this trip.");
        System.out.print("Enter 'y' if you wish to create this trip, or 'n' to cancel: ");
    }
}
