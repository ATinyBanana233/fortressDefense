package ca.cmpt213.fortressdefense.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the game board in the game.
 * @author Bei Bei Li + Adam Labecki
 */
public class GameBoard {

    private List<Coordinate> boardCells = new ArrayList<>();
    private List<Tank> tanks = new ArrayList<>();


    private int boardSizeX = 0;
    private int boardSizeY = 0;

    private int tankNum = 0;


    public GameBoard(int tankNum, int boardSizeX, int boardSizeY) {

        this.boardSizeX = boardSizeX;
        this.boardSizeY = boardSizeY;
        this.tankNum = tankNum;

        createBoard(boardSizeX, boardSizeY);
        placeTanks(tankNum, boardSizeX, boardSizeY);
        updateBoardWithTank();

    }

    /**
     * Method to create the grid for the game board
     * @param boardSizeX The width of the board (int)
     * @param boardSizeY The length of the board (int)
     */
    private void createBoard(int boardSizeX, int boardSizeY) {

        //this.boardCells = new ArrayList<Coordinate>();
        Coordinate insertCord;

        //1-10 for 10x10, index 1 - based
        for (int row = 1; row <= boardSizeX; row++) {
            for (int col = 1; col <= boardSizeY; col++) {
                insertCord = new Coordinate(row, col);
                this.boardCells.add(insertCord);
            }
        }
    }

    /**
     * Method to generate tanks and place them on the game board, if tank generated overlapps with another tank, it is abandoned.
     * @param tankNum The number of tanks to be generated (int)
     * @param boardSizeX The width of the board (int)
     * @param boardSizeY The length of the board (int)
     */
    private void placeTanks(int tankNum, int boardSizeX, int boardSizeY) {

        this.tanks = new ArrayList<Tank>();
        Tank insertTank;

        final int loopLimit = 161888;
        int limitCounter = 0;

        for (int id = 0; id < tankNum; id++){

            while (limitCounter < loopLimit) {

                insertTank = new Tank(id, boardSizeX, boardSizeY);

                if (checkTankPlacementOK(insertTank)) {
                    this.tanks.add(insertTank);
                    break;
                }

                limitCounter = limitCounter + 1;
            }

            if (limitCounter == loopLimit) {
                System.out.printf("Error: Unable to place %d on the board.%n", tankNum);
                exitProgram("Try running game again with fewer tanks.");
            }

        }

        //加个更新game board cell的function
        //updateBoardWithTank();

    }

    /**
     * Method to check if the tank overlaps with an already placed tank
     * @param insertTank The tank to be checked (Tank)
     * @return The boolean indicating if the tank overlaps or not with an already placed tank
     */
    private boolean checkTankPlacementOK(Tank insertTank) {

        boolean result = true;

        for (Tank eachTank : this.tanks) {
            for (Coordinate eachCord: eachTank.getMyCells()){
                for (Coordinate eachCheckCord: insertTank.getMyCells()) {

                    if (eachCord.getX() == eachCheckCord.getX() && eachCord.getY() == eachCheckCord.getY()) {
                        result = false;
                        break;
                    }

                }
            }
        }

        return result;
    }

    /**
     * Method to update the board cells and mark them as tank cells with tankID
     */
    private void updateBoardWithTank() {

        for (Tank eachTank : this.tanks) {
            for (Coordinate eachTankCord: eachTank.getMyCells()) {
                for (Coordinate eachBoardCord : boardCells) {

                    if (eachTankCord.getX() == eachBoardCord.getX() && eachTankCord.getY() == eachBoardCord.getY()) {
                        eachBoardCord.setTankCell();
                        eachBoardCord.setTankID(eachTank.getMyID());
                        continue;
                    }
                }
            }
        }

    }

    public List<Coordinate> getBoardCells() {
        return this.boardCells;
    }

    public int getBoardSizeX() {
        return boardSizeX;
    }

    public int getBoardSizeY() {
        return boardSizeY;
    }

    public List<Tank> getTanks() {
        return tanks;
    }

    public int getTankNum() {
        return tankNum;
    }

    private static void exitProgram(String error) {

        final int FAILURE = -1;
        System.out.println(error);
        System.out.println("Now exiting program.");
        System.exit(FAILURE);

    }
}
