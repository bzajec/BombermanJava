/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.entity;

import hr.algebra.constants.Direction;

/**
 *
 * @author Bruno
 */
public interface MovingEntity extends Entity {
    
    public void move(int steps, Direction direction);
    
}
