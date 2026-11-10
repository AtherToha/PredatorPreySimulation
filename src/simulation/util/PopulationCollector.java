
package simulation.util;

import java.util.stream.Collector;

public class PopulationCollector {

    private static class Accumulator {
        long samples;
        long totalRabbits;
        long totalWolves;
        long maximumRabbits;
        long maximumWolves;

        void add(PopulationSnapshot snapshot) {
            samples++;
            totalRabbits += snapshot.rabbits();
            totalWolves += snapshot.wolves();

            maximumRabbits =
                    Math.max(maximumRabbits, snapshot.rabbits());

            maximumWolves =
                    Math.max(maximumWolves, snapshot.wolves());
        }

        Accumulator combine(Accumulator other) {
            samples += other.samples;
            totalRabbits += other.totalRabbits;
            totalWolves += other.totalWolves;

            maximumRabbits =
                    Math.max(maximumRabbits, other.maximumRabbits);

            maximumWolves =
                    Math.max(maximumWolves, other.maximumWolves);

            return this;
        }

        PopulationSummary finish() {
            return new PopulationSummary(
                    samples,
                    totalRabbits,
                    totalWolves,
                    maximumRabbits,
                    maximumWolves
            );
        }
    }

    public static Collector<PopulationSnapshot, ?, PopulationSummary>
    toSummary() {

        return Collector.of(
                Accumulator::new,
                Accumulator::add,
                Accumulator::combine,
                Accumulator::finish,
                new Collector.Characteristics[0]
        );
    }
}
