package simulation.model;

public abstract class Entity {

    private int x;
    private int y;
    private boolean alive;

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
        this.alive = true;
    }

    // Returns the horizontal position
    public int getX() {
        return x;
    }

    // Returns the vertical position
    public int getY() {
        return y;
    }

    // Checks whether the entity is alive
    public boolean isAlive() {
        return alive;
    }

    // Moves the entity to a new position
    public void moveTo(int newX, int newY) {
        if (alive) {
            this.x = newX;
            this.y = newY;
        }
    }

    // Marks the entity as dead
    public void die() {
        alive = false;
    }

    protected void revive() {
        alive = true;
    }
}
