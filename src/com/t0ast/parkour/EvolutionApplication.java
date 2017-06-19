/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.t0ast.evolution.EvolvingPool;
import com.t0ast.parkour.training.ParkourEnvironment;
import com.t0ast.evolution.entities.instructional.InstructionGenerator;
import com.t0ast.evolution.entities.instructional.InstructionalEntityGenerator;
import com.t0ast.evolution.entities.instructional.InstructionalMutator;
import com.t0ast.evolution.misc.selectors.IterativeExponentialListElementSelector;
import com.t0ast.evolution.misc.selectors.EqualRandomListElementSelector;
import com.t0ast.evolution.training.trainers.CumulativeTrainer;
import com.t0ast.parkour.instructions.MoveInstruction;
import com.t0ast.parkour.instructions.TurnInstruction;
import com.t0ast.parkour.training.ParkourEntity;
import com.t0ast.parkour.training.ParkourFitnessRater;
import com.t0ast.parkour.training.ParkourResults;
import com.t0ast.parkour.visualization.VisualizationPanel;

/**
 *
 * @author T0astBread
 */
public class EvolutionApplication
{

    private VisualizationPanel visualization;
    private EvolvingPool<ParkourEntity, ParkourResults> pool;
    private ParkourEnvironment environment;

    public EvolutionApplication(VisualizationPanel visualizationPanel)
    {
        this.visualization = visualizationPanel;
        this.environment = getTrainingEnv();
        this.environment.setVisualization(this.visualization);
        ParkourFitnessRater rater = new ParkourFitnessRater();
        InstructionGenerator ig = new InstructionGenerator();
        ig.registerInstruction(MoveInstruction.class);
        ig.registerInstruction(TurnInstruction.class);
        this.pool = new EvolvingPool<>(new CumulativeTrainer<>(this.environment, rater),
        100, 50, new IterativeExponentialListElementSelector(.8, .02), new IterativeExponentialListElementSelector(.9, .2),
        new InstructionalMutator<>(ig, new EqualRandomListElementSelector()), EvolvingPool.MutationType.SINGLE_PARENT,
        new InstructionalEntityGenerator<>(new ParkourEntity(), ig));
        this.pool.initialize();
        if(visualizationPanel != null)
        {
            visualizationPanel.setPool(this.pool);
            visualizationPanel.drawEnvironment(this.environment, this.pool.getEntities());
        }
    }

    private ParkourEnvironment getTrainingEnv()
    {
//        //Simple
//        return new ParkourEnvironment(300, 100,
//        new Vector2(290, 50),
//        new Rectangle(100, 30, 100, 50),
//        new Rectangle(260, 10, 20, 75),
//        new Rectangle(10, 0, 10, 90));
        
        //Left turn
        return new ParkourEnvironment(600, 400,
        new Vector2(600, 400),
        new Rectangle(0, 10, 400, 100),
        new Rectangle(500, 0, 100, 300),
        new Rectangle(300, 150, 200, 150))
        .addCheckpoints(new Vector2(250, 180), new Vector2(250, 350));
        
//        //Weird wall-obstacle
//        return new ParkourEnvironment(600, 400, new Vector2(580, 350),
//        new Rectangle(300, 0, 300, 300),
//        new Rectangle(0, 100, 100, 300),
//        new Rectangle(100, 130, 150, 10),
//        new Rectangle(150, 150, 150, 10));
    }

    public void doGenerations(int generations)
    {
        for(int i = 0; i < generations && !Thread.interrupted(); i++)
        {
            this.pool.nextGen();
        }
        this.visualization.drawEnvironment(this.environment, this.pool.getEntities());
    }
}
