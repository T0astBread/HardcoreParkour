/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour.training;

import com.badlogic.gdx.math.Vector2;
import com.t0ast.evolution.entities.Entity;
import com.t0ast.evolution.training.TrainingResults;

/**
 *
 * @author T0astBread
 */
public class ParkourResults extends TrainingResults<ParkourEntity>
{
    private final Vector2[] checkPoints;
    private final Vector2 goal;

    public ParkourResults(Vector2[] checkPoints, Vector2 goal, ParkourEntity entity)
    {
        super(entity);
        this.checkPoints = checkPoints;
        this.goal = goal;
    }

    public Vector2[] getCheckPoints()
    {
        return checkPoints;
    }

    public Vector2 getGoal()
    {
        return goal;
    }
}
