/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour;

import com.t0ast.evolution.EvolvingPool;
import com.t0ast.parkour.training.ParkourEnvironment;
import com.t0ast.evolution.entities.instructional.InstructionGenerator;
import com.t0ast.evolution.entities.instructional.InstructionalEntity;
import com.t0ast.evolution.entities.instructional.InstructionalEntityGenerator;
import com.t0ast.evolution.entities.instructional.InstructionalMutator;
import com.t0ast.evolution.misc.EqualRandomListElementSelector;
import com.t0ast.evolution.training.Trainer;
import com.t0ast.parkour.instructions.MoveInstruction;
import com.t0ast.parkour.instructions.TurnInstruction;
import com.t0ast.parkour.training.ParkourEntity;
import com.t0ast.parkour.training.ParkourFitnessRater;
import com.t0ast.parkour.training.ParkourResults;

/**
 *
 * @author T0astBread
 */
public class EvolutionApplication
{

    public EvolutionApplication()
    {
        ParkourEnvironment env = new ParkourEnvironment();
        ParkourFitnessRater rater = new ParkourFitnessRater();
        InstructionGenerator ig = new InstructionGenerator();
        ig.registerInstruction(MoveInstruction.class);
        ig.registerInstruction(TurnInstruction.class);
        EvolvingPool<ParkourEntity, ParkourResults> pool = new EvolvingPool<>(new Trainer(env, rater),
        100, 50, new EqualRandomListElementSelector(), new EqualRandomListElementSelector(),
        new InstructionalMutator(), EvolvingPool.MutationType.SINGLE_PARENT, new InstructionalEntityGenerator(new ParkourEntity(), ig));
        pool.initialize();
    }
    
}
