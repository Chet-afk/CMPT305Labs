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

    /*
    Arguments:
    Predicate<PropertyAssessment> check = A Predicate to base the filter off of (i.e. filter for a specific ward)

    Purpose:
    This function is the general all-purpose filter to help reduce verbosity and maintain the DRY (don't repeat yourself) Principle.
    The passed Predicate is the test used to determine if something should be added to the new filtered output.
    */
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

    /*
    Arguments:
    int accountNumber = A potential property number.

    Purpose:
    This function compares the PropertyAssessment Objects in the database to the supplied property number.
    If the property exists, it returns that PropertyAssessment object
    If it does not, Null is returned.
    */
    @Override
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

    /*
    Arguments:
    String nameOfNeighbourhood = A string that contains the name of a neighbourhood to filter by

    Purpose:
    This function passes Predicate function neighbourhoodFilter through the filterProperties function.
    If no properties are found, null is returned.
    Else, the filtered array of properties in that neighbourhood is returned.
    */
    @Override
    public PropertyAssessment[] getNeighbourhood(String nameOfNeighbourhood){

        Predicate<PropertyAssessment> neighbourhoodFilter = property -> property.neighbourhoodName().equalsIgnoreCase(nameOfNeighbourhood);

        PropertyAssessment[] filtered = filterProperties(neighbourhoodFilter);

        if (filtered == null){
            return null;
        }
        return Arrays.copyOf(filtered, filtered.length);
    }

    /*
    Arguments:
    String nameOfAssessClass = A string that contains the name of an assessment class to filter by

    Purpose:
    This function passes Predicate function assessClass through the filterProperties function.
    If no properties are found, null is returned.
    Else, the filtered array of properties with that assessment class is returned.
    */
    @Override
    public PropertyAssessment[] getAssessClass(String nameOfAssessClass){

        Predicate<PropertyAssessment> assessClass = property -> property.assess1Name().equalsIgnoreCase(nameOfAssessClass) ||
                property.assess2Name().equalsIgnoreCase(nameOfAssessClass) ||
                property.assess3Name().equalsIgnoreCase(nameOfAssessClass);

        PropertyAssessment[] filtered = filterProperties(assessClass);

        if (filtered == null){
            return null;
        }
        return Arrays.copyOf(filtered,filtered.length);

    }
    /*
    Arguments:
    String nameOfWard = A string that contains the name of the ward to filter by

    Purpose:
    This function passes Predicate function wardFilter through the filterProperties function.
    If no properties are found, null is returned.
    Else, the filtered array of properties in that ward is returned.
    */
    @Override
    public PropertyAssessment[] getWard(String nameOfWard) {

        Predicate<PropertyAssessment> wardFilter = property -> property.getWard().equalsIgnoreCase(nameOfWard);

        PropertyAssessment[] filtered = filterProperties(wardFilter);

        if (filtered == null){
            return null;
        }
        return Arrays.copyOf(filtered,filtered.length);
    }

    /*
    Purpose:
    This simply returns the entire database.
     */
    @Override
    public PropertyAssessment[] getAll() {
        return Arrays.copyOf(allProperties, allProperties.length);
    }


    /*
    Arguments:
    int limit = The amount of properties to read up to.

    Purpose:
    This function reads all the properties up to the given limit (or if it goes past the file limit).
    If 0 or a negative number is passed, null is returned.
    Else, the filtered array with that properties with that assessment class is returned.
    */
    @Override
    public PropertyAssessment[] getData(int limit){

        if (limit <= 0 ){ // 0 or a negative value was passed into the argument
            return null;
        }

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
        return Arrays.copyOf(filtered,filterIndex);
    }
}
