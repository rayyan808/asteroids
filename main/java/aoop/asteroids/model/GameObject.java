package aoop.asteroids.model;

import aoop.asteroids.view.AsteroidsFrame;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * This class represents any object that is present in a game, such as a bullet, asteroid, or a player's ship. As an
 * abstract class, it provides some basic attributes that all objects in the game should have, like position and
 * velocity.
 */
public abstract class GameObject implements Serializable {
    /**
     * An x and y value pair indicating the object's current location.
     */
    protected Point.Double location;
    protected int multiplayerID;
    /**
     * An x and y value pair indicating the object's current velocity, in pixels per game tick.
     */
    protected Point.Double velocity;

    /**
     * Radius of the object.
     */
    protected double radius;

    /**
     * A flag that is set when this object collides with another. This tells the game engine that this object should be
     * removed from the game.
     */
    protected boolean destroyed;

    /**
     * The number of game ticks that must pass before this object is allowed to collide with other game objects. This
     * can also be thought of as a grace period, or temporary immunity.
     */
    private int stepsUntilCollisionPossible;

    /**
     * Constructs a new game object with the specified location, velocity and radius.
     *
     * @param locationX The object's location on the x-axis.
     * @param locationY The object's location on the y-axis.
     * @param velocityX Velocity in X direction.
     * @param velocityY Velocity in Y direction.
     * @param radius    Radius of the object.
     */
    protected GameObject(double locationX, double locationY, double velocityX, double velocityY, double radius) {
        this.location = new Point.Double(locationX, locationY);
        this.velocity = new Point.Double(velocityX, velocityY);
        this.radius = radius;
        this.stepsUntilCollisionPossible = this.getDefaultStepsUntilCollisionPossible();
        Random r = new Random();
        this.multiplayerID = r.nextInt(500);
    }

    /**
     * A convenience constructor that accepts points instead of individual coordinates.
     *
     * @param location A point representing the x- and y-coordinates of the object's location.
     * @param velocity A point representing the object's speed on both the x and y axes.
     * @param radius   The radius of the object.
     */
    protected GameObject(Point.Double location, Point.Double velocity, double radius) {
        this(location.getX(), location.getY(), velocity.getX(), velocity.getY(), radius);
    }

    /**
     * Child classes should implement this method to define what happens to an object when the game advances by one game
     * tick in the main loop. The amount of time that passes with each step should be the same, so that movement is
     * uniform even when performance may suffer.
     */
    public void nextStep() {
        this.location.x = (AsteroidsFrame.WINDOW_SIZE.width + this.location.x + this.velocity.x) % AsteroidsFrame.WINDOW_SIZE.width;
        this.location.y = (AsteroidsFrame.WINDOW_SIZE.height + this.location.y + this.velocity.y) % AsteroidsFrame.WINDOW_SIZE.height;
        if (this.stepsUntilCollisionPossible > 0) {
            this.stepsUntilCollisionPossible--;
        }
    }

    /**
     * Flags this object as destroyed, so that the game may deal with it.
     */
    public final void destroy() {
        this.destroyed = true;
    }

    /**
     * @return radius of the object in amount of pixels.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @return The current location of this object.
     */
    public Point.Double getLocation() {
        return this.location;
    }

    public void setLocation(double x, double y) {
        this.location.x = x;
        this.location.y = y;
    }

    /**
     * @return The current velocity of this object.
     */
    public Point.Double getVelocity() {
        return this.velocity;
    }

    public void setVelocity(double x, double y) {
        this.velocity.x = x;
        this.velocity.y = y;
    }

    /**
     * @return The speed of the object, as a scalar value combining the x- and y-velocities.
     */
    public double getSpeed() {
        return this.getVelocity().distance(0, 0); // A cheap trick: distance() is doing Math.sqrt(px * px + py * py) internally.
    }

    /**
     * @return true if the object is destroyed, false otherwise.
     */
    public final boolean isDestroyed() {
        return this.destroyed;
    }

    /**
     * Given some other game object, this method checks whether the current object and the given object collide with
     * each other. It does this by measuring the distance between the objects and checking whether it is larger than the
     * sum of the radii. Furthermore both objects should be allowed to collide.
     *
     * @param other The other object that it may collide with.
     * @return True if object collides with given object, false otherwise.
     */
    public boolean collides(GameObject other) {
        return this.getLocation().distance(other.getLocation()) < this.getRadius() + other.getRadius() && this.canCollide() && other.canCollide();
    }

    /**
     * @return Whether or not this object is immune from collisions.
     */
    private boolean canCollide() {
        return this.stepsUntilCollisionPossible <= 0;
    }

    /**
     * @return The number of steps, or game ticks, for which this object is immune from collisions.
     */
    protected abstract int getDefaultStepsUntilCollisionPossible();

    public String getType() {
        return "GAMEOBJECT";
    }

    public void setMultiplayerID(int ID) {
        this.multiplayerID = ID;
    }

    public int getMultiplayerID() {
        return this.multiplayerID;
    }

}
