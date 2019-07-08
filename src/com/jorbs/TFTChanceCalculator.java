package com.jorbs;

public class TFTChanceCalculator {
    public static class InputParameters {
        public int CurrentLevel;
        public int GoldAvailable;
        public int Remaining;
        public int Others;
        public int Tier;

        public InputParameters(int currentLevel, int goldAvailable, int remaining, int others, int tier){
            CurrentLevel = currentLevel;
            GoldAvailable = goldAvailable;
            Remaining = remaining;
            Others = others;
            Tier = tier;
        }
    }

    static int NUM_PICKS = 5;
    static double[][] ProbabilityTable = {
            {},
            {100},
            {65, 30, 5},
            {50, 35, 15},
            {37, 35, 25, 3},
            {24.5, 35, 30, 10, .5},
            {20, 30, 33, 15, 2},
            {15, 25, 35, 20, 5},
            {10, 15, 35, 30, 10}
    };

    public static InputParameters parseInputString(String input){
        return new InputParameters(0,0,0,0,0);
    }

    public static double getChoiceProbability(double desiredRemaining, double otherRemaining, double numPicks){
        double total = desiredRemaining + otherRemaining;
        double cumalativeProbability = 0;
        for(int i=0; i<numPicks; i+=1){
            cumalativeProbability +=
                (1.0 - cumalativeProbability) * (desiredRemaining / (total - i));
        }
        return cumalativeProbability;
    }

    public static String CalculateChances(InputParameters parameters){
        if(parameters.GoldAvailable < 2)
            return "0% chance, because you're broke";
        if(parameters.CurrentLevel < 2  || parameters.CurrentLevel > ProbabilityTable.length)
            return "That's not a valid level";
        if(parameters.Tier < 1  || parameters.Tier > 5)
            return "That's not a valid tier";
        if(ProbabilityTable[parameters.CurrentLevel - 1].length < parameters.Tier)
            return "Your level doesn't have champions of that tier";
        double champProbabilityWithinTier = getChoiceProbability(parameters.Remaining, parameters.Others, NUM_PICKS);

//        "There are 0 of the champion that you want remaining";
//        "You provided less than 5 total champs"; ???
        return "I dunno what your chances are";
    }
}
