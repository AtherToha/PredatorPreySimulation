
package simulation.util;

import java.util.List;

public class PopulationAnalytics {

    public static PopulationSummary analyseSequential(
            List<PopulationSnapshot> history) {

        return history.stream()
                .collect(PopulationCollector.toSummary());
    }

    public static PopulationSummary analyseParallel(
            List<PopulationSnapshot> history) {

        return history.parallelStream()
                .collect(PopulationCollector.toSummary());
    }
}
