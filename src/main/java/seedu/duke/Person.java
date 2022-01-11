package seedu.duke;


import java.util.HashMap;

//@@author joshualeeky
public class Person {
    private String name;
    private HashMap<String, Double> moneyOwed = new HashMap<>();
    private HashMap<String, Double> optimizedMoneyOwed = new HashMap<>();

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMoneyOwed(Person person, double amount) {
        double originalAmount = moneyOwed.get(person.getName());
        moneyOwed.put(person.getName(), originalAmount + amount);
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Double> getMoneyOwed() {
        return this.moneyOwed;
    }

    public HashMap<String, Double> getOptimizedMoneyOwed() {
        return this.optimizedMoneyOwed;
    }

    public void setOptimizedMoneyOwed(Person person) {
        optimizedMoneyOwed.put(person.getName(), 0.0);
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
