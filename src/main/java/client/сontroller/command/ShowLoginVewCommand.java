package client.сontroller.command;

import client.main.ChatMessengerApp;
import client.view.ChatPanelView;

public class ShowLoginVewCommand implements Command {
    private ChatMessengerApp app;
    private ChatPanelView panel;

    public ShowLoginVewCommand(ChatMessengerApp parent, ChatPanelView view) {
        app = parent;
        panel = view;
    }

    @Override
    public void execute() {
        panel.clearFields();
        panel.setVisible(false);
        app.getTimer().cancel();
        app.showLoginPanelView();
    }
}

