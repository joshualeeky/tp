package seedu.duke;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {
    static Person testPerson;

    @BeforeAll
    static void setUp() {
        testPerson = new Person("Test Person");
    }

    @Test
    void testSetName() {
        testPerson.setName("Test");
        assertEquals("Test", testPerson.getName());
    }

    @Test
    void testSetMoneyOwed() {
        Person albert = new Person("Albert");
        testPerson.getMoneyOwed().put("Albert", 0.0);
        testPerson.setMoneyOwed(albert, 200.00);
        assertEquals(200.00, testPerson.getMoneyOwed().get("Albert"));
    }
}
