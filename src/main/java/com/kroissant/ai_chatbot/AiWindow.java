package com.kroissant.ai_chatbot;

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
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AiWindow implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        AIChatBot chatbot = new AIChatBot(toolWindow);
        // Add the panel to the content
        Content content = ContentFactory.getInstance().createContent(chatbot.getPanel(),
                "Chat Bot", false);
        toolWindow.getContentManager().addContent(content);
    }
    private static class AIChatBot {
        List<String> context = new ArrayList<String>();
        ChatBot chat;
        ToolWindow window;
        JPanel panel = new JPanel();
        MessageSection messageSection = new MessageSection();
        JBTextField input = new JBTextField();
        JButton sendMessage = new JButton("SEND");
        JPanel inputPanel = new JPanel();
        JBScrollPane messagePanel = messageSection.getScrollableMessagePane();
        private AIChatBot(ToolWindow toolWindow) {
            window = toolWindow;
            setUpButton();
            panel.setLayout(new BorderLayout());
            System.out.println(panel.getHeight());
            panel.add(messagePanel,BorderLayout.NORTH);
            inputPanel = createSendMessagePanel(toolWindow);
            panel.add(inputPanel, BorderLayout.SOUTH);
            chat = new ChatBot();
            updateMessagePanel();
        }

        private void setUpButton() {
            sendMessage.addActionListener(e -> {
                updateMessagePanel();

            });
        }
        private JPanel createSendMessagePanel (ToolWindow toolWindow) {
            JPanel sendMessagePanel = new JPanel();
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
            try {
                messageSection.addUserMessage(input.getText());
                OllamaResult response = chat.chat(input.getText());
                messageSection.addAIMessage(response.getResponse());
            } catch (OllamaBaseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(window.getContentManager().getComponent().getHeight());
            System.out.println(messagePanel.getHeight());
            messagePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, window.getContentManager().getComponent().getHeight()-inputPanel.getHeight() - 20));
            panel.add(messagePanel, BorderLayout.NORTH);
            panel.revalidate();
            panel.repaint();


        }
        private JPanel getPanel() {
            return panel;
        }
    }
}
