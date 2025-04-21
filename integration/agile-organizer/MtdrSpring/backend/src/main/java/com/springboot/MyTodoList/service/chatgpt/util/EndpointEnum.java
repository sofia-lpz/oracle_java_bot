package com.springboot.MyTodoList.service.chatgpt.util;

public enum EndpointEnum {
    // ToDo Item endpoints
    TODO_LIST_GET_ALL("/todolist", "GET", 1, "Retrieve all todo items", "ToDoItemService", "findAll"),
    TODO_LIST_GET_BY_ID("/todolist/{id}", "GET", 2, "Retrieve a specific todo item by ID", "ToDoItemService", "getItemById"),
    
    // Sprint endpoints
    SPRINT_GET_ALL("/sprints", "GET", 11, "Retrieve all sprints", "SprintService", "findAll"),
    SPRINT_GET_BY_ID("/sprints/{id}", "GET", 12, "Retrieve a specific sprint by ID", "SprintService", "getSprintById"),
    
    // KPI endpoints
    KPI_GET_SUMMARY("/kpi/summary", "GET", 21, "Get KPI summary statistics", "KpiService", "getKpiSummary"),
    KPI_GET_ALL("/kpi", "GET", 22, "Retrieve all KPI metrics", "KpiService", "findAll"),
    KPI_GET_BY_ID("/kpi/{id}", "GET", 23, "Retrieve a specific KPI by ID", "KpiService", "getKpiById"),
    
    // Team endpoints
    TEAM_GET_ALL("/teams", "GET", 31, "Retrieve all teams", "TeamService", "findAll"),
    TEAM_GET_BY_ID("/teams/{id}", "GET", 32, "Retrieve a specific team by ID", "TeamService", "getTeamById"),
    
    // State endpoints
    STATE_GET_ALL("/states", "GET", 41, "Retrieve all states", "StateService", "findAll"),
    STATE_GET_BY_ID("/states/{id}", "GET", 42, "Retrieve a specific state by ID", "StateService", "getStateById"),
    STATE_GET_BY_NAME("/states/name/{name}", "GET", 43, "Retrieve a specific state by name", "StateService", "getStateByName"),
    
    // User endpoints
    USER_GET_ALL("/users", "GET", 51, "Retrieve all users", "UserService", "findAll"),
    USER_GET_BY_ID("/users/{id}", "GET", 52, "Retrieve a specific user by ID", "UserService", "getUserById"),
    USER_GET_BY_NAME("/users/name/{name}", "GET", 53, "Retrieve a specific user by name", "UserService", "getUserByName"),
    
    // Project endpoints
    PROJECT_GET_ALL("/projects", "GET", 61, "Retrieve all projects", "ProjectService", "findAll"),
    PROJECT_GET_BY_ID("/projects/{id}", "GET", 62, "Retrieve a specific project by ID", "ProjectService", "getProjectById");
    
    private final String path;
    private final String method;
    private final int number;
    private final String description;
    private final String serviceName;
    private final String serviceMethod;
    
    EndpointEnum(String path, String method, int number, String description, String serviceName, String serviceMethod) {
        this.path = path;
        this.method = method;
        this.number = number;
        this.description = description;
        this.serviceName = serviceName;
        this.serviceMethod = serviceMethod;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getMethod() {
        return method;
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
            
            // Format each endpoint
            sb.append(String.format("%d. %s %s - %s (Service: %s.%s)\n", 
                endpoint.getNumber(),
                endpoint.getMethod(),
                endpoint.getPath(),
                endpoint.getDescription(),
                endpoint.getServiceName(),
                endpoint.getServiceMethod()));
        }
        
        return sb.toString();
    }
}