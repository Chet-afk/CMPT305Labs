package com.macewan305;

import java.net.http.HttpClient;

public class PublicSchoolsDAO {
    private final String endpoint;

    private final HttpClient client;

    public PublicSchoolsDAO() {
        endpoint = "https://data.edmonton.ca/resource/d7pj-rist.csv";
        client = HttpClient.newHttpClient();
    }

    


}
