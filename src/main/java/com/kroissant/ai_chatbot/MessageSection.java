package com.kroissant.ai_chatbot;

import com.intellij.ui.components.JBScrollBar;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MessageSection extends JPanel{
    private final JPanel messageContainer;
    private final JScrollPane scrollPane;
    private final Parser parser;
    private final HtmlRenderer renderer;

    public MessageSection() {
        // Initialize Markdown parser and renderer
        parser = Parser.builder().build();
        renderer = HtmlRenderer.builder().build();

        // Set the layout for the main panel
        setLayout(new BorderLayout());

        // Create a container for messages
        messageContainer = new JPanel();
        messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));

        // Create a scroll pane to hold the message container
        scrollPane = new JScrollPane(messageContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add the scroll pane to the main panel
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addMessage(String message, String sender) {
        // Convert Markdown to HTML
        String html = convertMarkdownToHtml(message);

        // Create a JEditorPane to display the HTML message
        JEditorPane messageEditorPane = new JEditorPane("text/html", html);
        messageEditorPane.setEditable(false);
        messageEditorPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add some padding

        // Align the message based on the sender
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.add(messageEditorPane, sender.equalsIgnoreCase("AI") ? BorderLayout.WEST : BorderLayout.EAST);
        messageContainer.add(messagePanel);

        messageContainer.revalidate();
        messageContainer.repaint();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    private String convertMarkdownToHtml(String markdown) {
        // Parse and render Markdown to HTML
        return renderer.render(parser.parse(markdown));
    }

    public void clearMessages() {
        messageContainer.removeAll();
        messageContainer.revalidate();
        messageContainer.repaint();
    }
}