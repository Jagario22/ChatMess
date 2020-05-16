package client.model;

import client.main.ChatMessengerApp;
import domain.Message;

import javax.swing.*;
import java.util.*;

public class Model {

    private ChatMessengerApp parent;
    private String currentUser;
    private String loggedUser;
    private String lastMessageText;
    private String serverIPAddress = "127.0.0.1";
    private String receiver;
    private long lastMessageId;
    private Set<Message> messages;
    private final DefaultListModel<String> userNamesList = new DefaultListModel<String>();


    public boolean isContainUserName(String currentUser) {
        return userNamesList.contains(currentUser);
    }

    private static class ModelHolder {
        private static final Model INSTANCE = new Model();
    }


    public static Model getInstance() {
        return ModelHolder.INSTANCE;
    }

    public void initialize() {
        setMessages(new TreeSet<Message>() {
            @Override
            public String toString() {
                StringBuilder result = new StringBuilder("<html><body id ='body'>");
                Iterator<Message> i = iterator();
                while (i.hasNext()) {
                    result.append(i.next().toString()).append("\n");
                }
                return result.append("</body></html>").toString();
            }
        });

        lastMessageId = 0L;
        currentUser = "";
        loggedUser = "";
        lastMessageText = "";
        receiver = "General chat";
    }

    private Model() {
    }

    public String messagesToString() {
        return messages.toString();
    }

    public void addMessages(List<Message> messages) {
        this.getMessages().addAll(messages);
        parent.getChatPanelView(false)
                .modelChangedNotificationMessages(messages.toString());

    }

    public void addUsers(List<String> users) {
        boolean added = false;
        boolean deleted = false;

        //add
        for (String i : users) {
            if (i.equals(currentUser)) {
            }
            if (!userNamesList.contains(i) && !i.equals(currentUser) && !i.equals("")) {
                userNamesList.addElement(i);
                if (!added) added = true;
            }
        }

        //delete
        for (int i = 0; i < userNamesList.size(); i++) {
            if (!userNamesList.get(i).equals("General chat") && !users.contains(userNamesList.get(i))) {
                userNamesList.remove(i);
                if (!deleted) deleted = true;
            }
        }
        if (deleted || added) {
            parent.getChatPanelView(false).updateUsersLabel();
        }
    }

    //GETTERS

    public String getCurrentUser() {
        return currentUser;
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public String getReceiver() {
        return receiver;
    }

    public ChatMessengerApp getParent() {
        return parent;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public DefaultListModel<String> getUserNamesList() {
        return userNamesList;
    }

    public Long getLastMessageId() {
        return lastMessageId;
    }

    //SETTERS

    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public void setParent(ChatMessengerApp parent) {
        this.parent = parent;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public void setServerIPAddress(String serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
    }
}