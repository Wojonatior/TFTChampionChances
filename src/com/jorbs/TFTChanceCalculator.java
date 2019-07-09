package com.jorbs;

import java.lang.Math;

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
    static int REROLL_COST = 2;
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
        // Basic spellcheck?
        // Split into pairs and look for keywords in pair to identify
        return new InputParameters(0,0,0,0,0);
    }

    public static double GetChoiceProbability(double desiredRemaining, double otherRemaining, double numPicks){
        double total = desiredRemaining + otherRemaining;
        double cumalativeProbability = 0;
        for(int i=0; i<numPicks; i+=1){
            cumalativeProbability +=
                (1.0 - cumalativeProbability) * (desiredRemaining / (total - i));
        }
        return cumalativeProbability;
    }

    public static double GetBinomialDeviation(double probability, double n){
        return Math.sqrt(n * probability * (1.0 - probability));
    }

    public static double ProbabilityBeforeBroke(double probability, int goldRemaining){
        int chancesRemaining = goldRemaining / REROLL_COST;
        double cumalativeProbability = 0;
        for(int i=0; i<chancesRemaining; i++){
            cumalativeProbability +=
                (1.0 - cumalativeProbability) * (probability);
        }
        return cumalativeProbability;
    }

    public static String CalculateChances(InputParameters parameters){
        if(parameters.GoldAvailable < REROLL_COST)
            return "0% chance, because you're broke";
        if(parameters.CurrentLevel < 2  || parameters.CurrentLevel > ProbabilityTable.length)
            return "That's not a valid level";
        if(parameters.Tier < 1  || parameters.Tier > 5)
            return "That's not a valid tier";
        if(ProbabilityTable[parameters.CurrentLevel - 1].length < parameters.Tier)
            return "Your level doesn't have champions of that tier";
        double tierProbability = ProbabilityTable[parameters.CurrentLevel - 1][parameters.Tier - 1];
        double champChoiceProbability = GetChoiceProbability(parameters.Remaining, parameters.Others, NUM_PICKS);
        double totalProbability = tierProbability * champChoiceProbability;
        double singleRerollDeviation = REROLL_COST *  GetBinomialDeviation(totalProbability, 1);
        double probabilityBeforeBroke = ProbabilityBeforeBroke(totalProbability, parameters.GoldAvailable);
        return String.format("%.2f% likely per reroll, deviation of cost: %.2f gold, chance to hit before broke: %.2f%.",
                totalProbability,
                singleRerollDeviation,
                probabilityBeforeBroke);


//        "There are 0 of the champion that you want remaining";
//        "You provided less than 5 total champs"; ???
    }
}
