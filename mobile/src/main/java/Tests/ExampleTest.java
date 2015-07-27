package Tests;

import android.test.InstrumentationTestCase;

/**
 * Created by Dean on 05/05/2015.
 */
public class ExampleTest extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}
