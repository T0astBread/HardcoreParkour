/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour.training;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.t0ast.evolution.entities.instructional.instructions.Instruction;
import com.t0ast.evolution.training.trainers.environments.CumulativeTrainingEnvironment;
import com.t0ast.parkour.instructions.MoveInstruction;
import com.t0ast.parkour.instructions.TurnInstruction;
import com.t0ast.parkour.visualization.VisualizationPanel;
import java.util.List;

/**
 *
 * @author T0astBread
 */
public class ParkourEnvironment implements CumulativeTrainingEnvironment<ParkourEntity, ParkourResults>
{

    public static final int MAX_CYCLES_PER_TRAINING = 100, MOVEMENT_STEPS = 100;
    public static final float MOVEMENT_STEP_SIZE = 1f/MOVEMENT_STEPS;

    private int width, height;
    private Rectangle[] obstacles;
    private VisualizationPanel visualization;

    public ParkourEnvironment(int width, int height, Rectangle... obstacles)
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

        for(ParkourEntity entity : entities)
        {
            entity.getPosition().set(0, 0);
            entity.getDirection().set(1, 1);
        }

        for(int i = 0; i < MAX_CYCLES_PER_TRAINING; i++)
        {
            int stillHaveInstructionsLeft = 0;
            for(ParkourEntity entity : entities)
            {
                if(updateCycle(entity, i))
                {
                    stillHaveInstructionsLeft++;
                }
            }
            if(stillHaveInstructionsLeft == 0)
            {
                break;
            }
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
        Vector2 finalPosition = new Vector2();
        calcFinalPos(finalPosition, entity, 0);
        
        for(float i = 0; i < 1; i += MOVEMENT_STEP_SIZE)
        {
            calcFinalPos(finalPosition, entity, i);
            if(isCollisionWithObstacles(finalPosition))
            {
                calcFinalPos(finalPosition, entity, i - MOVEMENT_STEP_SIZE);
                break;
            }
        }

        entity.getPosition().set(finalPosition);
        
        entity.getPosition().x = MathUtils.clamp(entity.getPosition().x, 0, this.width);
        entity.getPosition().y = MathUtils.clamp(entity.getPosition().y, 0, this.height);
    }
    
    private void calcFinalPos(Vector2 finalPos, ParkourEntity entity, float scl)
    {
        finalPos.set(entity.getPosition()).add(entity.getDirection().cpy().scl(scl));
        
        finalPos.x = MathUtils.clamp(finalPos.x, 0, this.width);
        finalPos.y = MathUtils.clamp(finalPos.y, 0, this.height);
    }
    
    private boolean isCollisionWithObstacles(Vector2 finalPosition)
    {
        for(Rectangle obstacle : this.obstacles)
        {
            if(intersectPointRectangle(finalPosition, obstacle))
            {
                return true;
            }
        }
        return false;
    }
    
//    private boolean intersectLineRectangle(Vector2 start, Vector2 end, Rectangle rect)
//    {
////        return intersectPointRectangle(start, rect) || intersectPointRectangle(end, rect); //NOT good: only checks first and second point, not the actual line
//        
//        
//        float px0 = start.x, py0 = start.y,
//        x0 = rect.x, x1 = x0 + rect.width,
//        y0 = rect.y, y1 = y0 + rect.height;
//        return
//        Intersector.intersectLines(px0, py0, end.x, end.y, x0, y0, x1, y0, end) ||
//        Intersector.intersectLines(px0, py0, end.x, end.y, x0, y1, x1, y1, end) ||
//        Intersector.intersectLines(px0, py0, end.x, end.y, x0, y0, x0, y1, end) ||
//        Intersector.intersectLines(px0, py0, end.x, end.y, x1, y0, x1, y1, end);
//    }
    
    private boolean intersectPointRectangle(Vector2 point, Rectangle rect)
    {
        float px = point.x, py = point.y,
        x0 = rect.x, x1 = x0 + rect.width,
        y0 = rect.y, y1 = y0 + rect.height;
        return  px >= x0 && px <= x1
        && py >= y0 && py <= y1;
    }

    private void turn(ParkourEntity entity, int amountDeg)
    {
        entity.getDirection().setAngle(entity.getDirection().angle() + amountDeg);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Rectangle[] getObstacles()
    {
        return obstacles;
    }

    public void setVisualization(VisualizationPanel visualization)
    {
        this.visualization = visualization;
    }
}
