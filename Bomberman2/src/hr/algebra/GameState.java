/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.constants.Constants;
import java.util.Date;

/**
 *
 * @author Bruno
 */
public class GameState {
    
    public static int level;
    public static Date lastSaved;
    public static boolean hasUnsavedChanges;
    public static Constants.GameStatus gameStatus;
    
}
