package client.view;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

@Slf4j
public class ChatPanelView extends AbstractView {
    public static final String SEND_ACTION_COMMAND = "send";
    public static final String LOGOUT_ACTION_COMMAND = "logout";

    private JPanel messagesMainPanel; //главная панель
    private JPanel usersListPanel;
    private JPanel textMessagePanel; //панель нижняя
    private JPanel messagesPanel; //панель сообщений

    private JLabel promptLabel; //метка приветствия
    private JLabel usersLabel; //метка пользователей в сети

    private JScrollPane messagesListPanel; //панель текстового поля для сообщений
    private JTextPane messagesTextPane; //само поле сообщения
    private JTextField textMessageField; //поле ввода сообщения

    private JButton logoutButton;
    private JButton sendMessageButton;

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
        this.add(getMessagesMainPanel(), BorderLayout.CENTER);
        this.add(getTextMessagePanel(), BorderLayout.SOUTH);

        InputMap im = getSendMessageButton().getInputMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");
    }

    public void initModel(boolean getMessages) {
        parent.getModel().setLastMessageText("");
        if (getMessages) {
            getMessagesTextPane().setText(parent.getModel().messagesToString());
        }
        getPromptLabel().setText("Hello, " + parent.getModel().getLoggedUser() + "!");
        updateUsersLabel();
        getTextMessageField().requestFocusInWindow();
        parent.getRootPane().setDefaultButton(getSendMessageButton());

    }

    @Override
    public void clearFields() {
        getMessagesTextPane().setText("");
        getTextMessageField().setText("");
    }

    public void modelChangedNotificationMessages(String newMessages) {
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

    public JPanel getMessagesMainPanel() {
        if (messagesMainPanel == null) {
            messagesMainPanel = new JPanel();
            messagesMainPanel.setLayout(new BorderLayout());
            messagesMainPanel.add(getMessagesPanel(), BorderLayout.WEST);
            messagesMainPanel.add(getUsersListPanel(), BorderLayout.EAST);
            messagesMainPanel.add(getMessagesListPanel());
        }
        return messagesMainPanel;
    }

    public JPanel getMessagesPanel() {
        if (messagesPanel == null) {
            messagesPanel = new JPanel();
            messagesPanel.setLayout(new BorderLayout());
            messagesPanel.setFont(new Font(Font.SERIF, Font.PLAIN, 14));

        }
        return messagesPanel;
    }

    public JPanel getUsersListPanel() {
        if (usersListPanel == null) {

            usersListPanel = new JPanel();

            JLabel l = getUsersLabel();
            l.setFont(new Font(Font.SERIF, Font.PLAIN, 14));


            JPanel JListPanel = new JPanel();
            JListPanel.setLayout(new BorderLayout());
            JListPanel.add(getUsersJlist(), BorderLayout.CENTER);


            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

            userPanel.add(l);
            userPanel.add(JListPanel);

            usersListPanel.setLayout(new BorderLayout());
            usersListPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            usersListPanel.add(userPanel, BorderLayout.CENTER);
        }
        return usersListPanel;
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

    public JLabel getUsersLabel() {
        if (usersLabel == null) {
            usersLabel = new JLabel();
            usersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        }
        return usersLabel;
    }

    public JLabel getPromptLabel() {
        if (promptLabel == null) {
            promptLabel = new JLabel("Hello, " + parent.getModel().getLoggedUser() + "!");
        }
        return promptLabel;
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

    public JScrollPane getMessagesListPanel() {
        if (messagesListPanel == null) {
            messagesListPanel = new JScrollPane(getMessagesTextPane());
            messagesListPanel
                    .setVerticalScrollBarPolicy(
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return messagesListPanel;
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

    public JList<String> getUsersJlist() {
        if (usersJlist == null) {
            usersJlist = new JList<String>(parent.getModel().getUserNamesList());
            usersJlist.setPreferredSize(new Dimension((int)(getWidth()*0.2), getHeight()));
            usersJlist.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
            DefaultListCellRenderer renderer = (DefaultListCellRenderer) usersJlist.getCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            usersJlist.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if ( e.getClickCount() == 1 ) {
                        getMessagesTextPane().setText("");
                        parent.getModel().setLastMessageId(0);
                        parent.getModel().setReceiver(usersJlist.getSelectedValue());
                    }
                }
            });
        }
        return usersJlist;
    }

    //SETTERS
    public void updateUsersLabel() {
        getUsersLabel().setText("usersOnline: " + parent.getModel().getUserNamesList().size());
    }
}