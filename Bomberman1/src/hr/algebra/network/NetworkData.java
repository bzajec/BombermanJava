/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.network;

import java.io.Serializable;

/**
 *
 * @author Bruno
 */
public class NetworkData implements Serializable {

    
    
    public int positionX;
    public int positionY;
    public int layer;

    
    
    
    public NetworkData(int x, int y, int l){
        
        positionX=x;
        positionY=y;
        layer= l;
    }

    public NetworkData() {
    }
    
    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getLayer() {
        return layer;
    }
    
}