package simulation.ui;

import simulation.engine.Simulation;
import simulation.model.World;
import simulation.strategy.RabbitEscapeStrategy;
import simulation.strategy.WolfHuntStrategy;
import simulation.util.Statistics;

import javax.swing.*;
import java.awt.*;

public class SimulationWindow extends JFrame {

    private Simulation simulation;
    private SimulationPanel simulationPanel;

    private Timer timer;

    private JLabel rabbitLabel;
    private JLabel wolfLabel;
    private JLabel tickLabel;
    private JLabel energyLabel;

    private JSlider speedSlider;

    public SimulationWindow() {

        setTitle("Predator–Prey Ecosystem Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the simulation.
        simulation = createNewSimulation();

        // Create the graphical panel.
        simulationPanel = new SimulationPanel(simulation);
        simulation.addListener(simulationPanel);

        // Create the controls.
        JPanel controlPanel = createControlPanel();

        // Create the statistics display.
        JPanel statisticsPanel = createStatisticsPanel();

        // Arrange the interface.
        setLayout(new BorderLayout());

        add(controlPanel, BorderLayout.NORTH);
        add(simulationPanel, BorderLayout.CENTER);
        add(statisticsPanel, BorderLayout.SOUTH);

        // Create the simulation timer.
        timer = new Timer(250, event -> {
            simulation.advanceOneTick();
            updateStatistics();
        });

        updateStatistics();

        pack();
        setLocationRelativeTo(null);
    }

    private Simulation createNewSimulation() {

        World world = new World(60, 40, 100, 12, 0.65);

        return new Simulation(
                world,
                new RabbitEscapeStrategy(),
                new WolfHuntStrategy()
        );
    }

    private JPanel createControlPanel() {

        JPanel panel = new JPanel(new FlowLayout());

        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton resetButton = new JButton("Reset");

        startButton.addActionListener(event -> timer.start());

        pauseButton.addActionListener(event -> timer.stop());

        resetButton.addActionListener(event -> resetSimulation());

        speedSlider = new JSlider(50, 1000, 250);
        speedSlider.setMajorTickSpacing(250);
        speedSlider.setMinorTickSpacing(50);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        speedSlider.addChangeListener(event ->
                timer.setDelay(speedSlider.getValue())
        );

        panel.add(startButton);
        panel.add(pauseButton);
        panel.add(resetButton);

        panel.add(new JLabel("Speed:"));
        panel.add(speedSlider);

        return panel;
    }

    private JPanel createStatisticsPanel() {

        JPanel panel = new JPanel(new FlowLayout());

        rabbitLabel = new JLabel();
        wolfLabel = new JLabel();
        tickLabel = new JLabel();
        energyLabel = new JLabel();

        panel.add(rabbitLabel);
        panel.add(new JLabel(" | "));
        panel.add(wolfLabel);
        panel.add(new JLabel(" | "));
        panel.add(tickLabel);
        panel.add(new JLabel(" | "));
        panel.add(energyLabel);

        return panel;
    }

    private void updateStatistics() {

        rabbitLabel.setText(
                "Rabbits: " + Statistics.countRabbits(
                        simulation.getWorld())
        );

        wolfLabel.setText(
                "Wolves: " + Statistics.countWolves(
                        simulation.getWorld())
        );

        tickLabel.setText(
                "Tick: " + simulation.getTickCount()
        );

        energyLabel.setText(
                String.format(
                        "Average energy (Rabbits): %.1f",
                        Statistics.averageRabbitEnergy(
                                simulation.getWorld())
                )
        );
    }

    private void resetSimulation() {

        timer.stop();

        simulation.removeListener(simulationPanel);

        simulation = createNewSimulation();

        simulationPanel.setSimulation(simulation);
        simulation.addListener(simulationPanel);

        updateStatistics();
    }
}