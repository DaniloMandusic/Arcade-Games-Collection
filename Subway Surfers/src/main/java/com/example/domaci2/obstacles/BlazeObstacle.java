package com.example.domaci2.obstacles;

import com.example.domaci2.utility.Position;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class BlazeObstacle extends ObstacleBody
{
    private static final Color DEFAULT_OBSTACLE_COLOR = Color.WHITESMOKE;
    private static final PhongMaterial OBSTACLE_MATERIAL = new PhongMaterial(DEFAULT_OBSTACLE_COLOR);

    private static final double DEFAULT_OBSTACLE_DIMENSION = 28.0;

    private Group body;

    public BlazeObstacle(Position position)
    {
        super(position);
    }
    private double BODY_HEIGHT = 28.0 + 15; // +15 to lift plus up a bit

    @Override
    protected void createObstacleBody()
    {
        body = new Group();

        Box box1 = new Box(10, 28, 10);
        box1.setMaterial(new PhongMaterial(Color.YELLOW));

        Box box2 = new Box(28, 10, 10);
        box2.setMaterial(new PhongMaterial(Color.YELLOW));


        Group plus = new Group(box1, box2);

        Rotate rotation = new Rotate(0, Rotate.Y_AXIS);
        plus.getTransforms().add(rotation);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            rotation.setAngle(rotation.getAngle() + 1);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        body.getChildren().addAll(plus);

        this.getChildren().add(body);
    }

    @Override
    public double getObstacleHeight()
    {
        return BODY_HEIGHT;
    }

}
