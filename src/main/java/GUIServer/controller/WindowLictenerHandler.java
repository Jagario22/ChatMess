package GUIServer.controller;

import Server.ChatMessServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
@Slf4j

public class WindowLictenerHandler implements WindowListener {
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    @SneakyThrows
    public void windowClosing(WindowEvent we) {
        ChatMessServer.saveMessagesXMLFile();
        log.info("Server stopped");
        ChatMessServer.getServerSocket().close();
    }
    @Override
    @SneakyThrows
    public void windowClosed(WindowEvent we) {
        ChatMessServer.saveMessagesXMLFile();
        log.info("Server stopped");
        ChatMessServer.getServerSocket().close();
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
