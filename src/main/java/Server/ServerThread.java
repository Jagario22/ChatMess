package Server;

import domain.Message;
import domain.xml.MessageBuilder;
import domain.xml.MessageParser;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Slf4j
public class ServerThread extends  Thread{

    public static final String METHOD_GET = "GET";
    public static final String METHOD_PUT = "PUT";
    public static final String END_LINE_MESSAGE = "END";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_GET_MESSAGES = "MESSAGES";
    public static final String METHOD_GET_USERS = "USERS";
    public static final String METHOD_PUT_MESSAGE = "MESSAGE";
    public static final String METHOD_PUT_USER = "USER";

    private final Socket socket;
    private final AtomicInteger messageId;

    private final ListOfClients clients;
    private final Map<Long, Message> messagesList;

    private final BufferedReader in;
    private final PrintWriter out;


    public ServerThread(Socket socket, AtomicInteger messageId, Map<Long, Message> messagesList, ListOfClients clients) throws IOException {
        this.socket = socket;
        this.messageId = messageId;
        this.messagesList = messagesList;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    @Override
    public void run() {
        try {
            log.debug("New socket thread is starting...");
            String requestLine = in.readLine();
            log.debug(requestLine);
            switch (requestLine) {
                case METHOD_GET: {
                    String requestline = in.readLine();
                    switch (requestline) {
                        case METHOD_GET_MESSAGES: {
                            log.debug("get");
                            Long lastId = Long.valueOf(in.readLine());
                            log.debug(String.valueOf(lastId));
                            String currentUser = in.readLine();
                            log.debug("currentUser: " + currentUser);
                            String receiver = in.readLine();
                            log.debug("receiver: " + receiver);
                            List<Message> newMessages;
                            if (!receiver.equals("General chat")) {
                                newMessages = messagesList.entrySet().stream()
                                        .filter(message -> (message.getValue().getUserNF().equals(currentUser)
                                                && message.getValue().getUserNT().equals(receiver))
                                                || (message.getValue().getUserNF().equals(receiver)
                                                && message.getValue().getUserNT().equals(currentUser)))
                                        .filter(message -> message.getKey().compareTo(lastId) > 0)
                                        .map(Map.Entry::getValue).collect(Collectors.toList());
                                log.debug(newMessages.toString());
                            } else
                            {
                                newMessages = messagesList.entrySet().stream()
                                        .filter(message -> (message.getValue().getUserNT().equals("General chat")))
                                        .filter(message -> message.getKey().compareTo(lastId) > 0)
                                        .map(Map.Entry::getValue).collect(Collectors.toList());
                            }
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
                            Document document = builder.newDocument();
                            java.lang.String xmlContent = MessageBuilder.buildDocument(document, newMessages);
                            out.println(xmlContent);
                            out.println(END_LINE_MESSAGE);
                            out.flush();
                            break;
                        }
                        case METHOD_GET_USERS: {
                            out.println(clients.toString());
                            out.println(END_LINE_MESSAGE);
                            out.flush();
                            break;
                        }
                    }
                    break;
                }
                case METHOD_PUT: {
                    log.debug("put");
                    requestLine = in.readLine();
                    switch (requestLine)
                    {
                        case METHOD_PUT_MESSAGE: {
                            requestLine = in.readLine();
                            StringBuilder mesStr = new StringBuilder();

                            while (!END_LINE_MESSAGE.equals(requestLine)) {
                                mesStr.append(requestLine);
                                requestLine = in.readLine();
                            }

                            log.debug(String.valueOf(mesStr));

                            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                            SAXParser parser = saxParserFactory.newSAXParser();
                            List<Message> messages = new ArrayList<>();
                            MessageParser saxp = new MessageParser(messageId, messages);

                            InputStream is = new ByteArrayInputStream(mesStr.toString().getBytes());
                            parser.parse(is, saxp);


                            for (Message message : messages) {
                                messagesList.put(message.getId(), message);
                            }

                            log.trace("Echoing: " + messages);
                            out.println("OK");
                            out.flush();
                            out.close();
                        }
                        case METHOD_PUT_USER: {
                            String currentUser = in.readLine();
                            if (!clients.ContainsName(currentUser)) {
                                log.debug("Add name " + currentUser + " to clients");
                                clients.addName(currentUser);
                            }
                            log.debug("clientsOnline: " + clients.getUserNames());
                            out.println("OK");
                            out.flush();
                        }
                    }
                    break;
                }
                case METHOD_DELETE:
                    requestLine = in.readLine();
                    log.debug("Remove name " + requestLine + " from clients");
                    clients.removeName(requestLine);
                    log.debug("clientsOnline: " + clients.getUserNames());
                    out.println("OK");
                    out.flush();
                    break;

                default:
                    log.info("Unknown request: " + requestLine);
                    out.println("BAD REQUEST");
                    out.flush();
                    break;
            }
        }catch (Exception e) {
            log.error(e.getMessage());
            out.println("ERROR");
            out.flush();
        } finally {
            try {
                log.debug("Socket closing...");
                log.debug("Close stream objects");
                log.debug("clientsOnline: " + clients.getUserNames());
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                log.error("Socket not closed");
            }
        }
    }

    public AtomicInteger getMessageId() {
        return messageId;
    }
    public Map<Long, Message> getMessagesList() {
        return messagesList;
    }
}
