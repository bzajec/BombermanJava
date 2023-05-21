/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.chat.Chat;
import hr.algebra.constants.Constants;
import hr.algebra.constants.Direction;
import hr.algebra.entity.Entity;
import hr.algebra.gamecontroller.Input;
import hr.algebra.network.ClientThread;
import hr.algebra.network.NetworkData;
import hr.algebra.sandbox.Sandbox;
import static hr.algebra.sandbox.Sandbox.btnSend;
import static hr.algebra.sandbox.Sandbox.tfMessage;
import hr.algebra.sprites.Bomb;
import hr.algebra.sprites.Brick;
import hr.algebra.sprites.Explosion;
import hr.algebra.sprites.Grass;
import hr.algebra.sprites.Player;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Bruno
 */
public class GameLoop {
    
    static double currentGameTime;
    static double oldGameTime;
    static double deltaTime;
    final static long startNanoTime = System.nanoTime();
    private static int xExplosion;
    private static int yExplosion;
    private static int xBomb;
    private static int yBomb;
    private static Chat chat;
    private static ClientThread ct;
    static List playerMoves;
    
    public static void start(GraphicsContext gc) {
        
        chat = new Chat();
//        ct = new ClientThread();
//        ct.setDaemon(true);
//        ct.start();
//        Player player = Sandbox.getPlayer();
//        NetworkData nd = new NetworkData(player.getPositionX(),player.getPositionY(),player.getLayer());
//        ct.sendEntities(nd);
        
        
        GameState.gameStatus=Constants.GameStatus.Running;
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
            	oldGameTime = currentGameTime;
            	currentGameTime = (currentNanoTime - startNanoTime) / 1000000000.0;
            	deltaTime = currentGameTime - oldGameTime;
                gc.clearRect(0, 0, Constants.CANVAS_WIDTH, Constants.CANVAS_WIDTH);
                
                
                updateGame();
                renderGame();
            }
        }.start();
    }
    
    public static double getCurrentGameTime() {
        return currentGameTime;
    }
    
    public static void renderGame() {
        for (Entity e : Sandbox.getEntities()) {
            e.draw();
        }
    }
    
    public static void updateGame(){
        
        Input.handlePlayerMovements();
        Vector<Entity> entities = Sandbox.getEntities();
        Player player = Sandbox.getPlayer();
//        NetworkData nd = new NetworkData(player.getPositionX(),player.getPositionY(),player.getLayer());
//        ct = new ClientThread();
//        ct.setDaemon(true);
//        ct.sendEntities(nd);
//        ct.start();
        
        
        if(tfMessage.getText().trim().length() >0 && btnSend.isPressed()){
             chat.sendMessage(tfMessage.getText().trim());
             btnSend.disarm();
         }
        
        Iterator<Entity> it = entities.iterator();
        
        while (it.hasNext()) {
            Entity entity = it.next();
            
            if(entity instanceof Bomb){
                boolean alive = ((Bomb) entity).isAlive();
                
                if(!alive){
                    
                    it.remove();
                    player.incrementBombCount();
                    int xOS = entity.getPositionX();  
                    int yOS = entity.getPositionY();
                    Sandbox.addEntityToGame(new Explosion(xOS,yOS));
                    Sandbox.addEntityToGame(new Explosion(xOS+26,yOS));
                    Sandbox.addEntityToGame(new Explosion(xOS,yOS+26));
                    Sandbox.addEntityToGame(new Explosion(xOS,yOS-26));
                    
                    xBomb = xOS;
                    yBomb = yOS;
                    
                    break;
                }
            }
            
            if(entity instanceof Explosion){
                
                boolean exploding = ((Explosion) entity).isExploading();
                
                if(!exploding){
                    it.remove();
                    
                    xExplosion = entity.getPositionX();
                    yExplosion = entity.getPositionY();
                    
                }
                
            }
            
            if(entity instanceof Brick && entity.getPositionX() > xBomb && entity.getPositionX()<xBomb+32 && entity.getPositionY() <yBomb+16 && entity.getPositionY()>yBomb-16 && xBomb>0 
                    || entity instanceof Brick && entity.getPositionX() > xBomb-40 && entity.getPositionX()<xBomb && entity.getPositionY() <yBomb+16 && entity.getPositionY()>yBomb-16 && xBomb>0
                    || entity instanceof Brick && entity.getPositionX() > xBomb-16 && entity.getPositionX()<xBomb+16 && entity.getPositionY() >yBomb-40 && entity.getPositionY()<yBomb && xBomb>0
                    || entity instanceof Brick && entity.getPositionX() > xBomb-16 && entity.getPositionX()<xBomb+16 && entity.getPositionY() >yBomb && entity.getPositionY()<yBomb+40 && xBomb>0){
                    
                it.remove();
                
                int xOS = entity.getPositionX();
                int yOS = entity.getPositionY();
                Sandbox.addEntityToGame(new Grass(xOS,yOS));
                break;
            }
            
            if(entity instanceof Player && "Player 1".equals(entity.getName())){
                
                if(playerMoves.contains(KeyCode.UP) || playerMoves.contains(KeyCode.W)){
                    entity.move(5,Direction.UP);
                }
                if(playerMoves.contains(KeyCode.DOWN) || playerMoves.contains(KeyCode.S)){
                    entity.move(5,Direction.DOWN);
                }
                if(playerMoves.contains(KeyCode.LEFT) || playerMoves.contains(KeyCode.A)){
                    entity.move(5,Direction.LEFT);
                }
                if(playerMoves.contains(KeyCode.RIGHT) || playerMoves.contains(KeyCode.D)){
                    entity.move(5,Direction.RIGHT);
                }
                if( !playerMoves.contains(KeyCode.LEFT) &&
                    !playerMoves.contains(KeyCode.RIGHT) &&
                    !playerMoves.contains(KeyCode.UP) &&
                    !playerMoves.contains(KeyCode.DOWN) &&
                    !playerMoves.contains(KeyCode.W) &&
                    !playerMoves.contains(KeyCode.A) &&
                    !playerMoves.contains(KeyCode.S) &&
                    !playerMoves.contains(KeyCode.D)
                  )
                {
                    entity.move(0, Direction.DOWN);
                }
                if(playerMoves.contains(KeyCode.B)){
                if(player.hasMoreBombs()) {
                    Sandbox.addEntityToGame(new Bomb(entity.getPositionX()+ Constants.PLAYER_WIDTH/2, entity.getPositionY()+Constants.PLAYER_HEIGHT/2));
                    player.decrementBombCount();
                }
                }
                
            }
            
            
        }
        
    }
    public static void setPlayerMove(List l){
        playerMoves=l;
    }
    
}
