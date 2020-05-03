package client;

import Server.ChatMessChat;
import domain.Message;
import domain.xml.MessageParser;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class Utility {
    public static <T extends Container>  T findParent(Component comp,
                                            Class<T> clazz) {
        if (comp == null) {
            return null;
        }
        if (clazz.isInstance(comp)) {
            return (clazz.cast(comp));
        }
        else
            return findParent(comp.getParent(), clazz);
    }

    public static void messagesUpdate(ChatMessengerApp app) {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(app.getModel().getServerIPAddress());
            try (Socket socket = new Socket(addr, ChatMessChat.PORT)) {
                PrintWriter out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream()
                                )
                        ), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        ));
                {
                    Model model = app.getModel();
                    out.println("GET");
                    out.println(model.getLastMessageId());
                    out.flush();
                    String responseLine = in.readLine();
                    StringBuilder mesStr = new StringBuilder();
                    while (!"END".equals(responseLine)) {
                        mesStr.append(responseLine);
                        responseLine = in.readLine();
                    }
                    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                    SAXParser parser = parserFactory.newSAXParser();
                    List<Message> messages = new ArrayList<Message>() {
                        @Override
                        public String toString() {
                            return this.stream()
                                    .map(Message::toString)
                                    .collect(Collectors.joining("\n"));
                        }};
                    AtomicInteger id = new AtomicInteger(0);
                    MessageParser saxp = new MessageParser(id, messages);
                    parser.parse(new ByteArrayInputStream(mesStr.toString().getBytes()), saxp);
                    if (messages.size() > 0) {
                        model.addMessages(messages);
                        model.setLastMessageId(id.longValue());
                        log.trace("List of new messages: " + messages.toString());
                    }
                }
            }
            catch (IOException e) {
                log.error("Socket error");
            }
            catch (SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            log.error("Unknown host address: " + e.getMessage());
        }

    }

}
