package ca.cmpt213.fortressdefense.ui;

import ca.cmpt213.fortressdefense.model.Coordinate;
import ca.cmpt213.fortressdefense.model.GameBoard;
import ca.cmpt213.fortressdefense.model.Model;

import java.util.List;
import java.util.Scanner;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Class to display the text based game user interface to users.
 * @author Bei Bei Li + Adam Labecki
 */
public class UI {

    private Model myModel;
    private boolean cheat; //display unfogged board


    public UI(Model myModel, boolean cheat){

        this.myModel = myModel;
        this.cheat = cheat;

    }

    /**
     * Method to display each round and send user input to game model to process
     * @return The boolean play representing the state of continuation of the game
     */
    public boolean playEachRound() {

        boolean play = true;
        Scanner in = new Scanner(System.in);
        String getCord;
        int X = 0;
        int Y = 0;


        //Step1: display board
        displayGameBoard();


        //Step2: input validation
        boolean inputOK = false;

        while (!inputOK) {
            System.out.print("Enter your move: ");
            getCord = in.nextLine().toLowerCase();

            //use prePlayRound() to check for assignment 4....too lazy to write TAT
            //还是直接检查板上的cord比regex好
            inputOK = inputChecker(getCord);

            //Step3: input parsing
            if (inputOK) {
                X = parseCordX(getCord);
                Y = parseCordY(getCord);
                break;
            }

            System.out.println("Invalid target. Please enter a coordinate such as D10.");

        }

//        System.out.println("Test");
//        System.out.println(X);
//        System.out.println(Y);


        //Step3: let model process the move
        play = myModel.playRound(X,Y);

        //Step5: get the result of the move
        //tank damage, win or lose
        showHitResult();
        showTankDamage();

        //每轮回空两行
        System.out.println();

        //play = false; //testing

        if (!play) {
            displayGameBoard();
            showGameResult();
            System.out.println();
            displayCheat();
        }

        //tank player 输出

        return play;
    }

    /**
     * Method to display the unfogged game board
     */
    public void displayCheat() {
        char screen[][] = myModel.showBoard(cheat);

        System.out.println("Game Board:");

        for (char[] row : screen){
            printRow(row);
        }

        printFortStruct();
        System.out.println("(Lower case tank letters are where you shot.)");
    }

    /**
     * Method to display the fogged game board
     */
    //判定cheat, UI改变输出
    private void displayGameBoard() {

        char screen[][] = myModel.showBoard(false);

        System.out.println("Game Board:");

        for (char[] row : screen){

            printRow(row);
        }

        printFortStruct();

    }

    private void printRow(char[] row) {

        for (char i : row) {
            if (i == 58){
                System.out.printf("   %d", 10);
            }
            else {
                System.out.printf("   %s", i);
            }
        }
        System.out.println();
    }

    public void printGameHeader(int tankNum) {

        System.out.printf("Starting game with %d tanks.%n", tankNum);
        System.out.println("----------------------------");
        System.out.println("Welcome to Fortress Defense!");
        System.out.println("by Adam & Bei Bei :)");
        System.out.println("----------------------------");
        System.out.println();
        System.out.println();

    }

    private void printFortStruct() {
        System.out.printf("Fortress Structure Left: %d.%n", myModel.getPlayerLife());
    }

    /**
     * Method to validate the user move, the move must be a valid coordinate on the game board
     * @param input User input indicating the board coordinate the user wants to hit
     * @return The boolean indicating if the coordinate is valid
     */
    // should use model to valid, but I used regex for A3...too lazy to implement...
    // A3 known to be 10x10 and is fixed so this works
    // validateCord(int X, int Y) not yet implemented in model
    // should use parse then validate input XD
    private boolean inputChecker(String input) {

        boolean result = false;

        if (input.length() <= 3 && input.length() > 1) {


            if (input.length() == 2) {

                String row = "" + input.charAt(0);
                Pattern rowPattern = Pattern.compile("[a-j]");
                Matcher rowMatcher = rowPattern.matcher(row);

                String col = "" + input.charAt(1);
                Pattern colPattern = Pattern.compile("[1-9]");
                Matcher colMatcher = colPattern.matcher(col);

                if (rowMatcher.matches() && colMatcher.matches()){
                    result = true;
                }
            }
            else {

                String row = "" + input.charAt(0);
                Pattern rowPattern = Pattern.compile("[a-j]");
                Matcher rowMatcher = rowPattern.matcher(row);

                String col = "" + input.charAt(1) + input.charAt(2);
                Pattern colPattern = Pattern.compile("[1-9][0]");
                Matcher colMatcher = colPattern.matcher(col);

                if (rowMatcher.matches() && colMatcher.matches()){
                    result = true;
                }
            }


        }

        return result;

    }

    private int parseCordX(String input) {

        char row = input.charAt(0);
        int result = row - 96;
        return result;


    }

    private int parseCordY(String input) {

        int result = -1;

        String col = input.substring(1);
        result = Integer.parseInt(col);

        return result;
    }

    private void showHitResult() {
        if (this.myModel.getHit()) {
            System.out.println("HIT!");
        }
        else {
            System.out.println("Miss.");
        }
    }

    private void showTankDamage() {

        List<Integer> damage = myModel.getTankDamage();

        int tankTotal = myModel.getTankNum();

        for (int i = 0; i < damage.size(); i++){
            System.out.printf("Alive tank #%d of %d shot you for %d!%n", i+1, tankTotal, damage.get(i));
        }

    }

    private void showGameResult() {

        if (this.myModel.getGameResult()){
            System.out.println("Congratulations! You won!");
        }
        else {
            System.out.println("I'm sorry, your fortress has been smashed!");
        }
    }

}
