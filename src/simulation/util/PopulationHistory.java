
package simulation.util;

import simulation.engine.Simulation;
import simulation.model.World;

import java.util.ArrayList;
import java.util.List;

public class PopulationHistory {

    private final List<PopulationSnapshot> snapshots =
            new ArrayList<>();

    public void record(Simulation simulation) {
        World world = simulation.getWorld();

        snapshots.add(new PopulationSnapshot(
                simulation.getTickCount(),
                Statistics.countRabbits(world),
                Statistics.countWolves(world)
        ));
    }

    public List<PopulationSnapshot> getSnapshots() {
        return List.copyOf(snapshots);
    }
}
