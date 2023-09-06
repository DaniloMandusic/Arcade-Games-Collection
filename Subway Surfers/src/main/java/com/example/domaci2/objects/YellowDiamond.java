package com.example.domaci2.objects;

import com.example.domaci2.obstacles.DiamondObstacle;
import com.example.domaci2.obstacles.ObstacleBody;
import com.example.domaci2.obstacles.YellowDiamondObstacle;
import com.example.domaci2.utility.Position;
import javafx.scene.Node;

import java.util.Iterator;

public class YellowDiamond  extends GameObject {

    private static double OBSTACLE_SPEED = 4.0;

    private ObstacleBody obstacleBody;

    public YellowDiamond(Position position)
    {
        super(position);

        obstacleBody = new YellowDiamondObstacle(position);

        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY() - obstacleBody.getObstacleHeight() / 2); // const height u klasi nije updateovan, nego je custom setTranslateY setovan
        this.setTranslateZ(position.getZ());

        this.getChildren().add(obstacleBody);
    }

    public boolean move()
    {
        this.setTranslateZ(this.getTranslateZ() - OBSTACLE_SPEED);
        return isOnTrack();
    }

    public void setObstacleSpeed(double obstacleSpeed) {
        OBSTACLE_SPEED = obstacleSpeed;
    }

    public boolean isOnTrack()
    {
        return this.getTranslateZ() > 0;
    }
}
