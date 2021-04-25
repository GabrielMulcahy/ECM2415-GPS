/**
 * @author Gabriel Mulcahy, David Wakeling
 */

package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class Maps {
    private final static String KEY       = "AIzaSyDC6wHj2s9ZTXqtre3DbYNhQsvu-kH8d1w";
    private final static String OUTPUT    = "res/output.png";                          // Ouput file
    String latitude;
    String longitude;
    String zoom                           = "18";                                      // 1 ... 21, a zoom level of 0 will fail to fill the screen
    private final static String size      = "308x308";                                 // size of the widest part of the screen, allowing it to be rotated and still fill the screen
    String language;
    static String mapType                 = "roadmap";

    static byte[] readData( String latitude
                          , String longitude
                          , String zoom
                          , String size
                          , String language
                          ) {
      final String method = "GET";
      final String url
        = ( "https://maps.googleapis.com/maps/api/staticmap"
        + "?" + "center"   + "=" + latitude + "," + longitude
        + "&" + "zoom"     + "=" + zoom
        + "&" + "size"     + "=" + size
        + "&" + "maptype"  + "=" + mapType
        + "&" + "key"      + "=" + KEY
        + "&" + "language" + "=" + language
        );
      final byte[] body
        = {};
      final String[][] headers
        = {};
      byte[] response = HttpConnect.httpConnect( method, url, headers, body );
      return response;
    }

    /*
     * Write map data.
     */
    static void writeData( String file, byte[] data) {
        try {
            OutputStream os = new FileOutputStream( file );
            os.write( data, 0, data.length );
            os.close();
        } catch ( IOException ex ) {
            ex.printStackTrace();
            System.exit( 1 );
        }
    }

    /*
     * Toggle the style of the map.
     */
    public void toggleMapType() {
        if (mapType.equals("roadmap")) {
            mapType = "hybrid";
        } else {
            mapType = "roadmap";
        }
    }

    public void setLong(String longitude){
        this.longitude = longitude;
    }

    public void setLat(String latitude){
        this.latitude = latitude;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
  
    public void zoomIn(){
        int iZoom = Integer.parseInt(zoom); //create an integer form of zoom that can be incremented
        if (iZoom < 21) iZoom++;            //check that the max zoom has not been reached
        zoom = Integer.toString(iZoom);
    }
  
    public void zoomOut(){
        int iZoom = Integer.parseInt(zoom);
        if (iZoom > 1) iZoom--;
        zoom = Integer.toString(iZoom);
    }
  
    /*
     * Download map data.
     */
    public void make() {
        final byte[] data = readData( latitude, longitude, zoom, size, language );
        writeData( OUTPUT, data );
    }

}
