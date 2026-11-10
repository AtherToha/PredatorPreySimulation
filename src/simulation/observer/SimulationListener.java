package simulation.observer;

import simulation.engine.Simulation;

public interface SimulationListener {

    void onSimulationUpdated(Simulation simulation);
}