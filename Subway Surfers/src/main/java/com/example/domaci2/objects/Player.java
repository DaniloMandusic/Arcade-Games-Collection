package com.example.domaci2.objects;

import com.example.domaci2.Game;
import com.example.domaci2.utility.Position;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Player extends GameObject implements EventHandler<Event> 
{
	private static final double DEFAULT_POSITION_X = 0;
	private static final double DEFAULT_POSITION_Y = 0;
	private static final double DEFAULT_POSITION_Z = 0;
	
    public static final double NEAR_CLIP = 0.1;
    public static final double FAR_CLIP = 10_000;
    public static final double FIELD_OF_VIEW = 60;
	
    private PerspectiveCamera camera;
    private Group shape;
    
    private int lane = 1;

	private Rotate xRotate;
	
	public Player(Position position)
	{
		super(position);
		
		shape = new Group();
		makeShape();

        camera = new PerspectiveCamera(true);
        camera.setNearClip(NEAR_CLIP);
        camera.setFarClip(FAR_CLIP);
        camera.setFieldOfView(FIELD_OF_VIEW);

		xRotate = new Rotate(0, Rotate.Y_AXIS);
		camera.getTransforms().addAll(xRotate);

		toggleCamera();

        this.setTranslateY(position.getY());
        
        this.getChildren().addAll(shape, camera);
	}

	double oldY = 0;
	public void flyUp(){
		oldY = this.getTranslateY();
		this.setTranslateY(-100);
	}

	public void flyDown(){
		this.setTranslateY(oldY);
	}

	private void makeShape() {
		Sphere head = new Sphere(4);
		head.setTranslateY(-20);
		head.setMaterial(new PhongMaterial(Color.ORANGE));

		Box body = new Box(2, 14, 2);
		body.setTranslateY(-12);
		body.setMaterial(new PhongMaterial(Color.ORANGE));

		Box hands = new Box (10, 2, 2);
		hands.setTranslateY(-15);
		hands.setMaterial(new PhongMaterial(Color.ORANGE));

		Box hips = new Box (6, 2, 2);
		hips.setTranslateY(-6);
		hips.setMaterial(new PhongMaterial(Color.ORANGE));

		Box leftLeg = new Box (2,6,2);
		leftLeg.setTranslateX(-2);
		leftLeg.setTranslateY(-2);
		leftLeg.setMaterial(new PhongMaterial(Color.ORANGE));

		Box rightLeg = new Box (2,6,2);
		rightLeg.setTranslateX(2);
		rightLeg.setTranslateY(-2);
		rightLeg.setMaterial(new PhongMaterial(Color.ORANGE));

		shape.getChildren().addAll(head, body, hands, hips, leftLeg, rightLeg);
		shape.setTranslateY(14);

	}

	private boolean firstPerson = true;
	private void toggleCamera(){
		if(!firstPerson){
			camera.setTranslateZ(-100);
			camera.setTranslateY(-30);
		}else{
			camera.setTranslateZ(0);
			camera.setTranslateY(0);
		}
	}

	private void setFirstPerson(){
		firstPerson = true;
		toggleCamera();
	}

	private void setThirdPerson(){
		firstPerson = false;
		toggleCamera();
	}
	
	public static Player InstantiatePlayer() 
	{
		return new Player(new Position(DEFAULT_POSITION_X,
									   DEFAULT_POSITION_Y,
									   DEFAULT_POSITION_Z));
	}

	@Override
	public void handle(Event event) 
	{		
		if (event instanceof KeyEvent) 
		{
			KeyEvent keyEvent = (KeyEvent)event;
	        if (keyEvent.getCode() == KeyCode.ESCAPE &&
	        	keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
	        {
	            System.exit(0);
	        }
	        else 
	        {
	    		if (!Game.isGameActive())
	    		{
	    			return;
	    		}
	    		
		        if ((keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT) &&
			        	  keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
		        {
		        	moveLeft();
		        }
		        else if ((keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT) &&
			        	  keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
		        {
		        	moveRight();
		        }
				else if ((keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP) &&
						keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
				{
					jump();
				}
				else if ((keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN) &&
						keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
				{
					downJump();
				}
				else if ((keyEvent.getCode() == KeyCode.L) &&
						keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
				{
					Game.toggleLight();
				}
				else if ((keyEvent.getCode() == KeyCode.T) &&
						keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
				{
					xRotate.setAngle(15);
				}
				else if ((keyEvent.getCode() == KeyCode.R) &&
						keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
				{
					xRotate.setAngle(-15);
				}
				else if ((keyEvent.getCode() == KeyCode.T || keyEvent.getCode() == KeyCode.R) &&
						keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
				{
					xRotate.setAngle(0);
				}
				else if ((keyEvent.getCode() == KeyCode.NUMPAD1) &&
						keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
				{
					setFirstPerson();
				}
				else if ((keyEvent.getCode() == KeyCode.NUMPAD2) &&
						keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
				{
					setThirdPerson();
				}

	        }
		}
	}
	
	public Bounds getParentBounds() 
	{
		return this.shape.getBoundsInParent();
	}
	
    public Camera getCamera() 
    {
        return camera;
    }
    
    private void moveLeft() 
    {
    	if (lane == 0) 
    	{
    		return;
    	}
    	
    	lane--;
    	this.setTranslateX(this.getTranslateX() - Track.LANE_WIDTH);
    }
    
    private void moveRight() 
    {
    	if (lane == 2) 
    	{
    		return;
    	}
    	
    	lane++;
    	this.setTranslateX(this.getTranslateX() + Track.LANE_WIDTH);
    }


	private TranslateTransition moveUpTransition = new TranslateTransition(Duration.seconds(1), this);;
	private TranslateTransition moveDownTransition = new TranslateTransition(Duration.seconds(1), this);;
	private boolean inJump = false;
	private boolean inDoubleJump = false;
	private void jump(){

		if(this.getTranslateY() == 0){
			inJump = false;
			inDoubleJump = false;
		}else{
			if(inJump && inDoubleJump){
				return;
			}

		}

		if(!inJump){
			//standard jump
			inJump = true;

			moveUpTransition.stop();
			moveDownTransition.stop();

			moveUpTransition.setByY(-50);
			moveDownTransition.setByY(50);

			moveUpTransition.setOnFinished(event -> moveDownTransition.play());
			moveUpTransition.play();

		} else if (inJump && !inDoubleJump) {
			inDoubleJump = true;

			moveUpTransition.stop();
			moveDownTransition.stop();

			moveUpTransition.setByY(-50);
			moveDownTransition.setByY(50 - this.getTranslateY());

			moveUpTransition.setOnFinished(event -> moveDownTransition.play());
			moveUpTransition.play();

		}else if(inDoubleJump){
			return;
		}

	}

	private void downJump(){

		inJump = false;
		inDoubleJump = false;

		moveUpTransition.stop();
		moveDownTransition.stop();

		moveDownTransition.setByY(50 - this.getTranslateY() - 34 - 10);

		moveDownTransition.play();

	}

}
