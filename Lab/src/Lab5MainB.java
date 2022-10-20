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

        ApiPropertyAssessmentDAO database = new ApiPropertyAssessmentDAO();

        System.out.println(database.getAccountNum(accnum));
    }
}
