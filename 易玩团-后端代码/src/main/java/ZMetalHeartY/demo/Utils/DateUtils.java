package ZMetalHeartY.demo.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public int daysToNowDiff(String start_date_str) throws ParseException {

        SimpleDateFormat sdfs=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start_date=sdfs.parse(start_date_str);
        Date end_date=new Date();

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        start_date=sdf.parse(sdf.format(start_date));
        end_date=sdf.parse(sdf.format(end_date));
        Calendar cal = Calendar.getInstance();
        cal.setTime(start_date);
        long time1 = cal.getTimeInMillis();
        cal.setTime(end_date);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static Date addDayOfDate(Date date,int i){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        Date newDate = c.getTime();
        return newDate;
    }

    public static int compareDate(Date date1,Date date2){

        return date1.compareTo(date2);
    }

    public static String DateFormat(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}
