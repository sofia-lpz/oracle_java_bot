package com.springboot.MyTodoList.model.dto;

public class LoginUserDto {
    private String phoneNumber;
    
    private String password;
    
    public LoginUserDto() {
    }

    public LoginUserDto(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}