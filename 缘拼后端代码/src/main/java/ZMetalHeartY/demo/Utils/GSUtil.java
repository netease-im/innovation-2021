package ZMetalHeartY.demo.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GSUtil {

    private final double EARTH_RADIUS = 6378.137;

    private double rad(double d){
        return d * Math.PI / 180.0;
    }
    /**
     * 根据经纬度算距离
     * @param long1
     * @param lat1
     * @param long2
     * @param lat2
     * @return
     */
    public double getmeter(double long1, double lat1, double long2, double lat2) {
        double a, b, d, sa2, sb2;
        lat1 = rad(lat1);
        lat2 = rad(lat2);
        a = lat1 - lat2;
        b = rad(long1 - long2);

        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2   * EARTH_RADIUS
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));
        d= d * 1000;
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        return bg.doubleValue();

    }


}