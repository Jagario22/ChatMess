package GUIServer.view;



import GUIServer.controller.ListSelectionChangeHandler;
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
public class ServerPanelView extends AbstractView {
    public static final String TEXT_PANE_COLOR = "#4A586E";
    public static final String PANEL_COLOR = "#EEEDEA";

    private JScrollPane messagesListPanel; //панель текстового поля для сообщений
    private JPanel messagesMainPanel; //главная панель
    private JPanel usersListPanel;
    private JPanel messagesPanel; //панель сообщений
    private JLabel usersLabel; //метка пользователей в сети
    private JTextPane messagesTextPane; //само поле сообщения
    private JList<String> usersJlist;

    public ServerPanelView() {
        super();
        initialize();
    }

    public static ServerPanelView getInstance() {
        return ServerPanelViewHolder.INSTANCE;
    }

    private static class ServerPanelViewHolder {
        private static final ServerPanelView INSTANCE = new ServerPanelView();
    }

    @Override
    public void initialize() {
        this.setName("chatPanelView");
        this.setLayout(new BorderLayout());
        this.add(getMessagesMainPanel(), BorderLayout.CENTER);
    }

    public void initModel(boolean getMessages) {
        parent.getModel().setLastMessageText("");
        if (getMessages) {
            getMessagesTextPane().setText(parent.getModel().messagesToString());
        }
        updateUsersLabel();
    }


    @Override
    public void clearFields() {
        getMessagesTextPane().setText("");
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
            messagesMainPanel.add(getMessagesPanel(), BorderLayout.CENTER);
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

    public JLabel getUsersLabel() {
        if (usersLabel == null) {
            usersLabel = new JLabel();
            usersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            usersLabel.setFont(new Font("Tamoha", Font.BOLD, 14));
            usersLabel.setForeground(Color.decode(TEXT_PANE_COLOR));
        }
        return usersLabel;
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

    public JList<String> getUsersJlist() {
        if (usersJlist == null) {
            usersJlist = new JList<>(parent.getModel().getUserNamesList());

            usersJlist.setFont(new Font("Tamoha", Font.BOLD, 14));
            usersJlist.setForeground(Color.decode(TEXT_PANE_COLOR));
            usersJlist.setBackground(Color.decode(PANEL_COLOR));

            DefaultListCellRenderer renderer = (DefaultListCellRenderer) usersJlist.getCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            usersJlist.addMouseListener(new ListSelectionChangeHandler(usersJlist, parent));

        }
        return usersJlist;
    }

    //SETTERS
    public void updateUsersLabel() {
        getUsersLabel().setText("Users conversations: " + (parent.getModel().getUserNamesList().size()));
    }

}