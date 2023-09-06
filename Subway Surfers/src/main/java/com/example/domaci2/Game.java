package com.example.domaci2;

import java.util.List;
import java.util.Random;

import com.example.domaci2.objects.*;
import com.example.domaci2.utility.Position;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application 
{
    private static final double WINDOW_WIDTH = 720.0;
    private static final double WINDOW_HEIGHT = 400.0;
    
    private static final double OBSTACLE_SPAWN_DEPTH = 1200.0;
    
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.CADETBLUE;
    
    private static final int DEFAULT_OBSTACLE_TARGET_COUNT = 5;
    private static final long DEFAULT_OBSTACLE_CREATION_SPEED = 1500000000l;

    private Group objects;
    private Scene scene;
    private Stage stage;
    
    private Player player;
    private Track track;
    
    private long lastObstacleCreatedTime = 0;
    private int obstacleCount = 0;
	private int diamondCount = 0;
    private int targetObstacleCount = DEFAULT_OBSTACLE_TARGET_COUNT;
    private long obstacleCreationSpeed = DEFAULT_OBSTACLE_CREATION_SPEED;
    
    private static boolean isGameActive = true;
    
    private final UpdateTimer timer = new UpdateTimer();

    private class UpdateTimer extends AnimationTimer 
    {
        @Override
        public void handle(long now) 
        {
            updateObstacles(now);
        }
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		stage = primaryStage;
		
		setupScene();
        showStage();
	}

	static PointLight pointLight;
	private int points = 0;
	private int seconds = 0;
	private int minutes = 0;

	private Timeline timeline;
	private Label lifesLabel;

	
	private void setupScene() 
	{
		Group root = new Group();

		scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);

		objects = new Group();

		magnetSphere.setVisible(false);
		objects.getChildren().add(magnetSphere);
		
		scene.setFill(DEFAULT_BACKGROUND_COLOR);       
		scene.setCursor(Cursor.NONE);

		player = Player.InstantiatePlayer();
		scene.setCamera(player.getCamera());
        scene.setOnMouseMoved(player);
        scene.setOnKeyPressed(player);
        scene.setOnKeyReleased(player);
        
        track = new Track();

		AmbientLight ambientLight = new AmbientLight(Color.WHITE);
		ambientLight.setOpacity(0.2);
		ambientLight.setTranslateZ(-1000);
		ambientLight.setBlendMode(BlendMode.SOFT_LIGHT);

		//point light
		pointLight = new PointLight(Color.WHITE);
		pointLight.setTranslateY(-130);
		pointLight.setLightOn(false);

		objects.getChildren().add(pointLight);

		//timeline
		Label timeLabel = new Label("00:00");
		timeLabel.setTranslateX(65);
		timeLabel.setTranslateY(-50);
		timeLabel.setTranslateZ(130);


		Label pointsLabel = new Label("Points: 0");
		pointsLabel.setTranslateX(-100);
		pointsLabel.setTranslateY(-50);
		pointsLabel.setTranslateZ(130);


		timeLabel.setTextFill(Color.BLACK);
		pointsLabel.setTextFill(Color.BLACK);

		timeline = new Timeline(
				new KeyFrame(Duration.seconds(1), event -> {
					seconds++;
					if (seconds == 60) {
						seconds = 0;
						minutes++;
						if(gameSpeed < 10){
							gameSpeed += 1;
						}
					}

					//gameSpeed += 1;

					points+= pointsIncrement;

					timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
					pointsLabel.setText("Points: " + points);
				})
		);
		timeline.setCycleCount(Animation.INDEFINITE);

		StackPane overlayPane = new StackPane();
		overlayPane.getChildren().addAll(timeLabel, pointsLabel);

		//lifes
		Label lifesBackgroundLabel = new Label("+++");
		lifesBackgroundLabel.setTranslateX(-100);
		lifesBackgroundLabel.setTranslateY(-40);
		lifesBackgroundLabel.setTranslateZ(130);

		lifesLabel = new Label("+");
		lifesLabel.setTranslateX(-100);
		lifesLabel.setTranslateY(-40);
		lifesLabel.setTranslateZ(130);

		lifesBackgroundLabel.setTextFill(Color.RED);
		lifesLabel.setTextFill(Color.GREEN);

		overlayPane.getChildren().addAll(lifesBackgroundLabel, lifesLabel);
		updateLifesLabel();

		objects.getChildren().addAll(player, track, ambientLight);

		root.getChildren().addAll(objects, overlayPane);

		timeline.play();
	}

	public static void toggleLight(){
		pointLight.setLightOn(!pointLight.isLightOn());
	}
	
	private void showStage() 
	{
		stage.setTitle("Trka sa preprekama");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
		
		timer.start();
	}


	private int lifes = 1;
	private int pointsIncrement = 1;
	private boolean blazeOn = false;

	private double gameSpeed = 4.0;

	private Sphere magnetSphere = new Sphere(100);
	private boolean magnetOn = false;

	private void updateObstacles(long now)
	{		
		List<Node> children = objects.getChildren();
		 
		for (int i = 0; i < objects.getChildren().size(); i++) 
		{
			Node child = children.get(i);
			if (child instanceof Obstacle)
			{
	            if (child.getBoundsInParent().intersects((player.localToScene(player.getParentBounds())))) 
	            {
	            	//isGameActive = false;
					lifes--;
					updateLifesLabel();

					if(lifes <= 0){
						pointLight.setColor(Color.RED);
						pointLight.setLightOn(true);
						isGameActive = false;
						timeline.stop();
					}else{
						objects.getChildren().remove(child);
						obstacleCount--;
					}

	                return;
	            }
	            
				if (obstacleCount > 0 && !((Obstacle)child).move())
				{
					obstacleCount--;
					objects.getChildren().remove(child);
				}
			}

			if(child instanceof Diamond){
				if (child.getBoundsInParent().intersects((player.localToScene(player.getParentBounds()))) || (magnetOn && child.getBoundsInParent().intersects(magnetSphere.getBoundsInLocal())))
				{
					objects.getChildren().remove(child);
					diamondCount--;
					points += pointsIncrement;

					return;
				}

				if (diamondCount > 0 && !((Diamond)child).move())
				{
					diamondCount--;
					objects.getChildren().remove(child);
				}
			}

			if(child instanceof YellowDiamond){
				if (child.getBoundsInParent().intersects((player.localToScene(player.getParentBounds())))  || (magnetOn && child.getBoundsInParent().intersects(magnetSphere.getBoundsInLocal())))
				{
					objects.getChildren().remove(child);
					diamondCount--;

					Timeline startTimeline = new Timeline(
							new KeyFrame(Duration.millis(1), e -> {
								pointsIncrement = 2;
								//System.out.println("Value set to: " + pointsIncrement);
							})
					);

					Timeline revertTimeline = new Timeline(
							new KeyFrame(Duration.seconds(10), e -> {
								pointsIncrement = 1;
								//System.out.println("Value reverted back to: " + pointsIncrement);
								startTimeline.stop();
							})
					);

					startTimeline.setCycleCount(1);
					revertTimeline.setCycleCount(1);

					startTimeline.setOnFinished(e -> revertTimeline.play());
					startTimeline.play();

					return;
				}

				if (diamondCount > 0 && !((YellowDiamond)child).move())
				{
					diamondCount--;
					objects.getChildren().remove(child);
				}
			}

			if(child instanceof Life){
				if (child.getBoundsInParent().intersects((player.localToScene(player.getParentBounds())))  || (magnetOn && child.getBoundsInParent().intersects(magnetSphere.getBoundsInLocal())))
				{
					objects.getChildren().remove(child);
					diamondCount--;

					if(lifes < 3){
						lifes++;
						updateLifesLabel();
					}

					return;
				}

				if (diamondCount > 0 && !((Life)child).move())
				{
					diamondCount--;
					objects.getChildren().remove(child);
				}
			}

			if(child instanceof Blaze){
				if (child.getBoundsInParent().intersects((player.localToScene(player.getParentBounds())))  || (magnetOn && child.getBoundsInParent().intersects(magnetSphere.getBoundsInLocal())))
				{
					objects.getChildren().remove(child);
					diamondCount--;

					Timeline startTimeline = new Timeline(
							new KeyFrame(Duration.millis(1), e -> {
								blazeOn = true;
								player.flyUp();
							})
					);

					Timeline revertTimeline = new Timeline(
							new KeyFrame(Duration.seconds(10), e -> {
								blazeOn = false;
								player.flyDown();
								startTimeline.stop();
							})
					);

					startTimeline.setCycleCount(1);
					revertTimeline.setCycleCount(1);

					startTimeline.setOnFinished(e -> revertTimeline.play());
					startTimeline.play();

					return;
				}

				if (diamondCount > 0 && !((Blaze)child).move())
				{
					diamondCount--;
					objects.getChildren().remove(child);
				}
			}
			if(child instanceof Magnet){
				if (child.getBoundsInParent().intersects((player.localToScene(player.getParentBounds())))  || (magnetOn && child.getBoundsInParent().intersects(magnetSphere.getBoundsInLocal())))
				{
					objects.getChildren().remove(child);
					diamondCount--;

					Timeline startTimeline = new Timeline(
							new KeyFrame(Duration.millis(1), e -> {
								magnetOn = true;
							})
					);

					Timeline revertTimeline = new Timeline(
							new KeyFrame(Duration.seconds(10), e -> {
								magnetOn = false;
								startTimeline.stop();
							})
					);


					startTimeline.setCycleCount(1);
					revertTimeline.setCycleCount(1);

					startTimeline.setOnFinished(e -> revertTimeline.play());
					startTimeline.play();

					return;
				}

				if (diamondCount > 0 && !((Magnet)child).move())
				{
					diamondCount--;
					objects.getChildren().remove(child);
				}
			}

		}
		
		if (obstacleCount < targetObstacleCount && now > lastObstacleCreatedTime + obstacleCreationSpeed) 
		{
			lastObstacleCreatedTime = now;

			double obstacleRandomX = track.getRandomX();
			double diamondRandomX = track.getRandomX();

			while(obstacleRandomX == diamondRandomX){
				diamondRandomX = track.getRandomX();
			}

			Obstacle obstacle = new Obstacle(new Position(obstacleRandomX, track.getY(), OBSTACLE_SPAWN_DEPTH));
			obstacle.setObstacleSpeed(gameSpeed);
			objects.getChildren().add(obstacle);



			Random random = new Random();
			int randomNumber = random.nextInt(10) + 1;

			if(randomNumber <= 5){
				if(blazeOn){
					//objects.getChildren().add(new Diamond(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH)));

					Diamond diamond = new Diamond(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH));
					diamond.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(diamond);

				}else{
					Diamond diamond = new Diamond(new Position(diamondRandomX, track.getY(), OBSTACLE_SPAWN_DEPTH));
					diamond.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(diamond);
				}


				diamondCount++;
			}
			if(randomNumber == 6){
				if(blazeOn){
					//objects.getChildren().add(new Diamond(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH)));

					Magnet magnet = new Magnet(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH));
					magnet.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(magnet);

				}else{
					Magnet magnet = new Magnet(new Position(diamondRandomX, track.getY(), OBSTACLE_SPAWN_DEPTH));
					magnet.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(magnet);
				}

				diamondCount++;
			}
			if(randomNumber > 6 && randomNumber <= 8){
				if(blazeOn){
					//objects.getChildren().add(new Life(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH)));

					Life life = new Life(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH));
					life.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(life);
				}else {
					//objects.getChildren().add(new Life(new Position(diamondRandomX, track.getY(), OBSTACLE_SPAWN_DEPTH)));

					Life life = new Life(new Position(diamondRandomX, track.getY(), OBSTACLE_SPAWN_DEPTH));
					life.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(life);
				}
				diamondCount++;
			}
			if(randomNumber == 9){ // blaze
				if(blazeOn){
					//objects.getChildren().add(new Diamond(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH)));

					Diamond diamond = new Diamond(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH));
					diamond.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(diamond);

				} else{
					//objects.getChildren().add(new Blaze(new Position(diamondRandomX, track.getY(), OBSTACLE_SPAWN_DEPTH)));

					Blaze blaze = new Blaze(new Position(diamondRandomX, track.getY(), OBSTACLE_SPAWN_DEPTH));
					blaze.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(blaze);
				}

				diamondCount++;
			}
			if(randomNumber > 9){
				if(blazeOn){
					//objects.getChildren().add(new YellowDiamond(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH)));

					YellowDiamond yellowDiamond = new YellowDiamond(new Position(diamondRandomX, track.getY() - 100, OBSTACLE_SPAWN_DEPTH));
					yellowDiamond.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(yellowDiamond);
				} else{
					//objects.getChildren().add(new YellowDiamond(new Position(diamondRandomX, track.getY(), OBSTACLE_SPAWN_DEPTH)));

					YellowDiamond yellowDiamond = new YellowDiamond(new Position(diamondRandomX, track.getY(), OBSTACLE_SPAWN_DEPTH));
					yellowDiamond.setObstacleSpeed(gameSpeed);
					objects.getChildren().add(yellowDiamond);
				}

				diamondCount++;
			}

			obstacleCount++;
		}
	}

	private void updateLifesLabel(){

		switch (lifes) {
			case 0 -> lifesLabel.setTextFill(Color.RED);
			case 1 -> lifesLabel.setText("+     ");
			case 2 -> {lifesLabel.setText("++   ");}
			case 3 -> lifesLabel.setText("+++");
			default -> lifesLabel.setText("+     ");
		}

	}
	
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    public static boolean isGameActive() 
    {
    	return isGameActive;
    }
}
