package com.example.domaci2.objects;

import com.example.domaci2.obstacles.DefaultObstacle;
import com.example.domaci2.obstacles.HighObstacle;
import com.example.domaci2.obstacles.ObstacleBody;
import com.example.domaci2.utility.Position;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.Random;

public class Obstacle extends GameObject 
{
    private static double OBSTACLE_SPEED = 4.0;
    
	private ObstacleBody obstacleBody;

	public void setObstacleSpeed(double obstacleSpeed) {
		OBSTACLE_SPEED = obstacleSpeed;
	}

	public Obstacle(Position position)
	{
		super(position);

		Random random = new Random();

		int randomNumber = random.nextInt(2) + 1;

		System.out.println("Random number between 1 and 2: " + randomNumber);

		if(randomNumber == 2){
			obstacleBody = new HighObstacle(position);
		}else{
			obstacleBody = new DefaultObstacle(position);
		}

		
		this.setTranslateX(position.getX());
		this.setTranslateY(position.getY() - obstacleBody.getObstacleHeight() / 2);
		this.setTranslateZ(position.getZ());
		
		this.getChildren().add(obstacleBody);
	}
	
	public boolean move() 
	{
		this.setTranslateZ(this.getTranslateZ() - OBSTACLE_SPEED);
		return isOnTrack();
	}
	
	public boolean isOnTrack() 
	{
	    return this.getTranslateZ() > 0;
	}

}
