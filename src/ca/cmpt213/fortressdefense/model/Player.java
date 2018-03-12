package ca.cmpt213.fortressdefense.model;

/**
 * Class that represents the player: myFortStruct (int).
 * @author Bei Bei Li + Adam Labecki
 */
public class Player {

    private int myFortStruct = 1500;

    public int getMyFortStruct() {
        return this.myFortStruct;
    }

    public void decreaseMyFortStruct(int damage) {
        this.myFortStruct = this.myFortStruct - damage;

        //fortress struct cannot be negative
        if (this.myFortStruct < 0) {
            this.myFortStruct = 0;
        }
    }
}
