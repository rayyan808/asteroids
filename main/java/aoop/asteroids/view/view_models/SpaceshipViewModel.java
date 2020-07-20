package aoop.asteroids.view.view_models;

import aoop.asteroids.model.Spaceship;
import aoop.asteroids.util.PolarCoordinate;

import java.awt.*;
import java.awt.geom.Path2D;

import static java.lang.Math.PI;

/**
 * View model for displaying a spaceship object.
 */
public class SpaceshipViewModel extends GameObjectViewModel<Spaceship> {
    /**
     * Constructs a new view model with the given game object.
     *
     * @param gameObject The object that will be displayed when this view model is drawn.
     */
    public SpaceshipViewModel(Spaceship gameObject) {
        super(gameObject);
    }

    /**
     * Draws the game object that was given to this view model.
     *
     * @param graphics2D The graphics object which provides the necessary drawing methods.
     * @param location   The location at which to draw the spaceship.
     */
    @Override
    public void draw(Graphics2D graphics2D, Point.Double location) {
        Spaceship spaceship = this.getGameObject();
        this.drawMainBody(spaceship, graphics2D, location);
        if (spaceship.isAccelerating()) {
            this.drawExhaust(spaceship, graphics2D, location);
        }
    }

    /**
     * Draws the main body of the spaceship as a white triangle.
     *
     * @param spaceship  The spaceship object to draw. While we could retrieve this from this.getGameObject(), it is a
     *                   little easier to read this way.
     * @param graphics2D The graphics object to use when drawing.
     * @param location   The location at which to draw the spaceship.
     */
    private void drawMainBody(Spaceship spaceship, Graphics2D graphics2D, Point.Double location) {
        Path2D.Double spaceshipMainBody = this.buildTriangle(location, spaceship.getDirection(), new PolarCoordinate(0.0 * PI, 20), new PolarCoordinate(0.8 * PI, 20), new PolarCoordinate(1.2 * PI, 20));
        // The area where the spaceship's body goes is first cleared by filling it with black, then the path is drawn.
        graphics2D.setColor(spaceship.getShipColour());
        graphics2D.fill(spaceshipMainBody);
        graphics2D.setColor(Color.WHITE);
        graphics2D.draw(spaceshipMainBody);
    }

    /**
     * Draws the exhaust of the spaceship as a small yellow triangle.
     *
     * @param spaceship  The spaceship whose exhaust to draw.
     * @param graphics2D The graphics object to use when drawing.
     * @param location   The location at which to draw the spaceship.
     */
    private void drawExhaust(Spaceship spaceship, Graphics2D graphics2D, Point.Double location) {
        Path2D.Double exhaustFlame = this.buildTriangle(location, spaceship.getDirection(), new PolarCoordinate(1.0 * PI, 25), new PolarCoordinate(0.9 * PI, 15), new PolarCoordinate(1.1 * PI, 15));
        graphics2D.setColor(Color.YELLOW);
        graphics2D.fill(exhaustFlame);
    }

    /**
     * Builds a triangle shape using a starting location, direction, and three polar coordinates that define the corners
     * of the triangle.
     *
     * @param location        The location at which to center the triangle. This can be treated as the origin for the polar
     *                        coordinates.
     * @param facingDirection The direction that the triangle is facing, in radians. This essentially works as an offset
     *                        for the angle of every point on the triangle.
     * @param a               The first coordinate.
     * @param b               The second coordinate.
     * @param c               The third coordinate.
     * @return A path representing the points identified by the three polar coordinates given.
     */
    private Path2D.Double buildTriangle(
            Point.Double location, double facingDirection, PolarCoordinate a, PolarCoordinate b, PolarCoordinate c) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(location.getX() + Math.sin(facingDirection + a.getAngle()) * a.getRadius(), location.getY() - Math.cos(facingDirection + a.getAngle()) * a.getRadius());
        path.lineTo(location.getX() + Math.sin(facingDirection + b.getAngle()) * b.getRadius(), location.getY() - Math.cos(facingDirection + b.getAngle()) * b.getRadius());
        path.lineTo(location.getX() + Math.sin(facingDirection + c.getAngle()) * c.getRadius(), location.getY() - Math.cos(facingDirection + c.getAngle()) * c.getRadius());
        path.closePath();
        return path;
    }
}
