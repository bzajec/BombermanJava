/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.entity;

import hr.algebra.boundrybox.RectBoundryBox;
import hr.algebra.constants.Direction;

/**
 *
 * @author Bruno
 */
public interface Entity {
    
    boolean isColliding(Entity b);
    boolean isPlayerCollisionFriendly();
    void draw();
    //void removeFromScene();
    int getPositionX();
    int getPositionY();
    RectBoundryBox getBoundryBox();
    int getLayer();
    double getScale();
    String getName();
    void move(int x,Direction y);
    
}
