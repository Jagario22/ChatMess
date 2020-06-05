package client.main;


import client.model.Model;
import client.util.Utility;
import client.view.AbstractView;
import client.view.ChatPanelView;
import client.view.LoginPanelView;
import client.view.ViewFactory;
import client.сontroller.Controller;
import lombok.extern.slf4j.Slf4j;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;

@Slf4j
public class ChatMessengerApp extends JFrame {
    public static final long DELAY = 100;
    public static final long PERIOD = 1000;

    private static final Model MODEl;
    private static final Controller CONTROLLER;
    private static final ViewFactory VIEWS;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;

    private static Timer timer;


    public static void main(String[] args) {
        ChatMessengerApp frame = new ChatMessengerApp();
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                ChatMessengerApp app = frame;
                if (!(LoginPanelView.getInstance().isVisible())) {
                    Utility.deleteUser(app);
                    app.showLoginPanelView();
                }
            }

            public void windowClosed(WindowEvent we) {
                ChatMessengerApp app = frame;
                if (!(LoginPanelView.getInstance().isVisible())) {
                    Utility.deleteUser(app);
                    app.showLoginPanelView();
                }
            }
        });
        frame.setVisible(true);
        frame.repaint();
    }


    static {
        MODEl = Model.getInstance();
        CONTROLLER = Controller.getInstance();
        VIEWS = ViewFactory.getInstance();

        log.trace("MVC instatiated" + MODEl + ";" + CONTROLLER + ";" + VIEWS);
    }

    public ChatMessengerApp() {
        super();
        initialize();
    }


    private void initialize() {
        AbstractView.setParent(this);
        MODEl.setParent(this);
        MODEl.initialize();
        CONTROLLER.setParent(this);
        VIEWS.viewRegister("login", LoginPanelView.getInstance());
        VIEWS.viewRegister("chat", ChatPanelView.getInstance());
        timer = new Timer("Server request for update messages");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setTitle("Chat Messenger");

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(getLoginPanel(), BorderLayout.CENTER);
        this.setContentPane(contentPanel);
    }

    private JPanel getLoginPanel() {
        LoginPanelView loginPanelView = VIEWS.getview("login");
        loginPanelView.initModel();
        return loginPanelView;
    }

    public ChatPanelView getChatPanelView(boolean doGetMessages) {
        ChatPanelView chatPanelView = VIEWS.getview("chat");
        chatPanelView.initModel(doGetMessages);
        return chatPanelView;
    }

    public static Model getModel() {
        return MODEl;
    }

    public static Controller getController() {
        return CONTROLLER;
    }

    public static ViewFactory getViews() {
        return VIEWS;
    }

    public static Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void showChatPanelView() {
        showPanel(getChatPanelView(true));
        ChatPanelView.getInstance().createFocus();
    }

    private void showPanel(JPanel panel) {
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setVisible(true);
    }

    public void showLoginPanelView() {
        showPanel(getLoginPanel());
        LoginPanelView.getInstance().CreateFocus();
    }
}
