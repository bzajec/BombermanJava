/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.renderer;

import hr.algebra.GameLoop;
import hr.algebra.sandbox.Sandbox;
import hr.algebra.sprites.Sprite;
import hr.algebra.utils.ImageUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Bruno
 */
public class Renderer {
    
    static Image img;
    static {
        img = ImageUtils.loadImage("Resources/img/BombermanSprites2.png"); //classic--sprites_without_border--BombermanSprites2
    }
    public static void init() {
        
    }
    public static Image getSpiteSheet(){
        return img;
    }
    
    public static void playAnimation(Sprite sprite) {
        double time = GameLoop.getCurrentGameTime();
        GraphicsContext gc = Sandbox.getGraphicsContext();
        if (sprite.hasValidSpriteImages) {
            playAnimation(sprite.spriteImages, sprite.playSpeed, sprite.getXPosition(), sprite.getYPosition(), sprite.width*sprite.getScale(), sprite.height*sprite.getScale());
        } else {
            playAnimation(gc, time, sprite.actualSize, sprite.spriteLocationOnSheetX, sprite.spriteLocationOnSheetY, sprite.numberOfFrames, sprite.getXPosition(), sprite.getYPosition(), sprite.width, sprite.height, sprite.getScale(), sprite.resersePlay, sprite.playSpeed);
        }
    
    }
    
    public static void playAnimation(Image[] imgs, double speed, int x, int y, double w, double h) {
        double time = GameLoop.getCurrentGameTime();
        GraphicsContext gc = Sandbox.getGraphicsContext();
        int numberOfFrames = imgs.length;
        int index = findCurrentFrame(time, numberOfFrames, speed);
        //System.out.println("index= "+index+" x="+x+" y="+y+" w="+w+" h="+h+" no of frames="+imgs.length+" speed="+speed+" time="+time);
        gc.drawImage(imgs[index], x, y, w, h);
    }
    
    public static void playAnimation(GraphicsContext gc, double time, int actualSize, int startingPointX, int startingPointY, int numberOfFrames, int x, int y, double width, double height, double scale, boolean reversePlay, double playSpeed) {

        double speed = playSpeed >= 0 ? playSpeed : 0.3;

        // index reporesents the index of image to be drawn from the set of images representing frames of animation
        int index = findCurrentFrame(time, numberOfFrames, speed);

        // newX represents the X coardinate of image in the spritesheet image to be drawn on screen
        int newSpriteSheetX = reversePlay ? startingPointX + index * actualSize : startingPointX;
        // newY represents the X coardinate of image in the spritesheet image to be drawn on screen
        int newSpriteSheetY = reversePlay ? startingPointY : startingPointY + index * actualSize;
        //System.out.println("Time, Total Frames" + time + ", " + numberOfFrames);
        //System.out.println("index=" + index + " newSpriteSheetX=" + newSpriteSheetX + " newSpriteSheetY=" + newSpriteSheetY + " width=" + width + " height=" + height + " x=" + x + " y=" + y + " width=" + width * scale + " height=" + height * scale);
                    //img,             sx,              sy,     w,     h,  dx, dy,        dw,             dh
        gc.drawImage(img, newSpriteSheetX, newSpriteSheetY, width, height, x, y, width * scale, height * scale);
    }

    private static int findCurrentFrame(double time, int totalFrames, double speed) {
        return (int) (time % (totalFrames * speed) / speed);
    }
    
    
}
