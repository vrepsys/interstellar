package cc.interstellar.util;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtendedAssertions {

    public static void assertEqualsUnordered(Collection expected, Collection actual) {
        assertEquals(new HashSet(expected), new HashSet(actual));
    }

}
