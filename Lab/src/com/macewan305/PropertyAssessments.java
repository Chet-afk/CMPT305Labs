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

        // Convert to Sets, since Sets are much faster with the retainAll() function

        if (listOfProps.size() == 0) {
            return new ArrayList<>();
        }

        Set<PropertyAssessment> base = new HashSet<>(listOfProps.get(0));

        for(int i = 1; i < listOfProps.size(); i++) {
            Set<PropertyAssessment> compare = new HashSet<>(listOfProps.get(i));
            base.retainAll(compare);
        }

        List<PropertyAssessment> intersection = new ArrayList<>(base);

        return intersection;

    }


}
