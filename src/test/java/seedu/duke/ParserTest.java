package seedu.duke;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seedu.duke.parser.Parser;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserTest {

    @BeforeAll
    static void setup() {
    }

    @Test
    void getUserToConfirm_yWithExtraSpaces_success() {
        String input = "   Y  ";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Storage.setScanner(new Scanner(System.in));

        assertTrue(Parser.getUserToConfirm());
    }

    void getUserToConfirm_garbageInputFollowedByn_success() {
        String input = "abcd  " + System.lineSeparator()
                + "help" + System.lineSeparator()
                + "n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Storage.setScanner(new Scanner(System.in));

        assertFalse(Parser.getUserToConfirm());
    }
}