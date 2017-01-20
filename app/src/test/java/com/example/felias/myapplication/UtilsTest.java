package com.example.felias.myapplication;

import com.example.root.myapplication.util.Utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilsTest {
    @Test
    public void splitLongNames() throws Exception {
        String text = "Hello this is my first app test";

        String expectedResult="Hello this is\nmy first app\ntest";

        String result = Utils.limitCharPerLines(text, 15);

        assertEquals(expectedResult,result);
    }

    @Test
    public void splitVeryLongWord() throws Exception {
        String text = "Hello xxxxxxxxxxxxxxx app test";

        String expectedResult="Hello xxxx\nxxxxxxxxxx\nx app test";

        String result = Utils.limitCharPerLines(text, 10);

        assertEquals(expectedResult,result);
    }

    @Test
    public void splitWithMoreSpaces() throws Exception {
        String text = "Hello    app test";

        String expectedResult="Hello    app test";

        String result = Utils.limitCharPerLines(text, 30);

        assertEquals(expectedResult,result);
    }

    @Test
    public void splitLongSpaces() throws Exception {
        String text = "Hello            app test";

        String expectedResult="Hello     \n      app\ntest";

        String result = Utils.limitCharPerLines(text, 10);

        assertEquals(expectedResult,result);
    }

}