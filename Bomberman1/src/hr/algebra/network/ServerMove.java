/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.network;

import hr.algebra.GameLoop;
import static hr.algebra.GameLoop.lock;
import static hr.algebra.GameLoop.lock2;
import static hr.algebra.sandbox.Sandbox.tfMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class ServerMove extends Thread {
    
    public static final int SERVER_PORT = 54321;
    
    private static List keyInputs;
    
    public void sendInputs(List kI){
        keyInputs=kI;
    }
    
    
    @Override
    public void run() {
        
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
            //System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            //System.err.println("Server listening on port: " + serverSocket.getInetAddress());
            //System.err.println("X pozicija: " + networkdata.positionX);
            
            
            
            try (Socket socket = serverSocket.accept()) {
                
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream()); 
                List oo = keyInputs;
                os.writeObject(oo);
                
                
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream is = new ObjectInputStream(inputStream);
                
                if(tfMessage.getText().trim().length() > 5 && tfMessage.getText().trim().length() < 100){
                    
                    
                    
                    synchronized(lock2){
                 try {
                     lock2.wait();
                 }  catch(InterruptedException e){
                     Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, e);
                 }
                 
                 
                 
                 
                }
                    
                }
                
                List movesList = (List) is.readObject();
                
//                synchronized(lock){
//                    
//                    try {
//                        lock.wait();
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(ServerMove.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }

                
                
                
                GameLoop.setPlayerMove(movesList);
                socket.close();
                serverSocket.close();
            
            
        }catch (ClassNotFoundException ex) {
           Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }   
       
        
    }   catch (IOException ex) {
            Logger.getLogger(ServerMove.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
