package GUIServer.model;

import Server.ListOfClients;
import GUIServer.main.ChatMessServerApp;
import domain.Message;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class Model {
    private ChatMessServerApp parent;
    private String lastMessageText;
    private long lastMessageId;
    private Set<Message> messages;
    private String serverIPAddress = "127.0.0.1";
    private String receiver;
    private String currentUser;
    private final DefaultListModel<String> userNamesList = new DefaultListModel<String>();
    private static ListOfClients clientsOnline;


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
        lastMessageText = "";
        currentUser = "";
        receiver = "";
        clientsOnline = ListOfClients.getInstance();
    }

    private Model() {
    }

    public String messagesToString() {
        return messages.toString();
    }

    public void addMessages(List<Message> messages) {
        this.getMessages().addAll(messages);
        parent.getServerPanel(false)
                .modelChangedNotificationMessages(messages.toString());
    }

    public void addUsers(Collection<Message> messages) {
        Boolean added = false;
        for (Message i: messages)
        {
            if (!(userNamesList.contains(i.getUserNT() + "<->" + i.getUserNF()))
                    && !(userNamesList.contains(i.getUserNF() + "<->" + i.getUserNT()))) {
                userNamesList.addElement(i.getUserNT() + "<->" + i.getUserNF());
                added = true;
            }
        }
        if (added)
            parent.getServerPanel(false).updateUsersLabel();
    }

    //GETTERS

    public ChatMessServerApp getParent() {
        return parent;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public long getLastMessageId() {
        return lastMessageId;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public String getReceiver() {
        return receiver;
    }

    public DefaultListModel<String> getUserNamesList() {
        return userNamesList;
    }

    public static ListOfClients getClientsOnline() {
        return clientsOnline;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    //SETTERS

    public void setParent(ChatMessServerApp parent) {
        this.parent = parent;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public void setServerIPAddress(String serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public static void setClientsOnline(ListOfClients clientsOnline) {
        Model.clientsOnline = clientsOnline;
    }
}
