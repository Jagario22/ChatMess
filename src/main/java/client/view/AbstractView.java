package client.view;

import client.main.ChatMessengerApp;

import javax.swing.*;
import java.awt.*;

import static client.view.ChatPanelView.TEXT_PANE_COLOR;

public abstract class AbstractView extends JPanel {
    protected static ChatMessengerApp parent;

    public static void setParent(ChatMessengerApp parent) {
        AbstractView.parent = parent;
    }

    public AbstractView() {
        super();
    }

    public abstract void initialize();

    public abstract void clearFields();

    protected void addLabeledFiled(JPanel panel, String labelText, Component field) {
        JLabel label = new JLabel(labelText);
        label.setLabelFor(field);
        label.setFont(new Font("Baloo Chettan 2", Font.BOLD, 14));
        label.setForeground(Color.decode(TEXT_PANE_COLOR));
        panel.add(label);
        panel.add(field);
    }


}
