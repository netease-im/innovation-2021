package ZMetalHeartY.demo.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtil {
    public static String getCurrentTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentdate=sdf.format(new Date());
        return currentdate;
    }
    public static  String time(){

        return System.currentTimeMillis()+"";
    }
}
