package aoop.asteroids.model;

/**
 * The bullet is the ultimate weapon of the player. It has the same mechanics
 * as an asteroid, in which it cannot divert from its trajectory. However, the
 * bullet has the addition that it only exists for a certain amount of game
 * steps.
 */
public class Bullet extends GameObject {
    /**
     * The number of steps, or game ticks, that a bullet stays alive for, before it is destroyed.
     */
    public static final int DEFAULT_BULLET_STEP_LIFETIME = 45;

    /**
     * The amount of steps this bullet still is allowed to live. When this value drops below 0, the bullet is removed
     * from the game model.
     */
    private int stepsLeft;

    /**
     * Constructs a new bullet using the given location and velocity parameters, and a default number of steps until the
     * bullet is destroyed.
     *
     * @param locationX The location of this bullet on the x-axis.
     * @param locationY The location of this bullet on the y-axis.
     * @param velocityX velocity of the bullet as projected on the X-axis.
     * @param velocityY velocity of the bullet as projected on the Y-axis.
     */
    public Bullet(double locationX, double locationY, double velocityX, double velocityY) {
        this(locationX, locationY, velocityX, velocityY, DEFAULT_BULLET_STEP_LIFETIME);
    }

    /**
     * Constructs a new bullet with a set number of steps until it is destroyed.
     *
     * @param locationX The location of this bullet on the x-axis.
     * @param locationY The location of this bullet on the y-axis.
     * @param velocityX Velocity of the bullet as projected on the X-axis.
     * @param velocityY Velocity of the bullet as projected on the Y-axis.
     * @param stepsLeft Amount of steps the bullet is allowed to live.
     */
    private Bullet(double locationX, double locationY, double velocityX, double velocityY, int stepsLeft) {
        super(locationX, locationY, velocityX, velocityY, 0);
        this.stepsLeft = stepsLeft;
    }

    /**
     * Updates the bullet. First calls the parent's nextStep() method to update the object's location, and specifically
     * for the bullet class, there is a lifetime to the bullet, indicated by the number of steps left until it should be
     * destroyed. At each step, this value is decremented, and once it reaches zero, the bullet is destroyed.
     */
    @Override
    public void nextStep() {
        super.nextStep();

        this.stepsLeft--;
        if (this.stepsLeft <= 0) {
            this.destroy();
        }
    }

    /**
     * @return The number of steps, or game ticks, for which this object is immune from collisions.
     */
    @Override
    protected int getDefaultStepsUntilCollisionPossible() {
        return 3;
    }

    @Override
    public String getType() {
        return "BULLET";
    }

}
