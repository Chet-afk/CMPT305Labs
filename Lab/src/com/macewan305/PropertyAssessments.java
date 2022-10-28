package com.macewan305;

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
    public static List<Integer> getAssessmentValues(List<PropertyAssessment> loadedProperties){
        List<Integer> intList = new ArrayList<>();

        for (PropertyAssessment property: loadedProperties){
            intList.add(property.getAssessmentVal());
        }
        return intList;
    }

    public static List<PropertyAssessment> intersectProperties(List<List<PropertyAssessment>> listOfProps) {

        List<PropertyAssessment> intersectedList = listOfProps.get(0);

        for(int i = 1; i < listOfProps.size(); i++) {
            System.out.println("here");
            intersectedList.retainAll(listOfProps.get(i));
        }

        return intersectedList;

    }


}
