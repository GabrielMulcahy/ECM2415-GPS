package model;


import java.net.URLEncoder;


/**
 * Directions using the Google Maps APIs.
 * @author David Wakeling, Gabriel Mulcahy, Joshua Chalcraft
 */
public class Directions {
    private static final String KEY   = "AIzaSyD-VVgKS5-_C_ptg68nJ6xr5RI7BsTCpRo";
    private static String region      = "uk";
    private static String mode        = "walking"; /* "driving" */
    /*
     * Read directions.
     */
    private static byte[] readDirections( String origin
            , String destination
            , String region
            , String mode
            , String language) {
        try {
            final String encOrigin      = URLEncoder.encode( origin,      "UTF-8" );
            final String encDestination = URLEncoder.encode( destination, "UTF-8" );
            final String method = "GET";
            final String url
                    = ( "https://maps.googleapis.com/maps/api/directions/json"
                    + "?" + "origin"      + "=" + encOrigin
                    + "&" + "destination" + "=" + encDestination
                    + "&" + "region"      + "=" + region
                    + "&" + "mode"        + "=" + mode
                    + "&" + "language"    + "=" + language
                    + "&" + "key"         + "=" + KEY
            );
            final byte[] body
                    = {};
            final String[][] headers
                    = {};
            byte[] response = HttpConnect.httpConnect( method, url, headers, body );
            return response;
        } catch ( Exception ex ) {
            SoundPlayer.playFile("res/errorMessages/GoogleError.wav"); return null;
        }
    }

    /*
     * Turns byte array into string ready for JSON parsing - Joshua Chalcraft
     */
    public static String sendToParser(String latitude, String longitude, String destination, String language){
        final byte[] ds = readDirections(latitude +","+longitude, destination, region, mode, language);
        String directions = new String(ds);
        return directions;
    }
}
