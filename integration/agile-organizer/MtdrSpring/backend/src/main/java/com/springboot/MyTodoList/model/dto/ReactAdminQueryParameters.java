package com.springboot.MyTodoList.model.dto;

import java.util.Map;
import java.util.List;

public class ReactAdminQueryParameters {
    private String sort;
    private String range;
    private String filter;

    // Getters and setters
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}