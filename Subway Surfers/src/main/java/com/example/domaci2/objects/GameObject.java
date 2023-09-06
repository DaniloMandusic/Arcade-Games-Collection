package com.example.domaci2.objects;

import com.example.domaci2.utility.Position;
import javafx.scene.Group;

public class GameObject extends Group 
{
    protected Position position;

    public GameObject(Position position) 
    {
        this.position = position;
    }
}
