package client.view;

import client.сontroller.command.ListSelectionChangeHandler;
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
    public static final String LABEL_FONT = "Tahoma";
    public static final String TEXT_PANE_COLOR = "#4A586E";
    public static final String PANEL_COLOR = "#EEEDEA";

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
        header.setBackground(Color.decode(PANEL_COLOR));
        header.add(getPromptLabel(), BorderLayout.WEST);
        header.add(getLogoutButton(), BorderLayout.EAST);

        this.add(header, BorderLayout.NORTH);
        this.add(getMessagesMainPanel(), BorderLayout.CENTER);
        this.add(getTextMessagePanel(), BorderLayout.SOUTH);

        parent.getModel().getUserNamesList().addElement("General chat");
    }

    public void initModel(boolean getMessages) {
        parent.getModel().setLastMessageText("");
        if (getMessages) {
            getMessagesTextPane().setText(parent.getModel().messagesToString());
        }
        getPromptLabel().setText("Hello, " + parent.getModel().getLoggedUser() + "!");
        updateUsersLabel();

        createFocus();
    }
    public void createFocus()
    {
        ChatPanelView.getInstance().getTextMessageField().requestFocusInWindow();
        parent.getRootPane().setDefaultButton(ChatPanelView.getInstance().getSendMessageButton());
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

            JPanel JListPanel = new JPanel();
            JListPanel.setLayout(new BorderLayout());

            JListPanel.add(getUsersJlist(), BorderLayout.CENTER);


            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

            userPanel.add(l);
            userPanel.add(JListPanel);
            userPanel.setBackground(Color.decode(PANEL_COLOR));

            usersListPanel.setLayout(new BorderLayout());
            //usersListPanel.setBorder(BorderFactory.createLineBorder(Color.decode(TEXT_PANE_COLOR), 3));
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
            usersLabel.setFont(new Font("Tamoha", Font.BOLD, 14));
            usersLabel.setForeground(Color.decode(TEXT_PANE_COLOR));
        }
        return usersLabel;
    }

    public JLabel getPromptLabel() {
        if (promptLabel == null) {
            promptLabel = new JLabel("Hello, " + parent.getModel().getLoggedUser() + "!");
            promptLabel.setForeground(Color.decode(TEXT_PANE_COLOR));
            promptLabel.setFont(new Font(LABEL_FONT, Font.BOLD, 15));
        }
        return promptLabel;
    }

    public JTextPane getMessagesTextPane() {
        if (messagesTextPane == null) {
            messagesTextPane = new JTextPane();
            messagesTextPane.setBackground(Color.decode(TEXT_PANE_COLOR));
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
            messagesListPanel.setBorder(BorderFactory.createLineBorder(Color.decode(TEXT_PANE_COLOR), 2));
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
            textMessageField.setFont(new Font("Tamoha", Font.BOLD, 15));
        }
        return textMessageField;
    }

    public JButton getLogoutButton() {
        if (logoutButton == null) {
            logoutButton = new JButton();
            setButton(logoutButton, "Logout");
            logoutButton.setName("logoutButton");
            logoutButton.setActionCommand(LOGOUT_ACTION_COMMAND);
            logoutButton.addActionListener(parent.getController());
        }
        return logoutButton;
    }

    public JButton getSendMessageButton() {
        if (sendMessageButton == null) {
            sendMessageButton = new JButton();
            setButton(sendMessageButton, "Send");
            sendMessageButton.setFont(new Font("Tahoma", Font.BOLD, 14));
            sendMessageButton.setName("sendMessageButton");
            sendMessageButton.setActionCommand(SEND_ACTION_COMMAND);
            sendMessageButton.addActionListener(parent.getController());
        }
        return sendMessageButton;
    }

    public JList<String> getUsersJlist() {
        if (usersJlist == null) {
            usersJlist = new JList<>(parent.getModel().getUserNamesList());
            usersJlist.setPreferredSize(new Dimension((int) (getWidth() * 0.2), getHeight()));

            usersJlist.setFont(new Font("Tamoha", Font.BOLD, 14));
            usersJlist.setForeground(Color.decode(TEXT_PANE_COLOR));
            usersJlist.setBackground(Color.decode(PANEL_COLOR));

            DefaultListCellRenderer renderer = (DefaultListCellRenderer) usersJlist.getCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            //usersJlist.addListSelectionListener(new SharedListSelectionHandler(parent));
//            usersJlist.addMouseListener(new MouseAdapter() {
//                public void mouseClicked(MouseEvent e) {
//                    if (!usersJlist.getSelectedValue().equals(parent.getModel().getReceiver())) {
//                        if (e.getClickCount() == 1) {
//                            clearFields();
//                            parent.getModel().getMessages().clear();
//                            parent.getModel().setLastMessageId(0);
//                            parent.getModel().setReceiver(usersJlist.getSelectedValue());
//                        }
//                    }
//                }
//            });
            usersJlist.addMouseListener(new ListSelectionChangeHandler(usersJlist, parent));

        }
        return usersJlist;
    }

    //SETTERS
    public void updateUsersLabel() {
        getUsersLabel().setText("Users online: " + (parent.getModel().getUserNamesList().size() - 1));
    }

    public void setButton(JButton btn, String text) {
        btn.setBackground(Color.decode("#00848C"));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn.setText(text);
    }

    public void setSendMessageButton(JButton sendMessageButton) {
        this.sendMessageButton = sendMessageButton;
    }
}