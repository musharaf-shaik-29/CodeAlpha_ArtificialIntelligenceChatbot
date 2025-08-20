import java.io.*;
import java.util.*;

public class ChatBotTrainer {

    /**
     * Loads question-answer pairs from a training file (e.g., knowledge.txt)
     * Format: question=answer
     */
    public static Map<String, String> loadKnowledgeBase(String filePath) {
        Map<String, String> knowledgeBase = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip blank lines or invalid format
                if (line.isEmpty() || !line.contains("=")) continue;

                String[] parts = line.split("=", 2);
                String key = parts[0].toLowerCase().trim();
                String value = parts[1].trim();

                if (!key.isEmpty() && !value.isEmpty()) {
                    knowledgeBase.put(key, value);
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading knowledge base: " + e.getMessage());
        }

        return knowledgeBase;
    }
}
