package com.kroissant.ai_chatbot;

import com.kroissant.ai_chatbot.settings.AppSettings;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.OllamaResult;
import io.github.ollama4j.models.chat.OllamaChatMessage;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.chat.OllamaChatResult;
import io.github.ollama4j.types.OllamaModelType;
import io.github.ollama4j.utils.OptionsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatBot {
    OllamaAPI ollama;
    List<OllamaChatMessage> history = new ArrayList<>();
    AppSettings.State state =
            Objects.requireNonNull(AppSettings.getInstance().getState());
    OllamaChatRequestBuilder builder;
    OllamaAPI ollamaAPI;
    public ChatBot() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());

        ollamaAPI = new OllamaAPI(state.host);


    }
    public OllamaResult chat(String promt) throws OllamaBaseException, IOException, InterruptedException {
        OllamaChatResult chatResult ;
        builder = OllamaChatRequestBuilder.getInstance(state.llama_model);
        chatResult = ollamaAPI.chat(builder.withMessages(history).withMessage(OllamaChatMessageRole.USER, promt)
                .build());
        history = chatResult.getChatHistory();
        return chatResult;
    }
}
