package com.springboot.MyTodoList.service.chatgpt.util;

public enum EndpointEnum {
    // ToDo Item endpoints
    TODO_LIST_GET_ALL(1, "Retrieve all todo items", "ToDoItemService", "findAll", new String[]{}),
    TODO_LIST_GET_BY_ID(2, "Retrieve a specific todo item by ID", "ToDoItemService", "getItemById", new String[]{"id"}),
    
    // Sprint endpoints
    SPRINT_GET_ALL(3, "Retrieve all sprints", "SprintService", "findAll", new String[]{}),
    SPRINT_GET_BY_ID(4, "Retrieve a specific sprint by ID", "SprintService", "getSprintById", new String[]{"id"}),
    
    // KPI endpoints
    KPI_GET_SUMMARY(5, "Get KPI metrics with optional parameters such as KPI's per team and/or per user and/or per project, etc.", "KpiService", "getKpiSummary", new String[]{"userId", "teamId", "projectId", "sprintId"}),
    KPI_GET_ALL(6, "Retrieve all KPI metrics", "KpiService", "findAll", new String[]{}),
    KPI_GET_BY_ID(7, "Retrieve a specific KPI by ID", "KpiService", "getKpiById", new String[]{"id"}),

    // Team endpoints
    TEAM_GET_ALL(8, "Retrieve all teams", "TeamService", "findAll", new String[]{}),
    TEAM_GET_BY_ID(9, "Retrieve a specific team by ID", "TeamService", "getTeamById", new String[]{"id"}),
    
    // State endpoints
    STATE_GET_ALL(10, "Retrieve all states", "StateService", "findAll", new String[]{}),
    STATE_GET_BY_ID(11, "Retrieve a specific state by ID", "StateService", "getStateById", new String[]{"id"}),
    STATE_GET_BY_NAME(12, "Retrieve a specific state by name", "StateService", "getStateByName", new String[]{"name"}),
    
    // User endpoints
    USER_GET_ALL(13, "Retrieve all users", "UserService", "findAll", new String[]{}),
    USER_GET_BY_ID(14, "Retrieve a specific user by ID", "UserService", "getUserById", new String[]{"id"}),
    USER_GET_BY_NAME(15, "Retrieve a specific user by name", "UserService", "getUserByName", new String[]{"name"}),
    
    // Project endpoints
    PROJECT_GET_ALL(16, "Retrieve all projects", "ProjectService", "findAll", new String[]{}),
    PROJECT_GET_BY_ID(17, "Retrieve a specific project by ID", "ProjectService", "getProjectById", new String[]{"id"});
    
    private final int number;
    private final String description;
    private final String serviceName;
    private final String serviceMethod;
    private final String[] parameters;
    
    EndpointEnum(int number, String description, String serviceName, String serviceMethod, String[] parameters) {
        this.number = number;
        this.description = description;
        this.serviceName = serviceName;
        this.serviceMethod = serviceMethod;
        this.parameters = parameters;
    }
    
    public int getNumber() {
        return number;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public String getServiceMethod() {
        return serviceMethod;
    }
    
    public String[] getParameters() {
        return parameters;
    }

    public boolean requiresParameters() {
        return parameters.length > 0;
    }

    public static EndpointEnum getByNumber(int number) {
        for (EndpointEnum endpoint : values()) {
            if (endpoint.getNumber() == number) {
                return endpoint;
            }
        }
        return null;
    }
    
    public static String getAllEndpointsFormatted() {
        StringBuilder sb = new StringBuilder();
        String currentCategory = "";
        
        for (EndpointEnum endpoint : values()) {
            // Extract category from enum name
            String name = endpoint.name();
            String category = name.split("_")[0];
            
            // Add category header if changed
            if (!category.equals(currentCategory)) {
                if (!currentCategory.isEmpty()) {
                    sb.append("\n");
                }
                sb.append("=== ").append(category).append(" Endpoints ===\n");
                currentCategory = category;
            }
            
            // Format parameters string if present
            String paramInfo = "";
            if (endpoint.getParameters().length > 0) {
                paramInfo = " [Parameters: " + String.join(", ", endpoint.getParameters()) + "]";
            }
            
            // Format each endpoint
            sb.append(String.format("%d. %s - %s (Service: %s.%s)%s\n", 
                endpoint.getNumber(),
                endpoint.name(),
                endpoint.getDescription(),
                endpoint.getServiceName(),
                endpoint.getServiceMethod(),
                paramInfo));
        }
        
        return sb.toString();
    }
}