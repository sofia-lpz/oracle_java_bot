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
    
    private static final Logger logger = LoggerFactory.getLogger(ChatgptService.class);
    
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;
    
    @Value("${openai.api.url}")
    private String apiUrl;

    public String chat(String prompt) {
        // Log the prompt being sent to ChatGPT
        logger.info("ChatGPT Prompt: {}", prompt);
        
        ChatRequest request = new ChatRequest(model, prompt);
        
        try {
            ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
            
            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                logger.warn("Empty response received from ChatGPT");
                return "No response";
            }
            
            String responseContent = response.getChoices().get(0).getMessage().getContent();
            // Log a summary of the response (first 100 characters)
            logger.info("ChatGPT Response (summary): {}", 
                responseContent.length() > 100 ? responseContent.substring(0, 100) + "..." : responseContent);
            
            return responseContent;
        } catch (HttpClientErrorException e) {
            logger.error("Error communicating with ChatGPT API: {}", e.getMessage(), e);
            return "Error communicating with AI service: " + e.getMessage();
        } catch (Exception e) {
            logger.error("Unexpected error in ChatGPT communication: {}", e.getMessage(), e);
            return "Unexpected error occurred: " + e.getMessage();
        }
    }   
}