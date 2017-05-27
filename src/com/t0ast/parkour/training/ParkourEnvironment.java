/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour.training;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.t0ast.evolution.entities.instructional.instructions.Instruction;
import com.t0ast.evolution.training.trainers.environments.CumulativeTrainingEnvironment;
import com.t0ast.parkour.instructions.MoveInstruction;
import com.t0ast.parkour.instructions.TurnInstruction;
import com.t0ast.parkour.visualization.VisualizationPanel;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author T0astBread
 */
public class ParkourEnvironment implements CumulativeTrainingEnvironment<ParkourEntity, ParkourResults>
{

    public static final int MAX_CYCLES_PER_TRAINING = 100;

    private int width, height;
    private Polygon[] obstacles;
    private VisualizationPanel visualization;

    public ParkourEnvironment(int width, int height, Polygon... obstacles)
    {
        this.width = width;
        this.height = height;
        this.obstacles = obstacles;
    }

    @Override
    public ParkourResults[] train(List<ParkourEntity> entities)
    {
//        for(int i = 0; i < MAX_CYCLES_PER_TRAINING; i++) //Old individual update method: <code>ParkourResults train(ParkourEntity entity)</code>
//        {
//            if(i >= entity.getInstructionSize())
//            {
//                break;
//            }
//            Instruction inst = entity.getInstructions().get(i);
//            updateOneCycle(entity, inst);
//        }
//        return new ParkourResults((Math.round(entity.getPosition().x) * 1f)/this.width, entity);

        for(int i = 0; i < MAX_CYCLES_PER_TRAINING; i++)
        {
            int stillHaveInstructionsLeft = 0;
            for(ParkourEntity entity : entities)
            {
                if(updateCycle(entity, i)) stillHaveInstructionsLeft++;
            }
            if(stillHaveInstructionsLeft == 0) break;
            if(this.visualization != null)
            {
                this.visualization.drawEnvironmentUpdateStep(this, entities);
            }
        }

        return entities.stream().map(this::createResults).toArray(ParkourResults[]::new);
    }

    private ParkourResults createResults(ParkourEntity entity)
    {
        return new ParkourResults((Math.round(entity.getPosition().x) * 1f) / this.width, entity);
    }

    private boolean updateCycle(ParkourEntity entity, final int cycle)
    {
        if(cycle >= entity.getInstructionSize())
        {
            return false;
        }
        Instruction currentInst = entity.getInstructions().get(cycle);
        updateCycle(entity, currentInst);
        return true;
    }

    private void updateCycle(ParkourEntity entity, Instruction currentInst)
    {
        entity.getPosition().set(0, 0);
        if(currentInst instanceof MoveInstruction)
        {
            move(entity, ((MoveInstruction) currentInst).getValue());
        }
        else if(currentInst instanceof TurnInstruction)
        {
            turn(entity, ((TurnInstruction) currentInst).getValue());
        }
    }

    private void move(ParkourEntity entity, int distance)
    {
        entity.getDirection().nor().scl(distance);
        Vector2 finalPosition = entity.getPosition().cpy().add(entity.getDirection());

        Arrays.stream(this.obstacles).forEach(o ->
        {
            if(Intersector.intersectLinePolygon(entity.getPosition(), finalPosition, o, finalPosition))
            {
                finalPosition.sub(entity.getPosition());
            }
        });

        entity.getPosition().add(finalPosition);
        entity.getPosition().x = MathUtils.clamp(entity.getPosition().x, 0, this.width);
        entity.getPosition().y = MathUtils.clamp(entity.getPosition().y, 0, this.height);
    }

    private void turn(ParkourEntity entity, int amountDeg)
    {
        entity.getDirection().setAngle(amountDeg);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Polygon[] getObstacles()
    {
        return obstacles;
    }

    public void setVisualization(VisualizationPanel visualization)
    {
        this.visualization = visualization;
    }
}
