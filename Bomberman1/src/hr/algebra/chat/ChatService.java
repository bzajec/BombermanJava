/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javafx.scene.paint.Color;

/**
 *
 * @author Bruno
 */
public interface ChatService extends Remote {
    Color getColor() throws RemoteException;
    String getName() throws RemoteException;
    void send(String message) throws RemoteException;
}
