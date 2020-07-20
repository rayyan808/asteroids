package aoop.asteroids.view.view_models;

import aoop.asteroids.model.Bullet;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * View model for displaying bullet objects.
 */
public class BulletViewModel extends GameObjectViewModel<Bullet> {
    /**
     * Constructs the view model.
     *
     * @param gameObject The bullet to be displayed.
     */
    public BulletViewModel(Bullet gameObject) {
        super(gameObject);
    }

    /**
     * Draws the bullet that was given to this view model.
     *
     * @param graphics2D The graphics object which provides the necessary drawing methods.
     * @param location   The location at which to draw the object.
     */
    @Override
    public void draw(Graphics2D graphics2D, Point.Double location) {
        graphics2D.setColor(Color.YELLOW);
        Ellipse2D.Double bulletEllipse = new Ellipse2D.Double(location.getX() - 2.0, location.getY() - 2.0, 5.0, 5.0);
        graphics2D.draw(bulletEllipse);
    }
}
