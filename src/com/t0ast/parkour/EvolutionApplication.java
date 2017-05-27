/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour;

import com.badlogic.gdx.math.Polygon;
import com.t0ast.evolution.EvolvingPool;
import com.t0ast.parkour.training.ParkourEnvironment;
import com.t0ast.evolution.entities.instructional.InstructionGenerator;
import com.t0ast.evolution.entities.instructional.InstructionalEntityGenerator;
import com.t0ast.evolution.entities.instructional.InstructionalMutator;
import com.t0ast.evolution.misc.selectors.EqualRandomListElementSelector;
import com.t0ast.evolution.misc.selectors.FirstListElementSelector;
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
        100, 50, new FirstListElementSelector(), new EqualRandomListElementSelector(),
        new InstructionalMutator<>(ig, new EqualRandomListElementSelector()), EvolvingPool.MutationType.SINGLE_PARENT,
        new InstructionalEntityGenerator<>(new ParkourEntity(), ig));
        this.pool.initialize();
        if(visualizationPanel != null)
        {
            visualizationPanel.drawEnvironment(this.environment, this.pool.getEntities());
        }
    }

    private ParkourEnvironment getTrainingEnv()
    {
        Polygon obst = new Polygon();
        obst.setPosition(100, 50);
        obst.setOrigin(100, 50);
        obst.setVertices(new float[]
        {
            0, 0,
            0, 45,
            150, 20,
            120, 0
        });
        return new ParkourEnvironment(300, 100, obst);
    }

    public void doGenerations(int generations)
    {
        for(int i = 0; i < generations; i++)
        {
            this.pool.nextGen();
        }
        this.visualization.drawEnvironment(this.environment, this.pool.getEntities());
    }
}
