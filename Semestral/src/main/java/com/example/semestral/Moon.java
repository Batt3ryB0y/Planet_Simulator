package com.example.semestral;

import javafx.scene.image.Image;

public class Moon {
    /**
     * @var parent the parent planet
     * @var distance_from_planet the distance of the moon from the planet
     * @var size the size of the moon
     * @var ID the ID of the moon
     * @var image the image of the moon
     * @var angle the angle of the moon
     */
    Planet parent;
    int distance_from_planet;
    int size;
    int ID;
    Image image;
    double angle = Math.random() * 2 * Math.PI;

    /**
     * @param parent the parent planet
     * @param distance_from_planet the distance of the moon from the planet
     * @param size the size of the moon
     * @param ID the ID of the moon
     */
    public Moon(Planet parent, int distance_from_planet, int size, int ID) {
        this.parent = parent;
        this.distance_from_planet = distance_from_planet;
        this.size = size;
        this.ID = ID;

        try {
            this.image = new Image("file:images/moon.png", size, size , false, false);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * moves the moon around the planet
     * the moon moves around the planet based on the velocity of the planet and the distance from the planet
     * the angle of the moon is calculated based on the velocity of the planet and the distance from the planet
     */
    public void move() {
        angle = angle + parent.velocity * ((double) 1 / distance_from_planet * ( (double) 1 / parent.mass));
        parent.x = (int) (Math.cos(angle) * distance_from_planet + parent.x);
        parent.y = (int) (Math.sin(angle) * distance_from_planet + parent.y);
    }
}
