/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour.training;

import com.badlogic.gdx.math.Vector2;
import com.t0ast.evolution.training.FitnessRater;

/**
 *
 * @author T0astBread
 */
public class ParkourFitnessRater implements FitnessRater<ParkourResults>
{
    @Override
    public float rate(ParkourResults results)
    {
        float fitness = 1/getDistanceToNearestCheckpoint(results.getCheckPoints(), results.getEntity().getPosition());
        fitness += 2/results.getEntity().getPosition().dst(results.getGoal());
        return fitness;
    }
    
    private float getDistanceToNearestCheckpoint(Vector2[] checkpoints, Vector2 position)
    {
        float smallestDist = Float.MAX_VALUE;
        for(Vector2 checkpoint : checkpoints)
        {
            float dist = position.dst2(checkpoint);
            if(dist < smallestDist) smallestDist = dist;
        }
        return (float) Math.sqrt(smallestDist);
    }
}
