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
import hr.algebra.network.ServerMove;
import hr.algebra.network.ServerThread;
import hr.algebra.sandbox.Sandbox;
import static hr.algebra.sandbox.Sandbox.btnCreateDoc;
import static hr.algebra.sandbox.Sandbox.btnLoad;
import static hr.algebra.sandbox.Sandbox.btnLoadDOM;
import static hr.algebra.sandbox.Sandbox.btnSave;
import static hr.algebra.sandbox.Sandbox.btnSaveDOM;
import static hr.algebra.sandbox.Sandbox.btnSend;
import static hr.algebra.sandbox.Sandbox.c;
import static hr.algebra.sandbox.Sandbox.tfMessage;
import hr.algebra.sprites.Bomb;
import hr.algebra.sprites.Brick;
import hr.algebra.sprites.Explosion;
import hr.algebra.sprites.Grass;
import hr.algebra.sprites.Player;
import hr.algebra.sprites.SaveSprite;
import hr.algebra.sprites.Wall;
import hr.algebra.utils.ReflectionUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import utils.DOMUtils;
import utils.FileUtils;
import utils.SerializationUtils;

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
    private static int counterLoad = 0;
    private static int counterSave = 0;
    private static Chat chat;
    static List playerMoves;
    private static ServerMove sm;
    public final static ServerMove lock2 = new ServerMove();
    public static final Object lock = new Object();
    private static final String DOCUMENTATION_FILENAME = "documentation.txt";
    private static final String CLASSES_PATH = "src/hr/algebra/sprites";
    private static final String CLASSES_PACKAGE
            = CLASSES_PATH.substring(CLASSES_PATH.indexOf("/") + 1).replace("/", ".").concat(".");
    private static boolean isWakeupNeeded;
    
    
    public static void start(GraphicsContext gc) throws InterruptedException {
        
        chat = new Chat();
        
        GameState.gameStatus=Constants.GameStatus.Running;
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
            	oldGameTime = currentGameTime;
            	currentGameTime = (currentNanoTime - startNanoTime) / 1000000000.0;
            	deltaTime = currentGameTime - oldGameTime;
                gc.clearRect(0, 0, Constants.CANVAS_WIDTH, Constants.CANVAS_WIDTH);
                
                sm = new ServerMove();
                sm.setDaemon(true);
                sm.sendInputs(Input.keyboardInputs);
                sm.start();
                
                
                
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
        Object lockObject = new Object();
        
//        synchronized(sm){
//        
//        
//        
//        }
        if(btnSave.isPressed() && counterSave==0){
            List<SaveSprite> ssSprites = new ArrayList<SaveSprite>();
                    
                    Iterator<Entity> it = entities.iterator();
                    
                    while(it.hasNext()){
                        
                        Entity entities1 = it.next();
                        
                        int xOsovina = entities1.getPositionX();
                        int yOsovina = entities1.getPositionY();
                        int slojcina = entities1.getLayer();
                        SaveSprite temp = new SaveSprite();
                        SaveSprite igrac = new SaveSprite();
                        temp.setxOS(xOsovina);
                        temp.setyOS(yOsovina);
                        temp.setSloj(slojcina);
                        
                        
                        if(entities1 instanceof Player && "Player 1".equals(entities1.getName()) ){
                            temp.setxOS(entities1.getPositionX());
                            temp.setyOS(entities1.getPositionY());
                            temp.setSloj(2);
                        
                        }
//                        if(entities1 instanceof Player && "Player 1".equals(entities1.getName()) ){
//                            temp.setxOS(entities1.getPositionX());
//                            temp.setyOS(entities1.getPositionY());
//                            temp.setSloj(3);
//                        
//                        }
                        
                        ssSprites.add(temp);
                        
                    }
            try{        
                    File file = FileUtils.saveFileDialog(c.getScene().getWindow(), "ser");
                        if(file != null)
                        {
                            SerializationUtils.write(ssSprites, file.getAbsolutePath());
                        }
                    }
                    catch (IOException ex){
                        Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ssSprites.clear();
                    counterSave++;
        }
        
        if(btnLoad.isPressed() && counterLoad == 0){
            entities.clear();
            File file = FileUtils.uploadFileDialog(c.getScene().getWindow(), "ser");
            if(file != null){
                try{
                    List<SaveSprite>sssSprites = (List<SaveSprite>) SerializationUtils.read(file.getAbsolutePath());
                    
                    List<SaveSprite> s = new ArrayList<SaveSprite>(); 
                    Vector<Wall> walls = new Vector<Wall>();
                    Vector<Brick> bricks = new Vector<Brick>();
                    Vector<Grass> grasses = new Vector<Grass>();
                    
                    for(SaveSprite sS : sssSprites){
                        switch(sS.getSloj()){
                            case 1:
                                bricks.add(new Brick(sS.getxOS(),sS.getyOS()));
                                break;
                            case 3:
                                grasses.add(new Grass(sS.getxOS(),sS.getyOS()));
                                Sandbox.setPlayer(new Player(sS.getxOS(),sS.getyOS(),"Player 2"));
                            break;
                            case 2:    
                                grasses.add(new Grass(sS.getxOS(),sS.getyOS()));
                                Sandbox.setPlayer(new Player(sS.getxOS(),sS.getyOS(),"Player 1"));
                                break;
                            
                            case -1:
                                grasses.add(new Grass(sS.getxOS(),sS.getyOS()));
                                break;
                            case 0:    
                                walls.add(new Wall(sS.getxOS(),sS.getyOS()));
                                break;
                        }
                    }
                    for(Wall wall : walls) {
                        Sandbox.addEntityToGame(wall);
                    }
                    for(Brick brick : bricks){
                        Sandbox.addEntityToGame(brick);
                    }
                    for(Grass grass : grasses){
                        Sandbox.addEntityToGame(grass);
                    }
                    
                }
                catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
            counterLoad++;
        
        }
        
        if(btnSaveDOM.isPressed()){
            
                    ObservableList<SaveSprite> ssSprites = FXCollections.observableArrayList();
                    
                    Iterator<Entity> it = entities.iterator();
                    
                    while(it.hasNext()){
                        
                        Entity entities1 = it.next();
                        
                        int xOsovina = entities1.getPositionX();
                        int yOsovina = entities1.getPositionY();
                        int slojcina = entities1.getLayer();
                        SaveSprite temp = new SaveSprite();
                        SaveSprite igrac = new SaveSprite();
                        temp.setxOS(xOsovina);
                        temp.setyOS(yOsovina);
                        temp.setSloj(slojcina);
                        
                        if(entities1 instanceof Player && "Player 1".equals(entities1.getName()) ){
                            temp.setxOS(entities1.getPositionX());
                            temp.setyOS(entities1.getPositionY());
                            temp.setSloj(2);
                        
                        }
//                        if(entities1 instanceof Player && "Player 1".equals(entities1.getName()) ){
//                            temp.setxOS(entities1.getPositionX());
//                            temp.setyOS(entities1.getPositionY());
//                            temp.setSloj(3);
//                        
//                        }
//                        if(entities1 instanceof Player ){
//                            temp.setxOS(entities1.getPositionX());
//                            temp.setyOS(entities1.getPositionY());
//                            temp.setSloj(2);
//                        
//                        }
//                        if(entities1 instanceof Player  && "Player 1".equals(entities1.getName())){
//                            temp.setxOS(entities1.getPositionX());
//                            temp.setyOS(entities1.getPositionY());
//                            temp.setSloj(3);
//                        
//                        }
                        ssSprites.add(temp);
                        
                    }
                    DOMUtils.saveSprites(ssSprites);
        }
         if(btnLoadDOM.isPressed()){
            entities.clear();
            
            ObservableList<SaveSprite> olsS = FXCollections.observableArrayList();
            
            olsS= DOMUtils.loadSprites();
            
                    List<SaveSprite> s = new ArrayList<SaveSprite>(); 
                    Vector<Wall> walls = new Vector<Wall>();
                    Vector<Brick> bricks = new Vector<Brick>();
                    Vector<Grass> grasses = new Vector<Grass>();
            
            
                    for(SaveSprite sS : olsS){
                        
                        switch(sS.getSloj()){
                            
                            case 1:
                                bricks.add(new Brick(sS.getxOS(),sS.getyOS()));
                                break;
                            case 3:
                                grasses.add(new Grass(sS.getxOS(),sS.getyOS()));
                                Sandbox.setPlayer(new Player(sS.getxOS(),sS.getyOS(),"Player 2"));
                                Player player1 = Sandbox.getPlayer();
                                player1.setLayer(3);
                            break;
                            case 2:    
                                grasses.add(new Grass(sS.getxOS(),sS.getyOS()));
                                Sandbox.setPlayer(new Player(sS.getxOS(),sS.getyOS(),"Player 1"));
                                Player player2 = Sandbox.getPlayer();
                                player2.setLayer(2);
                                break;
//                            case 5:
//                                grasses.add(new Grass(sS.getxOS(),sS.getyOS()));
//                                Sandbox.setPlayer(new Player(sS.getxOS(),sS.getyOS(),"Igrac 2"));
//                                break;
                            case -1:
                                grasses.add(new Grass(sS.getxOS(),sS.getyOS()));
                                break;
                            case 0:    
                                walls.add(new Wall(sS.getxOS(),sS.getyOS()));
                                break;
                        }
                        
                        
                    }
                    
                    for(Wall wall : walls) {
                        Sandbox.addEntityToGame(wall);
                    }
                    for(Brick brick : bricks){
                        Sandbox.addEntityToGame(brick);
                    }
                    for(Grass grass : grasses){
                        Sandbox.addEntityToGame(grass);
                    }
        }
         
         
         
//         if(tfMessage.getText().trim().length() > 5 && tfMessage.getText().trim().length() < 100){
//             
//             synchronized(lock2){
//                 try {
//                     lock2.wait();
//                 }  catch(InterruptedException e){
//                     Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, e);
//                 }
//                 
//                 
//                 
//                 
//             }
//             
//         }
         
         if(tfMessage.getText().trim().length() >0 && btnSend.isPressed()){
             
            synchronized(lock2){
             
             chat.sendMessage(tfMessage.getText().trim());
             btnSend.disarm();
             lock2.notifyAll();
            }
         }
         
         
         if(btnCreateDoc.isPressed()){
             
             try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(DOCUMENTATION_FILENAME))) {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(CLASSES_PATH));

            StringBuilder classAndMembersInfo = new StringBuilder();
            stream.forEach(file -> {
                String filename = file.getFileName().toString();
                String className = filename.substring(0, filename.indexOf("."));

                classAndMembersInfo
                        .append(className)
                        .append(System.lineSeparator());
                
                try {
                    Class<?> clazz = Class.forName(CLASSES_PACKAGE.concat(className));
                    ReflectionUtils.readClassAndMembersInfo(clazz, classAndMembersInfo);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
                }

                classAndMembersInfo
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());

            });

            writer.write(classAndMembersInfo.toString());
        } catch (IOException ex) {
            Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
        }
             
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
                    Sandbox.addEntityToGame(new Explosion(xOS-26,yOS));
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
            
//            if(entity instanceof Player && entity.getPositionX() > xBomb && entity.getPositionX()<xBomb+32 && entity.getPositionY() <yBomb+16 && entity.getPositionY()>yBomb-16 && xBomb>0 
//                    || entity instanceof Brick && entity.getPositionX() > xBomb-40 && entity.getPositionX()<xBomb && entity.getPositionY() <yBomb+16 && entity.getPositionY()>yBomb-16 && xBomb>0
//                    || entity instanceof Brick && entity.getPositionX() > xBomb-16 && entity.getPositionX()<xBomb+16 && entity.getPositionY() >yBomb-40 && entity.getPositionY()<yBomb && xBomb>0
//                    || entity instanceof Brick && entity.getPositionX() > xBomb-16 && entity.getPositionX()<xBomb+16 && entity.getPositionY() >yBomb && entity.getPositionY()<yBomb+40 && xBomb>0){
//                
//                
//                it.remove();
//                
//            }
            
            
            if(entity instanceof Player && "Player 2".equals(entity.getName())){
                
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

