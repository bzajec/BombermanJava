/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.chat;

import static hr.algebra.sandbox.Sandbox.spMessage;
import static hr.algebra.sandbox.Sandbox.tfMessage;
import static hr.algebra.sandbox.Sandbox.vbMessage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Bruno
 */
public class Chat {

    private final static String TIME_FORMAT = "HH:mm:ss";
    private static final String MESSAGE_FORMAT = "%s (%s): %s";
    private static final String CLIENT_NAME = "Client";
    private static final int MESSAGE_LENGTH = 78;
    private static final int FONT_SIZE = 15;

    private static ObservableList<Node> messages;
    
    private static ChatClient chatClient;
    private static String poruka;
    
    public Chat(){
        chatClient = new ChatClient(this);
    }
    

    public static void sendMessage(String msg) {
        
        poruka=msg;
        chatClient.sendMessage(msg);
        addMessage(msg,CLIENT_NAME,Color.BLACK);
        tfMessage.clear();
        
    }

    private static void addMessage(String message, String name, Color color) {
        messages = FXCollections.observableArrayList();
        Label label = new Label();
        label.setFont(new Font(FONT_SIZE));
        label.setTextFill(color);
        label.setText(String.format(MESSAGE_FORMAT, LocalTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT)), name, message));
        messages.add(label);
        moveScrollPane();
        Bindings.bindContentBidirectional(messages, vbMessage.getChildren());
        vbMessage.getChildren().add(label);
        
    }

    private static void moveScrollPane() {
        spMessage.applyCss();
        spMessage.layout();
        spMessage.setVvalue(1D);
    }

    public static void postMessage(String message, String name, Color color) {
        Platform.runLater(() -> addMessage(message, name, color));
    }
    
    
}
