package client;

import domain.Message;

import java.util.*;

public class Model {

    private ChatMessengerApp parent;
    private String currentUser;
    private String loggedUser;
    private String lastMessageText;
    private long lastMessageId;
    private Set<Message> messages;
    private List<String> userOnline = new ArrayList<String>();
    private String serverIPAddress = "127.0.0.1";

    private static class ModelHolder {
        private static final Model INSTANCE = new Model();
    }

    public static Model getInstance() {
        return ModelHolder.INSTANCE;
    }

    public void initialize() {
        setMessages(new TreeSet<Message>(){
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
    }

    private Model(){  }

    public String messagesToString() {
        return messages.toString();
    }

    public long getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public void addMessages(List<Message> messages) {
        this.getMessages().addAll(messages);
        parent.getChatPanelView(false)
            .modelChangedNotification(messages.toString());
    }
    public List<String> getUserOnline() {
        return userOnline;
    }

    public boolean isContainUserName(String name)
    {
        return userOnline.contains(name);
    }

    public void setUserOnline(List<String> userOnline) {
        this.userOnline = userOnline;
    }


    public ChatMessengerApp getParent() {
        return parent;
    }
    public void setParent(ChatMessengerApp parent) {
        this.parent = parent;
    }
    public String getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    public String getLoggedUser() {
        return loggedUser;
    }
    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
    }
    public String getLastMessageText() {
        return lastMessageText;
    }
    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }
    public Set<Message> getMessages() {
        return messages;
    }
    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
    public String getServerIPAddress() { return serverIPAddress; }
    public void setServerIPAddress(String serverIPAddress) { this.serverIPAddress = serverIPAddress;}
}
