package client.сontroller.command;

import client.ChatMessengerApp;
import client.util.UpdateUserTask;
import client.view.LoginPanelView;
import client.util.UpdateMessageTask;
import client.util.Utility;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
@Slf4j
public class ShowChatViewCommand implements Command {

    private ChatMessengerApp app;
    private LoginPanelView view;

    public ShowChatViewCommand(ChatMessengerApp parent, LoginPanelView view) {
        app = parent;
        this.view = view;
    }

    @Override
    public void execute() {
        Utility.messagesUpdate(app);
        Utility.usersUpdate(app);
        app.getModel().setLoggedUser(view.getUserNameField().getText());
        view.clearFields();
        view.setVisible(false);
        app.setTimer(new Timer());
        app.getTimer().scheduleAtFixedRate(new UpdateMessageTask(app),
               ChatMessengerApp.DELAY, ChatMessengerApp.PERIOD);
        app.getTimer().scheduleAtFixedRate(new UpdateUserTask(app),
                ChatMessengerApp.DELAY, ChatMessengerApp.PERIOD);
        app.showChatPanelView();


    }
}
