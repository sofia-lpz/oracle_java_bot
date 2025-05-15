package com.springboot.MyTodoList.mapper;

import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.dto.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) return null;
        
        UserDto dto = new UserDto();
        dto.setID(user.getID());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        
        if (user.getTeam() != null) {
            dto.setTeam(user.getTeam());
        }
        
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        
        User user = new User();
        user.setID(dto.getID());
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        
        if (dto.getTeam() != null) {
            user.setTeam(dto.getTeam());
        }
        
        return user;
    }
}