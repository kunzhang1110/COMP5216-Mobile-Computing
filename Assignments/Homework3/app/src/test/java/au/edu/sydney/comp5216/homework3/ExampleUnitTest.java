package au.edu.sydney.comp5216.homework3;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void timeTest(){


        DateTime datetime = new DateTime();
        System.out.println(datetime.getMillis());
        DateTime thatMonday = datetime.withDayOfWeek(DateTimeConstants.MONDAY);
        System.out.println(thatMonday.getDayOfYear());
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM dd, yyyy");
        System.out.println(dtf.print(thatMonday));
    }
}