package ca.cmpt213.fortressdefense.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to represent the logic of the game, processes user moves and updates the game
 * @author Bei Bei Li + Adam Labecki
 */
public class Model {

    private int tankNum;


    private Player player;
    private GameBoard gameBoard;

    private char[][] screen; //2D array visual representation of the game board
    private int screenX = 0;
    private int screenY = 0;

    //这局游戏的结尾 false是输，true是赢
    //the result of the game, false = loss, true = win
    private boolean gameResult = false;

    //the result of the move provided in each round, false = miss, true = hit
    private boolean hit = false;

    public Model(int tankNum, int boardSizeX, int boardSizeY) {

        this.tankNum = tankNum;

        this.player = new Player();
        this.gameBoard = new GameBoard(tankNum, boardSizeX, boardSizeY);


        this.screenX = boardSizeX + 1;
        this.screenY = boardSizeY + 1;
        this.screen = new char[screenX][screenY];
    }

    //UI 调showBoard

    //A4应该直接调用,看看cord存在不存在
//    public boolean validateCord(int X, int Y) {
//        boolean badCord = false;
//
//        return badCord;
//    }

    /**
     * Method to process the user move in each round and updates the game accordingly
     * @param X The X cord of the user move (int)
     * @param Y The Y cord of the user move (int)
     * @return The boolean indicating if the game ended
     */
    public boolean playRound(int X, int Y) {

        boolean play = true;

        //Step1: Locate Board Cell
        Coordinate inputHit = locateCord(X, Y);

        //assert inputHit is not null
        assert inputHit != null;

        //Step2: update hit location
        updateHit(inputHit);

        //Step3: if tank cell has been hit, update tank
        boolean updateTank = checkTankCellHasBeenHit(inputHit);
        //如果cell是tank cell而且被打了，我要去update tank list

        if (updateTank) {
            hitTankCell(inputHit);
            hit = true;
        }
        else {
            hit = false;
        }

        //Step4: get all the tank damage and hit player fort struct
        hitPlayer();

        //Step5: 判定输赢, check win loss
        play = checkRoundResult();

        return play;

    }

    /**
     * Method to provide the screen, visual representation of the game board for the UI to use
     * @param cheat The boolean indicating if the screen should be fogged or unfogged
     * @return
     */
    public char[][] showBoard(boolean cheat){

        setScreenBorder();


        if (cheat){
            screenSetUpCheat();
        }
        else {
            screenSetUp();
        }

        return this.screen;


    }

    /**
     * Method to set up the unfogged screen
     */
    private void screenSetUpCheat() {


        List<Coordinate> myGameBoardCells = gameBoard.getBoardCells();


        for (Coordinate eachCord : myGameBoardCells) {

            int x = eachCord.getX();
            int y = eachCord.getY();

            if (eachCord.getIsTankCell()) {
                this.screen[x][y] = tankAccordingToId(eachCord);
            }
            else if (eachCord.getHasFiredAt()) {
                this.screen[x][y] = ' ';
            }
            else {
                this.screen[x][y] = '.';
            }


        }


    }

    /**
     * Method to set up the fogged screen
     */
    private void screenSetUp() {

        List<Coordinate> myGameBoardCells = gameBoard.getBoardCells();

        for (Coordinate eachCord : myGameBoardCells) {

            int x = eachCord.getX();
            int y = eachCord.getY();

            if (eachCord.getIsTankCell() && eachCord.getHasFiredAt()) {
                this.screen[x][y] = 'X';
            }
            else if (eachCord.getHasFiredAt()) {
                this.screen[x][y] = ' ';
            }
            else {
                this.screen[x][y] = '~';
            }


        }


    }

    /**
     * Method to set the border of the screen
     */
    private void setScreenBorder() {

        for (int row = 0; row < screenX; row++){
            for (int col = 0; col < screenY; col++){
                if (row == 0 && col == 0){
                    this.screen[row][col] = ' ';
                }
                else if (row == 0) {
                    this.screen[row][col] = (char)(col + 48);
                }
                else if (col == 0){
                    this.screen[row][col] = colAccordingToId(row);
                }
            }
        }
    }

    private char colAccordingToId(int row) {


        char result = (char) (row + 64);
        return result;
    }

    private char tankAccordingToId(Coordinate cord) {

        int id = cord.getTankID();
        char result = '-';

        if (cord.getHasFiredAt()){
            result = (char) (id + 97);
        }
        else {
            result = (char) (id + 65);
        }

        return result;
    }

    public int getPlayerLife() {
        return player.getMyFortStruct();
    }


    private Coordinate locateCord (int X, int Y) {

        Coordinate result = null;

        List<Coordinate> myGameBoardCells = this.gameBoard.getBoardCells();

        for (Coordinate eachCord : myGameBoardCells) {

            if (eachCord.getX() == X && eachCord.getY() == Y) {
                result = eachCord;
                break;
            }
        }

        return result;

    }

    private void updateHit(Coordinate cord) {

        if (!cord.getHasFiredAt()) {
            cord.setFired();
        }

    }

    private boolean checkTankCellHasBeenHit(Coordinate cord) {

        boolean hasBeenHit = false;

        if (cord.getIsTankCell() && cord.getHasFiredAt()) {
            hasBeenHit = true;
        }

        return hasBeenHit;
    }

    private void hitTankCell(Coordinate cord) {

        List<Tank> myTanks = this.gameBoard.getTanks();

        for (Tank eachTank : myTanks) {
            if (eachTank.getMyID() == cord.getTankID()) {
                eachTank.updateTankHit(cord);
            }
        }

    }

    private void hitPlayer() {

        List<Tank> myTanks = this.gameBoard.getTanks();
        int damage = 0;

        for (Tank eachTank : myTanks) {
            damage = eachTank.getMyDamage();
            this.player.decreaseMyFortStruct(damage);
        }

    }

    private boolean checkRoundResult() {

        boolean result = false;

        if (this.player.getMyFortStruct() > 0 && noMoreTankLeft()) {
            result = false;
            this.gameResult = true;
        }
        else if (this.player.getMyFortStruct() > 0) {
            result = true;
        }

        return result;

    }


    private boolean noMoreTankLeft() {

        boolean result = true;

        List<Tank> myTanks = this.gameBoard.getTanks();

        for (Tank eachTank : myTanks) {
            if (eachTank.getMyDamage() > 0) {
                result = false;
            }
        }

        return result;
    }

    public boolean getHit() {
        return this.hit;
    }

    public boolean getGameResult() {
        return this.gameResult;
    }

    public List<Integer> getTankDamage() {
        List<Tank> myTanks = this.gameBoard.getTanks();
        List<Integer> result = new ArrayList<>();

        for (Tank eachTank : myTanks) {
            if (eachTank.isAlive()) {
                result.add(eachTank.getMyDamage());
            }
        }

        return result;

    }

    public int getTankNum() {
        return this.tankNum;
    }

}
