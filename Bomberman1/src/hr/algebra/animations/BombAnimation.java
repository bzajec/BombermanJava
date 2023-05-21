/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.animations;

import hr.algebra.constants.Constants;
import hr.algebra.entity.Entity;
import hr.algebra.renderer.Renderer;
import hr.algebra.sprites.Sprite;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Bruno
 */
public class BombAnimation {
    
    Sprite blackBomb;
    double playSpeed;
    Sprite flameCenter;
    Sprite flameLeft;
    Sprite flameRight;
    Sprite flameUp;
    Sprite flameDown;
    
    public Sprite getBlackBomb() {
        return blackBomb;
    }
     
    public void setBlackBomb(Sprite blackBomb) {
        this.blackBomb = blackBomb;
        
    }
    
    public Sprite getFlameBombCenter(){
        return flameCenter;
    }
    
    public void setFlameBombCenter(Sprite flameBombCenter){
        this.flameCenter=flameBombCenter;
    }
    
    public BombAnimation(Entity e) {
        Image img = Renderer.getSpiteSheet();
        playSpeed=0.3;
        
        List<Rectangle> specs=new ArrayList<>();
        specs.add(new Rectangle(0, 48, 16, 15));
        specs.add(new Rectangle(16, 48, 15, 16));
        specs.add(new Rectangle(32, 48, 16, 16));
        blackBomb = new Sprite(e,16, playSpeed,img, specs,Constants.PLAYER_WIDTH+2, Constants.PLAYER_HEIGHT+2, e.getScale(), false);
        
        List<Rectangle> flameCenter1 = new ArrayList<>();
        flameCenter1.add(new Rectangle(32,96,16,16));
        flameCenter1.add(new Rectangle(128,96,16,16));
        flameCenter1.add(new Rectangle(32,176,16,16));
        flameCenter1.add(new Rectangle(128,176,16,16));
        flameCenter = new Sprite(e,30, playSpeed, img, flameCenter1,Constants.PLAYER_WIDTH+2, Constants.PLAYER_HEIGHT+2, e.getScale(),false);
    }
}
