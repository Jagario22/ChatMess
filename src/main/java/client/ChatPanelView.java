package client;

import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.IOException;

@Slf4j
public class ChatPanelView extends AbstractView {
    public static final String SEND_ACTION_COMMAND = "send";
    public static final String LOGOUT_ACTION_COMMAND = "logout";

    private JLabel promptLabel;
    private JScrollPane messagesListPanel;
    private JTextPane messagesTextPane;
    private JPanel textMessagePanel;
    private JButton sendMessageButton;
    private JTextField textMessageField;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JPanel messagesPanel;
    private JPanel usersListPanel;
    private JList<String> usersJlist;


    private ChatPanelView() {
        super();
        initialize();
    }

    public static ChatPanelView getInstance() {
        return ChatPanelViewHolder.INSTANCE;
    }
    private static class ChatPanelViewHolder {
        private static final ChatPanelView INSTANCE = new ChatPanelView();
    }

    @Override
    public void initialize() {
        this.setName("chatPanelView");
        this.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.add(getPromptLabel(), BorderLayout.WEST);
        header.add(getLogoutButton(), BorderLayout.EAST);
        this.add(header, BorderLayout.NORTH);
        //this.add(getMessagesListPanel(), BorderLayout.CENTER);
        //this.add(getUsersListPanel(), BorderLayout.EAST);
        this.add(getMainPanel(), BorderLayout.CENTER);
        this.add(getTextMessagePanel(), BorderLayout.SOUTH);
        InputMap im = getSendMessageButton().getInputMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");

    }

    @Override
    public void clearFields() {
        getMessagesTextPane().setText("");
        getTextMessageField().setText("");
    }

    public void modelChangedNotification(String newMessages) {
        if (newMessages.length() != 0) {
            log.trace("New messages arrived: " + newMessages);
            HTMLDocument document = (HTMLDocument) getMessagesTextPane().getStyledDocument();
            Element element = document.getElement(document.getRootElements()[0],
                    HTML.Attribute.ID, "body");
            try {
                document.insertBeforeEnd(element, newMessages);
            } catch (BadLocationException | IOException e) {
                log.error("Bad location error: " + e.getMessage());
            }
            getMessagesTextPane().setCaretPosition(document.getLength());
            log.trace("Messages text update");
        }
    }

    //GETTERS

    public JLabel getPromptLabel() {
        if (promptLabel == null)
            promptLabel = new JLabel("Hello, " + parent.getModel().getLoggedUser() + "!");
        return promptLabel;
    }

    public void initModel(boolean getMessages) {
        parent.getModel().setLastMessageText("");
        if (getMessages) {
            getMessagesTextPane().setText(parent.getModel().messagesToString());
        }
        getPromptLabel().setText("Hello, " + parent.getModel().getLoggedUser() + "!");
        getTextMessageField().requestFocusInWindow();
        parent.getRootPane().setDefaultButton(getSendMessageButton());
    }

    public JScrollPane getMessagesListPanel() {
        if (messagesListPanel == null) {
            messagesListPanel = new JScrollPane(getMessagesTextPane());
            messagesListPanel
                    .setVerticalScrollBarPolicy(
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return messagesListPanel;
    }

    public JPanel getMessagesPanel() {
        if (messagesPanel == null)
        {
            messagesPanel = new JPanel();
            messagesPanel.setLayout(new BorderLayout());
            messagesPanel.add(getMessagesListPanel());
        }
        return messagesPanel;
    }

    public JPanel getMainPanel() {
        if (mainPanel == null)
        {
            JPanel space = new JPanel();
            space.setLayout(new BorderLayout());
            mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            mainPanel.add(getMessagesPanel());
            mainPanel.add(getUsersListPanel());
        }
        return mainPanel;
    }

    public JPanel getUsersListPanel() {
        if (usersListPanel == null) {

            usersListPanel = new JPanel();
            Font  f1  = new Font(Font.SERIF, Font.PLAIN,  30);
            Label l = new Label("Online Users");
            l.setFont(f1);

            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));


            JPanel JListPanel = new JPanel();
            JListPanel.setLayout(new BorderLayout());
            JListPanel.add(getUsersJlist(), BorderLayout.CENTER);


            userPanel.add(l);
            userPanel.add(JListPanel);

            usersListPanel.setLayout(new BorderLayout());
            usersListPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            usersListPanel.add(userPanel, BorderLayout.CENTER);
        }
        return usersListPanel;
    }

    public JList<String> getUsersJlist() {
        if (usersJlist == null)
        {
            String[] names = {"He", "She", "It"};
            usersJlist = new JList<>(names);
            usersJlist.setPreferredSize(new Dimension(50, getHeight()));
            Font  f1  = new Font(Font.SERIF, Font.PLAIN,  30);
            usersJlist.setFont(f1);
            DefaultListCellRenderer renderer = (DefaultListCellRenderer) usersJlist.getCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return usersJlist;
    }

    public JTextPane getMessagesTextPane() {
        if (messagesTextPane == null) {
            messagesTextPane = new JTextPane();
            messagesTextPane.setContentType("text/html");
            messagesTextPane.setEditable(false);
            messagesTextPane.setName("messagesTextArea");
            ((DefaultCaret) messagesTextPane.getCaret())
                    .setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        }
        return messagesTextPane;

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
