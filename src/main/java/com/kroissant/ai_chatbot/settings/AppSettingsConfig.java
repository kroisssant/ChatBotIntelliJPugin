package com.kroissant.ai_chatbot.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.ini4j.Config;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public class AppSettingsConfig implements Configurable {
    private AppSettingsComponent comp;
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "AI Chatbot Configuration";
    }

    @Override
    public @Nullable JComponent createComponent() {
        comp = new AppSettingsComponent();
        return comp.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        return !comp.getModelText().equals(state.llama_model) || !comp.getHostText().equals(state.host);
    }

    @Override
    public void apply() throws ConfigurationException {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        state.llama_model = comp.getModelText();
        state.host = comp.getHostText();
    }
    @Override
    public void reset() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        comp.setHostText(state.host);
        comp.setModelText(state.llama_model);
    }
    @Override
    public void disposeUIResources() {
        comp = null;
    }
}
