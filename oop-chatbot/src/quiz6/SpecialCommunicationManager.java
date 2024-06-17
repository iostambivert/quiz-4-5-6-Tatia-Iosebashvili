package quiz6;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SpecialCommunicationManager {

    private static final String SPECIAL_SERVICE_URL = "https://special-service-url.com/api/chat";
    private static final String COMMON_SERVICE_URL = "https://common-service-url.com/api/chat";

    public String sendMessage(String userInput, List<String> conversationHistory) {
        // Check if any message in history or current message contains "help"
        if (containsHelpKeyword(userInput) || containsHelpKeywordInHistory(conversationHistory)) {
            // Redirect to special service URL with POST method
            return sendPostRequest(userInput, conversationHistory, SPECIAL_SERVICE_URL);
        } else {
            // Use common service URL with POST method
            return sendPostRequest(userInput, conversationHistory, COMMON_SERVICE_URL);
        }
    }

    private boolean containsHelpKeyword(String message) {
        return message.toLowerCase().contains("help");
    }

    private boolean containsHelpKeywordInHistory(List<String> conversationHistory) {
        for (String message : conversationHistory) {
            if (containsHelpKeyword(message)) {
                return true;
            }
        }
        return false;
    }

    private String sendPostRequest(String userInput, List<String> conversationHistory, String serviceUrl) {
        try {
            URL url = new URL(serviceUrl);
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

}