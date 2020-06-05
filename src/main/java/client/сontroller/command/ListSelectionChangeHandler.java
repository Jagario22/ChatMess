package client.сontroller.command;

import client.main.ChatMessengerApp;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ListSelectionChangeHandler implements MouseListener {
    JList usersJlist;
    ChatMessengerApp parent;
    public ListSelectionChangeHandler(JList usersJList, ChatMessengerApp parent)
    {
        this.usersJlist = usersJList;
        this.parent = parent;
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        if (!usersJlist.getSelectedValue().equals(parent.getModel().getReceiver())) {
            if (e.getClickCount() == 1) {
                parent.getChatPanelView(false).clearFields();
                parent.getModel().getMessages().clear();
                parent.getModel().setLastMessageId(0);
                parent.getModel().setReceiver(usersJlist.getSelectedValue().toString());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
