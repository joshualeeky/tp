package seedu.duke.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import seedu.duke.Person;
import seedu.duke.Storage;
import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.exceptions.SameNameException;
import seedu.duke.trip.Trip;
import seedu.duke.expense.Expense;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ExpenseSummaryTest {

    private static final DateTimeFormatter inputPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static Trip trip;

    @BeforeAll
    static void setUp() throws SameNameException, ForceCancelException {
        String[] stringArray = {"", "Canada", "02-03-2021", "cad", "1.08", "ben,jerry,tom"};
        trip = new Trip(stringArray);
        ArrayList<Trip> listOfTrips = new ArrayList<>();
        listOfTrips.add(trip);
        Storage.setListOfTrips(listOfTrips);
        Storage.setOpenTrip(Storage.getListOfTrips().indexOf(trip));
        ArrayList<Person> listOfPersons = trip.getListOfPersons();
        Person person1 = listOfPersons.get(0);
        Person person2 = listOfPersons.get(1);
        ArrayList<Person> listOfPersons1 = new ArrayList<>();
        listOfPersons1.add(person1);
        listOfPersons1.add(person2);
        HashMap<String, Double> amountSplit1 = new HashMap<>();
        Expense exp1 = new Expense(8.00, "chicken nuggets", listOfPersons1, "food",
                LocalDate.parse("11-02-2021", inputPattern), person1, amountSplit1);
        exp1.setAmountSplit(person1, 4.0);
        exp1.setAmountSplit(person2, 4.0);
        person1.setMoneyOwed(person1, -8.0);
        person1.setMoneyOwed(person2, 4.0);
        person2.setMoneyOwed(person1, -4.0);
        HashMap<String, Double> amountSplit2 = new HashMap<>();
        Expense exp2 = new Expense(16.00, "travel to hotel", listOfPersons, "travel",
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
    public void testExpenseSummary_individual() throws SameNameException, ForceCancelException {
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        String input = "summary ben";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Storage.setScanner(new Scanner(System.in));
        String expectedOutput = "ben has spent CAD $4.00 (SGD $3.70) on 2 expenses on the following categories:"
                + System.lineSeparator() + "travel: CAD $0.00 (SGD $0.00)"
                + System.lineSeparator() + "food: CAD $4.00 (SGD $3.70)"
                + System.lineSeparator();
        Parser.parseUserInput(input);
        assertEquals(expectedOutput, actualOutput.toString());

    }

    @Test
    public void testExpenseSummary_invalidInput() throws SameNameException, ForceCancelException {
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        String input = "summary 1";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Storage.setScanner(new Scanner(System.in));
        String expectedOutput = "There are no persons with the name of [1] in this trip."
                + System.lineSeparator() + "Please format your inputs as follows: "
                + System.lineSeparator() + "\"summary\" or \"summary [person name]\"."
                + System.lineSeparator();
        Parser.parseUserInput(input);
        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    public void testExpenseSummary_full() throws SameNameException, ForceCancelException {
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        String input = "summary";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Storage.setScanner(new Scanner(System.in));
        String expectedOutput = "ben has spent CAD $4.00 (SGD $3.70) on 2 expenses on the following categories:"
                + System.lineSeparator() + "travel: CAD $0.00 (SGD $0.00)"
                + System.lineSeparator() + "food: CAD $4.00 (SGD $3.70)"
                + System.lineSeparator() + System.lineSeparator()
                + "jerry has spent CAD $14.00 (SGD $12.96) on 2 expenses on the following categories:"
                + System.lineSeparator() + "travel: CAD $10.00 (SGD $9.26)"
                + System.lineSeparator() + "food: CAD $4.00 (SGD $3.70)"
                + System.lineSeparator() + System.lineSeparator()
                + "tom has spent CAD $6.00 (SGD $5.56) on 1 expenses on the following categories:"
                + System.lineSeparator() + "travel: CAD $6.00 (SGD $5.56)"
                + System.lineSeparator() + System.lineSeparator();
        Parser.parseUserInput(input);
        assertEquals(expectedOutput, actualOutput.toString());
    }
}
