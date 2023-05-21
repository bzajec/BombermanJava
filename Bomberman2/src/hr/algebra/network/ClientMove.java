/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.network;

import hr.algebra.GameLoop;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno
 */
public class ClientMove extends Thread {
    public static final int SERVER_PORT = 54321;
    
    private static List keyInputs;
    
    public void sendInputs(List kI){
        keyInputs=kI;
    }
    
    
    public void run() {
        
        try(Socket socket = new Socket("localhost",SERVER_PORT)){
            //System.err.println("Server listening on port: " + socket.getLocalPort());
            //System.err.println("X je: " + networkdata.getPositionX());
            
            OutputStream outputStream = socket.getOutputStream();
            
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            
            List KII = keyInputs;
            os.writeObject(KII);
            
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream is = new ObjectInputStream(inputStream);
            
            List movesList = (List) is.readObject();
            GameLoop.setPlayerMove(movesList);
            
            socket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ClientMove.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientMove.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }
    
}
