package seedu.duke.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import seedu.duke.Person;
import seedu.duke.Storage;
import seedu.duke.exceptions.ForceCancelException;
import seedu.duke.exceptions.SameNameException;
import seedu.duke.trip.Trip;
import seedu.duke.expense.Expense;
import java.time.LocalDate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


class PaymentOptimizerTest {

    static Expense exp1;
    static Expense exp2;
    static Expense exp3;
    private static final DateTimeFormatter inputPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @BeforeAll
    static void setUp() throws SameNameException, ForceCancelException {
        String[] stringArray = {"", "Canada", "02-03-2021", "cad", "0.123", "ben,jerry,tom"};
        Trip trip = new Trip(stringArray);
        ArrayList<Trip> listOfTrips = new ArrayList<>();
        listOfTrips.add(trip);
        Storage.setListOfTrips(listOfTrips);
        Storage.setOpenTrip(0);
        ArrayList<Person> listOfPersons = trip.getListOfPersons();
        Person person1 = listOfPersons.get(0);
        Person person2 = listOfPersons.get(1);
        HashMap<String, Double> amountSplit1 = new HashMap<>();
        Expense exp1 = new Expense(8.00, "chicken nuggers", listOfPersons, "food",
                LocalDate.parse("11-02-2021", inputPattern), person1, amountSplit1);
        exp1.setPayer(person1);
        person1.setMoneyOwed(person1, -8.0);
        person1.setMoneyOwed(person2, 5.0);
        person2.setMoneyOwed(person1, -5.0);
        Person person3 = listOfPersons.get(2);
        person1.setMoneyOwed(person3, 3.0);
        person3.setMoneyOwed(person1, -3.0);
        exp1.setAmountSplit(person2, 4.0);
        exp1.setAmountSplit(person3, 4.0);
        HashMap<String, Double> amountSplit2 = new HashMap<>();
        Expense exp2 = new Expense(16.00, "chicken", listOfPersons, "food",
                LocalDate.parse("11-02-2021", inputPattern), person2, amountSplit2);
        exp2.setAmountSplit(person2, 10.0);
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
    public void testOptimizePayments() throws ForceCancelException {
        Trip openTrip = Storage.getOpenTrip();
        Person person1 = openTrip.getListOfPersons().get(0);
        PaymentOptimizer.optimizePayments(openTrip);
        HashMap<String, Double> hashMapBen = person1.getOptimizedMoneyOwed();
        assertEquals(hashMapBen.get("jerry"), -2.0);
        Person person2 = openTrip.getListOfPersons().get(1);
        HashMap<String, Double> hashMapJerry = person2.getOptimizedMoneyOwed();
        assertEquals(hashMapJerry.get("ben"), 2.0);
        assertEquals(hashMapJerry.get("tom"), 9.0);
        Person person3 = openTrip.getListOfPersons().get(2);
        HashMap<String, Double> hashMapTom = person3.getOptimizedMoneyOwed();
        assertEquals(hashMapTom.get("jerry"), -9.0);


    }


}