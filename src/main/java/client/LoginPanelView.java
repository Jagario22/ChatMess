package client;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@Slf4j
public class LoginPanelView extends AbstractView {

    public static final String ACTION_COMMAND_LOGIN = "login";
    private JPanel loginPanel; //отображает все окно
    private  JPanel mainPanel; //поля ввода
    private JButton loginButton;
    private JTextField userNameFiels;
    private JTextField serverIpAddressField;
    private JLabel errorLabel;

    //Singleton pattern
    private LoginPanelView() {
        super();
        initialize();
    }

    public static LoginPanelView getInstance() {
        return LoginPanelViewHolder.INSTANCE;
    }

    private static class LoginPanelViewHolder {
        private static final LoginPanelView INSTANCE = new LoginPanelView();
    }

    @Override
    public void initialize() {
        this.setName("loginPanelView");
        this.setLayout(new BorderLayout());
        this.add(getLoginPanel(), BorderLayout.CENTER);
        InputMap im = getLoginButton().getInputMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");
    }

    @Override
    public void clearFiels() {
        getErrorLabel().setVisible(false);
        getUserNameFiels().setText("");
        getServerIpAddressField().setText(parent.getModel().getServerIPAddress());
    }

    public void initModel() {
        parent.getModel().setCurrentUser("");
        parent.getModel().setLoggedUser("");
        getUserNameFiels().requestFocusInWindow();
        parent.getRootPane().setDefaultButton(getLoginButton());

    }

    public JPanel getLoginPanel() {
        if (loginPanel == null) {
            loginPanel = new JPanel();
            loginPanel.setLayout(new BorderLayout());
            loginPanel.add(getMainPanel(), BorderLayout.NORTH);
            addLabeledFiled(getMainPanel(), "server ip-address", getServerIpAddressField());
            addLabeledFiled(getMainPanel(), "name of user:", getUserNameFiels());
            loginPanel.add(getLoginButton(), BorderLayout.SOUTH);
        }
        return loginPanel;
    }

    public JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        }
        return mainPanel;
    }

    public JButton getLoginButton() {
        if (loginButton == null) {
            loginButton = new JButton();
            loginButton.setText("Login...");
            loginButton.setName("loginButton");
            loginButton.setActionCommand(ACTION_COMMAND_LOGIN);
            loginButton.addActionListener(parent.getController());
        }
        return loginButton;
    }

    public JTextField getUserNameFiels() {
        if (userNameFiels == null) {
            userNameFiels = new JTextField(12);
            userNameFiels.setName("userNameField");
        }
        return userNameFiels;
    }

    public JTextField getServerIpAddressField() {
        if (serverIpAddressField == null) {
            serverIpAddressField = new JTextField(12);
            serverIpAddressField.setName("serverIpAddressField");
        }
        return serverIpAddressField;
    }

    public JLabel getErrorLabel() {
        if (errorLabel == null)
            errorLabel = new JLabel("Wrong server ip address or user name");
            errorLabel.setForeground(Color.red);
        return errorLabel;
    }

    private void setErrorLabelText(String errorText) {
        getErrorLabel().setText(errorText);
    }
}
