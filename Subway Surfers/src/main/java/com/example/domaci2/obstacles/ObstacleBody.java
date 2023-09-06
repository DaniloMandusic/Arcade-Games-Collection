package com.example.domaci2.obstacles;


import com.example.domaci2.objects.GameObject;
import com.example.domaci2.utility.Position;

public abstract class ObstacleBody extends GameObject
{   
	public ObstacleBody(Position position)
	{
		super(position);
		this.createObstacleBody();
	}
	
	protected abstract void createObstacleBody();
	
	public abstract double getObstacleHeight();
}
