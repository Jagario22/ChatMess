package GUIServer.util;

import Server.ChatMessServer;
import GUIServer.main.ChatMessServerApp;
import GUIServer.model.Model;
import domain.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utility {
    public static <T extends Container> T findParent(Component comp, Class<T> clazz) {

        if (comp == null) {
            return null;
        }
        if (clazz.isInstance(comp)) {
            return (clazz.cast(comp));
        } else
            return findParent(comp.getParent(), clazz);
    }

    public static void usersUpdate(ChatMessServerApp app) {
        Model model = app.getModel();
        if (!ChatMessServer.getMessagesList().isEmpty())
            model.addUsers(ChatMessServer.getMessagesList().values());
    }

    public static void messagesUpdate(ChatMessServerApp app) {
        Model model = app.getModel();
        List<Message> newMessages;
        List<Message> listMessages = new ArrayList<Message>() {
            @Override
            public String toString() {
                return this.stream()
                        .map(Message::toString)
                        .collect(Collectors.joining("\n"));
            }
        };
        String currentUser = model.getCurrentUser();
        String receiver = model.getReceiver();
        long lastId = model.getLastMessageId();
        if (!ChatMessServer.getMessagesList().isEmpty()) {
            newMessages = ChatMessServer.getMessagesList().entrySet().stream()
                    .filter(message -> (message.getValue().getUserNF().equals(currentUser)
                            && message.getValue().getUserNT().equals(receiver))
                            || (message.getValue().getUserNF().equals(receiver)
                            && message.getValue().getUserNT().equals(currentUser)))
                    .filter(message -> message.getKey().compareTo(lastId) > 0)
                    .map(Map.Entry::getValue).collect(Collectors.toList());
            if (!newMessages.isEmpty()) {
                listMessages.addAll(newMessages);
                model.setLastMessageId(newMessages.get(newMessages.size() - 1).getId());
                model.addMessages(listMessages);
            }

        }
    }

    public static void deleteUser(ChatMessServerApp app) {

    }
}
