package simulation.strategy;

import simulation.model.Animal;
import simulation.model.World;

public interface MovementStrategy {

    void move(Animal animal, World world);
}