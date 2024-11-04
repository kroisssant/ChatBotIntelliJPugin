package com.kroissant.ai_chatbot;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.OllamaResult;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AiWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Initialize the chatbot GUI
        AIChatBot chatbot = new AIChatBot(toolWindow);
        // Add the panel to the content
        Content content = ContentFactory.getInstance().createContent(chatbot.getPanel(),
                "Chat Bot", false);
        toolWindow.getContentManager().addContent(content);
    }

    // Private class for the GUI for the chat
    private static class AIChatBot {
        private final ChatBot chat;
        private final JPanel panel = new JPanel();
        private final MessageSection messageSection = new MessageSection();
        private final JBTextField input = new JBTextField();
        private final JButton sendMessage = new JButton("SEND");
        JPanel inputPanel = new JPanel();
        private final JPanel messagePanel = messageSection;


        private AIChatBot(ToolWindow toolWindow) {
            // Initialize the chatbot
            chat = new ChatBot();
            // Set the layout for the main panel
            panel.setLayout(new BorderLayout(10, 10));

            // Add the messagePanel to the main panel
            panel.add(messagePanel,BorderLayout.NORTH);

            // Create and add a panel for the input field and the SEND button
            inputPanel = createSendMessagePanel();
            panel.add(inputPanel, BorderLayout.SOUTH);

            // Add an action for the send button that updates the message panel and send the message
            sendMessage.addActionListener(e -> {
                updateMessagePanel();
            });
            // Add a keystroke action to the text field so that it updates and send the message on ENTER
            input.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
            input.getActionMap().put("enterAction", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateMessagePanel();
                }
            });
        }
        private JPanel createSendMessagePanel () {
            JPanel sendMessagePanel = new JPanel();
            // Set up layout for the SendMessagePanel
            sendMessagePanel.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            sendMessagePanel.add(input, gbc);

            input.setToolTipText("Enter a message");
            input.setTextToTriggerEmptyTextStatus("Enter a message");
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            sendMessagePanel.add(sendMessage, gbc);

            return sendMessagePanel;
        }
        private void updateMessagePanel() {
            // Get the app
            Application app = ApplicationManager.getApplication();

            // Add message to the message section
            messageSection.addMessage(input.getText(), "USER");

            // Make sure that the message section stays within limits
            messageSection.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight() - inputPanel.getHeight()- 10));

            // Creates thread with the repose from the LLM
            app.executeOnPooledThread(() -> {
                OllamaResult response = null;
                try {
                    response = chat.chat(input.getText());
                    // Add response to the GUI
                    messageSection.addMessage(response.getResponse(), "AI");
                } catch (OllamaBaseException | IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            // Repaint the main panel
            panel.revalidate();
            panel.repaint();


        }
        private JPanel getPanel() {
            return panel;
        }
    }
}
