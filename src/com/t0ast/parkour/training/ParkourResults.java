/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour.training;

import com.t0ast.evolution.entities.Entity;

/**
 *
 * @author T0astBread
 */
public class ParkourResults extends com.t0ast.evolution.training.TrainingResults
{
    private final float distance;

    public ParkourResults(float distance, Entity entity)
    {
        super(entity);
        this.distance = distance;
    }

    public float getDistance()
    {
        return distance;
    }
}
