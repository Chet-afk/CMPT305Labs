package com.macewan305;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApiPropertyAssessmentDAO implements PropertyAssessmentDAO{

    // This is the beginning of any call to the api + the client
    private String endpoint;
    private HttpClient client;

    private int limit = 50000;
    private int offset = 0;

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
    String queryType = A string that defines what kind of search we are requesting (i.e. neighbourhood name, ward name, account number etc.)
    String search = The filter by which we are searching

    Purpose:
    This generalist function creates, and conducts a query to the property assessment API. It also calls the createProperty method and fills a list
    with the obtained information.

    It then returns this list of the newly created PropertyAssessments made from the information found.
     */

    private List<PropertyAssessment> filter (String queryType, String search) throws UnsupportedEncodingException {

        String query = endpoint + "?$limit=" + limit + "&$offset=" + offset + queryType + URLEncoder.encode(search, StandardCharsets.UTF_8);
        List<PropertyAssessment> filter = new ArrayList<>();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .GET()
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String[] arr = response.body().split("\n");

            if (arr.length == 1) {     // Return if there was no retrieved property
                return null;
            }

            for (int i = 1; i < arr.length; i++) {
                filter.add(createProperty(arr[i]));
            }

            return filter;

        } catch (IOException | InterruptedException e){
            return null;
        }

    }

    public void changeLimit(int newLimit) {
        limit = newLimit;
    }
    @Override
    public PropertyAssessment getAccountNum(int accountNumber) throws UnsupportedEncodingException {

        List<PropertyAssessment> filtered = filter("&account_number=", Integer.toString(accountNumber));

        if (filtered != null) {
            return filtered.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood) throws UnsupportedEncodingException {

        List<PropertyAssessment> neighProps = new ArrayList<>();
        List<PropertyAssessment> obtained;

        while ((obtained = filter("&neighbourhood=",nameOfNeighbourhood.toUpperCase())) != null) {
            neighProps.addAll(obtained);
            offset += limit;
        }

        offset = 0;

        return neighProps;

    }

    @Override
    public List<PropertyAssessment> getAssessClass(String nameOfAssessClass) throws UnsupportedEncodingException {

        List<PropertyAssessment> allClassProps = new ArrayList<>();
        List<PropertyAssessment> classProps;

        String queryType = "&$where=mill_class_1='" + nameOfAssessClass.toUpperCase() + "' OR " + "mill_class_2='" + nameOfAssessClass.toUpperCase() + "' OR " + "mill_class_3='" + nameOfAssessClass.toUpperCase() + "'";

        queryType = queryType.replace("'","%27");
        queryType = queryType.replace(" ", "%20");

        while((classProps = filter(queryType, "")) != null) {
            allClassProps.addAll(classProps);

            offset+=limit;
        }

        offset = 0;
        return allClassProps;
    }
    @Override
    public List<PropertyAssessment> getWard(String nameOfWard) throws UnsupportedEncodingException {

        List<PropertyAssessment> wardProps = new ArrayList<>();
        List<PropertyAssessment> obtained;

        while ((obtained = filter("&ward=",nameOfWard)) != null) {
            wardProps.addAll(obtained);
            offset += limit;
        }

        offset = 0;

        return wardProps;
    }

    @Override
    public List<PropertyAssessment> getRange(int lowerVal, int higherVal) throws UnsupportedEncodingException {

        List<PropertyAssessment> inBetween = new ArrayList<>();
        List<PropertyAssessment> eachQuery;

        String queryType = "&$where=assessed_value between '"+ lowerVal + "' and " + "'" + higherVal + "'";
        queryType = queryType.replace("'","%27");
        queryType = queryType.replace(" ", "+");

        while ((eachQuery = filter(queryType,"")) != null) {
            inBetween.addAll(eachQuery);
            offset += limit;
        }

        offset = 0;

        return inBetween;
    }

    @Override
    public List<PropertyAssessment> getAll() throws UnsupportedEncodingException {

        List<PropertyAssessment> allProps = new ArrayList<>();
        List<PropertyAssessment> obtained;

        while ((obtained = filter("","")) != null) {
            allProps.addAll(obtained);
            offset += limit;
        }

        offset = 0;

        return allProps;
    }

    @Override
    public List<PropertyAssessment> getData(int limit) throws UnsupportedEncodingException {
        this.changeLimit(limit);
        List<PropertyAssessment> data = filter("", "");
        this.changeLimit(50000);
        return data;
    }

    @Override
    public List<PropertyAssessment> getData(int limit, int newOffset) throws UnsupportedEncodingException {

        this.changeLimit(limit);
        offset = newOffset;
        List<PropertyAssessment> data = filter("","");
        offset = 0;
        this.changeLimit(50000);
        return data;
    }

}
