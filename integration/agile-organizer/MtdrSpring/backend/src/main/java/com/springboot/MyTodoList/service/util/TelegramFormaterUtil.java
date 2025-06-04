package com.springboot.MyTodoList.service.util;

import java.util.List;
import java.util.Map;

import com.springboot.MyTodoList.model.ToDoItem;

public class TelegramFormaterUtil {
    public static String formatKpiProgress(Map<String, Integer> kpiData) {
        int sum = kpiData.get("sum");
        int total = kpiData.get("total");
        int percent = total > 0 ? (sum * 100) / total : 0;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d", percent));
        return sb.toString();
    }

    public static String formatTodoList(List<ToDoItem> items) {
        if (items.isEmpty()) {
            return "No tasks found.";
        }
        StringBuilder sb = new StringBuilder("Your Todo List:\n\n");
        for (ToDoItem item : items) {
            sb.append(String.format("📌 %s\n", item.getTitle()))
                    .append(String.format("Description: %s\n", item.getDescription()))
                    .append(String.format("Status: %s\n", item.isDone() ? "✅ Done" : "⏳ Pending"))
                    .append("\n");
        }
        return sb.toString();
    }
}