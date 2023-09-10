package Selenium_Webtable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class API {
    public static void main(String[] args) {
        // Part a: Print current timestamp
        printTimestamp();

        // Fetch data from URL and process it
        fetchAndProcessJsonFromUrl();
    }

    // Part a: Print current timestamp
    private static void printTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy-HH-mm-ss");
        String formattedDate = sdf.format(new Date());
        System.out.println("Current Timestamp: " + formattedDate);
    }

    // Fetch data from URL and process JSON
    private static void fetchAndProcessJsonFromUrl() {
        String apiUrl = "https://data.sfgov.org/resource/p4e4-a5a7.json";

        try {
            // Create a URL object and establish a connection
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Check if the response code is 200 (OK)
            if (conn.getResponseCode() == 200) {
                // Read the JSON content from the response
                InputStream inputStream = conn.getInputStream();
                Scanner scanner = new Scanner(inputStream, "UTF-8");
                StringBuilder jsonContent = new StringBuilder();

                while (scanner.hasNextLine()) {
                    jsonContent.append(scanner.nextLine());
                }

                // Close the input stream and connection
                scanner.close();
                inputStream.close();
                conn.disconnect();

                // Process the JSON content
                processJson(jsonContent.toString());
            } else {
                System.err.println("Failed to fetch data from " + apiUrl + ". Status code: " + conn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Process JSON content
    private static void processJson(String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);

            // Print the JSON data as a table
            System.out.println("Key\tValue");
            jsonNode.fields().forEachRemaining(entry -> {
                System.out.println(entry.getKey() + "\t" + entry.getValue());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
