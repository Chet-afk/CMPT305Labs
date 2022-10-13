package com.macewan305;

import java.util.*;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Files;
import static java.lang.Math.round;

public class PropertyAssessments {


    /*
    Arguments:
    Path CSVFile = A file path to a CSV file with property assessments.

    Purpose:
    This CSV file is read and the data for each line (each property) is converted into Property Assessment Objects
    and put into an array of PropertyAssessment objects which is then returned.
     */
    public static PropertyAssessment[] formatData(Path CSVFile) throws Exception{
        BufferedReader lineBuffer = Files.newBufferedReader(CSVFile);
        lineBuffer.readLine(); // Remove headers
        String eachLine;

        int index = 0;
        PropertyAssessment[] data = new PropertyAssessment[100];

        while((eachLine = lineBuffer.readLine()) != null){
            String[] propertyData = eachLine.split(",");

            // Force array to be size of 18
            propertyData = Arrays.copyOf(propertyData, 18);

            // Object.toString for 16 and 17 are null checks.
            PropertyAssessment eachProperty = new PropertyAssessment(Integer.parseInt(propertyData[0]),propertyData[1],propertyData[2],
                    propertyData[3],propertyData[4],propertyData[5],propertyData[6],propertyData[7],Integer.parseInt(propertyData[8]),propertyData[9],propertyData[10],propertyData[11],
                    propertyData[12], propertyData[13], propertyData[14],propertyData[15],Objects.toString(propertyData[16], ""),Objects.toString(propertyData[17], ""));


            if(index == data.length){

                data = Arrays.copyOf(data, data.length * 2);
            }

            data[index] = eachProperty;

            index++;
        }

        return Arrays.copyOf(data, index);
    }

    /*
    Arguments:
    PropertyAssessment[] loadedProperties = An array of Property Assessment objects.
    String nameOfNeighbourhood = A string that contains the name of a neighbourhood to filter by

    Purpose:
    This function cycles through an array of PropertyAssessment objects and checks if their neighbourhood matches the string argument.
    If the neighbourhood matches, that PropertyAssessment object is added to another PropertyAssessment array.
    This filtered array is then returned.
    */
    public static PropertyAssessment[] neighbourHoodFilter(PropertyAssessment[] loadedProperties, String nameOfNeighbourhood){

        PropertyAssessment[] filtered = new PropertyAssessment[100];
        int index = 0;
        int filterIndex = 0;
        while(index != loadedProperties.length){

            if (loadedProperties[index].neighbourhoodName().compareTo(nameOfNeighbourhood.toUpperCase()) == 0){

                if (filterIndex == filtered.length){
                    filtered = Arrays.copyOf(filtered, filtered.length * 2);
                }

                filtered[filterIndex] = loadedProperties[index];
                filterIndex ++;
            }
            index++;

        }
        if (filterIndex == 0){ // This means there were no matches.
            return null;
        }
        return Arrays.copyOf(filtered,filterIndex);
    }
    /*
    Arguments:
    PropertyAssessment[] loadedProperties = An array of Property Assessment objects.
    String nameOfAssessClass = A string that contains the name of an assessment class to filter by

    Purpose:
    This function cycles through an array of PropertyAssessment objects and checks if any of their assessment classes match
    the string argument.
    If any class matches, that PropertyAssessment object is added to another PropertyAssessment array.
    This filtered array is then returned.
    */
    public static PropertyAssessment[] assessClassFilter(PropertyAssessment[] loadedProperties, String nameOfAssessClass){

        PropertyAssessment[] filtered = new PropertyAssessment[100];
        int index = 0;
        int filterIndex = 0;
        while(index != loadedProperties.length){

            if ((loadedProperties[index].assess1Name().compareTo(nameOfAssessClass.toUpperCase()) == 0) || (loadedProperties[index].assess2Name().compareTo(nameOfAssessClass.toUpperCase()) == 0) || (loadedProperties[index].assess3Name().compareTo(nameOfAssessClass.toUpperCase()) == 0)){

                if (filterIndex == filtered.length){
                    filtered = Arrays.copyOf(filtered, filtered.length * 2);
                }

                filtered[filterIndex] = loadedProperties[index];
                filterIndex ++;
            }
            index++;

        }
        if (filterIndex == 0){ // This means there were no matches.
            return null;
        }
        return Arrays.copyOf(filtered,filterIndex);
    }

    public static int numOfLines(PropertyAssessment[] loadedProperties){

        return loadedProperties.length;
    }

    public static int[] lowHighAssess(PropertyAssessment[] loadedProperties){
        int lowAssess = 0;
        int highAssess = 0;
        int current;
        int index = 0;

        while(index != loadedProperties.length){

            current = loadedProperties[index].assessmentVal(); // Assumes that the property value will always be in the 8th column

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

    public static int range(int[] lowHigh){
        return lowHigh[1] - lowHigh[0];
    }

    public static int mean(PropertyAssessment[] loadedProperties){
        int line = 0;
        long total = 0; // long because total value gets too big for regular ints
        while (line != loadedProperties.length){
            total += loadedProperties[line].assessmentVal();
            line++;
        }
        return (Math.round((float) total / loadedProperties.length));
    }

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

    /*
    Arguments:
    PropertyAssessment[] loadedProperties = An array of Property Assessment objects.
    int propertyNum = A potential property number.

    Purpose:
    This function compares the PropertyAssessment Objects in a list to the supplied property number.
    If the property exists, it returns that PropertyAssessment object
    If it does not, Null is returned.
    */
    public static PropertyAssessment findAccount(PropertyAssessment[] loadedProperties, int propertyNum){

        int line = 0;
        while (line != loadedProperties.length){

            if (loadedProperties[line].accountNum() == propertyNum){     // Checks to see if the ward is part of the list to stop duplicates
                return loadedProperties[line];
            }
            line++;
        }
        return null;
    }

}
