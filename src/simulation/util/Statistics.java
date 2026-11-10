package simulation.util;

import simulation.model.Rabbit;
import simulation.model.Wolf;
import simulation.model.World;

public class Statistics {

    public static long countRabbits(World world) {

        return world.getRabbits()
                .stream()
                .filter(Rabbit::isAlive)
                .count();
    }

    public static long countWolves(World world) {

        return world.getWolves()
                .stream()
                .filter(Wolf::isAlive)
                .count();
    }

    public static double averageRabbitEnergy(World world) {

        return world.getRabbits()
                .stream()
                .filter(Rabbit::isAlive)
                .mapToDouble(Rabbit::getEnergy)
                .average()
                .orElse(0.0);
    }

    public static double averageWolfEnergy(World world) {

        return world.getWolves()
                .stream()
                .filter(Wolf::isAlive)
                .mapToDouble(Wolf::getEnergy)
                .average()
                .orElse(0.0);
    }
}