package seedu.duke.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seedu.duke.Person;
import seedu.duke.Storage;
import seedu.duke.Ui;
import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.exceptions.SameNameException;
import seedu.duke.expense.Expense;
import seedu.duke.trip.Trip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


class CommandExecutorTest {
    private static final DateTimeFormatter inputPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static Trip trip;

    @BeforeAll
    static void setUp() throws SameNameException, ForceCancelException {
        String[] stringArray = {"", "Canada", "02-03-2021", "cad", "0.123", "ben,jerry,tom"};
        trip = new Trip(stringArray);
        ArrayList<Trip> listOfTrips = new ArrayList<>();
        listOfTrips.add(trip);
        Storage.setListOfTrips(listOfTrips);
        Storage.setOpenTrip(0);
        ArrayList<Person> listOfPersons = trip.getListOfPersons();
        Person person1 = listOfPersons.get(0);
        Person person2 = listOfPersons.get(1);
        ArrayList<Person> listOfPersons1 = new ArrayList<>();
        listOfPersons1.add(person1);
        listOfPersons1.add(person2);
        HashMap<String, Double> amountSplit1 = new HashMap<>();
        Expense exp1 = new Expense(8.00, "chicken nuggets", listOfPersons1, "food",
                LocalDate.parse("11-02-2021", inputPattern), person1, amountSplit1);
        exp1.setAmountSplit(person1, 0.0);
        exp1.setAmountSplit(person2, 8.0);
        person1.setMoneyOwed(person1, -8.0);
        person1.setMoneyOwed(person2, 8.0);
        person2.setMoneyOwed(person1, -8.0);
        HashMap<String, Double> amountSplit2 = new HashMap<>();
        Expense exp2 = new Expense(16.00, "chicken", listOfPersons, "food",
                LocalDate.parse("11-02-2021", inputPattern), person2, amountSplit2);
        exp2.setAmountSplit(person1, 0.0);
        exp2.setAmountSplit(person2, 10.0);
        Person person3 = listOfPersons.get(2);
        exp2.setAmountSplit(person3, 6.0);
        person2.setMoneyOwed(person2, -16.0);
        person2.setMoneyOwed(person1, 10.0);
        person1.setMoneyOwed(person2, -10.0);
        person2.setMoneyOwed(person3, 6.0);
        person3.setMoneyOwed(person2, -6.0);
        trip.addExpense(exp1);
        trip.addExpense(exp2);

    }

    @Test
    void testViewAllExpenses() {
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        String expectedOutput = "List of all Expenses in detail: " + System.lineSeparator()
                        + "1. " + trip.getListOfExpenses().get(0).toString() + System.lineSeparator()
                        + "2. " + trip.getListOfExpenses().get(1).toString() + System.lineSeparator();
        trip.viewAllExpenses();
        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    void testViewFilterByCategory() {
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        String input = "view filter category food";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Storage.setScanner(new Scanner(System.in));
        String expectedOutput = "1. " + trip.getListOfExpenses().get(0).toString() + System.lineSeparator()
                + "2. " + trip.getListOfExpenses().get(1).toString() + System.lineSeparator();
        Parser.parseUserInput(input);
        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    void testViewFilterByPayer() {
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        String input = "view filter payer ben";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Storage.setScanner(new Scanner(System.in));
        String expectedOutput = "1. " + trip.getListOfExpenses().get(0).toString() + System.lineSeparator();
        Parser.parseUserInput(input);
        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    void testViewFilterByDescription() {
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        String input = "view filter description nuggets";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Storage.setScanner(new Scanner(System.in));
        String expectedOutput = "1. " + trip.getListOfExpenses().get(0).toString() + System.lineSeparator();
        Parser.parseUserInput(input);
        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    void testViewFilterByPersons() {
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        String input = "view filter person tom";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Storage.setScanner(new Scanner(System.in));
        String expectedOutput = "2. " + trip.getListOfExpenses().get(1).toString() + System.lineSeparator();
        Parser.parseUserInput(input);
        assertEquals(expectedOutput, actualOutput.toString());
    }


}