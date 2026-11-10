
package simulation.engine;

import simulation.model.Grass;
import simulation.model.Rabbit;
import simulation.model.Wolf;
import simulation.model.World;
import simulation.observer.SimulationListener;
import simulation.strategy.MovementStrategy;
import simulation.util.PopulationHistory;

import java.util.*;

public class Simulation {

    private final World world;
    private final MovementStrategy rabbitMovement;
    private final MovementStrategy wolfMovement;
    private final PopulationHistory populationHistory =  new PopulationHistory();
    private final List<SimulationListener> listeners = new ArrayList<>();
    private final Random random = new Random();

    private int tickCount = 0;

    public Simulation(World world,
                      MovementStrategy rabbitMovement,
                      MovementStrategy wolfMovement) {

        this.world = Objects.requireNonNull(world);
        this.rabbitMovement = Objects.requireNonNull(rabbitMovement);
        this.wolfMovement = Objects.requireNonNull(wolfMovement);
    }

    public void advanceOneTick() {

        world.growGrass();

        // Animals alive at the start of this tick
        List<Rabbit> rabbits = new ArrayList<>(world.getRabbits());
        List<Wolf> wolves = new ArrayList<>(world.getWolves());

        // Randomise movement order
        Collections.shuffle(rabbits, random);
        Collections.shuffle(wolves, random);

        // Rabbit movement
        for (Rabbit rabbit : rabbits) {
            if (rabbit.isAlive()) {
                rabbitMovement.move(rabbit, world);
            }
        }

        // Wolf movement
        for (Wolf wolf : wolves) {
            if (wolf.isAlive()) {
                wolfMovement.move(wolf, world);
            }
        }

        // Rabbits eat grass
        for (Rabbit rabbit : rabbits) {
            if (!rabbit.isAlive()) continue;

            Grass grass = world.getGrassAt(
                    rabbit.getX(), rabbit.getY());

            if (grass != null && grass.consume()) {
                rabbit.eatGrass();
            }
        }

        // Wolves hunt rabbits
        for (Wolf wolf : wolves) {
            if (!wolf.isAlive()) continue;

            List<Rabbit> possiblePrey = new ArrayList<>();

            for (Rabbit rabbit : rabbits) {
                if (rabbit.isAlive()
                        && rabbit.getX() == wolf.getX()
                        && rabbit.getY() == wolf.getY()) {

                    possiblePrey.add(rabbit);
                }
            }

            double huntingChance = 0.40;

            if (world.getRabbits().size() < 300) {
                huntingChance = 0.15;
            } else if (world.getRabbits().size() < 600) {
                huntingChance = 0.25;
            }

            if (!possiblePrey.isEmpty()
                    && random.nextDouble() < huntingChance) {

                Rabbit prey = possiblePrey.get(
                        random.nextInt(possiblePrey.size()));

                prey.die();
                wolf.eatRabbit();
            }
        }

        // Rabbit metabolism and ageing
        for (Rabbit rabbit : rabbits) {
            if (rabbit.isAlive()) {
                rabbit.updateMetabolism();
            }
        }

        // Wolf metabolism and ageing
        for (Wolf wolf : wolves) {
            if (wolf.isAlive()) {
                wolf.updateMetabolism();
            }
        }

        // Remove dead animals
        world.removeDeadAnimals();

        // Rabbit reproduction with local density check
        for (Rabbit rabbit : rabbits) {

            if (rabbit.canReproduce()
                    && world.hasEmptyNeighbour(
                    rabbit.getX(), rabbit.getY())
                    && world.countNearbyAnimals(
                    rabbit.getX(), rabbit.getY(), 2) < 18) {

                Rabbit baby = rabbit.reproduce();

                world.placeOffspring(
                        baby, rabbit.getX(), rabbit.getY());
            }
        }

        // Wolf reproduction with local density check

// Wolf reproduction
        for (Wolf wolf : wolves) {

            if (wolf.isAlive()
                    && wolf.canReproduce()
                    && world.hasEmptyNeighbour(
                    wolf.getX(), wolf.getY())) {

                Wolf baby = wolf.reproduce();

                world.placeOffspring(
                        baby, wolf.getX(), wolf.getY());
            }
        }


        tickCount++;
        populationHistory.record(this);
        notifyListeners();
    }

    public void addListener(SimulationListener listener) {
        listeners.add(Objects.requireNonNull(listener));
    }

    public void removeListener(SimulationListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (SimulationListener listener : List.copyOf(listeners)) {
            listener.onSimulationUpdated(this);
        }
    }

    public World getWorld() {
        return world;
    }

    public int getTickCount() {
        return tickCount;
    }

    public PopulationHistory getPopulationHistory() {
        return populationHistory;
    }
}
