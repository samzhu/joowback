import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created by samzhu on 2015/3/29.
 */
public class TimeTest {
    public static void main(String[] args){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        System.out.println(sdf.format(System.currentTimeMillis()));

        //Date yourUtcDate = sdf.parse(yourOriginalDate);

    }

}
