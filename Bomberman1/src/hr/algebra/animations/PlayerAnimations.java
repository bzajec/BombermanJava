/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.animations;

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
public class PlayerAnimations {
    
    Sprite moveRight;
    Sprite moveLeft;
    Sprite moveUp;
    Sprite moveDown;
    Sprite idle;
    Sprite die;
    double playSpeed;
    
    public PlayerAnimations(Entity e,int scale) {
        Image img = Renderer.getSpiteSheet();
        playSpeed=0.1;
        moveDown  = new Sprite(e, 15, 0.1, 48,  0, 3, 18 -0.5, 21-5.4, scale, true);
        moveLeft  = new Sprite(e, 15, 0.1, 0, 0, 3, 18, 21 -5.4, scale, true);
        moveUp    = new Sprite(e, 15, 0.1, 48, 16, 3, 18 - 1.5, 21 -5.4, scale, true);
        moveRight = new Sprite(e, 15, 0.1, 0, 16, 3, 18, 21 -5.4, scale, true);
        idle      = new Sprite(e, 15, 0.1, 62, 0, 1, 18 + 2, 21 -5.4, scale, false);
        
        List<Rectangle> specs=new ArrayList<>();
        specs.add(new Rectangle(149, 0,20,21));
        specs.add(new Rectangle(179, 1,19,20));
        specs.add(new Rectangle(118, 30,21,21));
        specs.add(new Rectangle(149, 30,20,21));
        specs.add(new Rectangle(179, 30,19,21));
        specs.add(new Rectangle(118, 60,21,21));
        specs.add(new Rectangle(147, 60,23,22));
        die = new Sprite(e,30,playSpeed,img, specs,18+2, 21+2, scale, false);
    }
    
    public Sprite getMoveRightSprite() {
        return moveRight;
    }

    public Sprite getMoveLeftSprite() {
        return moveLeft;
    }

    public Sprite getMoveUpSprite() {
        return moveUp;
    }

    public Sprite getMoveDownSprite() {
        return moveDown;
    }
    public Sprite getPlayerIdleSprite(){
        return idle;
    }
    public Sprite getPlayerDying(){
        return die;
    }
}
