package aoop.asteroids.view.view_models;

import aoop.asteroids.model.Asteroid;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * View model for displaying an asteroid object.
 */
public class AsteroidViewModel extends GameObjectViewModel<Asteroid> {
    /**
     * Constructs a new view model with the given game object.
     *
     * @param gameObject The object that will be displayed when this view model is drawn.
     */
    public AsteroidViewModel(Asteroid gameObject) {
        super(gameObject);
    }

    /**
     * Draws the game object that was given to this view model.
     *
     * @param graphics2D The graphics object which provides the necessary drawing methods.
     * @param location   The location at which to draw the object.
     */
    @Override
    public void draw(Graphics2D graphics2D, Point.Double location) {
        double radius = this.getGameObject().getRadius();
        graphics2D.setColor(Color.GRAY);
        Ellipse2D.Double asteroidEllipse = new Ellipse2D.Double(location.getX() - radius, location.getY() - radius, 2 * radius, 2 * radius);
        graphics2D.fill(asteroidEllipse);
    }
}
