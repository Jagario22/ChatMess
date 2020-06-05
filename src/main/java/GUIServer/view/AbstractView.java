package GUIServer.view;

import GUIServer.main.ChatMessServerApp;

import javax.swing.*;

public abstract class AbstractView extends JPanel {
    protected static ChatMessServerApp parent;

    public static void setParent(ChatMessServerApp parent) {
        AbstractView.parent = parent;
    }

    public AbstractView() {
        super();
    }

    public abstract void initialize();

    public abstract void clearFields();

}
