package client.util;

import client.main.ChatMessengerApp;
import client.view.ChatPanelView;
import java.util.TimerTask;


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
