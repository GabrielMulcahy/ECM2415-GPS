/**
 * @author Rob Wells
 */
package model;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ModelTripComputer {

    long startTime;
    public ModelTripComputer(){
        startTime=System.currentTimeMillis();
    }
    public static Double deg2rad(Double deg) {
        return deg * (Math.PI / 180);
    }

    /**
     * Function that will calculate the distance between two latitude/longitude coordinantes in km
     * @param lat1 previous latitude
     * @param lon1 previous longitude
     * @param lat2 current latitude
     * @param lon2 current longitude
     * @return distance between points in km
     */
    public static double getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        int r = 6371; // radius of the earth
        double dLat = deg2rad(lat2 - lat1);
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r* c;
        return d;
    }

    /**
     * Function that will take a time in seocnds and convert it into minutes and seconds
     * @param timeSeconds inital time in seconds
     * @return A string in the format "X Min Y Sec"
     */
    public static String getTimeInMins(int timeSeconds){
        int timeMins =0;
        while ((timeSeconds -60) >=0){
            timeSeconds-=60;
            timeMins+=1;
        }
        return Integer.toString(timeMins)+" Min "+Integer.toString(timeSeconds)+" Sec";
    }

    /**
     * Figures out the current elapsed time in seconds by using the  System.currentTimeMillis() funcion
     * @return time elapsed in seconds
     */
    public static int getCurrentTime(){
        long startTime = ModelManager.getStartTime();
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
        return (int) elapsedSeconds;

    }

}
