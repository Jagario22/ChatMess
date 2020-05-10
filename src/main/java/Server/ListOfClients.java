package Server;

import java.net.Socket;
import java.util.HashMap;

public class ListOfClients {
    private HashMap<Socket,String> userNames = new HashMap<Socket, String>();
    private ListOfClients() {
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < userNames.size() - 1; i++) {
            str.append(userNames.get(i)).append(",");
        }
        str.append(userNames.get(userNames.size() - 1));
        return str.toString();
    }

    public HashMap<Socket, String> getUserNames() {
        return userNames;
    }

    public void removeName(Socket socket) {
        userNames.remove(socket);
    }

    public void addName(Socket socket, String name) {
        userNames.put(socket, name);
    }

    public boolean ContainsName(Socket name) {
        if (userNames.containsKey(name) && userNames.get(name).equals(""))
            return false;

        return true;
    }

    public static ListOfClients getInstance() {
        return ListOfClientsHolder.INSTANCE;
    }

    private static class ListOfClientsHolder {
        private static final ListOfClients INSTANCE = new ListOfClients();
    }
}
