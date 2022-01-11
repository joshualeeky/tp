//@@author leeyikai

package seedu.duke.parser;

import seedu.duke.Person;
import seedu.duke.trip.Trip;

import java.util.ArrayList;
import java.util.HashMap;

class PaymentOptimizer {
    static double EPSILON = 0.001;

    /**
     * Calls different methods that helps to calculate the optimized payments.
     *
     * @param trip {@link Trip} that you want to optimize the payments for.
     */
    static void optimizePayments(Trip trip) {
        ArrayList<Person> listOfPersons = trip.getListOfPersons();
        ArrayList<Double> totalExpenses = new ArrayList<>();
        boolean isAllPaid = false;
        getTotalAmountForPerson(totalExpenses, listOfPersons);
        int currentIndex;
        while (!isAllPaid) {
            for (Person person : listOfPersons) {
                currentIndex = listOfPersons.indexOf(person);
                if (totalExpenses.get(currentIndex) < 0) {
                    findNextPersonToPay(totalExpenses, currentIndex, listOfPersons);
                }
            }
            isAllPaid = checkIfAllPaid(totalExpenses);
        }
    }

    /**
     * Checks if every {@link Person} in the currently opened {@link Trip} has been paid.
     *
     * @param totalExpenses {@link ArrayList} containing the net total expenses of each person.
     * @return true if every {@link Person} in the current {@link Trip} has been paid.
     */
    private static boolean checkIfAllPaid(ArrayList<Double> totalExpenses) {
        for (Double i : totalExpenses) {
            if (!isZero(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the next {@link Person} to pay that is being owed money. Returns only when every single {@link Person}
     * in the {@link Trip} has been paid.
     *
     * @param totalExpenses {@link ArrayList} containing the net total expenses of each person.
     * @param indexOfPersonPaying index of the person who will be paying.
     * @param listOfPersons list of persons in the currently opened trip
     */
    private static void findNextPersonToPay(ArrayList<Double> totalExpenses, int indexOfPersonPaying,
            ArrayList<Person> listOfPersons) {
        Double expensesOfCurrentPerson;
        Double expensesOfPersonPaying;
        Person personPaying;
        Person personReceiving;
        String nameOfPersonPaying;
        String nameOfPersonReceiving;
        for (Person person : listOfPersons) {
            int indexOfPersonReceiving = listOfPersons.indexOf(person);
            expensesOfCurrentPerson = totalExpenses.get(indexOfPersonReceiving);
            expensesOfPersonPaying = -totalExpenses.get(indexOfPersonPaying);
            personPaying = listOfPersons.get(indexOfPersonPaying);
            personReceiving = listOfPersons.get(indexOfPersonReceiving);
            nameOfPersonPaying = personPaying.getName();
            nameOfPersonReceiving = personReceiving.getName();
            if (totalExpenses.get(indexOfPersonReceiving) > 0) {
                if (isMoreThanOrEqual(expensesOfPersonPaying, expensesOfCurrentPerson)) {
                    personPaying.getOptimizedMoneyOwed().put(nameOfPersonReceiving, -expensesOfCurrentPerson);
                    personReceiving.getOptimizedMoneyOwed().put(nameOfPersonPaying, expensesOfCurrentPerson);
                    totalExpenses.set(indexOfPersonReceiving, 0.0);
                    totalExpenses.set(indexOfPersonPaying, -(expensesOfPersonPaying - expensesOfCurrentPerson));
                } else {
                    personPaying.getOptimizedMoneyOwed().put(nameOfPersonReceiving, -expensesOfPersonPaying);
                    personReceiving.getOptimizedMoneyOwed().put(nameOfPersonPaying, expensesOfPersonPaying);
                    totalExpenses.set(indexOfPersonPaying, 0.0);
                    totalExpenses.set(indexOfPersonReceiving, expensesOfCurrentPerson - expensesOfPersonPaying);
                }
            }
            if (isZero(totalExpenses.get(indexOfPersonPaying))) {
                return;
            }
        }
    }

    /**
     * Gets the net total of expenses for every {@link Person} in {@link Trip}.
     *
     * @param totalExpenses {@link ArrayList} containing the net total of each {@link Person} in the open {@link Trip}.
     * @param listOfPersons {@link ArrayList} of {@link Person} in the currently open {@link Trip}.
     */
    private static void getTotalAmountForPerson(ArrayList<Double> totalExpenses, ArrayList<Person> listOfPersons) {
        Double totalAmountPerPerson;
        for (Person person : listOfPersons) {
            totalAmountPerPerson = 0.0;
            HashMap<String, Double> personExpenses = person.getMoneyOwed();
            String otherPersonName;
            for (Person otherPerson : listOfPersons) {
                if (!otherPerson.equals(person)) {
                    otherPersonName = otherPerson.getName();
                    totalAmountPerPerson += personExpenses.get(otherPersonName);
                    person.setOptimizedMoneyOwed(otherPerson);
                }

            }
            totalExpenses.add(totalAmountPerPerson);
        }
    }


    /**
     * Checks if {@param firstValue} is larger or equal to {@param secondValue}.
     *
     * @param firstValue bigger or equal value that we want to compare
     * @param secondValue smaller or equal value that we want to compare
     * @return {@link Boolean} that is true if {@param firstValue} is greater or equal to {@param secondValue}
     */
    private static boolean isMoreThanOrEqual(double firstValue, double secondValue) {
        if (isEqual(firstValue, secondValue)) {
            return true;
        }
        return firstValue > secondValue;
    }

    /**
     * Checks if {@param firstValue} is equal to {@param secondValue}.
     * @param firstValue first value that we want to check.
     * @param secondValue second value that we want to check against.
     * @return true if {@param firstValue} is equals to {@param secondValue}.
     */
    private static boolean isEqual(double firstValue, double secondValue) {
        double difference = firstValue - secondValue;
        return difference < EPSILON && difference > -EPSILON;
    }

    /**
     * Checks if {@param value} is zero.
     * @param value value that we want to check.
     * @return true if value is 0.
     */
    private static boolean isZero(double value) {
        return value < EPSILON && value > -EPSILON;
    }
}
//@@author
