package client;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

@Slf4j
public class ChatPanelView extends AbstractView{
    public static final String SEND_ACTION_COMMAND = "send";
    public static final String LOGOUT_ACTION_COMMAND = "logout";
    private JScrollPane messagesListPanel;
    private JTextPane messagesTextPanel;
    private JPanel textMessagePanel;
    private JButton sendMessageButton;
    private JTextField textMessageField;
    private JButton logoutButton;

    private ChatPanelView() {
        super();
        initialize();
    }
    public static ChatPanelView getInstance() {
        return ChatPanelViewHolder.INSTANCE;
    }
    private static class ChatPanelViewHolder {
        private static final ChatPanelView  INSTANCE = new ChatPanelView();
    }
    @Override
    public void initialize() {
        this.setName("chatPannelView");
        this.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("Hello, " + parent.getModel().getCurrentUser() + "!"),
                BorderLayout.WEST);
        header.add(getLogoutButton(), BorderLayout.EAST);
        this.add(header, BorderLayout.NORTH);
        this.add(getMessagesListPanel(), BorderLayout.CENTER);
        this.add(getTextMessagePanel(), BorderLayout.SOUTH);
        InputMap im = getSendMessageButton().getInputMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");
    }

    @Override
    public void clearFiels() {
        getMessagesTextPanel().setText("");
        getTextMessageField().setText("");
    }

    @Override
    public void initModel() {

    }


    public void initModel(boolean getMessages) {
        parent.getModel().setLastMessageText("");
        if (getMessages) {
            getMessagesTextPanel().setText(parent.getModel().messagesToString());
        }
        getTextMessageField().requestFocusInWindow();
        parent.getRootPane().setDefaultButton(getSendMessageButton());
    }

    public JScrollPane getMessagesListPanel() {
        if (messagesListPanel == null) {
            messagesListPanel = new JScrollPane(getMessagesTextPanel());
            messagesListPanel.setSize(getMaximumSize());
            messagesListPanel
                    .setVerticalScrollBarPolicy(
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return messagesListPanel;
    }

    public JTextPane getMessagesTextPanel() {
        if (messagesTextPanel == null) {
            messagesTextPanel = new JTextPane();
            messagesTextPanel.setContentType("text/html");
            messagesTextPanel.setEditable(false);
            messagesTextPanel.setName("messagesTextArea");
            ((DefaultCaret)messagesTextPanel.getCaret())
                    .setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        }
        return messagesTextPanel;

    }

    public JPanel getTextMessagePanel() {
        if (textMessagePanel == null) {
            textMessagePanel = new JPanel();
            textMessagePanel.setLayout(new BoxLayout(textMessagePanel, BoxLayout.X_AXIS));
            addLabeledFiled(textMessagePanel, "Enter message: ", getTextMessageField());
            textMessagePanel.add(getSendMessageButton());
        }
        return textMessagePanel;
    }

    public JButton getSendMessageButton() {
        if (sendMessageButton == null) {
            sendMessageButton = new JButton();
            sendMessageButton.setText("Send");
            sendMessageButton.setName("sendMessageButton");
            sendMessageButton.setActionCommand(SEND_ACTION_COMMAND);
            sendMessageButton.addActionListener(parent.getController());
        }
        return sendMessageButton;
    }

    public JTextField getTextMessageField() {
        if (textMessageField == null) {
            textMessageField = new JTextField(12);
            textMessageField.setName("textMessageField");

        }
        return textMessageField;
    }

    public JButton getLogoutButton() {
        if (logoutButton == null) {
            logoutButton = new JButton();
            logoutButton.setText("Logout");
            logoutButton.setName("logoutButton");
            logoutButton.setActionCommand(LOGOUT_ACTION_COMMAND);
            logoutButton.addActionListener(parent.getController());
        }
        return logoutButton;
    }
}
