
package simulation.strategy;

import simulation.model.Animal;
import simulation.model.Rabbit;
import simulation.model.Wolf;
import simulation.model.World;

public class WolfHuntStrategy implements MovementStrategy {

    private final RandomMovementStrategy fallback =
            new RandomMovementStrategy();

    @Override
    public void move(Animal animal, World world) {

        if (!(animal instanceof Wolf wolf) || !wolf.isAlive()) {
            return;
        }

        Rabbit target = null;
        int closest = Integer.MAX_VALUE;

        for (Rabbit rabbit : world.getRabbits()) {

            if (!rabbit.isAlive()) continue;

            int dx = rabbit.getX() - wolf.getX();
            int dy = rabbit.getY() - wolf.getY();
            int distance = dx * dx + dy * dy;

            // Wolf can detect rabbits within 4 cells.
            if (distance <= 16 && distance < closest) {
                closest = distance;
                target = rabbit;
            }
        }

        if (target == null) {
            fallback.move(wolf, world);
            return;
        }

        int newX = wolf.getX() + Integer.compare(
                target.getX(), wolf.getX());

        int newY = wolf.getY() + Integer.compare(
                target.getY(), wolf.getY());

        if (world.isInsideWorld(newX, newY)) {
            wolf.moveTo(newX, newY);
        }
    }
}
