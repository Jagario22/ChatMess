package client.util;

import client.main.ChatMessengerApp;
import client.view.ChatPanelView;
import client.view.LoginPanelView;
import client.сontroller.command.ShowLoginVewCommand;

import java.awt.*;
import java.util.TimerTask;

import static client.view.ChatPanelView.LOGOUT_ACTION_COMMAND;

public class UpdateUserTask extends TimerTask {

    ChatMessengerApp app;

    public UpdateUserTask(ChatMessengerApp app) {
        this.app = app;
    }

    @Override
    public void run() {
        if (!Utility.usersUpdate(app))
        {
            ChatPanelView panel = ChatPanelView.getInstance();
            panel.clearFields();
            panel.setVisible(false);
            app.getTimer().cancel();
            app.showLoginPanelView();
        }
    }
}
