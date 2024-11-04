package com.kroissant.ai_chatbot;

import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class MessageSection extends JPanel{
    JBScrollPane scrollableMessagePane;
    public MessageSection() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    public JBScrollPane getScrollableMessagePane() {
        scrollableMessagePane = new JBScrollPane(this);
        scrollableMessagePane.setVerticalScrollBarPolicy(JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollableMessagePane;
    }
    public void addUserMessage(String text) {
        JPanel messageComp = new JPanel();
        messageComp.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JLabel messageText = new JLabel(text);
        messageComp.add(messageText);
        add(messageComp);
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollableMessagePane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
            revalidate();
            repaint();
        });

    }
    public void addAIMessage(String text) {
        JPanel messageComp = new JPanel();
        messageComp.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel messageText = new JLabel(text);
        messageComp.add(messageText);
        add(messageComp);
        revalidate();
        repaint();
    }

}
