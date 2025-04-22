package com.springboot.MyTodoList.service;

import java.util.Map;

public class EndpointResponse {
    private Integer endpointNumber;
    private Map<String, String> parameters;
    private String errorMessage;
    
    // Default constructor (required for Jackson)
    public EndpointResponse() {}
    
    // Getters and setters
    public Integer getEndpointNumber() {
        return endpointNumber;
    }
    
    public void setEndpointNumber(Integer endpointNumber) {
        this.endpointNumber = endpointNumber;
    }
    
    public Map<String, String> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}