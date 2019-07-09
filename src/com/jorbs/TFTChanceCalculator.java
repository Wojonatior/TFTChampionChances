package com.jorbs;

import java.lang.Math;
import java.util.ArrayList;

public class TFTChanceCalculator {
    protected static class InputParameters {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InputParameters params = (InputParameters) o;
            return CurrentLevel == params.CurrentLevel &&
                    GoldAvailable == params.GoldAvailable &&
                    Remaining == params.Remaining &&
                    Others == params.Others &&
                    Tier == params.Tier;
        }
    }

    private static int NUM_PICKS = 5;
    private static int REROLL_COST = 2;
    private static double[][] ProbabilityTable = {
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

    private static boolean HasNumbers(String str){
        return str.matches(".*\\d.*");
    }

    private static boolean HasLetters(String str){
        return str.matches(".*\\D.*");
    }

    private static InputParameters AssignParameter(String paramName, int paramValue, InputParameters params){
        switch(paramName) {
            case "level":
                params.CurrentLevel = paramValue;
                break;
            case "gold":
                params.GoldAvailable = paramValue;
                break;
            case "remaining":
                params.Remaining = paramValue;
                break;
            case "others":
                params.Others = paramValue;
                break;
            case "tier":
                params.Tier = paramValue;
                break;
            default:
                break;
        }
        return params;
    }

    private static InputParameters SplitTokenAndAssign(String mixedToken, InputParameters params){
        String[] splitToken = mixedToken.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        if(splitToken.length == 2) {
            if (HasLetters(splitToken[0]))
                params = AssignParameter(splitToken[0], Integer.parseInt(splitToken[1]), params);
            else
                params = AssignParameter(splitToken[1], Integer.parseInt(splitToken[0]), params);
        }
        return params;
    }

    protected static InputParameters ParseInputString(String input){
        String[] tokens = input.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
        InputParameters params = new InputParameters(-1, -1,-1,-1,-1);
        String paramName = null;
        int paramValue = -1;

        for(int i=0; i<tokens.length; i+=1){
            if(HasNumbers(tokens[i]) && HasLetters(tokens[i]))
                params = SplitTokenAndAssign(tokens[i], params);
            else {
                if(HasLetters(tokens[i]))
                    paramName = tokens[i];
                if(HasNumbers(tokens[i]))
                    paramValue = Integer.parseInt(tokens[i]);
                if(paramValue > -1 && paramName != null){
                    params = AssignParameter(paramName, paramValue, params);
                    paramName = null;
                    paramValue = -1;
                }
            }
        }
        return params;
    }

    protected static double GetChoiceProbability(double desiredRemaining, double otherRemaining, double numPicks){
        double total = desiredRemaining + otherRemaining;
        double cumalativeProbability = 0;
        for(int i=0; i<numPicks; i+=1){
            cumalativeProbability +=
                (1.0 - cumalativeProbability) * (desiredRemaining / (total - i));
        }
        return cumalativeProbability;
    }

    private static double GetBinomialDeviation(double probability, double n){
        return Math.sqrt(n * probability * (1.0 - probability));
    }

    protected static double ProbabilityBeforeBroke(double probability, int goldRemaining){
        int chancesRemaining = goldRemaining / REROLL_COST;
        double cumalativeProbability = 0;
        for(int i=0; i<chancesRemaining; i++){
            cumalativeProbability +=
                (1.0 - cumalativeProbability) * (probability);
        }
        return cumalativeProbability;
    }

    private static String CheckMissingParams(InputParameters parameters){
        ArrayList<String> missingParams = new ArrayList<String>();
        if(parameters.CurrentLevel == -1)
            missingParams.add("level");
        if(parameters.GoldAvailable == -1)
            missingParams.add("gold");
        if(parameters.Remaining == -1)
            missingParams.add("remaining");
        if(parameters.Others == -1)
            missingParams.add("others");
        if(parameters.Tier == -1)
            missingParams.add("tier");
        if(!missingParams.isEmpty())
            return "You forgot to provide: " + String.join(", ", missingParams);
        return null;
    }

    private static String CheckForErrors(InputParameters parameters){
        String missingParamsMessage = CheckMissingParams(parameters);
        if(missingParamsMessage != null)
            return missingParamsMessage;
        if(parameters.GoldAvailable < REROLL_COST)
            return "0% chance, because you're broke";
        if(parameters.CurrentLevel < 2  || parameters.CurrentLevel > ProbabilityTable.length)
            return "That's not a valid level";
        if(parameters.Tier < 1  || parameters.Tier > 5)
            return "That's not a valid tier";
        if(ProbabilityTable[parameters.CurrentLevel - 1].length < parameters.Tier)
            return "Your level doesn't have champions of that tier";
        return null;
    }

    protected static String CalculateChances(InputParameters parameters){
        String errorMessage = CheckForErrors(parameters);
        if(errorMessage != null)
            return errorMessage;

        double tierProbability = ProbabilityTable[parameters.CurrentLevel - 1][parameters.Tier - 1];
        double champChoiceProbability = GetChoiceProbability(parameters.Remaining, parameters.Others, NUM_PICKS);
        double totalProbability = tierProbability * champChoiceProbability;
        double singleRerollDeviation = REROLL_COST *  GetBinomialDeviation(totalProbability, 1);
        double probabilityBeforeBroke = ProbabilityBeforeBroke(totalProbability, parameters.GoldAvailable);

        return String.format("%.2f% likely per reroll, deviation of cost: %.2f gold, chance to hit before broke: %.2f%.",
                totalProbability,
                singleRerollDeviation,
                probabilityBeforeBroke);
    }

    public static String CreateBotResponse (String inputString) {
        InputParameters inputParameters = TFTChanceCalculator.ParseInputString(inputString);
        return TFTChanceCalculator.CalculateChances(inputParameters);
    }
}
