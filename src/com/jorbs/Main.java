package com.jorbs;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Tests();
    }

    public static void Tests(){
        System.out.println(
            "0% chance, because you're broke" ==
            TFTChanceCalculator.CalculateChances(
                new TFTChanceCalculator.InputParameters(2,0,1,0,1) ));
        System.out.println(
                "That's not a valid level" ==
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(0,2,5,5,1) ));
        System.out.println(
                "That's not a valid level" ==
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(10,2,5,5,1) ));
        System.out.println(
                "That's not a valid tier" ==
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(2,2,5,5,0) ));
        System.out.println(
                "That's not a valid tier" ==
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(2,2,5,5,6) ));
        System.out.println(
                "Your level doesn't have champions of that tier" ==
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(2,2,5,5,2) ));
        System.out.println(
                "Your level doesn't have champions of that tier" ==
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(2,2,5,5,5) ));
        System.out.println(
                "Your level doesn't have champions of that tier" ==
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(5,2,5,5,5) ));
        System.out.println(
                "Your level doesn't have champions of that tier" ==
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(2,2,5,5,5) ));

        System.out.println(.001 > 0.3 - TFTChanceCalculator.GetChoiceProbability(1, 9, 3));
        System.out.println(.0001 > .5 - TFTChanceCalculator.probabilityBeforeBroke(.5, 2));
        System.out.println(.0001 > .75 - TFTChanceCalculator.probabilityBeforeBroke(.5, 4));
        System.out.println(.0001 > .875 - TFTChanceCalculator.probabilityBeforeBroke(.5, 6));
    }

    public static void InputLoop(){
        Scanner scanner = new Scanner(System.in);
        String textInput = "Start";
        System.out.println("Tell me what you'd like to calculate your chances on:");
        while(textInput.length() > 0){
            textInput = scanner.nextLine();
            TFTChanceCalculator.InputParameters inputParameters = TFTChanceCalculator.parseInputString(textInput);
            String responseString = TFTChanceCalculator.CalculateChances(inputParameters);
            System.out.println(responseString);
        }
    }
}
