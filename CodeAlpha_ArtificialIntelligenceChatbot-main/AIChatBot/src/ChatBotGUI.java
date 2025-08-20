import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Arrays;
import java.util.Date;


public class ChatBotGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private Map<String, String> knowledgeBase;
    private List<String> fallbackResponses;
    private BufferedWriter logWriter;

    public ChatBotGUI() {
        setTitle("AI ChatBot - CodeAlpha");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        knowledgeBase = ChatBotTrainer.loadKnowledgeBase("data" + File.separator + "knowledge.txt");

        fallbackResponses = Arrays.asList(
                "Hmm, I'm not sure I understand that.",
                "Can you rephrase that?",
                "I'm still learning. Try asking something else.",
                "That's interesting! I’ll learn about it soon."
        );

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        inputField.addActionListener(e -> handleUserInput());

        JButton clearButton = new JButton("Clear Chat");
        clearButton.addActionListener(e -> chatArea.setText("Bot: Hello again! What can I do for you?\n"));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(clearButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Logging Setup
        setupLogging();

        appendToChat("Bot: Hello! I'm CodeAlphaBot 🤖. How can I help you today?");
    }

    private void handleUserInput() {
        String userText = inputField.getText().trim().toLowerCase();
        if (userText.isEmpty()) return;

        appendToChat("You: " + userText);
        log("You: " + userText);
        inputField.setText("");

        new Timer().schedule(new TimerTask() {
            public void run() {
                String reply = getBotReply(userText);
                appendToChat("Bot: " + reply);
                log("Bot: " + reply);
            }
        }, 250);
    }

    private String getBotReply(String input) {
        for (String key : knowledgeBase.keySet()) {
            if (input.matches(".*\\b" + Pattern.quote(key) + "\\b.*")) {
                return knowledgeBase.get(key);
            }
        }
        return fallbackResponses.get(new Random().nextInt(fallbackResponses.size()));
    }

    private void appendToChat(String message) {
        chatArea.append(message + "\n");
    }

    private void setupLogging() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File logFile = new File("chat_log_" + timestamp + ".txt");
            logWriter = new BufferedWriter(new FileWriter(logFile, true));
        } catch (IOException e) {
            System.err.println("Failed to initialize chat log: " + e.getMessage());
        }
    }

    private void log(String message) {
        try {
            if (logWriter != null) {
                logWriter.write(message + "\n");
                logWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Logging error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatBotGUI::new);
    }
}
