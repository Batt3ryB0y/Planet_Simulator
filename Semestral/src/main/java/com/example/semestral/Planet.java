package com.example.semestral;

import javafx.scene.image.Image;

import java.util.Random;

public class Planet {
    /**
     * @var x horizontal coordinate of the planet
     * @var y vertical coordinate of the planet
     * @var size used to calculate the size of the image to be displayed and the movement
     * @var ID ID of the planet is used to assign the correct image to the planet
     * @var image the image of the planet
     * @var velocity the velocity of the planet (used for the "speed" of the simulation)
     * @var angle the angle of the planet
     * @var distance_from_sun the distance of the planet from the sun
     * @var mass the mass of the planet
     * @var parent the parent screen
     * @var RANDOM random number generator used for the initial placement of each planet
     * @var local_velocity the local velocity of the planet
     */
    int x;
    int y;
    int size;
    int ID;
    Image image;
    double velocity = 1;
    double angle = Math.random() * 2 * Math.PI;
    int distance_from_sun;
    int mass;
    Screen parent;
    Random RANDOM = new Random();
    double local_velocity;


    /**
     * @param parent the parent screen
     * @param x horizontal coordinate of the planet
     * @param y vertical coordinate of the planet
     * @param size used to calculate the size of the image to be displayed and the movement
     * @param ID ID of the planet is used to assign the correct image to the planet
     * @param mass the mass of the planet
     * @param local_velocity the local velocity of the planet
     *      when adding a custom planet, the ID is -1, Image for the planet is selected randomly and the planet is placed on the screen
     *                       based on the x and y coordinates of the mouse click
     */
    public Planet(Screen parent, int x, int y, int size, int ID, int mass, double local_velocity) {
        this.parent = parent;
        System.out.println(ID);
        this.x = x;
        this.distance_from_sun = (parent.WIDTH / 2) - x ;
        this.y = y;
        this.size = size;
        this.ID = ID;
        this.mass = mass;
        this.local_velocity = local_velocity;
        try {
            if(ID == -1){
                this.image = new Image("file:images/planet" + RANDOM.nextInt(3) + ".png", size, size , false, false);

                angle = Math.atan2(y - (double) parent.HEIGHT / 2, x - (double) parent.WIDTH / 2);
                this.distance_from_sun = (int) Math.sqrt(Math.pow(x - (double) parent.WIDTH / 2, 2) + Math.pow(y - (double) parent.HEIGHT / 2, 2));
                return;
            }
            this.image = new Image("file:images/" + ID + ".png", size, size , false, false);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Move the planet
     * this is called in every "tick" of the simulation
     * when the ID is 0, the planet is the sun, and it is placed in the center of the screen - it does not move
     */
    public void move() {
        if(ID == 0) {
            x = parent.WIDTH / 2;
            y = parent.HEIGHT / 2;
            return;
        }
        angle = angle + local_velocity * ((double) 1 / distance_from_sun * ( (double) 1 / mass)) * velocity;
        x = (int) (Math.cos(angle) * distance_from_sun + parent.WIDTH / 2);
        y = (int) (Math.sin(angle) * distance_from_sun + parent.HEIGHT / 2);
    }
}

