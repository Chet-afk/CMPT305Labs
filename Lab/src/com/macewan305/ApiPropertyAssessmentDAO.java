package com.macewan305;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
    String propertyInfo = A string that hasn't been modified at all, but holds the information to make a property

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

    /*
    Arguments:
    String queryType = A string that defines what kind of search we are requesting (i.e. neighbourhood name, ward name, account number etc)
    String search = The filter by which we are searching

    Purpose:
    This generalist function creates, and conducts a query to the property assessment API. It also calls the createProperty method and fills a list
    with the obtained information.

    It then returns this list of the newly created PropertyAssessments made from the information found.
     */

    private List<PropertyAssessment> filter (String queryType, String search) {

        String query = endpoint + "?" + queryType + "=" + search;
        List<PropertyAssessment> filter = new ArrayList<>();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .GET()
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String[] arr = response.body().split("\n");

            if(arr.length == 1){     // Return if there was no retrieved property
                return null;
            }

            for (int i = 1 ; i < arr.length; i++) {

                filter.add(createProperty(arr[i]));
            }

            return filter;

        } catch (IOException | InterruptedException e){
            return null;
        }

    }

    @Override
    public PropertyAssessment getAccountNum(int accountNumber) {

        List<PropertyAssessment> filtered = filter("account_number", Integer.toString(accountNumber));

        if (filtered != null) {
            return filtered.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood) {

        return filter("neighbourhood", nameOfNeighbourhood.toUpperCase());
    }

    @Override
    public List<PropertyAssessment> getAssessClass(String nameOfAssessClass) {

        List<PropertyAssessment> classProps = filter("mill_class_1", nameOfAssessClass.toUpperCase());
        List<PropertyAssessment> classProps2 = filter("mill_class_2", nameOfAssessClass.toUpperCase());
        List<PropertyAssessment> classProps3 = filter("mill_class_3", nameOfAssessClass.toUpperCase());

        // Null checks, which is why the calls are separate
        if (classProps2 != null) {
            classProps.addAll(classProps2);
        }

        if (classProps3 != null) {
            classProps.addAll(classProps3);
        }

        return classProps;
    }
    @Override
    public List<PropertyAssessment> getWard(String nameOfWard) {
        return filter("ward", nameOfWard);
    }

    @Override
    public List<PropertyAssessment> getAll() {

        List<PropertyAssessment> allProps = new ArrayList<>();
        List<PropertyAssessment> obtained;
        int page = 0;

        while ((obtained = getData(10000, page)) != null) {
            allProps.addAll(obtained);
            page += 10000;
            System.out.println(allProps.size());
        }

        return allProps;
    }

    @Override
    public List<PropertyAssessment> getData(int limit) {
        return filter("$limit", Integer.toString(limit));
    }

    @Override
    public List<PropertyAssessment> getData(int limit, int offset) {
        return filter("$limit=" + limit + "&$offset", Integer.toString(offset));
    }

}
