package com.macewan305;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PropertyAssessments {

    /*
    Arguments:
    PropertyAssessment[] loadedProperties = An array of Property Assessment objects.

    Purpose:
    This function cycles through an array of PropertyAssessment objects and adds their assessment values to
    an Integer List.
    This Integer List is then returned.
    */
    public static List<Integer> getAssessmentValues(PropertyAssessment[] loadedProperties){
        List<Integer> intList = new ArrayList<>();

        for (int eachProperty = 0; eachProperty < loadedProperties.length; eachProperty++){
            intList.add(loadedProperties[eachProperty].assessmentVal());
        }
        return intList;
    }


}
