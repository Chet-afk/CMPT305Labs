package com.macewan305;

import java.util.*;


public class Statistics {

    /*
    Arguments:
    List<Integer> intList = A List of integer values

    Purpose:
    This returns the median value of the given Integer List.
    */

    public static int median(List<Integer> intList){

        Collections.sort(intList);
        if((intList.size() % 2) == 0){     // If even amount of values
            int value1 = intList.get(intList.size() / 2);
            int value2 = intList.get((intList.size() / 2) - 1);
            return((value2+value1) / 2);
        }

        else{
            return(intList.get(intList.size() / 2));
        }
    }

    /*
    Arguments:
    int[] lowHigh = An array of 2 integer values

    Purpose:
    This returns the difference between the two integers in the array.
    */
    public static int range(int[] lowHigh){
        return lowHigh[1] - lowHigh[0];
    }

    /*
    Arguments:
    List<Integer> intList = A List of integer values

    Purpose:
    This returns the average (mean) of the integer List.
    */
    public static int mean(List<Integer> intList){
        int line = 0;
        long total = 0; // long because total value gets too big for regular ints
        while (line != intList.size()){
            total += intList.get(line);
            line++;
        }
        return (Math.round((float) total / intList.size()));
    }

    /*
    Arguments:
    List<Integer> intList = A List of integer values

    Purpose:
    This returns an integer array of size 2, that includes the lowest and highest values in the integer list
    */
    public static int[] lowHighAssess(List<Integer> intList){
        int lowAssess = 0;
        int highAssess = 0;
        int current;
        int index = 0;

        while(index != intList.size()){

            current = intList.get(index); // Assumes that the property value will always be in the 8th column

            if(index == 0){         // Set lowest count to the first checked value, so there is a baseline for comparison.
                lowAssess = current;
            }
            if (current > highAssess) {
                highAssess = current;
            } else if (current < lowAssess) {
                lowAssess = current;
            }
            index++;

        }

        int[] returnVals = new int[2];
        returnVals[0] = lowAssess;
        returnVals[1] = highAssess;

        return returnVals;

    }

}
