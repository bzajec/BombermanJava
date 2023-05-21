/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.chat;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 *
 * @author Bruno
 */
public class ChatServer {
    
    private static final String CLIENT_NAME = "Client";
    private static final String RMI_CLIENT = "client";
    private static final String RMI_SERVER = "server";
    private static final int REMOTE_PORT = 1099;
    private static final int RANDOM_PORT_HINT = 0;
    
    private ChatService server;
    private ChatService client;
    private Registry registry;
    
    private final Chat chat;
    
    public ChatServer(Chat chatController) {
        this.chat = chatController;
        publishServer();
        waitForClient();
    }
    
    public void publishServer(){
        server = new ChatService() {
            @Override
            public Color getColor() throws RemoteException {
                return Color.DARKCYAN;
            }

            @Override
            public String getName() throws RemoteException {
                return CLIENT_NAME;
            }

            @Override
            public  void send(String message) throws RemoteException {
                chat.postMessage(message, getName(), getColor());
            }
        };
        try {

            registry = LocateRegistry.createRegistry(REMOTE_PORT);
            ChatService stub = (ChatService) UnicastRemoteObject.exportObject(server, RANDOM_PORT_HINT);
            registry.rebind(RMI_SERVER, stub);

        } catch (RemoteException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
     public void waitForClient() {

        Thread thread = new Thread(() -> {
            while (client == null) {
                try {
                    client = (ChatService) registry.lookup(RMI_CLIENT);
                } catch (RemoteException | NotBoundException ex) {
                    System.out.println("waiting for client");
                }
                System.out.println(client);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        thread.setDaemon(true);
        thread.start();

    }
     
     public void sendMessage(String message) {
        try {
            client.send(message);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
