package com.example.domaci1;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class HelloApplication extends Application {
    int windowWidth = 400;
    int windowHeight = 400;

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int ECLIPSE_WIDTH = 20;
    private static final int ECLIPSE_HEIGHT = 30;
    private static final int RECT1_WIDTH = 8;
    private static final int RECT1_HEIGHT = 20;
    private static final int RECT2_WIDTH = 20;
    private static final int RECT2_HEIGHT = 6;
    private static final int CROSS_SIZE = 50;
    private static final int RECTANGLE_THICKNESS = 6;
    private double tailDirection = 0.0;
    private static final double ROTATION_ANGLE = 10.0;

    private Group helicopter;
    private double speed = 0.0;
    private double MAX_SPEED = 20.0;
    private double MIN_SPEED = -20.0;
    private double ACCELERATION = 0.1;
    private double DECELERATION = 0.1;

    private Timeline increaseSpeedTimeline;

    private boolean movingForward = true;
    private boolean movingBackward = false;

    private Group g;

    Group heliodrom;
    Rectangle recharge;

    Group createHeliodrom(){
        int squareSize = 100;
        Rectangle square = new Rectangle(squareSize, squareSize, Color.RED);

        Circle circle1 = new Circle(squareSize/2);

        circle1.setTranslateX(squareSize/2);
        circle1.setTranslateY(squareSize/2);

        Circle circle2 = new Circle(squareSize/2-6);
        circle2.setTranslateX(squareSize/2);
        circle2.setTranslateY(squareSize/2);


        Shape cutout = Shape.subtract(square, circle1);

        cutout = Shape.union(cutout, circle2);

        Line line1 = new Line(0, 0, square.getWidth(), square.getHeight());
        line1.setStrokeWidth(4);
        cutout = Shape.subtract(cutout, line1);

        Line line2 = new Line(0, square.getWidth(), square.getWidth(), 0);
        line2.setStrokeWidth(4);
        cutout = Shape.subtract(cutout, line2);
        cutout.setFill(Color.GRAY);

        recharge = new Rectangle(10,10, Color.LIGHTGREEN);

        Group heliodrom = new Group();
        heliodrom.getChildren().addAll(cutout, recharge);

        return heliodrom;
    }

    Group createHeliodrom1(){
        int squareSize = 100;
        Rectangle square = new Rectangle(squareSize, squareSize, Color.RED);

        Circle circle1 = new Circle(squareSize/2);

        circle1.setTranslateX(squareSize/2);
        circle1.setTranslateY(squareSize/2);

        Circle circle2 = new Circle(squareSize/2-6);
        circle2.setTranslateX(squareSize/2);
        circle2.setTranslateY(squareSize/2);


        Shape cutout = Shape.subtract(square, circle1);

        cutout = Shape.union(cutout, circle2);

        Line line1 = new Line(0, 0, square.getWidth(), square.getHeight());
        line1.setStrokeWidth(4);
        cutout = Shape.subtract(cutout, line1);

        Line line2 = new Line(0, square.getWidth(), square.getWidth(), 0);
        line2.setStrokeWidth(4);
        cutout = Shape.subtract(cutout, line2);
        cutout.setFill(Color.GRAY);

        recharge1 = new Rectangle(10,10, Color.LIGHTGREEN);

        Group heliodrom = new Group();
        heliodrom.getChildren().addAll(cutout, recharge1);

        return heliodrom;
    }

    RotateTransition rt;
    public Group createHelicopter(){
        Color color = Color.BLUE;
        if(helicopterColor.equals("red")){
            color = Color.RED;
        } else if (helicopterColor.equals("yellow")) {
            color = Color.YELLOW;
        }

        Ellipse ellipse = new Ellipse();
        ellipse.setCenterX(WINDOW_WIDTH / 2);
        ellipse.setCenterY(WINDOW_HEIGHT / 2);
        ellipse.setRadiusX(ECLIPSE_WIDTH / 2);
        ellipse.setRadiusY(ECLIPSE_HEIGHT / 2);
        ellipse.setFill(Color.BLUE);

        LinearGradient linearGradient = new LinearGradient(
                0.5, 0,
                0.5, 1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.YELLOW),
                new Stop(1, color)
        );

        ellipse.setFill(linearGradient);


        Rectangle rect1 = new Rectangle();
        rect1.setWidth(RECT1_WIDTH);
        rect1.setHeight(RECT1_HEIGHT);
        rect1.setFill(color);


        rect1.setTranslateX((WINDOW_WIDTH - RECT1_WIDTH) / 2);
        rect1.setTranslateY((WINDOW_HEIGHT + ECLIPSE_HEIGHT * 2/3) / 2);

        Rectangle rect2 = new Rectangle();
        // obrnute width i height
        rect2.setWidth(RECT2_WIDTH);
        rect2.setHeight(RECT2_HEIGHT);
        rect2.setFill(color);


        rect2.setTranslateX((WINDOW_WIDTH - RECT2_WIDTH) / 2);
        rect2.setTranslateY((WINDOW_HEIGHT + ECLIPSE_HEIGHT * 5/4) / 2);


        //elise
        Rectangle verticalRectangle = new Rectangle(
                (CROSS_SIZE - RECTANGLE_THICKNESS) / 2, 0,
                RECTANGLE_THICKNESS, CROSS_SIZE);
        verticalRectangle.setFill(Color.BLACK);


        Rectangle horizontalRectangle = new Rectangle(
                0, (CROSS_SIZE - RECTANGLE_THICKNESS) / 2,
                CROSS_SIZE, RECTANGLE_THICKNESS);
        horizontalRectangle.setFill(Color.BLACK);


        Group elise = new Group(verticalRectangle, horizontalRectangle);

        rt = new
                RotateTransition(Duration.seconds(2), elise);
        rt.setFromAngle(0); rt.setToAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(Timeline.INDEFINITE);
        rt.play();

        elise.setTranslateX((WINDOW_WIDTH - CROSS_SIZE)/2);
        elise.setTranslateY((WINDOW_HEIGHT - CROSS_SIZE)/2);


        Group helicopter = new Group();
        helicopter.getChildren().addAll(ellipse, rect1, rect2, elise);

        return helicopter;
    }

    private void rotateLeft() {
        tailDirection -= ROTATION_ANGLE;
        helicopter.setRotate(tailDirection);
    }

    private void rotateRight() {
        tailDirection += ROTATION_ANGLE;
        helicopter.setRotate(tailDirection);
    }

    private void increaseSpeed() {
        speed = Math.min(speed + ACCELERATION, MAX_SPEED);

        if(speed < MAX_SPEED){
            if(speedLimiter.getTranslateY() >= -WINDOW_HEIGHT/2 - 31)
                speedLimiter.setTranslateY(speedLimiter.getTranslateY() - (WINDOW_HEIGHT/(MAX_SPEED/ACCELERATION))/2);
            else {
                System.out.println("a");
            }
        }
    }

    private void decreaseSpeed() {
        speed = Math.max(speed - DECELERATION, -MAX_SPEED);
        if(speed > -MAX_SPEED){
            if(speedLimiter.getTranslateY() < WINDOW_HEIGHT/2)
                speedLimiter.setTranslateY(speedLimiter.getTranslateY() + (WINDOW_HEIGHT/(MAX_SPEED/DECELERATION))/2);
        }
    }

    private void startMovingForward() {
        if (increaseSpeedTimeline != null) {
            increaseSpeedTimeline.stop();
        }

        if(speed <= 0){
            startMovingBackwards();
        }else{
            increaseSpeedTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> moveForward()));
            increaseSpeedTimeline.setCycleCount(Timeline.INDEFINITE);
            increaseSpeedTimeline.play();
        }

    }

    private void startMovingBackwards() {
        if (increaseSpeedTimeline != null) {
            increaseSpeedTimeline.stop();
        }

        if(speed > 0){
            startMovingForward();
        }else{
            increaseSpeedTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> moveBackward()));
            increaseSpeedTimeline.setCycleCount(Timeline.INDEFINITE);
            increaseSpeedTimeline.play();
        }


    }

    private void moveBackward() {
        toggleElise();
        if(!helicopterCanMove){
            return;
        }

        double dx = Math.cos(Math.toRadians(tailDirection + 90)) * (-speed);
        double dy = Math.sin(Math.toRadians(tailDirection + 90)) * (-speed);

        double x = helicopter.getTranslateX() + dx;
        double y = helicopter.getTranslateY() + dy;

        if(x < -WINDOW_WIDTH/2 || x > WINDOW_WIDTH/2 || y < -WINDOW_HEIGHT/2 || y > WINDOW_HEIGHT/2 || checkCollisionsWithObstacles()){
            return;
        }

        helicopter.setTranslateX(helicopter.getTranslateX() + dx);
        helicopter.setTranslateY(helicopter.getTranslateY() + dy);

        checkCollisions();
    }

    private void moveForward() {
        toggleElise();
        if(!helicopterCanMove){
            return;
        }

        double dx = -Math.cos(Math.toRadians(tailDirection + 90)) * speed;
        double dy = -Math.sin(Math.toRadians(tailDirection + 90)) * speed;

        double x = helicopter.getTranslateX() + dx;
        double y = helicopter.getTranslateY() + dy;

        if(x < -WINDOW_WIDTH/2 || x > WINDOW_WIDTH/2 || y < -WINDOW_HEIGHT/2 || y > WINDOW_HEIGHT/2 || checkCollisionsWithObstacles()){
            return;
        }

        helicopter.setTranslateX(helicopter.getTranslateX() + dx);
        helicopter.setTranslateY(helicopter.getTranslateY() + dy);

        checkCollisions();
    }

    private static final int SQUARE_SIZE = 10;
    private static final int SQUARE_COUNT = 30;


    private List<Rectangle> squares;

    private void createSquares(){
        squares = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < SQUARE_COUNT; i++) {
            Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, Color.GREEN);
            square.setTranslateX(random.nextDouble() * (WINDOW_WIDTH - SQUARE_SIZE));
            square.setTranslateY(random.nextDouble() * (WINDOW_HEIGHT - SQUARE_SIZE));
            square.setFill(Color.DARKRED);
            squares.add(square);
            g.getChildren().add(square);
        }
    }

    int squaresLeft = SQUARE_COUNT;
    private void checkCollisions() {
        Iterator<Rectangle> iterator = squares.iterator();
        while (iterator.hasNext()) {
            Rectangle square = iterator.next();
            if (square.getBoundsInParent().intersects(helicopter.getBoundsInParent())) {
                iterator.remove();
                g.getChildren().remove(square);
                squaresLeft--;
                if(squaresLeft == 0){
                    endGame("Congratulations, you won!");
                }
            }
        }
    }

    Circle speedLimiter;
    double SPEED_LIMITER_WIDTH = 10;
    public void createSpeedLimiter(){
        Rectangle rect = new Rectangle(SPEED_LIMITER_WIDTH, WINDOW_HEIGHT, Color.BLACK);
        rect.setTranslateX(WINDOW_WIDTH);

        speedLimiter = new Circle(SPEED_LIMITER_WIDTH/2, Color.RED);
        speedLimiter.setTranslateX(WINDOW_WIDTH + SPEED_LIMITER_WIDTH/2);
        speedLimiter.setCenterY(WINDOW_HEIGHT/2);

        g.getChildren().addAll(rect, speedLimiter);

    }

    Text timerText = new Text();
    long startTime;

    public void createTimer(){
        startTime = System.nanoTime();


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateTimerText();
            }
        };
        timer.start();
        timerText.setTranslateY(WINDOW_HEIGHT*5/6);

        g.getChildren().add(timerText);
    }

    private void updateTimerText() {
        long elapsedTime = (System.nanoTime() - startTime) / 1_000_000_000; // u sekundama

        long minutes = elapsedTime / 60;
        long seconds = elapsedTime % 60;

        String timeText = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeText);
        timerText.setFill(Color.BLACK);
        timerText.setLayoutX(10);
        timerText.setLayoutY(30);
    }


    private static final int OBSTACLE_SIZE = 30;
    private static final int OBSTACLE_COUNT = 4;


    private List<Rectangle> obstacles;

    private void createObstacles(){
        obstacles = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < OBSTACLE_COUNT; i++) {
            Image drvo = new Image("wood.jpg");
            ImagePattern tekstura = new ImagePattern(drvo, 0, 0, 1, 1, true);

            Rectangle square = new Rectangle(OBSTACLE_SIZE, OBSTACLE_SIZE, Color.GREEN);
            square.setTranslateX(random.nextDouble() * (WINDOW_WIDTH - OBSTACLE_SIZE));
            square.setTranslateY(random.nextDouble() * (WINDOW_HEIGHT - OBSTACLE_SIZE));
            square.setFill(tekstura);

            while(square.getBoundsInParent().intersects(heliodrom.getBoundsInParent())){
                square.setTranslateX(random.nextDouble() * (WINDOW_WIDTH - OBSTACLE_SIZE));
                square.setTranslateY(random.nextDouble() * (WINDOW_HEIGHT - OBSTACLE_SIZE));
            }

            obstacles.add(square);
            g.getChildren().add(square);
        }
    }

    private boolean checkCollisionsWithObstacles() {
        Iterator<Rectangle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Rectangle square = iterator.next();
            if (square.getBoundsInParent().intersects(helicopter.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }


    double height = 0;
    double MAX_HEIGHT = 10;
    double HEIGHT_METER_WIDTH = 10;
    Rectangle heightMeter;
    boolean isMoving = false;
    boolean goingUp = true;
    Timeline heightTimeline;

    Group g1 = new Group();

    private void createHeightMeter(){
        Rectangle grayRectangle = new Rectangle(HEIGHT_METER_WIDTH, WINDOW_HEIGHT, Color.GRAY);

        heightMeter = new Rectangle(HEIGHT_METER_WIDTH, WINDOW_HEIGHT, Color.BLUE);

        heightMeter.setTranslateY(WINDOW_HEIGHT);

        g1.getChildren().addAll(grayRectangle, heightMeter);
    }

    private void toggleMovement() {
        if (isMoving) {
            stopMovement();
            if(goingUp){
                goingUp = false;
            } else {
                goingUp = true;
            }

        } else {
            startMovement();
        }
    }

    private void startMovement() {
        isMoving = true;
        heightTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> moveRectangle()));
        heightTimeline.setCycleCount(Timeline.INDEFINITE);
        heightTimeline.play();
    }

    private void stopMovement() {
        isMoving = false;
        heightTimeline.stop();
    }

    private void moveRectangle() {
        double newY = heightMeter.getY();
        if (goingUp) {
            height += 1;
            if (height >= MAX_HEIGHT) {
                goingUp = false;
                height = MAX_HEIGHT;
            }

            if(newY + WINDOW_HEIGHT > 0){
                newY -= WINDOW_HEIGHT/MAX_HEIGHT;
            }

            if(newY + WINDOW_HEIGHT == 0){
                stopMovement();

            }

        } else {
            height -= 1;
            if (height <= 0) {
                goingUp = true;
                height = 0;
            }

            if(newY + WINDOW_HEIGHT < WINDOW_HEIGHT){
                newY += WINDOW_HEIGHT/MAX_HEIGHT;
            }

            if(newY + WINDOW_HEIGHT == WINDOW_HEIGHT){
                stopMovement();
            }
        }
        toggleElise();
        heightMeter.setY(newY);

    }

    boolean helicopterCanMove = false;

    void toggleElise(){
        if(height == 0){
            rt.stop();
            helicopterCanMove = false;

            if(water.getBoundsInParent().intersects(helicopter.getBoundsInParent())){
                //game over
                endGame("Game over, you landed in water!");
            }

        }else {
            rt.play();
            helicopterCanMove = true;
        }
    }

    Line needle;
    Rotate rotateNeedle;

    void createFuelLimiter(){
        Arc blackArc = new Arc(40, 40, 30, 30, 0, 180);
        blackArc.setType(ArcType.OPEN);
        blackArc.setFill(null);
        blackArc.setStroke(Color.BLACK);
        blackArc.setStrokeWidth(6.0);

        Arc redArc = new Arc(40, 40, 30, 30, 120, 60);
        redArc.setType(ArcType.OPEN);
        redArc.setFill(null);
        redArc.setStroke(Color.RED);
        redArc.setStrokeWidth(6.0);

        needle = new Line(40, 40, 70, 40);
        needle.setStroke(Color.BLACK);
        needle.setStrokeWidth(6.0);

        rotateNeedle = new Rotate();
        rotateNeedle.setPivotX(40);
        rotateNeedle.setPivotY(40);

        needle.getTransforms().add(rotateNeedle);

        g.getChildren().addAll(blackArc, redArc, needle);
    }

    double fuel = 100;
    double fuelDecrement = 2;

    void lowerFuel(){
        heightTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> moveNeedle()));
        heightTimeline.setCycleCount(Timeline.INDEFINITE);
        heightTimeline.play();
    }

    boolean heliodromHasRecharge = true;
    boolean gameJustStarted = true;

    boolean heliodrom1HasRecharge = true;

    private void moveNeedle() {
        if(heliodrom.getBoundsInParent().intersects(helicopter.getBoundsInParent()) && !helicopterCanMove && heliodromHasRecharge && !gameJustStarted) {
            fuel += fuelDecrement;

            if (fuel >= 100) {
                fuel = 100;
                heliodromHasRecharge = false;
                recharge.setFill(Color.RED);
            }
        } else if (heliodrom1.getBoundsInParent().intersects(helicopter.getBoundsInParent()) && !helicopterCanMove && heliodrom1HasRecharge) {
            fuel += fuelDecrement;

            if (fuel >= 100) {
                fuel = 100;
                heliodrom1HasRecharge = false;
                recharge1.setFill(Color.RED);
            }
        } else if (gameJustStarted) {
            fuel = 100;
        } else{
            fuel -= fuelDecrement;
        }

        if(fuel < 0){
            endGame("Game over, you run out of fuel!");
            return;
            // game over
        }

        rotateNeedle.setAngle(fuel/100 * 180 + 180);
    }

    Group heliodrom1;
    Rectangle recharge1;

    int dummyI = 0;
    private void startGame(){
        helicopter = createHelicopter();

        heliodrom = createHeliodrom();
        heliodrom.setTranslateX((WINDOW_WIDTH-100)/2);
        heliodrom.setTranslateY((WINDOW_HEIGHT-100)/2);

        Random random = new Random();
        double randomX =  random.nextDouble() * (WINDOW_WIDTH - 100);
        double randomY =  random.nextDouble() * (WINDOW_HEIGHT - 100);
        heliodrom.setTranslateX(randomX);
        heliodrom.setTranslateY(randomY);
        helicopter.setTranslateX(heliodrom.getTranslateX() - WINDOW_WIDTH/2 + 50);
        helicopter.setTranslateY(heliodrom.getTranslateY() - WINDOW_HEIGHT/2 + 50);

        heliodrom1 = createHeliodrom1();
        recharge1 = new Rectangle(10,10, Color.LIGHTGREEN);

        do {
            heliodrom1.setTranslateX(random.nextDouble() * (WINDOW_WIDTH - 100));
            heliodrom1.setTranslateY(random.nextDouble() * (WINDOW_HEIGHT - 200));

        } while (heliodrom1.getBoundsInParent().intersects(heliodrom.getBoundsInParent()));

        heliodrom1.getChildren().add(recharge1);


        Image trava = new Image(terrainColor);
        ImagePattern tekstura = new ImagePattern(trava, 0, 0, 1, 1, true);
        Rectangle pozadina = new Rectangle(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
        pozadina.setFill(tekstura);

        g = new Group();
        g.getChildren().addAll(pozadina, heliodrom, heliodrom1);

        createWater();

        createSquares();

        createObstacles();

        g.getChildren().add(helicopter);

        createSpeedLimiter();

        createTimer();

        g.setTranslateX(HEIGHT_METER_WIDTH);

        createHeightMeter();

        g1.getChildren().add(g);

        toggleElise();

        createFuelLimiter();

        helicopterCanMove = true;
        gameJustStarted = true;

        lowerFuel();

        Scene scene = new Scene(g1, HEIGHT_METER_WIDTH+ WINDOW_WIDTH + SPEED_LIMITER_WIDTH, WINDOW_HEIGHT);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    if(!helicopterCanMove){
                        break;
                    }

                    rotateLeft();
                    break;
                case RIGHT:
                    if(!helicopterCanMove){
                        break;
                    }

                    rotateRight();
                    break;
                case UP:
                    if(!helicopterCanMove){
                        break;
                    }

                    increaseSpeed();
                    startMovingForward();
                    break;
                case DOWN:
                    if(!helicopterCanMove){
                        break;
                    }

                    decreaseSpeed();
                    startMovingBackwards();
                    break;
                case SPACE:
                    dummyI ++;
                    if(dummyI >= 2){
                        gameJustStarted = false;
                    }

                    toggleMovement();

                    break;
                default:
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case SPACE:
                    toggleElise();
                    break;
                default:
                    break;
            }
        });

        primaryStage.setScene(scene);
    }

    Stage primaryStage;

    private void startMenu(){
        Button blueSquareButton = createHelicopterColorButton("Blue", "blue");

        Button redSquareButton = createHelicopterColorButton("Red", "red");
        redSquareButton.setTranslateX(50);

        Button yellowSquareButton = createHelicopterColorButton("Yellow", "yellow");
        yellowSquareButton.setTranslateX(100);


        Button grayCircleButton = createTerrainColorButton("Gray", "gray");
        grayCircleButton.setTranslateY(50);

        Button greenCircleButton = createTerrainColorButton("Green", "green");
        greenCircleButton.setTranslateY(50);
        greenCircleButton.setTranslateX(50);


        blueSquareButton.setOnAction(e -> { helicopterColor = "blue"; ACCELERATION = 1; DECELERATION = 1; MAX_SPEED = 10;});
        redSquareButton.setOnAction(e -> { helicopterColor = "red"; ACCELERATION = 2; DECELERATION = 2; fuelDecrement = 3; MAX_SPEED = 20;});
        yellowSquareButton.setOnAction(e -> { helicopterColor = "yellow"; ACCELERATION = 3; DECELERATION = 3; fuelDecrement = 5; MAX_SPEED = 30;});


        greenCircleButton.setOnAction(e -> terrainColor = "grass.jpg");
        grayCircleButton.setOnAction(e -> terrainColor = "concrete.jpg");


        Button startButton = new Button("Start");
        startButton.setTranslateY(100);
        startButton.setOnAction(e -> startGame());

        Group menu = new Group();
        menu.getChildren().addAll(startButton, blueSquareButton, redSquareButton, yellowSquareButton, grayCircleButton, greenCircleButton);

        Scene menuScene = new Scene(menu, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setTitle("Helicopter");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void endGame(String message){
        Group messageGroup = new Group();

        Text messageText = new Text(message);
        messageText.setTranslateY(50);
        messageText.setTranslateX(50);
        messageGroup.getChildren().add(messageText);

        Scene endScene = new Scene(messageGroup, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setScene(endScene);
    }

    String helicopterColor;
    String terrainColor;

    private Button createHelicopterColorButton(String text, String color) {
        Button button = new Button(text);
        button.setOnAction(e -> {
            helicopterColor = color;
        });
        return button;
    }

    private Button createTerrainColorButton(String text, String color) {
        Button button = new Button(text);
        button.setOnAction(e -> {
            terrainColor = color;
        });
        return button;
    }

    Rectangle water;
    private void createWater(){
        water = new Rectangle(100, 200, Color.BLUE);

        Random random = new Random();
        do {
            water.setTranslateX(random.nextDouble() * (WINDOW_WIDTH - 100));
            water.setTranslateY(random.nextDouble() * (WINDOW_HEIGHT - 200));

        } while (water.getBoundsInParent().intersects(heliodrom.getBoundsInParent()));

        g.getChildren().add(water);

    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        startMenu();

    }

    public static void main(String[] args) {
        launch();
    }
}