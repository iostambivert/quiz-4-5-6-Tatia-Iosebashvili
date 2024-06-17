package quiz5;

import quiz6.SpecialCommunicationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInteractionManager {

    private DummyCommunicationManager dummyCommunicationManager;
    private SpecialCommunicationManager specialCommunicationManager;
    private boolean useSpecialManager; // Flag to track if SpecialCommunicationManager should be used
    private Scanner scanner;
    private List<String> chatHistory;

    public UserInteractionManager() {
        dummyCommunicationManager = new DummyCommunicationManager();
        specialCommunicationManager = new SpecialCommunicationManager();
        useSpecialManager = false; // Start with DummyCommunicationManager
        scanner = new Scanner(System.in);
        chatHistory = new ArrayList<>();
    }

    public void startChat() {
        System.out.println("Chat started. Type 'exit' to end the chat.");

        String input;

        do {
            System.out.print("You: ");
            input = scanner.nextLine().trim();

            if (!input.equalsIgnoreCase("exit")) {
                // Check if input or history contains "help" to switch communication manager
                if (!useSpecialManager && containsHelpKeyword(input)) {
                    useSpecialManager = true;
                    System.out.println("Switching to Special Communication Manager.");
                }

                // Determine which communication manager to use
                String response;
                if (useSpecialManager) {
                    response = specialCommunicationManager.sendMessage(input, chatHistory);
                } else {
                    response = dummyCommunicationManager.sendMessage(input,chatHistory);
                }

                // Print bot response and update chat history
                System.out.println("Bot: " + response);
                chatHistory.add("You: " + input);
                chatHistory.add("Bot: " + response);
            }
        } while (!input.equalsIgnoreCase("exit"));

        System.out.println("Chat ended.");
        scanner.close();
    }

    private boolean containsHelpKeyword(String message) {
        return message.toLowerCase().contains("help");
    }

    public static void main(String[] args) {
        UserInteractionManager manager = new UserInteractionManager();
        manager.startChat();
    }

}
