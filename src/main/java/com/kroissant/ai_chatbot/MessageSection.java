package com.kroissant.ai_chatbot;

import javax.swing.*;
import java.awt.*;

import com.intellij.ui.components.JBScrollPane;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MessageSection extends JPanel{
    private final JPanel messagePanel;
    private final JBScrollPane scrollPane;
    private final Parser parser;
    private final HtmlRenderer renderer;


    public MessageSection() {
        // Initialize Markdown parser and renderer
        parser = Parser.builder().build();
        renderer = HtmlRenderer.builder().build();

        // Set the layout of this panel
        setLayout(new BorderLayout());

        // Create a panel to store the messages
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        // Create a scroll pane to hold the messagePanel
        scrollPane = new JBScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add the scroll pane to this panel
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addMessage(String message, String sender) {
        // Convert Markdown to HTML
        String html = convertMarkdownToHtml(message);

        // Create a EditorPane that can display HTML
        JEditorPane messageEditorPane = new JEditorPane("text/html", html);
        messageEditorPane.setEditable(false);

        // Align the message based on the sender
        JPanel messageContainer = new JPanel();
        messageContainer.setLayout(new BorderLayout());
        messageContainer.add(messageEditorPane, sender.equals("AI") ? BorderLayout.WEST : BorderLayout.EAST);

        // Add the new message to the GUI
        messagePanel.add(messageContainer);

        // Repaint the panel
        messagePanel.revalidate();
        messagePanel.repaint();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    private String convertMarkdownToHtml(String markdown) {
        // Convert Markdown into HTML
        return renderer.render(parser.parse(markdown));
    }
}