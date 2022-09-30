package com.macewan305;

import java.util.*;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Files;
import static java.lang.Math.round;

public class PropertyAssessments {


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
    public PropertyAssessment[] neighbourHoodFilter(PropertyAssessment[] loadedProperties, String nameOfNeighbourhood){

        PropertyAssessment[] filtered = new PropertyAssessment[100];
        int index = 0;
        int filterIndex = 0;
        while(index != loadedProperties.length){

            if (loadedProperties[index].neighbourhoodName().compareTo(nameOfNeighbourhood) == 0){

                if (filterIndex == filtered.length){
                    filtered = Arrays.copyOf(filtered, filtered.length * 2);
                }

                filtered[filterIndex] = loadedProperties[index];
                filterIndex ++;
            }
            index++;

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

    public static int median(PropertyAssessment[] loadedProperties){
        int eachProperty = 0;
        List<Integer> intList= new ArrayList<>();
        while(eachProperty != loadedProperties.length){
            intList.add(loadedProperties[eachProperty].assessmentVal());
            eachProperty++;
        }
        Collections.sort(intList);
        if((loadedProperties.length % 2) == 0){     // If even amount of properties
            int property1 = intList.get(loadedProperties.length / 2);
            int property2 = intList.get((loadedProperties.length / 2) - 1);
            return((property2+property1) / 2);
        }

        else{
            return(intList.get(loadedProperties.length / 2));
        }
    }

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


    /*public static List<String> assessClass(PropertyAssessment[] loadedProperties){

        int line = 0;
        List<String> classes = new ArrayList<>();


        while (line != loadedProperties.length) {

            String class1Full;
            String class2Full;
            String class3Full;


            if (loadedProperties.length == 16) {      // 16 means that there is only 1 assessment class. 17 / 18 for 2 and 3 classes respectively [Do not want to go out of bounds]

                class1Full = String.join(" ",loadedProperties[line][15], loadedProperties[line][12] + "%");
                classes.add(class1Full);

            }

            else if (loadedProperties.length == 17) {

                class1Full = String.join(" ",loadedProperties[line][15], loadedProperties[line][12] + "%");
                class2Full = String.join(" ",loadedProperties[line][16], loadedProperties[line][13] + "%");

                classes.add(String.join(" / ", class1Full,class2Full));

            }

            else if (loadedProperties.length == 18) {

                class1Full = String.join(" ",loadedProperties[line][15], loadedProperties[line][12] + "%");
                class2Full = String.join(" ",loadedProperties[line][16], loadedProperties[line][13] + "%");
                class3Full = String.join(" ",loadedProperties[line][17], loadedProperties[line][14] + "%");

                classes.add(String.join(" / ", class1Full,class2Full,class3Full));

            }


            line++;
        }

        return classes;
    }*/
}
