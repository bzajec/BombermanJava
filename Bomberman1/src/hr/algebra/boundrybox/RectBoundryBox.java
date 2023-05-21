/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.boundrybox;

import javafx.geometry.Rectangle2D;

/**
 *
 * @author Bruno
 */
public class RectBoundryBox {
    
    int x;
    int y;
    int width;
    int height;
    Rectangle2D boundary;
    
    public RectBoundryBox(int x,int y,int w,int h){
        this.x=x;
        this.y=y;
        width=w;
        height=h;
        boundary = new Rectangle2D(x, y, width, height);
    }
    
    public Rectangle2D getBoundary() {
        return boundary;
    }
    
    public void setBoundary(Rectangle2D boundaryRect){
        boundary = boundaryRect;
    }
    
    public boolean checkCollision(RectBoundryBox b) {
        return b.getBoundary().intersects(getBoundary());
    }

    public void setPosition(int x, int y, double reductionPercent) {
    	this.x = x+(int)(18*reductionPercent);
    	this.y = y+(int)(21*reductionPercent);
    	boundary = new Rectangle2D(this.x, this.y, width, height);
    }
    
}
