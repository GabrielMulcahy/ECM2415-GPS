package map;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
 * Maps
 *
 * Gabriel Mulcahy
 */

public class Maps {
  final static String KEY = "AIzaSyDC6wHj2s9ZTXqtre3DbYNhQsvu-kH8d1w";
  final static String OUTPUT    = "src/map/output.png";  /* Ouput file        */
  String latitude;
  String longitude;     
  String zoom      = "18";           /* 0 .. 21           */
  String size      = "191x241";     /* Size              */

  static byte[] readData( String latitude
                        , String longitude
                        , String zoom
                        , String size
                        ) {
    final String method = "GET";
    final String url
      = ( "https://maps.googleapis.com/maps/api/staticmap"
        + "?" + "center" + "=" + latitude + "," + longitude
        + "&" + "zoom"   + "=" + zoom
        + "&" + "size"   + "=" + size
        + "&" + "key"    + "=" + KEY
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
   *
   * David Wakeling 2018
   */
  static void writeData( String file, byte[] data) {
    try {
      OutputStream os = new FileOutputStream( file );
      os.write( data, 0, data.length );
      os.close();
    } catch ( IOException ex ) {
      ex.printStackTrace(); System.exit( 1 );
    }
  }
  
  void setLong(String longitude){
    this.longitude = longitude;
  }

  void setLat(String latitude){
    this.latitude = latitude;
  }
  
  void zoomIn(){
    int iZoom = Integer.parseInt(zoom); //create an integer form of zoom that can be incremented
    if (iZoom < 21) iZoom++;            //check that the max zoom has not been reached
    zoom = Integer.toString(iZoom);
  }
  
  void zoomOut(){
    int iZoom = Integer.parseInt(zoom);
    if (iZoom > 0) iZoom--;
    zoom = Integer.toString(iZoom);
  }
  
  /*
   * Download map data.
   *
   * David Wakeling 2018
   */
  public void make() {
    final byte[] data = readData( latitude, longitude, zoom, size );
    writeData( OUTPUT, data ); 
  }

  //creates an image to be rendered facing north/south
  public void northSouth() {
    size = "191x241";
  }

  //creates an image to be rendered facing east/west
  public void eastWest() {
    size = "241x191";
  }
}
