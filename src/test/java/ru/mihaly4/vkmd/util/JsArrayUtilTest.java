package ru.mihaly4.vkmd.util;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Below code is answer from https://stackoverflow.com/a/42259988
 */
public class JsArrayUtilTest {
    @Test
    public void main() {
        final String[] array = new String[]{"a", "b", "c", "d", "e", "f"};

        assertArrayEquals(new String[]{"c", "d", "e", "f"}, JsArrayUtil.splice(array, 0, 2));
        assertArrayEquals(new String[]{"a", "d", "e", "f"}, JsArrayUtil.splice(array, 1, 2));
        assertArrayEquals(new String[]{"a", "b", "e", "f"}, JsArrayUtil.splice(array, 2, 2));
        assertArrayEquals(new String[]{"a", "b", "c", "f"}, JsArrayUtil.splice(array, 3, 2));
        assertArrayEquals(new String[]{"a", "b", "c", "d"}, JsArrayUtil.splice(array, 4, 2));
        try {
            JsArrayUtil.splice(array, 5, 2);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (final ArrayIndexOutOfBoundsException e) {
        }

        try {
            JsArrayUtil.splice(array, -2, 3);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        assertArrayEquals(new String[]{"a", "b", "c", "d"}, JsArrayUtil.splice(array, -2, 2));
        assertArrayEquals(new String[]{"a", "b", "c", "f"}, JsArrayUtil.splice(array, -3, 2));
        assertArrayEquals(new String[]{"a", "b", "e", "f"}, JsArrayUtil.splice(array, -4, 2));
        assertArrayEquals(new String[]{"a", "d", "e", "f"}, JsArrayUtil.splice(array, -5, 2));
        assertArrayEquals(new String[]{"c", "d", "e", "f"}, JsArrayUtil.splice(array, -6, 2));
        try {
            JsArrayUtil.splice(array, -7, 2);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (final ArrayIndexOutOfBoundsException e) {
        }

        assertArrayEquals(new String[]{}, JsArrayUtil.splice(array, 0));
        assertArrayEquals(new String[]{"a"}, JsArrayUtil.splice(array, 1));
        assertArrayEquals(new String[]{"a", "b"}, JsArrayUtil.splice(array, 2));
        assertArrayEquals(new String[]{"a", "b", "c"}, JsArrayUtil.splice(array, 3));
        assertArrayEquals(new String[]{"a", "b", "c", "d"}, JsArrayUtil.splice(array, 4));
        assertArrayEquals(new String[]{"a", "b", "c", "d", "e"}, JsArrayUtil.splice(array, 5));
        assertArrayEquals(new String[]{"a", "b", "c", "d", "e", "f"}, JsArrayUtil.splice(array, 6));
        try {
            JsArrayUtil.splice(array, 7);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (final ArrayIndexOutOfBoundsException e) {
        }

        assertArrayEquals(new String[]{"a", "b", "c", "d", "e"}, JsArrayUtil.splice(array, -1));
        assertArrayEquals(new String[]{"a", "b", "c", "d"}, JsArrayUtil.splice(array, -2));
        assertArrayEquals(new String[]{"a", "b", "c"}, JsArrayUtil.splice(array, -3));
        assertArrayEquals(new String[]{"a", "b"}, JsArrayUtil.splice(array, -4));
        assertArrayEquals(new String[]{"a"}, JsArrayUtil.splice(array, -5));
        assertArrayEquals(new String[]{}, JsArrayUtil.splice(array, -6));
        try {
            JsArrayUtil.splice(array, -7);
            fail("Expected NegativeJsArrayUtilizeException");
        } catch (final Exception e) {
        }

        assertArrayEquals(new String[]{"x", "y", "z", "c", "d", "e", "f"}, JsArrayUtil.splice(array, 0, 2, "x", "y", "z"));
        assertArrayEquals(new String[]{"a", "x", "y", "z", "d", "e", "f"}, JsArrayUtil.splice(array, 1, 2, "x", "y", "z"));
        assertArrayEquals(new String[]{"a", "b", "x", "y", "z", "e", "f"}, JsArrayUtil.splice(array, 2, 2, "x", "y", "z"));
        assertArrayEquals(new String[]{"a", "b", "c", "x", "y", "z", "f"}, JsArrayUtil.splice(array, 3, 2, "x", "y", "z"));
        assertArrayEquals(new String[]{"a", "b", "c", "d", "x", "y", "z"}, JsArrayUtil.splice(array, 4, 2, "x", "y", "z"));
        try {
            JsArrayUtil.splice(array, 5, 2, "x", "y", "z");
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (final ArrayIndexOutOfBoundsException e) {
        }

        assertArrayEquals(new String[]{"a", "b", "c", "d", "x", "y", "z"}, JsArrayUtil.splice(array, -2, 2, "x", "y", "z"));
        assertArrayEquals(new String[]{"a", "b", "c", "x", "y", "z", "f"}, JsArrayUtil.splice(array, -3, 2, "x", "y", "z"));
        assertArrayEquals(new String[]{"a", "b", "x", "y", "z", "e", "f"}, JsArrayUtil.splice(array, -4, 2, "x", "y", "z"));
        assertArrayEquals(new String[]{"a", "x", "y", "z", "d", "e", "f"}, JsArrayUtil.splice(array, -5, 2, "x", "y", "z"));
        assertArrayEquals(new String[]{"x", "y", "z", "c", "d", "e", "f"}, JsArrayUtil.splice(array, -6, 2, "x", "y", "z"));
        try {
            JsArrayUtil.splice(array, -7, 2, "x", "y", "z");
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
    }
}
