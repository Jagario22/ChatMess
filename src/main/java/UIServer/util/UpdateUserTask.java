package UIServer.util;

import UIServer.main.ChatMessServerApp;

import java.util.TimerTask;

public class UpdateUserTask  extends TimerTask {

    ChatMessServerApp app;

    public UpdateUserTask(ChatMessServerApp app) {
        this.app = app;
    }

    @Override
    public void run() {
        Utility.usersUpdate(app);
    }
}
