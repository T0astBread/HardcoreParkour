/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour.training;

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
        return results.getDistance();
    }
    
}
