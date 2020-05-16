package client.util;

import client.main.ChatMessengerApp;
import client.view.ChatPanelView;

import java.util.TimerTask;

public class UpdateMessageTask extends TimerTask {
    ChatMessengerApp app;
    public UpdateMessageTask(ChatMessengerApp app) {
        this.app = app;
    }

    @Override
    public void run() {
        if (!Utility.messagesUpdate(app))
        {
            ChatPanelView panel = ChatPanelView.getInstance();
            panel.clearFields();
            panel.setVisible(false);
            app.getTimer().cancel();
            app.showLoginPanelView();
        }
    }
}
