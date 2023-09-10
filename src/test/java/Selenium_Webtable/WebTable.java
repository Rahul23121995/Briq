package Selenium_Webtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebTable{

    public static void main(String[] args) {
        try {
            // URL of the web page containing the table
            String url = "http://the-internet.herokuapp.com/challenging_dom";

            // Connecting to the website and fetch the HTML content
            Document doc = Jsoup.connect(url).get();

            // Finding the table element
            Element table = doc.select("table").first();

            // Check if a table was found
            if (table != null) {
                // Parse the table and print it with the last column removed
                parseAndPrintTable(table);
            } else {
                System.out.println("No table found on the web page.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private static void parseAndPrintTable(Element table) {
        // Find the header row in the table
        Element headerRow = table.select("thead tr").first();
        if (headerRow != null) {
            // Get all header cells in the header row
            Elements headerCells = headerRow.select("th");
            List<String> headers = new ArrayList();

            // Find all rows in the table body
            Elements rows = table.select("tbody tr");

            // Parse and store the header text
            for (int i = 0; i < headerCells.size() - 1; i++) {
                String headerText = headerCells.get(i).text();
                headers.add(headerText);
            }

            // Find the maximum length of each column
            int[] columnWidths = new int[headers.size()];
            for (int i = 0; i < headers.size(); i++) {
                columnWidths[i] = headers.get(i).length();
            }

            // Calculate column widths based on data
            for (Element row : rows) {
                Elements cells = row.select("td");
                for (int i = 0; i < cells.size() - 1 && i < columnWidths.length; i++) {
                    int cellLength = cells.get(i).text().length();
                    if (cellLength > columnWidths[i]) {
                        columnWidths[i] = cellLength;
                    }
                }
            }

            // Print the headers with padding
            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                int padding = columnWidths[i] - header.length() + 1;
                System.out.print(header + " ".repeat(padding));
            }
            System.out.println();

            // Print the data rows, excluding the last column for each row
            for (Element row : rows) {
                Elements cells = row.select("td");
                for (int i = 0; i < cells.size() - 1 && i < columnWidths.length; i++) {
                    String cellText = cells.get(i).text();
                    int padding = columnWidths[i] - cellText.length() + 1;
                    System.out.print(cellText + " ".repeat(padding));
                }
                System.out.println();
            }
        }
    }
}
