/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.sandbox;

import hr.algebra.GameLoop;
import hr.algebra.chat.ChatClient;
import hr.algebra.entity.Entity;
import hr.algebra.gamecontroller.EventHandler;
import hr.algebra.network.ClientThread;
import hr.algebra.network.NetworkData;
import hr.algebra.renderer.Renderer;
import hr.algebra.sprites.Brick;
import hr.algebra.sprites.Grass;
import hr.algebra.sprites.Player;
import hr.algebra.sprites.Wall;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author Bruno
 */
public class Sandbox {
    
    static Scene s;
    static Group root;
    public static Canvas c;
    static Canvas c2;
    public static GraphicsContext gc;
    private static boolean sceneStarted;
    public static TextField tfMessage;
    public static ScrollPane spMessage;
    public static VBox vbMessage;
    static Player sandboxPlayer;
    public static Text level, bomb, time;
    public static AnchorPane ap;
    public static Button btnSend;
    //private static ChatServer chatServer;   --chat 
    public static Button btnSave;
    public static Button btnLoad;
    public static String LOAD_MAP = "Resources/maps/sandbox_map.txt";
    public static Button btnSaveDOM;
    public static Button btnLoadDOM;
    private static ChatClient chatClient;
    static{
        sceneStarted=false;
    }
    private static Vector<Entity> entities = new Vector<Entity>();

	public static Vector<Entity> getEntities(){
		return entities;
	}
        
        static Comparator<Entity> layerComparator=new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            int result = Integer.compare(o1.getLayer(),o2.getLayer());
            return result;
        }
    };
        
        public static boolean addEntityToGame(Entity e){
		if(!entities.contains(e)){
			entities.add(e);
            Collections.sort(entities,layerComparator);
            return true;
		} else {
			return false;
		}
	} 
    public static void setupScene(){
        if(!sceneStarted){
            init();
            sceneStarted=true;
        }
    }
    
    private static void init() {
    
        root = new Group();
        s = new Scene(root, 960, 670);
        c = new Canvas(2000, 2000);
        c.setLayoutY(32);
        tfMessage = new TextField();
        tfMessage.setLayoutY(520);
        tfMessage.setPrefSize(300, 50);
        tfMessage.setPromptText("Insert message");
        
        vbMessage = new VBox();
        
        spMessage = new ScrollPane();
        spMessage.setPrefSize(300, 500);
        spMessage.setContent(vbMessage);
        
        vbMessage.setPrefSize(290, 400);
        
        
        btnSend = new Button();
        btnSend.setLayoutY(590);
        btnSend.setPrefSize(300,50);
        btnSend.setText("Send");
        
        ap = new AnchorPane();
        ap.setLayoutX(650);
        ap.setLayoutY(0);
        ap.setPrefHeight(500);
        ap.setPrefWidth(100);
        ap.getChildren().add(tfMessage);
        ap.getChildren().add(vbMessage);
        ap.getChildren().add(spMessage);
        ap.getChildren().add(btnSend);
        ap.setMaxSize(100.0,200.0);
        Pane pane = new Pane();
        
        
        
        pane.setMinSize(640, 30);
        pane.setStyle("-fx-background-color: #353535");
        
        
        root.getChildren().add(c);
        root.getChildren().add(pane);
        root.getChildren().add(ap);
        gc = c.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.setFill(Color.BLUE);
        
//        chatClient = new ChatClient();
//        chatClient.publishClient();
//        chatClient.fetchServer();
        
        Renderer.init();
        GameLoop.start(gc);
        
        try
        {
            loadMap(new File(LOAD_MAP));
        } catch (IOException e)
        {
            System.err.println("Unable to load map.");
            System.exit(1);
        }
        EventHandler.attachEventHandlers(s);

    }
    
    public static void loadMap(File file) throws IOException
    {
    	Vector<Wall> walls = new Vector<Wall>();
        Vector<Brick> bricks = new Vector<Brick>();
        Vector<Grass> grasses = new Vector<Grass>();
    	boolean playerSet = false;

        try(BufferedReader inputStream = new BufferedReader(new FileReader(file)))
        {
            String line;
            int y = 0;
            while((line = inputStream.readLine()) != null)
            {
                for(int x = 0; x < line.length(); x++)
                {
                    switch (line.charAt(x))
                    {
                        case 'W':
                            walls.add(new Wall(x * 32, y * 32));
                            break;
                        case 'B':
                            bricks.add(new Brick(x * 32,y * 32));
                            break;
                        case 'O':
                            grasses.add(new Grass(x * 32,y * 32));
                            break;
                        case 'P':
                            //Initialize Objects
                            grasses.add(new Grass(x * 32,y * 32));
                            setPlayer(new Player(x * 32, y * 32,"Player 2"));
                            playerSet = true;
                            break;
                    }
                }
                y++;
            }
        }
        
        if(!playerSet){
            System.err.println("No player location is set on map.");
            System.exit(1);
        }
        ClientThread ct= new ClientThread();   //ovo za igraca
        ct.setDaemon(true);
        Player player = getPlayer();
        NetworkData nd = new NetworkData(player.getPositionX(),player.getPositionY(),player.getLayer());
        ct.sendEntities(nd);
        ct.start();
        
        for(Wall wall : walls) {
            addEntityToGame(wall);
    	}
        for(Brick brick : bricks){
            addEntityToGame(brick);
        }
        for(Grass grass : grasses){
            addEntityToGame(grass);
        }
        
    }
    

    public static Scene getScene() {
        return s;
    }

    public static GraphicsContext getGraphicsContext() {
        return gc;
    }

    public static Canvas getCanvas() {
        return c;
    }
    
    public static void setPlayer(Player p){
        sandboxPlayer = p;
        addEntityToGame(p);
    }
    public static Player getPlayer(){
        return sandboxPlayer;
    }


    
}
