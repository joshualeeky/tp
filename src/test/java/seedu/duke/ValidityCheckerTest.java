package seedu.duke;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seedu.duke.parser.Parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidityCheckerTest {

    @BeforeAll
    static void setUp() {

    }

    @Test
    public void testDateCannotBeParsed() {
        assertFalse(Parser.doesDateReallyExist("aa-bb-cccc"));
        assertFalse(Parser.doesDateReallyExist("01-bb-cccc"));
        assertFalse(Parser.doesDateReallyExist("01-01-cccc"));
        assertTrue(Parser.doesDateReallyExist("01-01-2001"));
    }

    @Test
    public void testDateDoesNotExist_InputDatesReallyDontExist() {

        //nonsense inputs
        assertFalse(Parser.doesDateReallyExist("35-02-2021"));
        assertFalse(Parser.doesDateReallyExist("00-11-2021"));
        assertFalse(Parser.doesDateReallyExist("25-00-2021"));
        assertFalse(Parser.doesDateReallyExist("16-23-2021"));

        //checking for 31st
        assertFalse(Parser.doesDateReallyExist("31-04-2021"));
        assertFalse(Parser.doesDateReallyExist("31-11-2021"));

        //leap years
        assertFalse(Parser.doesDateReallyExist("29-02-2021"));
        assertTrue(Parser.doesDateReallyExist("29-02-2020"));
        assertTrue(Parser.doesDateReallyExist("28-02-2020"));

        //this exists
        assertTrue(Parser.doesDateReallyExist("08-12-2020"));
    }
}
