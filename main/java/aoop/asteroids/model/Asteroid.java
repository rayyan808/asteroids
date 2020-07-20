package aoop.asteroids.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An asteroid is the object which can be destroyed by bullets, but also will destroy a player's ship if the two
 * collide. Each asteroid has a certain size, which determines its radius, as well as if any smaller asteroids spawn
 * when that one is destroyed.
 */
public class Asteroid extends GameObject {
    /**
     * By how much (both positive and negative) can the successor asteroids that spawn when an asteroid is destroyed
     * vary in velocity compared to their parent.
     */
    public static final double SUCCESSOR_VELOCITY_DIFFERENCE = 5.0;

    /**
     * The size of this asteroid.
     */
    private AsteroidSize size;

    /**
     * Constructs a new asteroid at the specified location, with specified velocities in both X and Y direction and the
     * specified radius.
     *
     * @param location the location in which to spawn an asteroid.
     * @param velocity The velocity of the asteroid.
     * @param size     The size of the asteroid.
     */
    public Asteroid(Point.Double location, Point.Double velocity, AsteroidSize size) {
        super(location, velocity, (int) size.getRadius());
        this.size = size;


    }

    /**
     * Generates some asteroids that spawn as a result of the destruction of this asteroid. Some sizes of asteroids may
     * not produce any successors because they're too small.
     *
     * @return A collection of the successors.
     */
    public Collection<Asteroid> getSuccessors() {
        Collection<Asteroid> successors = new ArrayList<>(2); // Initialize the array to a fixed capacity to improve performance.
        AsteroidSize successorSize = this.size.getSuccessorSize();
        if (successorSize != null) {
            successors.add(this.generateSuccessor());
            successors.add(this.generateSuccessor());
        }

        return successors;
    }

    /**
     * Generates a new asteroid that should be spawned when this one is destroyed.
     * <p>
     * The asteroid is created at the same location as the current one, and is one size smaller. The new asteroid's
     * velocity is set to the current asteroid's velocity, with some random speed adjustments.
     *
     * @return A newly created asteroid, if the size of this asteroid allows for successors. Otherwise null.
     */
    private Asteroid generateSuccessor() {
        if (this.size.getSuccessorSize() == null) {
            return null;
        }
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        return new Asteroid(this.getLocation(), new Point.Double(this.getVelocity().getX() + rng.nextDouble(-SUCCESSOR_VELOCITY_DIFFERENCE, SUCCESSOR_VELOCITY_DIFFERENCE), this.getVelocity().getY() + rng.nextDouble(-SUCCESSOR_VELOCITY_DIFFERENCE, SUCCESSOR_VELOCITY_DIFFERENCE)), this.size.getSuccessorSize());
    }

    @Override
    public String getType() {
        return "ASTEROID";
    }

    /**
     * @return The number of steps, or game ticks, for which this object is immune from collisions.
     */
    @Override
    protected int getDefaultStepsUntilCollisionPossible() {
        return 30;
    }
}
