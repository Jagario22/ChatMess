package GUIServer.util;

import GUIServer.main.ChatMessServerApp;

import java.util.TimerTask;

public class UpdateMessageTask extends TimerTask {
    ChatMessServerApp app;

    public UpdateMessageTask(ChatMessServerApp app) {
        this.app = app;
    }

    @Override
    public void run() {
        Utility.messagesUpdate(app);
    }
}
