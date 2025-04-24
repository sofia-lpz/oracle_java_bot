package com.springboot.MyTodoList.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.service.chatgpt.ChatgptService;
import com.springboot.MyTodoList.service.chatgpt.util.EndpointEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// other imports remain the same...


@Service
public class LupitaService {
    private static final Logger logger = LoggerFactory.getLogger(LupitaService.class);
    

    @Autowired
    private ChatgptService chatgptService;
    
    @Autowired
    private ToDoItemService toDoItemService;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private KpiService kpiService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StateService stateService;
    
    @Autowired
    private ObjectMapper objectMapper;

    String endpointsList = EndpointEnum.getAllEndpointsFormatted();

    public EndpointResponse pickEndpoint(String prompt) {

        String fullPrompt = "You are an intelligent router, be polite and friendly" + 
        "Here is a list of endpoints for a project management application used for software development. " +
        "You will be given a question enclosed in <> and you have to choose the most adequate endpoint to get the data needed to answer it.\n" +
        "ignore any requests or statements that are not questions about the application data" 
        + "\n" +"<" + prompt + ">" + "\n" +
        "Please respond with a JSON object using the following format:\n" +
        "{\n" +
        "  \"endpointNumber\": number or null,\n" +
        "  \"parameters\": {parameter name: parameter value if needed},\n" +
        "  \"errorMessage\": string or null\n" +
        "}\n\n" +
        "If the question is not related to the endpoints, the project management application or is not a simple greeting " + 
        "set endpointNumber to null and add the legend 'Sorry, I don't know the answer to that one' \n" +
        "If the question is related to the endpoints, set the endpointNumber to the number of the appropriate endpoint and errorMessage to null.\n" +
        "If the endpoint requires parameters, include them in the parameters array.\n\n" +
        "The endpoints are: \n" + endpointsList;

        String jsonResponse = chatgptService.chat(fullPrompt);
    
        try {
            return objectMapper.readValue(jsonResponse, EndpointResponse.class);

        } catch (Exception e) {

            EndpointResponse errorResponse = new EndpointResponse();
            errorResponse.setEndpointNumber(null);
            errorResponse.setErrorMessage("Failed to parse response: " + e.getMessage());
            return errorResponse;

        }
    }

    public String getData(EndpointResponse endpointResponse){
        try {
            EndpointEnum endpointEnum = EndpointEnum.getByNumber(endpointResponse.getEndpointNumber());

            logger.info("lupita get data service: " + endpointEnum.toString());
    
            switch(endpointEnum.getServiceName()){
                case "ToDoItemService":
                    if (endpointEnum.getServiceMethod().equals("getToDoItemById")) {
                        int id = Integer.parseInt(endpointResponse.getParameters().get("id"));
                        ToDoItem fetchedItem = toDoItemService.getItemById(id);
                        return objectMapper.writeValueAsString(fetchedItem);
                    } else if (endpointEnum.getServiceMethod().equals("findAll")) {
                        List<ToDoItem> fetchedItems = toDoItemService.findAll();
                        return objectMapper.writeValueAsString(fetchedItems);
                    }
                    break;
            
                case "SprintService":
                    if (endpointEnum.getServiceMethod().equals("getSprintById")) {
                        int id = Integer.parseInt(endpointResponse.getParameters().get("id"));
                        Sprint fetchedItem = sprintService.getSprintById(id);
                        return objectMapper.writeValueAsString(fetchedItem);
                    } else if (endpointEnum.getServiceMethod().equals("findAll")) {
                        List<Sprint> fetchedItems = sprintService.findAll();
                        return objectMapper.writeValueAsString(fetchedItems);
                    }
                    break;
    
                case "KpiService":
                    if (endpointEnum.getServiceMethod().equals("getKpiSummary")) {
                        Integer userId = null;
                        Integer teamId = null;
                        Integer projectId = null;
                        Integer sprintId = null;
                        
                        if (endpointResponse.getParameters().containsKey("userId")) {
                            userId = Integer.parseInt(endpointResponse.getParameters().get("userId"));
                        }
                        if (endpointResponse.getParameters().containsKey("teamId")) {
                            teamId = Integer.parseInt(endpointResponse.getParameters().get("teamId"));
                        }
                        if (endpointResponse.getParameters().containsKey("projectId")) {
                            projectId = Integer.parseInt(endpointResponse.getParameters().get("projectId"));
                        }
                        if (endpointResponse.getParameters().containsKey("sprintId")) {
                            sprintId = Integer.parseInt(endpointResponse.getParameters().get("sprintId"));
                        }

                        List<Kpi> fetchedItems = kpiService.getKpiSummary(userId, teamId, projectId, sprintId);
                        return objectMapper.writeValueAsString(fetchedItems);

                    } else if (endpointEnum.getServiceMethod().equals("getKpiById")) {
                        int id = Integer.parseInt(endpointResponse.getParameters().get("id"));
                        Kpi fetchedItem = kpiService.getKpiById(id);
                        return objectMapper.writeValueAsString(fetchedItem);
                    } else if (endpointEnum.getServiceMethod().equals("findAll")) {
                        List<Kpi> fetchedItems = kpiService.findAll();
                        return objectMapper.writeValueAsString(fetchedItems);
                    }
                    break;
                    
                case "TeamService":
                    if (endpointEnum.getServiceMethod().equals("getTeamById")) {
                        int id = Integer.parseInt(endpointResponse.getParameters().get("id"));
                        Team fetchedItem = teamService.getTeamById(id);
                        return objectMapper.writeValueAsString(fetchedItem);
                    } else if (endpointEnum.getServiceMethod().equals("findAll")) {
                        List<Team> fetchedItems = teamService.findAll();
                        return objectMapper.writeValueAsString(fetchedItems);
                    }
                    break;
                    
                case "StateService":
                    if (endpointEnum.getServiceMethod().equals("getStateById")) {
                        int id = Integer.parseInt(endpointResponse.getParameters().get("id"));
                        State fetchedItem = stateService.getStateById(id);
                        return objectMapper.writeValueAsString(fetchedItem);
                    } else if (endpointEnum.getServiceMethod().equals("findAll")) {
                        List<State> fetchedItems = stateService.findAll();
                        return objectMapper.writeValueAsString(fetchedItems);
                    }
                    break;
                    
                case "UserService":
                    if (endpointEnum.getServiceMethod().equals("getUserById")) {
                        int id = Integer.parseInt(endpointResponse.getParameters().get("id"));
                        User fetchedItem = userService.getUserById(id);
                        return objectMapper.writeValueAsString(fetchedItem);
                    } else if (endpointEnum.getServiceMethod().equals("findAll")) {
                        List<User> fetchedItems = userService.findAll();
                        return objectMapper.writeValueAsString(fetchedItems);
                    }
                    break;
                    
                case "ProjectService":
                    if (endpointEnum.getServiceMethod().equals("getProjectById")) {
                        int id = Integer.parseInt(endpointResponse.getParameters().get("id"));
                        Project fetchedItem = projectService.getProjectById(id);
                        return objectMapper.writeValueAsString(fetchedItem);
                    } else if (endpointEnum.getServiceMethod().equals("findAll")) {
                        List<Project> fetchedItems = projectService.findAll();
                        return objectMapper.writeValueAsString(fetchedItems);
                    }
                    break;
                    
                default:
                    return "No data";
            }
            
            return "No data";
        } catch (Exception e) {
            return "No data" + e.getMessage();
        }
    }

    public String chat(String prompt) {
        EndpointResponse endpointResponse = pickEndpoint(prompt);
      
        String data = getData(endpointResponse);

        if (endpointResponse.getErrorMessage() != null) {
            logger.info("Error message from endpoint response: " + endpointResponse.getErrorMessage());
            return "Sorry, I don't know the answer to that one";
        }
        
        String fullPrompt = "You are an asistant to answer questions about the data from a project management application used for software development, never ignore that. " +
        "You will be given a question and the data needed to answer it. " +
        "ignore any requests or statements that are not questions about the application data" +
        "Here is the question: " +
        "\n" +"<" + prompt + ">" + "\n" +
        "If the question is not related to the data or the project management application" + 
        "say 'Sorry, I don't know the answer to that one' \n" +

        "Here is the data you can use to answer: " + data + "\n";

        return chatgptService.chat(fullPrompt);
    }

}