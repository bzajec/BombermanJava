/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.sprites;

import hr.algebra.animations.FlameAnimation;
import hr.algebra.boundrybox.RectBoundryBox;
import hr.algebra.constants.Direction;
import hr.algebra.entity.Entity;
import hr.algebra.renderer.Renderer;
import java.util.Date;

/**
 *
 * @author Bruno
 */
public class Explosion implements Entity {
    
    public int positionX = 0;
    public int positionY = 0;
    private int height;
    private int width;
    private Sprite sprite;
    RectBoundryBox entityBoundary;
    FlameAnimation bomb_animations;
    Date addedDate;
    int timerDurationInMillis = 2000; 
    STATE explosionState;
    int layer;
    double scale=1;
    private String stranaExp;

    @Override
    public void move(int x, Direction y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    enum STATE
    {
        INACTIVE,
        ACTIVE,
        END;
    }
    
    public Explosion(int x, int y) {
        positionX = x;
    	positionY = y;
    	width = 16;
    	height = 16;
    	layer=6;
    	setScale(1.3);
        bomb_animations=new FlameAnimation(this);
        sprite=bomb_animations.getFlameBombCenter();
        //sprite=bomb_animations.getFlameBombCenter();
        entityBoundary = new RectBoundryBox(positionX+2, positionY+4, (int)(width * getScale()), (int)(height  * getScale()));
        addedDate=new Date();
        explosionState=STATE.ACTIVE;
    }
    
    public boolean isExploading(){
        STATE s = checkExplosionState();
        
        if(s==STATE.END){
            
            return false;
        }
        else{
            if(s==STATE.ACTIVE||s==STATE.INACTIVE){
                return true;
            }
            return true;
        }
        
    }
    public STATE checkExplosionState(){
        
        if(new Date().getTime()>timerDurationInMillis+addedDate.getTime()){
            return STATE.END;
            
            
        }
        
        else{
            
            return STATE.ACTIVE;
        }
    }
    
    @Override
    public boolean isColliding(Entity b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw() {
        Renderer.playAnimation(sprite);
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
        return true;
    }

    @Override
    public int getLayer() { return layer; }

    @Override
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
}
