package ca.cmpt213.fortressdefense.model;

/**
 * Class that represents the cells on the game board using X, Y coordinates.
 * @author Bei Bei Li + Adam Labecki
 */
public class Coordinate {

    private int x;
    private int y;
    private boolean isTankCell;
    private boolean hasFiredAt;
    private int tankID;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        this.isTankCell = false;
        this.hasFiredAt = false;
        this.tankID = -1;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setTankCell() {
        this.isTankCell = true;
    }

    public boolean getIsTankCell() {
        return this.isTankCell;
    }

    public void setFired() {
        this.hasFiredAt = true;
    }

    public boolean getHasFiredAt() {
        return this.hasFiredAt;
    }

    public void setTankID(int id) {
        this.tankID = id;
    }

    public int getTankID() {
        return tankID;
    }
}
