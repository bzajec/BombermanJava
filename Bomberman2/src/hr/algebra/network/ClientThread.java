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
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class ClientThread extends Thread {
    
    public static final int SERVER_PORT = 12346;
    
    private NetworkData networkdata;
    
    
    public void sendEntities(NetworkData networkData){
        networkdata = networkData;
    }
    
    
    @Override
    public void run() {
        
        try(Socket socket = new Socket("localhost",SERVER_PORT)){
            
            OutputStream outputStream = socket.getOutputStream();
            
            //ObjectOutputStream os = new ObjectOutputStream(outputStream);
            
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());  
            NetworkData nd = networkdata;
            os.writeObject(networkdata);
            
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream is = new ObjectInputStream(inputStream);
            
            NetworkData nD = (NetworkData) is.readObject();
                
                //socket.close();
            if(nD.layer==5){
                Sandbox.addEntityToGame(new Player(nD.getPositionX(),nD.positionY,"Player 1"));
            }
            
            socket.close();
            
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }
    
    
}
