/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.sprites;

import hr.algebra.animations.PlayerAnimations;
import hr.algebra.boundrybox.RectBoundryBox;
import hr.algebra.constants.Direction;
import hr.algebra.entity.Entity;
import hr.algebra.entity.MovingEntity;
import hr.algebra.renderer.Renderer;
import hr.algebra.sandbox.Sandbox;

/**
 *
 * @author Bruno
 */
public class Player implements MovingEntity {
    
    private boolean isAlive;
    RectBoundryBox playerBoundary;

    Sprite currentSprite;
    PlayerAnimations playerAnimations;

    Direction currentDirection;

    public int positionX = 0;
    public int positionY = 0;
    int layer;
    String name;
    double scale = 2;
    double reduceBoundarySizePercent=0.45;
    public int bombCount = 1;
    
    
    public Player(int posX, int posY, String ime) {
        init(posX, posY, ime);
        name = ime;
        isAlive = true;
        layer=5;
    }
    private void init(int x, int y, String ime) {
        name = ime;

        playerAnimations = new PlayerAnimations(this,2);
        setScale(2);
        positionX = x;
        positionY = y;

        playerBoundary = new RectBoundryBox(positionX+(int)(18*getReduceBoundarySizePercent()),
                                            positionY+(int)(21*getReduceBoundarySizePercent()),
                                            (int)(18 * getScale())-2*+(int)(18*getReduceBoundarySizePercent()),
                                            (int)(21 * getScale())-2*+(int)(21*getReduceBoundarySizePercent())
                                            );

        currentSprite = playerAnimations.getPlayerIdleSprite();
    }
    
    public void move(Direction direction) {
        move(1, direction);
    }
    private void setCurrentSprite(Sprite s) {
        if (s != null) {
            currentSprite = s;
        } else {
            System.out.println("Sprite missing!");
        }
    }
    public boolean isAlive() {
        return isAlive;
    }
    public String toString() {
        return name;
    }
    @Override
    public boolean isColliding(Entity b) {
       // playerBoundary.setPosition(positionX, positionY);
        RectBoundryBox otherEntityBoundary = (RectBoundryBox) b.getBoundryBox();
        return playerBoundary.checkCollision(otherEntityBoundary);
    }
    
    @Override
    public void draw() {
        if (currentSprite != null) {
            Renderer.playAnimation(currentSprite);
        }
    }
    private boolean checkCollisions(int x, int y) {
    	playerBoundary.setPosition(x, y,getReduceBoundarySizePercent());

        for (Entity e : Sandbox.getEntities()) {
            if (e != this && isColliding(e) && !e.isPlayerCollisionFriendly()) {
            	playerBoundary.setPosition(positionX, positionY,getReduceBoundarySizePercent());
                
                return true;
            }
        }
    	playerBoundary.setPosition(positionX, positionY,getReduceBoundarySizePercent());
        return false;
    }
    @Override
    public void move(int steps, Direction direction) {

        if (steps == 0) {
            setCurrentSprite(playerAnimations.getPlayerIdleSprite());
            return;
        } else {
            switch (direction) {
                case UP:
                	if(!checkCollisions(positionX, positionY - steps)) {
	                    positionY -= steps;
	                    setCurrentSprite(playerAnimations.getMoveUpSprite());
	                    currentDirection = Direction.UP;
                	}
                    break;
                case DOWN:
                	if(!checkCollisions(positionX, positionY + steps)) {
                		positionY += steps;
	                    setCurrentSprite(playerAnimations.getMoveDownSprite());
	                    currentDirection = Direction.DOWN;
                	}
                    break;
                case LEFT:
                	if(!checkCollisions(positionX - steps, positionY)) {
                		positionX -= steps;
	                    setCurrentSprite(playerAnimations.getMoveLeftSprite());
	                    currentDirection = Direction.LEFT;
                	}
                    break;
                case RIGHT:
                	if(!checkCollisions(positionX + steps, positionY)) {
                		 positionX += steps;
	                    setCurrentSprite(playerAnimations.getMoveRightSprite());
	                    currentDirection = Direction.RIGHT;
                	}
                    break;
                default:
                    setCurrentSprite(playerAnimations.getPlayerIdleSprite());
            }
        }
    }
    
    @Override @Getter (comment = "retrives X possition")
    public int getPositionX() {
        return positionX;
    }

    @Override @Getter (comment = "retrives Y possition")
    public int getPositionY() {
        return positionY;
    }

    @Override
    public RectBoundryBox getBoundryBox() {
        playerBoundary.setPosition(positionX, positionY,getReduceBoundarySizePercent());
        return playerBoundary;
    }

    @Override
    public boolean isPlayerCollisionFriendly() {
        return true;
    }
    
    @Override @Getter (comment = "retrives layer")
    public int getLayer() { return layer; }

    public double getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
    
    public double getReduceBoundarySizePercent() {
        return reduceBoundarySizePercent;
    }
    public void setReduceBoundarySizePercent(double reduceBoundarySizePercent) {
        this.reduceBoundarySizePercent = reduceBoundarySizePercent;
    }

    public boolean hasMoreBombs() {
        return bombCount>0;
    }

    public void incrementBombCount(){
        ++bombCount;
    }

    public void decrementBombCount(){
        --bombCount;
    }

    @Override @Getter (comment = "retrives Name")
    public String getName() {
        return name;
    }
    
    public void setLayer(int layer1){
        layer = layer1;
    }
}
