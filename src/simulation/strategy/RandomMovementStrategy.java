package simulation.strategy;

import simulation.model.Animal;
import simulation.model.World;

import java.util.Random;

public class RandomMovementStrategy implements MovementStrategy {

    private final Random random = new Random();

    @Override
    public void move(Animal animal, World world) {

        if (!animal.isAlive()) {
            return;
        }

        int dx;
        int dy;

        // Choose a random direction.
        do {
            dx = random.nextInt(3) - 1;
            dy = random.nextInt(3) - 1;
        } while (dx == 0 && dy == 0);

        int newX = animal.getX() + dx;
        int newY = animal.getY() + dy;

        // Move only if the new position is inside the world.
        if (world.isInsideWorld(newX, newY)) {
            animal.moveTo(newX, newY);
        }
    }
}