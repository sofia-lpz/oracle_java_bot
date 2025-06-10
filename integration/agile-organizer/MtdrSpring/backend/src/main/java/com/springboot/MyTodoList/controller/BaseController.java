package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.util.ReactAdminHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public abstract class BaseController {
    
    /**
     * Creates a paginated response entity for React Admin
     * @param <T> The entity type
     * @param page The page of entities
     * @param rangeJson Range parameter from React Admin
     * @return ResponseEntity with appropriate headers
     */
    protected <T> ResponseEntity<List<T>> createPaginatedResponse(Page<T> page, String rangeJson) {
        List<T> content = page.getContent();
        Map<String, String> headers = ReactAdminHelper.createPaginationHeaders(page.getTotalElements(), rangeJson);
        
        HttpHeaders responseHeaders = new HttpHeaders();
        headers.forEach(responseHeaders::set);
        // Important for React Admin to work correctly
        responseHeaders.set("Access-Control-Expose-Headers", "Content-Range");
        
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(content);
    }
    
    /**
     * Parses pagination parameters from React Admin
     * @param rangeJson Range parameter (e.g. [0, 9])
     * @param sortJson Sort parameter (e.g. ["name", "ASC"])
     * @return PageRequest object for repository
     */
    protected PageRequest parsePageRequest(String rangeJson, String sortJson) {
        Sort sort = ReactAdminHelper.parseSort(sortJson);
        return ReactAdminHelper.parsePagination(rangeJson, sort);
    }
    
    /**
     * Parses filter parameters from React Admin
     * @param filterJson Filter parameter (e.g. {"name": "test"})
     * @return Map of filter parameters
     */
    protected Map<String, Object> parseFilters(String filterJson) {
        return ReactAdminHelper.parseFilter(filterJson);
    }
    
    /**
     * Creates a standard response with content-range headers for React Admin list views
     * @param <T> The entity type
     * @param items List of items
     * @param total Total number of items
     * @param rangeJson Range parameter from React Admin
     * @return ResponseEntity with appropriate headers
     */
    protected <T> ResponseEntity<List<T>> createListResponse(List<T> items, long total, String rangeJson) {
        Map<String, String> headers = ReactAdminHelper.createPaginationHeaders(total, rangeJson);
        
        HttpHeaders responseHeaders = new HttpHeaders();
        headers.forEach(responseHeaders::set);
        responseHeaders.set("Access-Control-Expose-Headers", "Content-Range");
        
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(items);
    }
}