/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.gamecontroller;

import hr.algebra.GameLoop;
import hr.algebra.constants.Constants;
import hr.algebra.constants.Direction;
import hr.algebra.sandbox.Sandbox;
import hr.algebra.sprites.Bomb;
import hr.algebra.sprites.Player;
import java.util.List;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Bruno
 */
public class Input {
    public static List keyboardInputs;
    
     public static void handlePlayerMovements(){
        keyboardInputs = EventHandler.getInputList();
        Player player = Sandbox.getPlayer();
        
        
            
        
        
        if(keyboardInputs.contains(KeyCode.UP) || keyboardInputs.contains(KeyCode.W)){
            player.move(5,Direction.UP);
        }
        if(keyboardInputs.contains(KeyCode.DOWN) || keyboardInputs.contains(KeyCode.S)){
            player.move(5,Direction.DOWN);
        }
        if(keyboardInputs.contains(KeyCode.LEFT) || keyboardInputs.contains(KeyCode.A)){
            player.move(5,Direction.LEFT);
        }
        if(keyboardInputs.contains(KeyCode.RIGHT) || keyboardInputs.contains(KeyCode.D)){
            player.move(5,Direction.RIGHT);
        }
        if( !keyboardInputs.contains(KeyCode.LEFT) &&
            !keyboardInputs.contains(KeyCode.RIGHT) &&
            !keyboardInputs.contains(KeyCode.UP) &&
            !keyboardInputs.contains(KeyCode.DOWN) &&
            !keyboardInputs.contains(KeyCode.W) &&
            !keyboardInputs.contains(KeyCode.A) &&
            !keyboardInputs.contains(KeyCode.S) &&
            !keyboardInputs.contains(KeyCode.D)
          )
        {
            player.move(0, Direction.DOWN);
        }
        
        //Drop bomb
        if(keyboardInputs.contains(KeyCode.B)){
            if(player.hasMoreBombs()) {
                Sandbox.addEntityToGame(new Bomb(player.getPositionX()+ Constants.PLAYER_WIDTH/2, player.getPositionY()+Constants.PLAYER_HEIGHT/2));
                player.decrementBombCount();
            }
        }
        
//        if(keyboardInputs.contains(KeyCode.ENTER)){
//            Chat.sendMessage();
//        }
    }
     
     
     public List getKeyboardInputs(){
         return keyboardInputs;
     }
    
}
