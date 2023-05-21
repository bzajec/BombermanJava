/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.network;

import hr.algebra.sandbox.Sandbox;
import hr.algebra.sprites.Player;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class ServerThread extends Thread {
    
    public static int SERVER_PORT = 12346;
    
    private static NetworkData networkdata;
    
    public void sendEnteties(NetworkData networkData){
        networkdata=networkData;
    }

    
    @Override
    public void run(){
        
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            System.err.println("Server listening on port: " + serverSocket.getInetAddress());
        
            try (Socket socket = serverSocket.accept()) {
                
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());  
                NetworkData nd = networkdata;
                os.writeObject(networkdata);
                
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream is = new ObjectInputStream(inputStream);
                //ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                NetworkData nD = (NetworkData) is.readObject();
                
                //socket.close();
                if(nD.layer==0){
                    Sandbox.addEntityToGame(new Player(nD.getPositionX(),nD.positionY,"Player 2"));
                }
                
                
            
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            serverSocket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
