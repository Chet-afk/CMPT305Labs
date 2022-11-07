package com.macewan305;

import java.util.*;

public class PropertyAssessments {



    /**
     * This function cycles through an array of PropertyAssessment objects and adds their assessment values to
     * an Integer List.
     *
     * @param loadedProperties: An array of Property Assessment objects.
     * @return A list of the collected integers
     */
    public static List<Integer> getAssessmentValues(List<PropertyAssessment> loadedProperties){
        List<Integer> intList = new ArrayList<>();

        for (PropertyAssessment property: loadedProperties){
            intList.add(property.getAssessmentVal());
        }
        return intList;
    }

    /**
     *
     * Each list of property assessments (in the overall List of lists) are assumed to be from different filters. This will
     * create a single List of intersection of all those filters (i.e the ones they have in common).
     *
     * @param listOfProps: A list that contains lists of property assessments
     * @return A list of all the properties that each list (filter type) have in common
     */
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
