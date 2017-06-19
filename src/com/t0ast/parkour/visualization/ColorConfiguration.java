/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t0ast.parkour.visualization;

import java.awt.Color;

/**
 *
 * @author T0astBread
 */
public class ColorConfiguration
{
    private Color background, floor, entityBody, entityHead, entityBorders;
    private Runnable updateCallback;

    public ColorConfiguration()
    {
        this.background = Color.BLACK;
        this.floor = Color.GRAY;
        this.entityBody = Color.YELLOW;
        this.entityHead = Color.ORANGE;
        this.entityBorders = Color.BLACK;
    }

    public ColorConfiguration(Runnable updateCallback)
    {
        this();
        this.updateCallback = updateCallback;
    }

    public Color getBackground()
    {
        return background;
    }

    public void setBackground(Color background)
    {
        this.background = background;
        update();
    }

    public Color getFloor()
    {
        return floor;
    }

    public void setFloor(Color floor)
    {
        this.floor = floor;
        update();
    }

    public Color getEntityBody()
    {
        return entityBody;
    }

    public void setEntityBody(Color entityBody)
    {
        this.entityBody = entityBody;
        update();
    }

    public Color getEntityHead()
    {
        return entityHead;
    }

    public void setEntityHead(Color entityHead)
    {
        this.entityHead = entityHead;
        update();
    }

    public Color getEntityBorders()
    {
        return entityBorders;
    }

    public void setEntityBorders(Color entityBorders)
    {
        this.entityBorders = entityBorders;
        update();
    }
    
    private void update()
    {
        this.updateCallback.run();
    }
}
