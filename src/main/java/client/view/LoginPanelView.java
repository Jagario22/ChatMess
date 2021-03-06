package client.view;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class LoginPanelView extends AbstractView {

    public static final String ACTION_COMMAND_LOGIN = "login";
    public static final String LOGIN_BUTTON_COLOR = "#4A586E";
    public static final String MAIN_PANEL_COLOR = "#DAE4E5";
    public static final String TEXT_FIELD_FONT = "Courier New";

    private JPanel loginPanel; //отображает все окно
    private JPanel mainPanel; //поля ввода
    private JButton loginButton;
    private JTextField userNameField;
    private JTextField serverIpAddressField;
    private JLabel errorWrongNameLabel;



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
        clearFields();
        initModel();
    }

    @Override
    public void clearFields() {
        getErrorNameLabel().setVisible(false);
        getUserNameField().setText("");
        getServerIpAddressField().setText(parent.getModel().getServerIPAddress());
    }

    public void CreateFocus()
    {
        getUserNameField().requestFocusInWindow();
        parent.getRootPane().setDefaultButton(getLoginButton());
    }

    public void initModel() {
        parent.getModel().setCurrentUser("");
        parent.getModel().setLoggedUser("");
        CreateFocus();
    }

    public JPanel getLoginPanel() {
        if (loginPanel == null) {
            loginPanel = new JPanel();
            loginPanel.setLayout(new BorderLayout());
            loginPanel.add(getMainPanel(), BorderLayout.NORTH);
            loginPanel.setBackground(Color.decode(MAIN_PANEL_COLOR));
            addLabeledFiled(getMainPanel(), "name of user:", getUserNameField());
            addLabeledFiled(getMainPanel(), "server ip-address", getServerIpAddressField());
            loginPanel.add(getLoginButton(), BorderLayout.SOUTH);
        }
        return loginPanel;
    }

    public JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setBackground(Color.decode(MAIN_PANEL_COLOR));
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        }
        return mainPanel;
    }

    public JButton getLoginButton() {
        if (loginButton == null) {
            loginButton = new JButton();
            setButton(loginButton, "Login...");
            loginButton.setName("loginButton");
            loginButton.setActionCommand(ACTION_COMMAND_LOGIN);
            loginButton.addActionListener(parent.getController());
        }
        return loginButton;
    }

    public JTextField getUserNameField() {
        if (userNameField == null) {
            userNameField = new JTextField(12);
            userNameField.setFont(new Font(TEXT_FIELD_FONT, Font.BOLD, 16));
            userNameField.setName("userNameField");
        }
        return userNameField;
    }

    public JTextField getServerIpAddressField() {
        if (serverIpAddressField == null) {
            serverIpAddressField = new JTextField(12);
            serverIpAddressField.setFont(new Font(TEXT_FIELD_FONT, Font.BOLD, 15));
            serverIpAddressField.setName("serverIpAddressField");
        }
        return serverIpAddressField;
    }

    public JLabel getErrorNameLabel() {
        if (errorWrongNameLabel == null)
            errorWrongNameLabel = new JLabel();

        errorWrongNameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        errorWrongNameLabel.setForeground(Color.red);
        return errorWrongNameLabel;
    }
    public void setButton(JButton btn, String text) {
        btn.setBackground(Color.decode(LOGIN_BUTTON_COLOR));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn.setText(text);
    }

}
