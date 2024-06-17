package quiz5;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyCommunicationManager {
    private Map<String, Map<String, String>> responses;
    private List<String> conversationHistory;  // New field to store conversation history

    public DummyCommunicationManager() {
        loadResponses();
    }

    private void loadResponses() {
        // JSON-like data manually parsed into a HashMap
        responses = new HashMap<>();

        // Load responses from JSON-like structure
        // were using JSON-like structure because we are doing this in Dummy file
        responses.put("greetings", new HashMap<>());
        responses.get("greetings").put("Hello!", "Hi!");
        responses.get("greetings").put("Hi there!", "Hello!");
        responses.get("greetings").put("Hey, how can I help?", "Hey there!");

        responses.put("questions", new HashMap<>());
        responses.get("questions").put("What can you do?", "I can help answer questions about various topics. Just ask!");
        responses.get("questions").put("How are you?", "I'm just a bot, but I'm here to assist you!");
        responses.get("questions").put("Tell me a joke.", "Why don't scientists trust atoms? Because they make up everything!");

        responses.put("default_answer", new HashMap<>());
        responses.get("default_answer").put("default", "I'm sorry, I didn't understand that.");

        responses.put("time_question", new HashMap<>());
        responses.get("time_question").put("What time is it now?", "{display_time}");

        // Add more categories and responses as needed
    }

    public String sendMessage(String message, List<String> chatHistory) {
        // Check each category for a matching message
        conversationHistory = chatHistory;// it takes in chatHistory too
        for (Map.Entry<String, Map<String, String>> category : responses.entrySet()) {
            if (category.getValue().containsKey(message)) {
                String response = category.getValue().get(message);

                // Handle dynamic placeholders for time
                if (response.contains("{display_time}")) {
                    response = response.replace("{display_time}", getCurrentTime());
                }

                return response;
            }
        }

        // Return the default unknown response if no match is found
        return responses.get("default_answer").get("default");
    }

    public void updateConversationHistory(List<String> conversationHistory) {
        this.conversationHistory = conversationHistory;
        // Optionally, we can log or process the updated history here
    }

    private String getCurrentTime() {
        // Use java.util.Date to get current time
        Date now = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(now);
    }
}


