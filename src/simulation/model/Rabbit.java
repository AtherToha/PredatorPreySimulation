package simulation.model;

public class Rabbit extends Animal {

    private static final double MAX_ENERGY = 100;
    private static final double INITIAL_ENERGY = 60;
    private static final double FOOD_ENERGY = 25;
    private static final double ENERGY_LOSS = 1.4;

    private static final double REPRODUCTION_THRESHOLD = 95;
    private static final double REPRODUCTION_COST = 50;

    private static final int REPRODUCTION_COOLDOWN = 35;
    private static final int MAX_AGE = 200;

    private int reproductionTimer = 0;

    public Rabbit(int x, int y) {
        super(x, y, INITIAL_ENERGY, MAX_ENERGY);
    }

    public void eatGrass() {
        gainEnergy(FOOD_ENERGY);
    }

    public void updateMetabolism() {

        if (!isAlive()) return;

        ageOneTick();
        loseEnergy(ENERGY_LOSS);

        if (reproductionTimer > 0) {
            reproductionTimer--;
        }

        if (getAge() >= MAX_AGE) {
            die();
        }
    }

    public boolean canReproduce() {
        return isAlive()
                && getEnergy() >= REPRODUCTION_THRESHOLD
                && reproductionTimer == 0;
    }

    public Rabbit reproduce() {

        if (!canReproduce()) return null;

        loseEnergy(REPRODUCTION_COST);
        reproductionTimer = REPRODUCTION_COOLDOWN;

        return new Rabbit(getX(), getY());
    }
}