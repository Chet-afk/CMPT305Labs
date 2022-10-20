package com.macewan305;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ApiPropertyAssessmentDAO implements PropertyAssessmentDAO{

    // This is the beginning of any call to the api + the client
    private String endpoint;
    private HttpClient client;


    public ApiPropertyAssessmentDAO() {
        client = HttpClient.newHttpClient();
        endpoint = "https://data.edmonton.ca/resource/q7d6-ambg.csv";
    }

    /*
    Arguments:
    String[] propertyInfo = A string that hasn't been modified at all, but holds the information to make a property

    Purpose:
    This function creates a property assessment with information provided by a string.
     */
    private PropertyAssessment createProperty(String propertyInfo) {

        String[] splitInfo = propertyInfo.split(",");

        for(int i = 0; i < splitInfo.length; i++) {
            splitInfo[i] = splitInfo[i].replaceAll("\"","");
        }

        splitInfo = Arrays.copyOf(splitInfo, 18);   // Ensure it has a space of 18 to prevent going oob

        // Assume everything in splitInfo is of type string. i.e. we must convert all numbers to actual ints if PropertyAssessment object demands it

        PropertyAssessment newProperty = new PropertyAssessment(Integer.parseInt(splitInfo[0]), splitInfo[1], splitInfo[2],
                splitInfo[3], splitInfo[4], splitInfo[5], splitInfo[6], splitInfo[7], Integer.parseInt(splitInfo[8]), splitInfo[9], splitInfo[10], splitInfo[11],
                splitInfo[12], splitInfo[13], splitInfo[14], splitInfo[15], Objects.toString(splitInfo[16], ""), Objects.toString(splitInfo[17], ""));

        return newProperty;

    }

    @Override
    public PropertyAssessment getAccountNum(int accountNumber) {

        String query = endpoint + "?account_number=" + accountNumber;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .GET()
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String[] arr = response.body().split("\n");

            if(arr.length < 2){     // Return if there was no retrieved property
                return null;
            }

            PropertyAssessment retrievedProp = createProperty(arr[1]);

            return retrievedProp;

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood){

        return null;
    }
    @Override
    public List<PropertyAssessment> getAssessClass(String nameOfAssessClass) {
        return null;
    }
    @Override
    public List<PropertyAssessment> getWard(String nameOfWard) {
        return null;
    }

    @Override
    public List<PropertyAssessment> getAll() {
        return null;
    }

    @Override
    public List<PropertyAssessment> getData(int limit) {
        return null;
    }


}
