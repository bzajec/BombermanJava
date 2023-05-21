/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.sprites;

import java.io.Serializable;

/**
 *
 * @author Bruno
 */
public class SaveSprite implements Serializable {
    
    public  int xOS;
    public  int yOS;
    public  int sloj;

    public SaveSprite() {
    }
    
    public SaveSprite(int x, int y, int s){
        xOS = x;
        yOS = y;
        sloj=s;
    }

    public int getxOS() {
        return xOS;
    }

    public void setxOS(int xOS1) {
        xOS = xOS1;
    }

    public int getyOS() {
        return yOS;
    }

    public void setyOS(int yOS1) {
        yOS = yOS1;
    }

    public int getSloj() {
        return sloj;
    }

    public void setSloj(int sloj1) {
        sloj = sloj1;
    }
    
    
}
