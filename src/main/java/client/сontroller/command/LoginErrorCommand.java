package client.сontroller.command;


import client.view.LoginPanelView;

import javax.swing.*;

public class  LoginErrorCommand implements Command {
    public static final String WRONG_NAME_ERROR = "WRONG";
    public static final String NAME_EXIST = "EXIST";
    public static final String SERVER_ERROR = "SOCKET ERROR";
    private String error;
    private LoginPanelView view;
    public LoginErrorCommand(LoginPanelView view, String error) {
        this.error = error;
        this.view = view;
    }

    @Override
    public void execute() {
        view.setVisible(false);
        JLabel errorLabel = view.getErrorNameLabel();

        switch (error)
        {
            case WRONG_NAME_ERROR:
                errorLabel.setText("Wrong server ip address or user name");
                view.getMainPanel().add(errorLabel);
                view.getErrorNameLabel().setVisible(true);
                view.setVisible(true);
                view.repaint();
                break;

            case NAME_EXIST:
                errorLabel.setText("This name already exist");
                view.getMainPanel().add(errorLabel);
                view.getErrorNameLabel().setVisible(true);
                view.setVisible(true);
                view.repaint();
                break;

            case SERVER_ERROR:
                errorLabel.setText("Server is not responding");
                view.getMainPanel().add(errorLabel);
                view.getErrorNameLabel().setVisible(true);
                view.setVisible(true);
                view.repaint();
                break;
        }
    }
}
