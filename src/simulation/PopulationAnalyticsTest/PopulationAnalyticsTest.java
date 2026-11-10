
package simulation.PopulationAnalyticsTest;

import simulation.util.PopulationAnalytics;
import simulation.util.PopulationSnapshot;
import simulation.util.PopulationSummary;

import java.util.List;

public class PopulationAnalyticsTest {

    public static void main(String[] args) {

        List<PopulationSnapshot> history = List.of(
                new PopulationSnapshot(100, 500, 50),
                new PopulationSnapshot(200, 800, 100),
                new PopulationSnapshot(300, 400, 150),
                new PopulationSnapshot(400, 600, 120)
        );

        PopulationSummary sequential =
                PopulationAnalytics.analyseSequential(history);

        PopulationSummary parallel =
                PopulationAnalytics.analyseParallel(history);

        if (!sequential.equals(parallel)) {
            throw new AssertionError(
                    "Sequential and parallel results differ");
        }

        if (sequential.samples() != 4
                || sequential.totalRabbits() != 2300
                || sequential.totalWolves() != 420
                || sequential.maximumRabbits() != 800
                || sequential.maximumWolves() != 150) {
            throw new AssertionError(
                    "Incorrect population statistics");
        }

        System.out.println("All tests passed!");
        System.out.println(
                "Average rabbits: " + sequential.averageRabbits());
        System.out.println(
                "Average wolves: " + sequential.averageWolves());
    }
}
