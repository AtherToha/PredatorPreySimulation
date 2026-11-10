package simulation.model;

public class Grass extends Entity {

    private static final int MAX_GROWTH = 100;
    private static final int GROWTH_PER_TICK = 5;
    private static final int FOOD_VALUE = 25;

    private int growth;

    public Grass(int x, int y) {
        super(x, y);
        this.growth = MAX_GROWTH;
    }

    public int getGrowth() {
        return growth;
    }

    // Checks whether the grass has enough growth to be eaten
    public boolean isAvailable() {
        return isAlive() && growth >= FOOD_VALUE;
    }

    // Rabbit consumes grass
    public boolean consume() {

        if (!isAvailable()) {
            return false;
        }

        growth -= FOOD_VALUE;

        if (growth == 0) {
            die();
        }

        return true;
    }

    // Grass grows over time
    public void grow() {

        if (growth < MAX_GROWTH) {
            growth = Math.min(MAX_GROWTH, growth + GROWTH_PER_TICK);
        }

        if (growth >= FOOD_VALUE && !isAlive()) {
            revive();
        }
    }
}