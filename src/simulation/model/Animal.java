package simulation.model;

public abstract class Animal extends Entity {

    private double energy;
    private final double maxEnergy;
    private int age;

    public Animal(int x, int y, double initialEnergy, double maxEnergy) {

        super(x, y);

        if (maxEnergy <= 0 || initialEnergy < 0
                || initialEnergy > maxEnergy) {
            throw new IllegalArgumentException("Invalid energy values");
        }

        this.energy = initialEnergy;
        this.maxEnergy = maxEnergy;
        this.age = 0;
    }

    public double getEnergy() {
        return energy;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    public int getAge() {
        return age;
    }

    // Animals lose energy as time passes
    public void loseEnergy(double amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("Energy loss cannot be negative");
        }

        if (!isAlive()) {
            return;
        }

        energy -= amount;

        if (energy <= 0) {
            energy = 0;
            die();
        }
    }

    // Animals gain energy from food
    public void gainEnergy(double amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("Energy gain cannot be negative");
        }

        if (!isAlive()) {
            return;
        }

        energy = Math.min(maxEnergy, energy + amount);
    }

    // Checks whether an animal needs food
    public boolean isHungry() {
        return energy < maxEnergy * 0.5;
    }

    // Increases animal age by one simulation tick
    public void ageOneTick() {

        if (isAlive()) {
            age++;
        }
    }
}