
package simulation.strategy;

import simulation.model.Animal;
import simulation.model.Grass;
import simulation.model.Rabbit;
import simulation.model.Wolf;
import simulation.model.World;

import java.util.Random;

public class RabbitEscapeStrategy implements MovementStrategy {

    private final Random random = new Random();
    private final RandomMovementStrategy fallback =
            new RandomMovementStrategy();

    @Override
    public void move(Animal animal, World world) {

        if (!(animal instanceof Rabbit rabbit) || !rabbit.isAlive()) {
            return;
        }

        Wolf threat = null;
        int closest = Integer.MAX_VALUE;

        // Look for wolves within 3 cells.
        for (Wolf wolf : world.getWolves()) {
            if (!wolf.isAlive()) continue;

            int distance = distanceSquared(
                    rabbit.getX(), rabbit.getY(),
                    wolf.getX(), wolf.getY());

            if (distance <= 9 && distance < closest) {
                closest = distance;
                threat = wolf;
            }
        }

        if (threat != null) {
            escape(rabbit, threat, world);
        } else if (rabbit.isHungry()) {
            findFood(rabbit, world);
        } else {
            fallback.move(rabbit, world);
        }
    }

    private void escape(Rabbit rabbit, Wolf wolf, World world) {

        int bestX = rabbit.getX();
        int bestY = rabbit.getY();
        int bestDistance = distanceSquared(
                bestX, bestY, wolf.getX(), wolf.getY());

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                int x = rabbit.getX() + dx;
                int y = rabbit.getY() + dy;

                if (!world.isInsideWorld(x, y)) continue;

                int distance = distanceSquared(
                        x, y, wolf.getX(), wolf.getY());

                if (distance > bestDistance) {
                    bestDistance = distance;
                    bestX = x;
                    bestY = y;
                }
            }
        }

        rabbit.moveTo(bestX, bestY);
    }

    private void findFood(Rabbit rabbit, World world) {

        int targetX = rabbit.getX();
        int targetY = rabbit.getY();
        int closest = Integer.MAX_VALUE;

        // Search for grass within 5 cells.
        for (int dx = -5; dx <= 5; dx++) {
            for (int dy = -5; dy <= 5; dy++) {

                int x = rabbit.getX() + dx;
                int y = rabbit.getY() + dy;

                if (!world.isInsideWorld(x, y)) continue;

                Grass grass = world.getGrassAt(x, y);

                if (grass == null || !grass.isAvailable()) continue;

                int distance = dx * dx + dy * dy;

                if (distance < closest) {
                    closest = distance;
                    targetX = x;
                    targetY = y;
                }
            }
        }

        if (closest == Integer.MAX_VALUE) {
            fallback.move(rabbit, world);
            return;
        }

        int newX = rabbit.getX() + Integer.compare(
                targetX, rabbit.getX());
        int newY = rabbit.getY() + Integer.compare(
                targetY, rabbit.getY());

        rabbit.moveTo(newX, newY);
    }

    private int distanceSquared(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return dx * dx + dy * dy;
    }
}
