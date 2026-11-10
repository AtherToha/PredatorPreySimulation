
package simulation.util;

public record PopulationSummary(
        long samples,
        long totalRabbits,
        long totalWolves,
        long maximumRabbits,
        long maximumWolves
) {
    public double averageRabbits() {
        return samples == 0 ? 0 :
                (double) totalRabbits / samples;
    }

    public double averageWolves() {
        return samples == 0 ? 0 :
                (double) totalWolves / samples;
    }
}
