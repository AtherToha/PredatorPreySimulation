
package simulation.util;

public record PopulationSnapshot(
        int tick,
        long rabbits,
        long wolves
) {
}
