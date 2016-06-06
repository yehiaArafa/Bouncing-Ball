/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.ModelAndTexuredInfo;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Omar K. Rostom
 */
public class Enemy extends Entity {

    public Enemy(ModelAndTexuredInfo model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }
    
}
