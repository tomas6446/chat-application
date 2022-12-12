package com.chat.app.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Tomas Kozakas
 */

public class Database {
    private Map<String, Chat> chatMap = new HashMap<>();
    private Map<String, User> userMap = new HashMap<>();

    public Database() {
        importData();
    }

    public void importData() {
        try {
            List<User> userList = new ObjectMapper().readValue(new File("data/user.json"), new TypeReference<>() {
            });
            userMap = userList.stream().collect(Collectors.toMap(User::getName, Function.identity()));

            List<Chat> chatList = new ObjectMapper().readValue(new File("data/chat.json"), new TypeReference<>() {
            });
            chatMap = chatList.stream().collect(Collectors.toMap(Chat::getName, Function.identity()));
        } catch (IOException e) {
            System.err.println("Unable to import data");
            e.printStackTrace();
        }
    }

    public void exportData() {
        try {
            List<User> userList = userMap.values().stream().toList();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new FileWriter("data/user.json"), userList);

            List<Chat> chatList = chatMap.values().stream().toList();
            objectMapper.writeValue(new FileWriter("data/chat.json"), chatList);
        } catch (IOException e) {
            System.err.println("Unable to export data");
        }
    }

    public boolean containsUser(String username, String password) {
        return userMap.containsKey(username) && userMap.get(username).getPassword().equals(password);
    }

    public boolean containsChat(String chatName) {
        return chatMap.containsKey(chatName);
    }

    public User getUser(String username) {
        return userMap.get(username);
    }

    public Chat getChat(String chatName) {
        return chatMap.get(chatName);
    }

    public void addChat(Chat chat) {
        chatMap.put(chat.getName(), chat);
    }

    public void addUser(User user) {
        userMap.put(user.getName(), user);
    }

    public void updateUser(User user) {
        userMap.replace(user.getName(), user);
    }

    public void updateChat(Chat chat) {
        chatMap.replace(chat.getName(), chat);
    }

    public List<String> getChatMessages(String name) {
        return chatMap.get(name).getMessages();
    }
}