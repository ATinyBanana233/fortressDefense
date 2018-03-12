//unicode 只是输出表示

package ca.cmpt213.fortressdefense.ui;


import ca.cmpt213.fortressdefense.model.Model;

/**
 * The class that holds the main application for the FortressDefense program.
 * @author Bei Bei Li + Adam Labecki
 */
public class FortressDefense {

    public static void main(String[] args) {

        boolean useDefault;
        boolean useCheat = false;

        //check arguments
        argsNumCheck(args);
        argsTankNumCheck(args);
        useDefault = argsDefaultCheck(args);

        //check cheat
        if (!useDefault) {
            useCheat = argsCheatCheck(args);
        }

        //initializing model

        int tankNum = 0;
        if (useDefault){
            tankNum = 5;
        }
        else {
            tankNum = Integer.parseInt(args[0]);
        }

        int boardSizeX = 10;
        int boardSizeY = 10;

        Model myModel = new Model(tankNum, boardSizeX, boardSizeY);

        //initializing UI
        UI myUI = new UI(myModel, useCheat);

        boolean play = true;

        if (useCheat){
            myUI.displayCheat();
            System.out.println();
            System.out.println();
        }

        myUI.printGameHeader(tankNum);

        while (play) {
            play = myUI.playEachRound();
        }

    }

    private static void argsNumCheck(String[] args) {

        int maxValidArgsNumber = 2;

        if (args.length > maxValidArgsNumber) {
            exitProgram("The program expects 0, 1, 2 arguments");
        }

    }

    private static void argsTankNumCheck(String[] args) {

        if (args.length > 0) {
            int tankNum = Integer.parseInt(args[0]);
            if (tankNum < 1) {
                exitProgram("0 tank specified, game cannot start");
            }
            else if (tankNum > 25) {
                exitProgram("Impossible to place this number of tanks on the 10x10 grid");
            }
        }

    }

    private static boolean argsDefaultCheck(String[] args){

        boolean result = false;

        if (args.length == 0) {
            result = true;
        }

        return result;

    }

    private static boolean argsCheatCheck(String[] args) {

        boolean result = false;

        if (args.length == 2) {

            String cheat = args[1];

            if (cheat.toLowerCase().compareTo("--cheat") == 0) {
                //System.out.println("test --cheat");
                result = true;
            }
            else {
                exitProgram("Second argument is invalid, must be \"--cheat\" (case insensitive)");
            }

        }

        return result;

    }

    private static void exitProgram(String error) {

        final int FAILURE = -1;
        System.out.println(error);
        System.out.println("Now exiting program.");
        System.exit(FAILURE);

    }


}
