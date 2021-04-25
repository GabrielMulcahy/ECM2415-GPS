/**
 * @author: Cai Davies
 */

package controller;

public enum MenuAction {

    ON_OFF_STATE(0),
    MAIN_STATE(1),
    WHERE_TO_STATE(2),
    TRIP_COMPUTER_STATE(3),
    MAP_STATE(4),
    SPEECH_STATE(5),
    SATELLITE_STATE(6),
    ABOUT_STATE(7);

    private int val;

    MenuAction(int val) {
        this.val = val;
    }

    public int getVal() { return this.val; }

}
