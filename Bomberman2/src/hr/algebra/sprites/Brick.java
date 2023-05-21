/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.sprites;

import hr.algebra.boundrybox.RectBoundryBox;
import hr.algebra.constants.Direction;
import hr.algebra.entity.Entity;
import hr.algebra.renderer.Renderer;
import javafx.scene.paint.Color;

/**
 *
 * @author Bruno
 */
public class Brick implements Entity {
    
    public int positionX = 0;
    public int positionY = 0;
    private int height;
    private int width;
    private Sprite sprite;
    RectBoundryBox entityBoundary;
    int layer;
    double scale=1;
    
    public Brick(int x,int y){
        
        positionX = x;
        positionY = y;
        setScale(2);
        width = 16;
        height = 16;
        layer = 1;
        sprite = new Sprite(this, 16, 0, 63, 48, 1,  (int)(width), (int)(height) ,getScale(), false);
        entityBoundary = new RectBoundryBox(positionX, positionY,  (int)(width * getScale()), (int)(height  * getScale()));
    }
    
    @Override
    public boolean isColliding(Entity b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int getPositionX() {
        return positionX;
    }

    @Override
    public int getPositionY() {
        return positionY;
    }

    @Override
    public RectBoundryBox getBoundryBox() {
        return entityBoundary;
    }

    @Override
    public boolean isPlayerCollisionFriendly() {
        return false;
    }

    @Override
    public int getLayer() { return layer; }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void move(int x, Direction y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw() {
        Renderer.playAnimation(sprite);
    }
}
