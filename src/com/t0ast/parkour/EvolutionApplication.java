/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour;

import com.badlogic.gdx.math.Polygon;
import com.t0ast.evolution.EvolvingPool;
import com.t0ast.evolution.entities.Entity;
import com.t0ast.parkour.training.ParkourEnvironment;
import com.t0ast.evolution.entities.instructional.InstructionGenerator;
import com.t0ast.evolution.entities.instructional.InstructionalEntityGenerator;
import com.t0ast.evolution.entities.instructional.InstructionalMutator;
import com.t0ast.evolution.misc.selectors.EqualRandomListElementSelector;
import com.t0ast.evolution.misc.selectors.FirstListElementSelector;
import com.t0ast.evolution.training.trainers.CumulativeTrainer;
import com.t0ast.evolution.training.trainers.IndividualTrainer;
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
        ParkourEnvironment env = getTrainingEnv();
        ParkourFitnessRater rater = new ParkourFitnessRater();
        InstructionGenerator ig = new InstructionGenerator();
        ig.registerInstruction(MoveInstruction.class);
        ig.registerInstruction(TurnInstruction.class);
        EvolvingPool<ParkourEntity, ParkourResults> pool = new EvolvingPool<>(new CumulativeTrainer<>(env, rater),
        100, 50, new FirstListElementSelector(), new EqualRandomListElementSelector(),
        new InstructionalMutator<>(ig, new EqualRandomListElementSelector()), EvolvingPool.MutationType.SINGLE_PARENT,
        new InstructionalEntityGenerator<>(new ParkourEntity(), ig));
        pool.initialize();
        for(int i = 0; i < 10000; i++)
        {
            pool.nextGen();
        }
        pool.getEntities().stream().map(Entity::getFitness).forEach(System.out::println);
    }

    private ParkourEnvironment getTrainingEnv()
    {
        return new ParkourEnvironment(300, 100,
        new Polygon(new float[]
        {
            100, 50,
            100, 95,
            250, 70,
            220, 50
        }));
    }
}
