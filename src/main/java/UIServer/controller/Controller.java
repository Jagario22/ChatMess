package UIServer.controller;

import UIServer.main.ChatMessServerApp;
import UIServer.util.UpdateMessageTask;
import UIServer.util.UpdateUserTask;
import UIServer.util.Utility;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;

@Slf4j
public class Controller  {
    private ChatMessServerApp parent;

    private Controller() {
    }

    public static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    public void SetUpdateTimer() {
        //Utility.messagesUpdate(parent);
        //Utility.usersUpdate(parent);
        parent.setTimer(new Timer());
        parent.getTimer().scheduleAtFixedRate(new UpdateMessageTask(parent),
                ChatMessServerApp.DELAY, ChatMessServerApp.PERIOD);
        parent.getTimer().scheduleAtFixedRate(new UpdateUserTask(parent),
                ChatMessServerApp.DELAY, ChatMessServerApp.PERIOD);
    }

    private static class ControllerHolder {
        private static final Controller INSTANCE = new Controller();
    }
    public void setParent(ChatMessServerApp parent) {
        this.parent = parent;
    }
}
