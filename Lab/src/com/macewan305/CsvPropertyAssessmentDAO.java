package com.macewan305;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class CsvPropertyAssessmentDAO implements PropertyAssessmentDAO {


    // Database of all property assessments in CSV file.
    private PropertyAssessment[] allProperties;

    /*
    Arguments:
    Path CSVFile = A file path to a CSV file with property assessments.

    Purpose:
    This CSV file is read and the data for each line (each property) is converted into Property Assessment Objects
    and put into an array of PropertyAssessment objects which is then returned.
     */
    public CsvPropertyAssessmentDAO(Path CSVFile) throws Exception{
        BufferedReader lineBuffer = Files.newBufferedReader(CSVFile);
        lineBuffer.readLine(); // Remove headers
        String eachLine;

        int index = 0;
        allProperties = new PropertyAssessment[100];

        while((eachLine = lineBuffer.readLine()) != null){
            String[] propertyData = eachLine.split(",");

            // Force array to be size of 18
            propertyData = Arrays.copyOf(propertyData, 18);

            // Object.toString for 16 and 17 are null checks.
            PropertyAssessment eachProperty = new PropertyAssessment(Integer.parseInt(propertyData[0]),propertyData[1],propertyData[2],
                    propertyData[3],propertyData[4],propertyData[5],propertyData[6],propertyData[7],Integer.parseInt(propertyData[8]),propertyData[9],propertyData[10],propertyData[11],
                    propertyData[12], propertyData[13], propertyData[14],propertyData[15], Objects.toString(propertyData[16], ""),Objects.toString(propertyData[17], ""));


            if(index == allProperties.length){

                allProperties = Arrays.copyOf(allProperties, allProperties.length * 2);
            }

            allProperties[index] = eachProperty;

            index++;
        }
    }

    @Override
    /*
    Arguments:
    PropertyAssessment[] loadedProperties = An array of Property Assessment objects.
    int propertyNum = A potential property number.

    Purpose:
    This function compares the PropertyAssessment Objects in a list to the supplied property number.
    If the property exists, it returns that PropertyAssessment object
    If it does not, Null is returned.
    */
    public PropertyAssessment getAccountNum(int accountNumber){

        int line = 0;
        while (line != allProperties.length){

            if (allProperties[line].accountNum() == accountNumber){     // Checks to see if the ward is part of the list to stop duplicates
                return allProperties[line];
            }
            line++;
        }
        return null;
    }
}
