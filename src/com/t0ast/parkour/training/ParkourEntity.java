/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour.training;

import com.badlogic.gdx.math.Vector2;
import com.t0ast.evolution.entities.Entity;
import com.t0ast.evolution.entities.instructional.InstructionalEntity;

/**
 *
 * @author T0astBread
 */
public class ParkourEntity extends InstructionalEntity
{
    private Vector2 position, direction;

    public ParkourEntity()
    {
        super(100);
        this.position = new Vector2(0, 0);
        this.direction = new Vector2(1, 1);
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    @Override
    public Entity duplicate()
    {
        ParkourEntity entity = new ParkourEntity();
        entity.position = this.position.cpy();
        entity.direction = this.direction.cpy();
        entity.getInstructions().addAll(this.getInstructions());
        return entity;
    }
}
