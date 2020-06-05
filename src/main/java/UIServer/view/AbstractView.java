package UIServer.view;

import UIServer.main.ChatMessServerApp;

import javax.swing.*;
import java.awt.*;

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
