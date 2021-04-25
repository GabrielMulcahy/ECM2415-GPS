package map;


import java.net.URLEncoder;


/*
 * Directions using the Google Maps APIs.
 *
 * See https://developers.google.com/maps/documentation/directions/intro
 *
 * David Wakeling, 2018.
 *
 * Modified by Gabriel Mulcahy and Joshua Chalcraft
 */
public class Directions {
    static String origin      = "The Forum, Exeter University";
    static String destination = "Cathedral Green, Exeter";
    static String region      = "uk";
    static String mode        = "walking"; /* "driving" */
    static String language    = "en-GB";
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
                    + "&" + "langauage"   + "=" + language
            );
            final byte[] body
                    = {};
            final String[][] headers
                    = {};
            byte[] response = HttpConnect.httpConnect( method, url, headers, body );
            return response;
        } catch ( Exception ex ) {
            System.out.println( ex ); System.exit( 1 ); return null;
        }
    }

    /*
     * Turns byte array into string ready for JSON parsing - Joshua Chalcraft
     */
    public static String sendToParser() {
        final byte[] ds = readDirections( origin, destination, region, mode, language );
        String directions = new String(ds);
        return directions;
    }

    public void setOrigin(String origin){
        this.origin = origin;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
