package com.macewan305;

import java.util.function.Predicate;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class CsvPropertyAssessmentDAO implements PropertyAssessmentDAO {


    // Database of all property assessments in CSV file.
    private PropertyAssessment[] allProperties;

    private PropertyAssessment[] filterProperties(Predicate<PropertyAssessment> check) {

        PropertyAssessment[] filtered = new PropertyAssessment[100];
        int index = 0;
        int filterIndex = 0;
        while(index != allProperties.length){

            if (check.test(allProperties[index])){
                if (filterIndex == filtered.length){
                    filtered = Arrays.copyOf(filtered, filtered.length * 2);
                }

                filtered[filterIndex] = allProperties[index];
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
    Path CSVFile = A file path to a CSV file with property assessments.

    Purpose:
    This CSV file is read and the data for each line (each property) is converted into Property Assessment Objects
    and put into an array of PropertyAssessment objects which is considered the database
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

        allProperties = Arrays.copyOf(allProperties, index);
    }

    @Override
    /*
    Arguments:
    int propertyNum = A potential property number.

    Purpose:
    This function compares the PropertyAssessment Objects in the database to the supplied property number.
    If the property exists, it returns that PropertyAssessment object
    If it does not, Null is returned.
    */
    public PropertyAssessment getAccountNum(int accountNumber){

        int line = 0;
        while (line != allProperties.length){

            if (allProperties[line].accountNum() == accountNumber) {
                return allProperties[line];
            }
            line++;
        }
        return null;
    }

    @Override
    /*
    Arguments:
    String nameOfNeighbourhood = A string that contains the name of a neighbourhood to filter by

    Purpose:
    This function cycles through an array of PropertyAssessment objects and checks if their neighbourhood matches the string argument.
    If the neighbourhood matches, that PropertyAssessment object is added to another PropertyAssessment array.
    This filtered array is then returned.
    */
    public PropertyAssessment[] getNeighbourhood(String nameOfNeighbourhood){

        Predicate<PropertyAssessment> neighbourhoodFilter = property -> property.neighbourhoodName().equalsIgnoreCase(nameOfNeighbourhood);

        PropertyAssessment[] filtered = filterProperties(neighbourhoodFilter);
        return Arrays.copyOf(filtered, filtered.length);
    }

    @Override
    /*
    Arguments:
    String nameOfAssessClass = A string that contains the name of an assessment class to filter by

    Purpose:
    This function cycles through an array of PropertyAssessment objects and checks if any assessment classes match the string argument.
    If the class matches, that PropertyAssessment object is added to another PropertyAssessment array.
    This filtered array is then returned.
    */
    public PropertyAssessment[] getAssessClass(String nameOfAssessClass){

        PropertyAssessment[] filtered = new PropertyAssessment[100];
        int index = 0;
        int filterIndex = 0;
        while(index != allProperties.length){

            if ((allProperties[index].assess1Name().compareTo(nameOfAssessClass.toUpperCase()) == 0) || (allProperties[index].assess2Name().compareTo(nameOfAssessClass.toUpperCase()) == 0) || (allProperties[index].assess3Name().compareTo(nameOfAssessClass.toUpperCase()) == 0)){

                if (filterIndex == filtered.length){
                    filtered = Arrays.copyOf(filtered, filtered.length * 2);
                }

                filtered[filterIndex] = allProperties[index];
                filterIndex ++;
            }
            index++;

        }
        if (filterIndex == 0){ // This means there were no matches.
            return null;
        }
        return Arrays.copyOf(filtered,filterIndex);

    }

    @Override
    public PropertyAssessment[] getAll() {
        return Arrays.copyOf(allProperties, allProperties.length);
    }

    @Override
    public PropertyAssessment[] getData(int limit){

        PropertyAssessment[] filtered = new PropertyAssessment[100];
        int index = 0;
        int filterIndex = 0;
        while(index != limit && index < allProperties.length) {

            if (filterIndex == filtered.length) {
                filtered = Arrays.copyOf(filtered, filtered.length * 2);
            }

            filtered[filterIndex] = allProperties[index];
            filterIndex++;
            index++;
        }
        if (filterIndex == 0){ // This means there were no matches.
            return null;
        }
        return Arrays.copyOf(filtered,filterIndex);
    }


}
