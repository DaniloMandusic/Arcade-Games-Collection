package com.example.domaci2.obstacles;

import com.example.domaci2.utility.Position;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class DefaultObstacle extends ObstacleBody 
{
    private static final Color DEFAULT_OBSTACLE_COLOR = Color.WHITESMOKE;
    private static final PhongMaterial OBSTACLE_MATERIAL = new PhongMaterial(DEFAULT_OBSTACLE_COLOR);
    
    private static final double DEFAULT_OBSTACLE_DIMENSION = 28.0;
    
    private Group body;
    
	public DefaultObstacle(Position position)
	{
		super(position);
	}
	private double BODY_HEIGHT = 28.0;

	@Override
	protected void createObstacleBody() 
	{

		body = new Group();

		Box leftPart = new Box(4, DEFAULT_OBSTACLE_DIMENSION, 4);
		leftPart.setMaterial(OBSTACLE_MATERIAL);
		leftPart.setTranslateX(-12);

		Box rightPart = new Box(4, DEFAULT_OBSTACLE_DIMENSION, 4);
		rightPart.setMaterial(OBSTACLE_MATERIAL);
		rightPart.setTranslateX(12);

		Box part1 = new Box(4,4,4);
		part1.setMaterial(new PhongMaterial(Color.RED));
		part1.setTranslateY(-DEFAULT_OBSTACLE_DIMENSION/2 + 2);
		part1.setTranslateX(-8);

		Box part2 = new Box(4,4,4);
		part2.setMaterial(OBSTACLE_MATERIAL);
		part2.setTranslateY(-DEFAULT_OBSTACLE_DIMENSION/2 + 2);
		part2.setTranslateX(-4);

		Box part3 = new Box(4,4,4);
		part3.setMaterial(new PhongMaterial(Color.RED));
		part3.setTranslateY(-DEFAULT_OBSTACLE_DIMENSION/2 + 2);

		Box part4 = new Box(4,4,4);
		part4.setMaterial(OBSTACLE_MATERIAL);
		part4.setTranslateY(-DEFAULT_OBSTACLE_DIMENSION/2 + 2);
		part4.setTranslateX(4);

		Box part5 = new Box(4,4,4);
		part5.setMaterial(new PhongMaterial(Color.RED));
		part5.setTranslateY(-DEFAULT_OBSTACLE_DIMENSION/2 + 2);
		part5.setTranslateX(8);

		body.getChildren().addAll(leftPart, rightPart, part1, part2, part3, part4, part5);

		this.getChildren().add(body);
	}

	@Override
	public double getObstacleHeight() 
	{
		return BODY_HEIGHT;
	}
	
}
