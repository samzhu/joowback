import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created by samzhu on 2015/3/29.
 */
public class TimeTest {
    public static void main(String[] args){

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'");
        //sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        //System.out.println(sdf.format(System.currentTimeMillis()));

        System.out.println(DigestUtils.sha1Hex("fdsf"));
        System.out.println(DigestUtils.md5Hex("fdsf"));


        //Date yourUtcDate = sdf.parse(yourOriginalDate);

    }

}
