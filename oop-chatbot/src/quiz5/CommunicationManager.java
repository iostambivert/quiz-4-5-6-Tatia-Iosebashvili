package quiz5;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CommunicationManager {

    private static final String API_URL = "https://dummy-api-url.com/api/chat";  // Replace with your actual API endpoint

    public String sendRequest(String userInput, List<String> conversationHistory) {
        try {
            // Create URL object and open connection
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set HTTP method and headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Construct JSON payload
            String jsonInputString = "{\"message\": \"" + userInput + "\", \"history\": " + convertHistoryToJsonArray(conversationHistory) + "}";

            // Enable output (POST data)
            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                wr.write(input, 0, input.length);
            }

            // Read response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String convertHistoryToJsonArray(List<String> conversationHistory) {
        StringBuilder jsonArray = new StringBuilder("[");
        for (int i = 0; i < conversationHistory.size(); i++) {
            jsonArray.append("\"").append(conversationHistory.get(i)).append("\"");
            if (i < conversationHistory.size() - 1) {
                jsonArray.append(",");
            }
        }
        jsonArray.append("]");
        return jsonArray.toString();
    }

    public static void main(String[] args) {
        CommunicationManager manager = new CommunicationManager();
        String userInput = "Hello!";
        List<String> conversationHistory = List.of("Hi!", "How are you?", "I'm fine, thank you!");
        String response = manager.sendRequest(userInput, conversationHistory);
        System.out.println("API Response: " + response);
    }
}