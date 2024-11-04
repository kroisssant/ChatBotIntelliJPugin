package com.kroissant.ai_chatbot;

import com.kroissant.ai_chatbot.settings.AppSettings;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.OllamaResult;
import io.github.ollama4j.types.OllamaModelType;
import io.github.ollama4j.utils.OptionsBuilder;

import java.io.IOException;
import java.util.Objects;

public class ChatBot {
    OllamaAPI ollama;
    AppSettings.State state =
            Objects.requireNonNull(AppSettings.getInstance().getState());
    public ChatBot() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        String host = state.host;
        String model = state.llama_model;
        System.out.println(host);
        System.out.println(model);
        ollama = new OllamaAPI(host);

    }
    public OllamaResult chat(String promt) throws OllamaBaseException, IOException, InterruptedException {
        OllamaResult result = ollama.generate(state.llama_model, promt, false ,new OptionsBuilder().build());
        return result;
    }
}
