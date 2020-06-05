package GUIServer.main;

import Server.ChatMessServer;
import GUIServer.controller.Controller;
import GUIServer.controller.WindowLictenerHandler;
import GUIServer.model.Model;
import GUIServer.view.AbstractView;
import GUIServer.view.ServerPanelView;
import GUIServer.view.ViewFactory;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;


import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.Timer;

@Slf4j
public class ChatMessServerApp extends JFrame {
    public static final long DELAY = 100;
    public static final long PERIOD = 1000;

    private static final Model MODEl;
    private static final Controller CONTROLLER;
    private static final ViewFactory VIEWS;
    private static final ChatMessServer SERVER;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;

    private static Timer timer;

    public static void main(String[] args) {
        JFrame frame = new ChatMessServerApp();
        frame.addWindowListener(new WindowLictenerHandler());
        frame.setVisible(true);
        frame.repaint();
        try {
            ChatMessServer.run();
        } catch (IOException e) {

        } catch (ParserConfigurationException e) {

        } catch (SAXException e) {

        }
    }

    static {
        MODEl = Model.getInstance();
        CONTROLLER = Controller.getInstance();
        VIEWS = ViewFactory.getInstance();
        log.trace("MVC instatiated" + MODEl + ";" + CONTROLLER + ";" + VIEWS);
        SERVER = ChatMessServer.getInstance();

    }

    public ChatMessServerApp() {
        super();
        initialize();
    }

    private void initialize() {
        AbstractView.setParent(this);
        MODEl.setParent(this);
        MODEl.initialize();
        CONTROLLER.setParent(this);
        VIEWS.viewRegister("server", ServerPanelView.getInstance());
        timer = new Timer("Server request for update messages");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setTitle("Server Messenger");

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(getServerPanel(true), BorderLayout.CENTER);
        this.setContentPane(contentPanel);
    }


    public ServerPanelView getServerPanel(boolean doGetMessages) {
        ServerPanelView chatPanelView = VIEWS.getView("server");
        chatPanelView.initModel(doGetMessages);
        getController().SetUpdateTimer();
        return chatPanelView;
    }

    //GETTERS
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

    //SETTERS
    public void setTimer(Timer timer) {
        this.timer = timer;
    }

}
