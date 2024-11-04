package com.kroissant.ai_chatbot.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {
    private final JPanel mainPanel;
    private final JBTextField model = new JBTextField();
    private final JBTextField host = new JBTextField();


    public AppSettingsComponent() {
        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Model:"), model, 1, false)
                .addLabeledComponent(new JBLabel("Host"), host, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @NotNull
    public String getModelText() {
        return model.getText();
    }
    @NotNull
    public String getHostText() {
        return host.getText();
    }
    public void setModelText(String text) {
        model.setText(text);
    }
    public void setHostText(String text) {
        host.setText(text);
    }
}
