package com.macewan305;

import java.util.function.Predicate;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CsvPropertyAssessmentDAO implements PropertyAssessmentDAO {


    // Database of all property assessments in CSV file.
    private List<PropertyAssessment> allProperties = new ArrayList<>();

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

        while((eachLine = lineBuffer.readLine()) != null){
            String[] propertyData = eachLine.split(",");

            // Force array to be size of 18
            propertyData = Arrays.copyOf(propertyData, 18);

            // Object.toString for 16 and 17 are null checks.
            PropertyAssessment eachProperty = new PropertyAssessment(Integer.parseInt(propertyData[0]),propertyData[1],propertyData[2],
                    propertyData[3],propertyData[4],propertyData[5],propertyData[6],propertyData[7],Integer.parseInt(propertyData[8]),propertyData[9],propertyData[10],propertyData[11],
                    propertyData[12], propertyData[13], propertyData[14],propertyData[15], Objects.toString(propertyData[16], ""),Objects.toString(propertyData[17], ""));


            allProperties.add(eachProperty);
        }
    }

    /*
    Arguments:
    Predicate<PropertyAssessment> check = A Predicate to base the filter off of (i.e. filter for a specific ward)

    Purpose:
    This function is the general all-purpose filter to help reduce verbosity and maintain the DRY (don't repeat yourself) Principle.
    The passed Predicate is the test used to determine if something should be added to the new filtered output.
    */
    private List<PropertyAssessment> filterProperties(Predicate<PropertyAssessment> check) {

        List<PropertyAssessment> filtered = new ArrayList<>();

        for (PropertyAssessment eachProperty : allProperties){

            if (check.test(eachProperty)){
                filtered.add(eachProperty);
            }

        }

        return filtered;
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

        for (PropertyAssessment property : allProperties){

            if (property.getAccountNum() == accountNumber) {
                return property;
            }
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
    public List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood){

        Predicate<PropertyAssessment> neighbourhoodFilter = property -> property.getNeighbourhoodName().equalsIgnoreCase(nameOfNeighbourhood);

        List<PropertyAssessment> filtered = filterProperties(neighbourhoodFilter);

        return filtered;
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
    public List<PropertyAssessment> getAssessClass(String nameOfAssessClass){

        Predicate<PropertyAssessment> assessClass = property -> property.getAssess1Name().equalsIgnoreCase(nameOfAssessClass) ||
                property.getAssess2Name().equalsIgnoreCase(nameOfAssessClass) ||
                property.getAssess3Name().equalsIgnoreCase(nameOfAssessClass);

        List<PropertyAssessment> filtered = filterProperties(assessClass);

        return filtered;

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
    public List<PropertyAssessment> getWard(String nameOfWard) {

        Predicate<PropertyAssessment> wardFilter = property -> property.getWard().equalsIgnoreCase(nameOfWard);

        List<PropertyAssessment> filtered = filterProperties(wardFilter);

        return filtered;
    }

    /*
    Purpose:
    This simply returns the entire database.
     */
    @Override
    public List<PropertyAssessment> getAll() {
        return allProperties;
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
    public List<PropertyAssessment> getData(int limit){

        List<PropertyAssessment> filtered = new ArrayList<>();

        if (limit <= 0 ){ // 0 or a negative value was passed into the argument
            return filtered;
        }

        int index = 0;

        for (PropertyAssessment property: allProperties) {

            if (index == limit){
                break;
            }

            filtered.add(property);
            index++;

        }
        return filtered;
    }

    @Override
    public List<PropertyAssessment> getData(int limit, int offset){

        List<PropertyAssessment> filtered = new ArrayList<>();

        if (limit <= 0 || offset < 0 || offset >= allProperties.size()){ // 0 or a negative value was passed in either argument
            return filtered;
        }

        int index = offset;
        int limitCount = 0;

        while (limitCount < limit && index < allProperties.size()) {

            filtered.add(allProperties.get(index));
            index++;
            limitCount++;

        }
        return filtered;
    }
}
