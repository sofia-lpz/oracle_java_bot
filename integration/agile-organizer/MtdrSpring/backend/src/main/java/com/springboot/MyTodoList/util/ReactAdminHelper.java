package com.springboot.MyTodoList.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class ReactAdminHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static Sort parseSort(String sortJson) {
        if (sortJson == null || sortJson.isEmpty()) {
            return Sort.unsorted();
        }
        
        try {
            String[] sortParams = objectMapper.readValue(sortJson, String[].class);
            if (sortParams.length < 2) {
                return Sort.unsorted();
            }
            
            String field = sortParams[0];
            String direction = sortParams[1];
            
            return Sort.by(direction.equalsIgnoreCase("ASC") ? 
                    Sort.Direction.ASC : Sort.Direction.DESC, field);
        } catch (JsonProcessingException e) {
            return Sort.unsorted();
        }
    }
    
    public static PageRequest parsePagination(String rangeJson, Sort sort) {
        if (rangeJson == null || rangeJson.isEmpty()) {
            return PageRequest.of(0, 10, sort);
        }
        
        try {
            int[] range = objectMapper.readValue(rangeJson, int[].class);
            if (range.length < 2) {
                return PageRequest.of(0, 10, sort);
            }
            
            int start = range[0];
            int end = range[1];
            int size = end - start + 1;
            int page = size > 0 ? start / size : 0;
            
            return PageRequest.of(page, size, sort);
        } catch (JsonProcessingException e) {
            return PageRequest.of(0, 10, sort);
        }
    }
    
    public static Map<String, Object> parseFilter(String filterJson) {
        if (filterJson == null || filterJson.isEmpty()) {
            return new HashMap<>();
        }
        
        try {
            return objectMapper.readValue(filterJson, Map.class);
        } catch (JsonProcessingException e) {
            return new HashMap<>();
        }
    }
    
    public static Map<String, String> createPaginationHeaders(long total, String rangeJson) {
        Map<String, String> headers = new HashMap<>();
        if (rangeJson == null || rangeJson.isEmpty()) {
            return headers;
        }
        
        try {
            int[] range = objectMapper.readValue(rangeJson, int[].class);
            if (range.length < 2) {
                return headers;
            }
            
            int start = range[0];
            int end = Math.min(range[1], (int)total - 1);
            headers.put("Content-Range", String.format("%d-%d/%d", start, end, total));
            
            return headers;
        } catch (JsonProcessingException e) {
            return headers;
        }
    }
}