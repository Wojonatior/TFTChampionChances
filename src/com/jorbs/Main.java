package com.jorbs;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InputLoop();
    }

    public static void TestInputParser(){
        System.out.println("basic");
        System.out.println(new TFTChanceCalculator.InputParameters(4, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("level 4, gold 30, remaining 16, others 80, tier 3")));
        System.out.println("no commas");
        System.out.println(new TFTChanceCalculator.InputParameters(4, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("level 4 gold 30 remaining 16 others 80 tier 3")));
        System.out.println("all flipped");
        System.out.println(new TFTChanceCalculator.InputParameters(4, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("4 level, 30 gold, 16 remaining, 80 others, 3 tier")));
        System.out.println("some smooshed words/numbers");
        System.out.println(new TFTChanceCalculator.InputParameters(4, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("4level, gold30, 16remaining, others80, tier3")));
        System.out.println("some flipped");
        System.out.println(new TFTChanceCalculator.InputParameters(4, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("level 4, gold 30, 16 remaining, 80 others, tier 3")));
        System.out.println("mixed case");
        System.out.println(new TFTChanceCalculator.InputParameters(4, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("Level 4, GOLD 30, rEmAiNiNg 16, otherS 80, tier 3")));
        System.out.println("1 missing pair");
        System.out.println(new TFTChanceCalculator.InputParameters(4, -1, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("level 4, remaining 16, others 80, tier 3")));
        System.out.println("some missing pairs");
        System.out.println(new TFTChanceCalculator.InputParameters(4, -1, -1, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("level 4, others 80, tier 3")));
        System.out.println("mixed pairs");
        System.out.println(new TFTChanceCalculator.InputParameters(4, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("others 80, remaining 16, tier 3, gold 30, level 4")));
        System.out.println("extra commas");
        System.out.println(new TFTChanceCalculator.InputParameters(4, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("level 4,, gold 30,,, remaining 16, others 80, tier 3")));
        System.out.println("missing value");
        System.out.println(new TFTChanceCalculator.InputParameters(-1, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("level, gold 30, remaining 16, others 80, tier 3")));
        System.out.println("missing values");
        System.out.println(new TFTChanceCalculator.InputParameters(-1, 30, -1, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("level, gold 30, remaining, others 80, tier 3")));
        System.out.println("missing parameter");
        System.out.println(new TFTChanceCalculator.InputParameters(-1, 30, 16, 80, 3).equals(
                TFTChanceCalculator.ParseInputString("4, gold 30, remaining 16, others 80, tier 3")));
        System.out.println("missing parameters");
        System.out.println(new TFTChanceCalculator.InputParameters(4, -1, 16, 0, 3).equals(
                TFTChanceCalculator.ParseInputString("level 4, 30, remaining 16, 80, tier 3")));
        System.out.println("empty string");
        System.out.println(new TFTChanceCalculator.InputParameters(-1, -1, -1, -1, -1).equals(
                TFTChanceCalculator.ParseInputString("")));
    }

    public static void TestProbabilityFunctions(){
        System.out.println(.001 > 0.3 - TFTChanceCalculator.GetChoiceProbability(1, 9, 3));
        System.out.println(.0001 > .5 - TFTChanceCalculator.ProbabilityBeforeBroke(.5, 2));
        System.out.println(.0001 > .75 - TFTChanceCalculator.ProbabilityBeforeBroke(.5, 4));
        System.out.println(.0001 > .875 - TFTChanceCalculator.ProbabilityBeforeBroke(.5, 6));
    }

    public static void TestErrors(){
        System.out.println(
                "You forgot to provide: level".equals(
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(-1,2,1,0,1) )));
        System.out.println(
                "You forgot to provide: level, gold, remaining, others, tier".equals(
                        TFTChanceCalculator.CalculateChances(
                                new TFTChanceCalculator.InputParameters(-1, -1, -1, -1, -1) )));
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

    }

    public static void InputLoop(){
        Scanner scanner = new Scanner(System.in);
        String textInput = "Start";
        System.out.println("Tell me what you'd like to calculate your chances on:");
        while(textInput.length() > 0){
            textInput = scanner.nextLine();
            System.out.println(TFTChanceCalculator.CreateBotResponse(textInput));
        }
    }
}
