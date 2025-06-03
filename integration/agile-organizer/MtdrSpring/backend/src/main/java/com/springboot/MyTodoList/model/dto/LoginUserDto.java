package com.springboot.MyTodoList.model.dto;

public class LoginUserDto {
    private String phoneNumber;
    
    private String password;

    private Long telegramChatId;

    public LoginUserDto() {
    }

    public LoginUserDto(String phoneNumber, String password, Long telegramChatId) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.telegramChatId = telegramChatId; 
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

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(Long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }
}