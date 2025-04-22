package com.springboot.MyTodoList.service.chatgpt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.springboot.MyTodoList.model.chatgpt.ChatRequest;
import com.springboot.MyTodoList.model.chatgpt.ChatResponse;

@Service
public class ChatgptService {
    
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;
    
    @Value("${openai.api.url}")
    private String apiUrl;

    public String chat(String prompt) {

        ChatRequest request = new ChatRequest(model, prompt);
        
        try {
            ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
            
            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                return "No response";
            }
            
            return response.getChoices().get(0).getMessage().getContent();
        } catch (HttpClientErrorException e) {
            return "Error communicating with AI service: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error occurred: " + e.getMessage();
        }
    }   
}