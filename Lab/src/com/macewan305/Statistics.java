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

}
