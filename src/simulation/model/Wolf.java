package simulation.model;

public class Wolf extends Animal {

    private static final double MAX_ENERGY = 150;
    private static final double INITIAL_ENERGY = 100;
    private static final double FOOD_ENERGY = 65;
    private static final double ENERGY_LOSS = 2.0;

    private static final double REPRODUCTION_THRESHOLD = 120;
    private static final double REPRODUCTION_COST = 55;

    private static final int REPRODUCTION_COOLDOWN = 25;
    private static final int MAX_AGE = 250;

    private int reproductionTimer = 0;

    public Wolf(int x, int y) {
        super(x, y, INITIAL_ENERGY, MAX_ENERGY);
    }

    public void eatRabbit() {
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

    public Wolf reproduce() {

        if (!canReproduce()) return null;

        loseEnergy(REPRODUCTION_COST);
        reproductionTimer = REPRODUCTION_COOLDOWN;

        return new Wolf(getX(), getY());
    }
}