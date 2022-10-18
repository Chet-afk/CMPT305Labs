/*
This main function implements the API DAO
 */

import com.macewan305.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Lab5MainB {

    public static void main(String[] args) {

        System.out.println("Enter account number: ");
        Scanner sysin = new Scanner(System.in);

        int accnum = sysin.nextInt();

        String end = "https://data.edmonton.ca/resource/q7d6-ambg.csv";
        String query = end + "?account_number=" + accnum;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
