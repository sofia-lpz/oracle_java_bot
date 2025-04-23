package com.springboot.MyTodoList.model.response;

import com.springboot.MyTodoList.controller.Util.GroupBy;

public class OtherKpiResponse {
    public Integer realHours;
    public Integer numberOfTasks;
    public Integer numberOfStoryPoints;

    public GroupBy groupBy;
    public String groupByName;

}
