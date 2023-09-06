package com.example.domaci2.obstacles;

import com.example.domaci2.utility.Position;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class YellowDiamondObstacle extends ObstacleBody
{
    private static final Color DEFAULT_OBSTACLE_COLOR = Color.WHITESMOKE;
    private static final PhongMaterial OBSTACLE_MATERIAL = new PhongMaterial(DEFAULT_OBSTACLE_COLOR);

    private static final double DEFAULT_OBSTACLE_DIMENSION = 28.0;

    private Group body;

    public YellowDiamondObstacle(Position position)
    {
        super(position);
    }
    private double BODY_HEIGHT = 28.0;

    @Override
    protected void createObstacleBody()
    {
        body = new Group();

        float[] points = {
                0, 0, 0,            // Top vertex
                -10, 30, 10,        // Bottom-left front vertex
                10, 30, 10,         // Bottom-right front vertex
                10, 30, -10,        // Bottom-right back vertex
                -10, 30, -10        // Bottom-left back vertex
        };

        int[] faces = {
                0, 0,  1, 0,  2, 0,  // Front face
                0, 0,  2, 0,  3, 0,  // Right face
                0, 0,  3, 0,  4, 0,  // Back face
                0, 0,  4, 0,  1, 0,  // Left face
                1, 0,  2, 0,  4, 0,  // Bottom face
                4, 0,  3, 0,  2, 0   // Bottom face
        };

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().setAll(points);
        mesh.getTexCoords().addAll(0, 0);
        mesh.getFaces().setAll(faces);

        MeshView upperPart = new MeshView(mesh);
        upperPart.setDrawMode(DrawMode.FILL);
        upperPart.setCullFace(CullFace.NONE);
        upperPart.setRotationAxis(Rotate.Y_AXIS);
        upperPart.setRotate(45);

        // second part

        float[] points1 = {
                0, 60, 0,           // Top vertex
                -10, 30, 10,        // Bottom-left front vertex
                10, 30, 10,         // Bottom-right front vertex
                10, 30, -10,        // Bottom-right back vertex
                -10, 30, -10        // Bottom-left back vertex
        };

        int[] faces1 = {
                0, 0,  1, 0,  2, 0,  // Front face
                0, 0,  2, 0,  3, 0,  // Right face
                0, 0,  3, 0,  4, 0,  // Back face
                0, 0,  4, 0,  1, 0,  // Left face
                1, 0,  3, 0,  2, 0,  // Bottom face (reversed)
                4, 0,  3, 0,  1, 0   // Bottom face (reversed)
        };


        TriangleMesh mesh1 = new TriangleMesh();
        mesh1.getPoints().setAll(points1);
        mesh1.getTexCoords().addAll(0, 0);
        mesh1.getFaces().setAll(faces1);

        MeshView lowerPart = new MeshView(mesh1);
        lowerPart.setDrawMode(DrawMode.FILL);
        lowerPart.setCullFace(CullFace.NONE);
        lowerPart.setRotationAxis(Rotate.Y_AXIS);
        lowerPart.setRotate(45);


        upperPart.setTranslateY(-50);
        lowerPart.setTranslateY(-50);

        PhongMaterial greenMaterial = new PhongMaterial(Color.YELLOW);
        upperPart.setMaterial(greenMaterial);
        lowerPart.setMaterial(greenMaterial);


        Group diamond = new Group(upperPart, lowerPart);

        Rotate rotation = new Rotate(0, Rotate.Y_AXIS);
        diamond.getTransforms().add(rotation);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            rotation.setAngle(rotation.getAngle() + 1);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        body.getChildren().addAll(diamond);

        this.getChildren().add(body);
    }

    @Override
    public double getObstacleHeight()
    {
        return BODY_HEIGHT;
    }

}
