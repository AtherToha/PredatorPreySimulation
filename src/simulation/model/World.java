
package simulation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private final int width;
    private final int height;
    private final Grass[][] grass;
    private final List<Rabbit> rabbits = new ArrayList<>();
    private final List<Wolf> wolves = new ArrayList<>();
    private final Random random = new Random();

    public World(int width, int height, int rabbitCount,
                 int wolfCount, double grassDensity) {

        if (width <= 0 || height <= 0
                || rabbitCount < 0 || wolfCount < 0
                || rabbitCount + wolfCount > width * height
                || grassDensity < 0 || grassDensity > 1) {
            throw new IllegalArgumentException("Invalid world settings");
        }

        this.width = width;
        this.height = height;
        this.grass = new Grass[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (random.nextDouble() < grassDensity) {
                    grass[x][y] = new Grass(x, y);
                }
            }
        }

        for (int i = 0; i < rabbitCount; i++) {
            int[] position = randomEmptyCell();
            rabbits.add(new Rabbit(position[0], position[1]));
        }

        for (int i = 0; i < wolfCount; i++) {
            int[] position = randomEmptyCell();
            wolves.add(new Wolf(position[0], position[1]));
        }
    }

    private int[] randomEmptyCell() {
        int x;
        int y;

        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
        } while (!isCellEmpty(x, y));

        return new int[]{x, y};
    }

    public boolean isInsideWorld(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isCellEmpty(int x, int y) {
        if (!isInsideWorld(x, y)) return false;

        for (Rabbit rabbit : rabbits) {
            if (rabbit.isAlive()
                    && rabbit.getX() == x && rabbit.getY() == y) {
                return false;
            }
        }

        for (Wolf wolf : wolves) {
            if (wolf.isAlive()
                    && wolf.getX() == x && wolf.getY() == y) {
                return false;
            }
        }

        return true;
    }

    public boolean hasEmptyNeighbour(int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if ((dx != 0 || dy != 0)
                        && isCellEmpty(x + dx, y + dy)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean placeOffspring(Animal offspring, int x, int y) {
        if (offspring == null) return false;

        List<int[]> available = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if ((dx != 0 || dy != 0)
                        && isCellEmpty(x + dx, y + dy)) {
                    available.add(new int[]{x + dx, y + dy});
                }
            }
        }

        if (available.isEmpty()) return false;

        int[] position = available.get(random.nextInt(available.size()));
        offspring.moveTo(position[0], position[1]);

        if (offspring instanceof Rabbit rabbit) {
            rabbits.add(rabbit);
        } else if (offspring instanceof Wolf wolf) {
            wolves.add(wolf);
        } else {
            return false;
        }

        return true;
    }

    public void growGrass() {
        // Regenerate existing grass.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grass[x][y] != null) {
                    grass[x][y].grow();
                }
            }
        }

        // Occasionally spread grass to an adjacent empty grass cell.
        List<int[]> newGrass = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grass[x][y] == null) continue;
                if (!grass[x][y].isAvailable()) continue;
                if (random.nextDouble() >= 0.002) continue;

                int nx = x + random.nextInt(3) - 1;
                int ny = y + random.nextInt(3) - 1;

                if (isInsideWorld(nx, ny) && grass[nx][ny] == null) {
                    newGrass.add(new int[]{nx, ny});
                }
            }
        }

        for (int[] position : newGrass) {
            int x = position[0];
            int y = position[1];

            if (grass[x][y] == null) {
                grass[x][y] = new Grass(x, y);
            }
        }
    }

    public Grass getGrassAt(int x, int y) {
        return isInsideWorld(x, y) ? grass[x][y] : null;
    }

    public void removeDeadAnimals() {
        rabbits.removeIf(rabbit -> !rabbit.isAlive());
        wolves.removeIf(wolf -> !wolf.isAlive());
    }

    public void addRabbit(Rabbit rabbit) {
        if (rabbit != null && isInsideWorld(rabbit.getX(), rabbit.getY())) {
            rabbits.add(rabbit);
        }
    }

    public void addWolf(Wolf wolf) {
        if (wolf != null && isInsideWorld(wolf.getX(), wolf.getY())) {
            wolves.add(wolf);
        }
    }

    public List<Rabbit> getRabbits() {
        return List.copyOf(rabbits);
    }

    public List<Wolf> getWolves() {
        return List.copyOf(wolves);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int countNearbyAnimals(int x, int y, int radius) {

        int count = 0;

        for (Rabbit rabbit : rabbits) {
            if (!rabbit.isAlive()) continue;

            if (Math.abs(rabbit.getX() - x) <= radius
                    && Math.abs(rabbit.getY() - y) <= radius) {
                count++;
            }
        }

        for (Wolf wolf : wolves) {
            if (!wolf.isAlive()) continue;

            if (Math.abs(wolf.getX() - x) <= radius
                    && Math.abs(wolf.getY() - y) <= radius) {
                count++;
            }
        }

        return count;
    }
}
