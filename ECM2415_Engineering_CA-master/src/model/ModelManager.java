/**
 * @author Cai Davies, Scott Woodward
 */

package model;

import controller.Location;
import controller.MenuAction;
import controller.MockLocation;
import controller.UserController;
import view.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ModelManager {

    UserController uc;

    //MockLocation location;  // used for MockData while testing
    Location location;
    Maps mapGenerator;
    ArrayList<HashMap<String, String>> directions;
    SpeechGenerator speech;
    Language currentLanguage;

    private static MenuAction currentView = MenuAction.ON_OFF_STATE;
    double distance, reCalcDistance;
    static long startTime;
    final static double TOLERANCE = 0.025; // 25m tolerance
    final static double RECALC_TOLERANCE = 0.02; // if walked more than 20m from closest approach to next point

    String destination, token;

    public static String longitude,latitude,direction, timeSinceUpdate;

    // views
    private MenuState views[] = new MenuState[8];

    public ModelManager(UserController uc) {

        this.uc = uc;

        //location = new MockLocation();
        location = new Location();
        mapGenerator = new Maps();
        directions = null; //new Directions();
        speech = new SpeechGenerator();
        currentLanguage = Language.OFF;
        currentView = MenuAction.ON_OFF_STATE;
        startTime = System.currentTimeMillis();

        Thread renewToken = new Thread(){
            public void run(){
                token = SpeechGenerator.renewAccessToken();
                try {
                    sleep(600000); //Sleeps for 10 minutes
                } catch (InterruptedException e) {
                    e.printStackTrace(); //Doesn't matter
                }
            }
        };

        // create new state objects
        hardReset();

        // start the first state, and paint
        views[currentView.getVal()].start();
        uc.repaint();

        location.openPort("COM4");
        Thread thread = new Thread(location);
        thread.start();
        renewToken.start();

    }

    public void update() {

        if(currentView==MenuAction.ON_OFF_STATE) return; // if device is off

        String[] x = location.getData();
        HashMap<String, String> leg = null;
        if (directions != null && !directions.isEmpty()) leg = directions.get(0); // if there are directions to follow, leg is next part of directions

        if(!x[0].equals("") && !latitude.equals("")) { // if there is satellite data, update trip computer distance
            distance += ModelTripComputer.getDistance(Double.parseDouble(latitude),Double.parseDouble(longitude),
                    Double.parseDouble(x[0]),Double.parseDouble(x[1]));
        }
        if(startTime ==0){
            startTime = Integer.valueOf(x[4]);
        }

        latitude = x[0];
        longitude = x[1];
        direction = x[2];

        if(currentView==MenuAction.SATELLITE_STATE) {
            ((SatelliteMode)views[currentView.getVal()]).update(latitude,longitude);
        }else if(currentView==MenuAction.MAP_STATE){ //Added basic map state -Scott

            if (Integer.valueOf(timeSinceUpdate) + 5 < Integer.valueOf(x[4]) || !direction.equals("")){
                ((MapState)views[currentView.getVal()]).update(latitude, longitude, direction, currentLanguage.getCode());
                timeSinceUpdate = x[4];
            }

        }else if(currentView==MenuAction.TRIP_COMPUTER_STATE){
            ((TripComputer)views[currentView.getVal()]).updateTripComputerMode(String.valueOf(distance),x[3],String.valueOf(Integer.valueOf(x[4])-startTime));
        }

        if ( leg!=null && !latitude.equals("")&& !longitude.equals("")) {

            double dist = ModelTripComputer.getDistance(Double.parseDouble(latitude), Double.parseDouble(longitude), Double.parseDouble(leg.get("endLat")), Double.parseDouble(leg.get("endLong"))); // current distance from target

            if (dist <= TOLERANCE) { // if within TOLERANCE to target, reached target and go to next leg of journey
                directions.remove(0);

                if (!directions.isEmpty()) { // if there is another leg
                    leg = directions.get(0);
                    //Maybe speak next direction??
                    if (currentLanguage != Language.OFF){
                        speech.generate(token, leg.get("Directions"), currentLanguage.getCode(), currentLanguage.getGender(), currentLanguage.getArtist());
                        SoundPlayer.playDirection();
                    }

                    reCalcDistance = -1; // reset distance to next target
                } else {
                    leg = null;
                }

            }else{
                if (reCalcDistance == -1) { //Set distance
                    reCalcDistance = ModelTripComputer.getDistance(Double.parseDouble(latitude), Double.parseDouble(longitude), Double.parseDouble(leg.get("endLat")), Double.parseDouble(leg.get("endLong")));
                }else{ //Check for time
                    Double newDist = ModelTripComputer.getDistance(Double.parseDouble(latitude), Double.parseDouble(longitude), Double.parseDouble(leg.get("endLat")), Double.parseDouble(leg.get("endLong")));
                    if (newDist > (reCalcDistance+RECALC_TOLERANCE)){ // if moved further than RECALC_TOLERANCE from closest approach, recalculate journey
                        //Recalculate//
                        SoundPlayer.playFile("res/errorMessages/Recalculating.wav");
                        reCalcDistance = -1;
                        this.directions = JSONParser.getDirections(Directions.sendToParser(latitude, longitude, this.destination, this.currentLanguage.getCode()));
                    }else if (newDist < reCalcDistance) {
                        this.reCalcDistance = newDist;
                    }

                }
            }
        }

    }

    public void doAction(NavigationAction action) {
        switch(action) {
            case POWER:
                if(currentView==MenuAction.ON_OFF_STATE) {
                    views[currentView.getVal()].stop();
                    currentView = MenuAction.MAIN_STATE;
                    views[currentView.getVal()].start();
                }else {
                    views[currentView.getVal()].stop();
                    currentView = MenuAction.ON_OFF_STATE;
                    views[currentView.getVal()].start();
                    uc.getScreen().removeAll();
                    uc.getScreen().repaint();
                    hardReset();
                }
                break;
            case MENU:
                if(currentView==MenuAction.MAIN_STATE) return;
                views[currentView.getVal()].navigationButton(NavigationAction.MENU);
                views[currentView.getVal()].stop();
                currentView = MenuAction.MAIN_STATE;
                views[currentView.getVal()].stop();
                views[currentView.getVal()].start();
                //states[state.getVal()].render();
                break;
            case SELECT:
                views[currentView.getVal()].navigationButton(NavigationAction.SELECT);
                break;
            case PLUS:
                views[currentView.getVal()].navigationButton(NavigationAction.PLUS);
                break;
            case MINUS:
                views[currentView.getVal()].navigationButton(NavigationAction.MINUS);
                break;
        }
    }

    public void actionPerformed(ActionEvent e) {
        views[currentView.getVal()].actionPerformed(e);
    }

    public void goToState(MenuAction state) {
        if(views[currentView.getVal()]==null) return;
        views[currentView.getVal()].stop();
        currentView = state;
        views[currentView.getVal()].start();
        uc.revalidate();
        views[currentView.getVal()].render();
    }

    public void paint(Graphics2D g2d) {
        if(views[currentView.getVal()]!=null) views[currentView.getVal()].render();
    }

    public void paintScreen(Graphics2D g2d) {
        if(views[currentView.getVal()]!=null) views[currentView.getVal()].render();
    }

    // reset all values and states, used for turning on and off
    public void hardReset() {
        views[MenuAction.ON_OFF_STATE.getVal()] = new OnOffState();
        views[MenuAction.MAIN_STATE.getVal()] = new MainMenuState(this);
        views[MenuAction.TRIP_COMPUTER_STATE.getVal()] = new TripComputer();
        views[MenuAction.WHERE_TO_STATE.getVal()] = new WhereTo(this);
        views[MenuAction.MAP_STATE.getVal()] = new MapState();
        views[MenuAction.SPEECH_STATE.getVal()] = new SpeechMode(this); //change by Josh - renamed class
        views[MenuAction.SATELLITE_STATE.getVal()] = new SatelliteMode();
        views[MenuAction.ABOUT_STATE.getVal()] = new AboutMode();

        longitude = "";
        latitude = "";
        direction = "0";
        timeSinceUpdate = "0";
        distance = 0;
        startTime = System.currentTimeMillis();

        destination = "";
        reCalcDistance = -1;

        for(MenuState view:views) {
            if(view!=null) {
                view.setRenderer((Graphics2D)(uc.getGraphics()));
                view.setFrame(uc);
                view.setListener(uc);
                view.setPanel(uc.getScreen());
            }
        }

    }

    public boolean isOff() { return currentView==MenuAction.ON_OFF_STATE; }
    public static long getStartTime(){return startTime;}
    public void setLanguage(Language language) { this.currentLanguage = language; /*Reget directions in new languages*/}
    public void setView(MenuAction view) { this.currentView = view; }
    public Language getLanguage() { return this.currentLanguage; }
    public MenuAction getView() { return this.currentView; }

    public void setDestination(String destination){
        if (!destination.equals(this.destination)){
            this.destination = destination;
            distance = 0;
            TripComputer.resetTripComputer(); /*

            Added by - Rob Wells: Will reset the Trip Computer View when a new
            */
            startTime = System.currentTimeMillis();
            if (!currentLanguage.equals(Language.OFF)) {
                this.directions = JSONParser.getDirections(Directions.sendToParser(latitude, longitude, this.destination, this.currentLanguage.getCode()));
            }
        }
    }
}
