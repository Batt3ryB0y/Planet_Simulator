package com.example.semestral;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.Arrays;


public class Screen extends Application {
    /**
     * @var planets array of planets
     * @var moons array of moons
     * @var ID global ID for planets, this is used to assign the correct Image to the planet
     * @var canvas the canvas where the simulation is drawn
     * @var gc the graphics context of the canvas
     * @var WIDTH the width of the canvas
     * @var HEIGHT the height of the canvas
     * @var addPlanetBool boolean to check if the user wants to add a planet
     */
    Planet[] planets = new Planet[100];
    Moon[] moons = new Moon[100];
    int ID = 0;
    Canvas canvas;
    GraphicsContext gc;
    int WIDTH = 1200;
    int HEIGHT = 900;
    boolean addPlanetBool = false;

    /**
     * @param stage initialization and the main loop of the program
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Planet simulation");
        stage.show();

        // create the initial bounding box
        HBox root = new HBox();
        Pane pane = new Pane();
        root.getChildren().add(pane);

        // set the background image and the initial background size, add the canvas to the pane and create the graphics context
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("file:images/bg.jpg"), null, null, null,
                new BackgroundSize(1920, 1080, false, false, false, false));
        pane.setBackground(new Background(backgroundImage));
        stage.show();
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);
        initPlanets();

        // listener for the width and height of the stage
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            WIDTH = newVal.intValue();
            canvas.setWidth(WIDTH);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            HEIGHT = newVal.intValue();
            canvas.setHeight(HEIGHT);
        });

        
        // the main timeline loop of the simulation
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            gc.clearRect(0, 0, WIDTH, HEIGHT);
            for(var item : planets){
                if(item != null) {
                    // move the planet by a "tick"
                    item.move();
                    // draw the orbit of the planet as a white circle
                    gc.setStroke(Color.WHITE);
                    gc.strokeOval((double) WIDTH / 2 - item.distance_from_sun,
                            (double) HEIGHT / 2 - item.distance_from_sun,
                            item.distance_from_sun * 2,
                            item.distance_from_sun * 2);
                    // draw the planet
                    gc.drawImage(item.image, item.x - ((double) item.size / 2), item.y - ((double) item.size / 2));
                }
            }
            for (var item : moons) {
                if (item != null) {
                    // move the moon by a "tick"
                    item.move();
                    // draw the moon
                    gc.drawImage(item.image, item.parent.x + (Math.cos(item.angle) * item.distance_from_planet) - ((double) item.size / 2),
                            item.parent.y + (Math.sin(item.angle) * item.distance_from_planet) - ((double) item.size / 2));
                }
            }
        }));

        // create the side panel where all the buttons and sliders are located
        Pane buttonPane = new Pane();
        buttonPane.setPrefSize(300, HEIGHT);
        buttonPane.setStyle("-fx-background-color: rgba(100, 200, 255, 0.5);");
        root.getChildren().add(buttonPane);

        // create the button for pausing the simulation
        Button pauseButton = new Button("Pause");
        pauseButton.setPrefSize(100, 20);
        pauseButton.setLayoutY(0);
        pauseButton.setLayoutX(25);
        pauseButton.setOnAction(e -> {
            if(pauseButton.getText().equals("Pause")) {
                pauseButton.setText("Play");
                timeline.pause();
            } else {
                pauseButton.setText("Pause");
                timeline.play();
            }
        });
        buttonPane.getChildren().add(pauseButton);

        // title for the "speed of the simulation" slider
        Text speedText = new Text("Speed of simulation");
        speedText.setLayoutY(40);
        speedText.setLayoutX(5);
        buttonPane.getChildren().add(speedText);

        // create the slider for changing the speed of the simulation
        Slider speedSlider = new Slider(-100, 100, 1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(10);
        speedSlider.setBlockIncrement(10);
        speedSlider.setLayoutY(40);
        buttonPane.getChildren().add(speedSlider);


        // create the button for applying the speed of the simulation
        Button speedButton = new Button("Apply speed");
        speedButton.setPrefSize(100, 20);
        speedButton.setLayoutY(75);
        speedButton.setLayoutX(25);
        speedButton.setOnAction(e -> {
            for(var item : planets){
                if(item != null) {
                    // change the velocity of each planet
                    item.velocity = speedSlider.getValue();
                }
            }
        });
        buttonPane.getChildren().add(speedButton);

        // create the button for resetting the simulation
        Button resetButton = new Button("Reset");
        resetButton.setPrefSize(100, 20);
        resetButton.setLayoutY(105);
        resetButton.setLayoutX(25);
        resetButton.setOnAction(e -> {
            // null the arrays of planets and moons, reset the ID and initialize the planets
            Arrays.fill(planets, null);
            Arrays.fill(moons, null);
            ID = 0;
            initPlanets();
            speedSlider.setValue(1);
        });
        buttonPane.getChildren().add(resetButton);


        // helping text for adding a planet
        Text text = new Text("Click on the \n screen \n to add a planet");
        text.setLayoutY(300);
        buttonPane.getChildren().add(text);
        text.setVisible(false);

        // explanation of the sliders for adding a planet
        Text planetSizeText = new Text("Planet size");
        planetSizeText.setLayoutY(180);
        planetSizeText.setLayoutX(5);
        buttonPane.getChildren().add(planetSizeText);
        planetSizeText.setVisible(false);

        // slider for the size of the planet to be added
        Slider planetSizeSlider = new Slider(0, 100, 1);
        planetSizeSlider.setShowTickLabels(true);
        planetSizeSlider.setShowTickMarks(true);
        planetSizeSlider.setMajorTickUnit(25);
        planetSizeSlider.setBlockIncrement(10);
        planetSizeSlider.setLayoutY(185);
        planetSizeSlider.setVisible(false);
        buttonPane.getChildren().add(planetSizeSlider);

        // explanation of the sliders for adding a planet
        Text planetVelocityText = new Text("Planet velocity");
        planetVelocityText.setLayoutY(240);
        planetVelocityText.setLayoutX(5);
        buttonPane.getChildren().add(planetVelocityText);
        planetVelocityText.setVisible(false);

        // slider for the velocity of the planet to be added
        Slider planetVelocitySlider = new Slider(0, 20, 1);
        planetVelocitySlider.setShowTickLabels(true);
        planetVelocitySlider.setShowTickMarks(true);
        planetVelocitySlider.setMajorTickUnit(5);
        planetVelocitySlider.setBlockIncrement(10);
        planetVelocitySlider.setLayoutY(245);
        planetVelocitySlider.setVisible(false);
        buttonPane.getChildren().add(planetVelocitySlider);

        // button for adding a planet
        Button addPlanetButton = new Button("Add planet");
        addPlanetButton.setPrefSize(100, 20);
        addPlanetButton.setLayoutY(135);
        addPlanetButton.setLayoutX(25);
        addPlanetButton.setOnAction(e -> {
            // change the visibility of the sliders and text based on the addPlanetBool
            if (addPlanetBool) {
                addPlanetBool = false;
                addPlanetButton.setText("Add planet");
                text.setVisible(false);
                planetSizeSlider.setVisible(false);
                planetVelocitySlider.setVisible(false);
                planetSizeText.setVisible(false);
                planetVelocityText.setVisible(false);
            } else {
                addPlanetBool = true;
                addPlanetButton.setText("Cancel");
                text.setVisible(true);
                planetSizeSlider.setVisible(true);
                planetVelocitySlider.setVisible(true);
                planetSizeText.setVisible(true);
                planetVelocityText.setVisible(true);
            }
        });
        buttonPane.getChildren().add(addPlanetButton);

        // mouse click listener which places a planet on the x, y coordinates of the mouse click if the addPlanetBool is true
        canvas.setOnMouseClicked(e -> {
            if (!addPlanetBool) {
                return;
            }
            planets[ID++] = new Planet(this, (int) e.getX(), (int) e.getY(), (int) planetSizeSlider.getValue(), -1, 50, planetVelocitySlider.getValue());
        });

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Initialize planets in the planet array
     * - this is done either in the beginning or when the reset button is pressed
     */
    public void initPlanets() {
        planets[0] = new Planet(this, (WIDTH / 2), (HEIGHT / 2), 100, ID++, 1, 1);             // Sun
        planets[1] = new Planet(this, (WIDTH / 2) - 60, (HEIGHT / 2), 15, ID++, 10, 1);     // Mercury
        planets[2] = new Planet(this, (WIDTH / 2) - 90, (HEIGHT / 2), 25, ID++, 20, 1);     // Venus
        planets[3] = new Planet(this, (WIDTH / 2) - 140, (HEIGHT / 2), 50, ID++, 30, 1);     // Earth
        planets[4] = new Planet(this, (WIDTH / 2) - 200, (HEIGHT / 2), 30, ID++, 40, 1);     // Mars
        planets[5] = new Planet(this, (WIDTH / 2) - 270, (HEIGHT / 2), 80, ID++, 50, 1);     // Jupiter
        planets[6] = new Planet(this, (WIDTH / 2) - 350, (HEIGHT / 2), 140, ID++, 60, 1);     // Saturn
        planets[7] = new Planet(this, (WIDTH / 2) - 410, (HEIGHT / 2), 50, ID++, 70, 1);     // Uranus
        planets[8] = new Planet(this, (WIDTH / 2) - 460, (HEIGHT / 2), 40, ID++, 100, 1);     // Neptune
        moons[9] = new Moon(planets[3], 15, 10, ID++);     // Moon
    }


}
