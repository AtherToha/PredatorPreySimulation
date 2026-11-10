package simulation.ui;

import simulation.engine.Simulation;
import simulation.model.Grass;
import simulation.model.Rabbit;
import simulation.model.Wolf;
import simulation.model.World;
import simulation.observer.SimulationListener;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SimulationPanel extends JPanel implements SimulationListener {

    private Simulation simulation;

    public SimulationPanel(Simulation simulation) {
        this.simulation = simulation;

        setPreferredSize(new Dimension(900, 600));
        setBackground(new Color(235, 235, 235));
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        repaint();
    }

    @Override
    public void onSimulationUpdated(Simulation simulation) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (simulation == null) {
            return;
        }

        Graphics2D g = (Graphics2D) graphics;

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        World world = simulation.getWorld();

        int cellWidth = getWidth() / world.getWidth();
        int cellHeight = getHeight() / world.getHeight();

        // Draw the background.
        g.setColor(new Color(245, 245, 235));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw grass.
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {

                Grass grass = world.getGrassAt(x, y);

                if (grass != null && grass.getGrowth() > 0) {

                    int green = 100 + grass.getGrowth();
                    green = Math.min(green, 220);

                    g.setColor(new Color(65, green, 65));

                    g.fillRect(
                            x * cellWidth,
                            y * cellHeight,
                            cellWidth,
                            cellHeight
                    );
                }
            }
        }

        // Draw grid lines.
        g.setColor(new Color(210, 210, 210));

        for (int x = 0; x <= world.getWidth(); x++) {
            g.drawLine(
                    x * cellWidth, 0,
                    x * cellWidth, getHeight()
            );
        }

        for (int y = 0; y <= world.getHeight(); y++) {
            g.drawLine(
                    0, y * cellHeight,
                    getWidth(), y * cellHeight
            );
        }

        // Draw rabbits.
        g.setColor(new Color(245, 245, 245));

        for (Rabbit rabbit : world.getRabbits()) {

            if (rabbit.isAlive()) {
                g.fillOval(
                        rabbit.getX() * cellWidth + 2,
                        rabbit.getY() * cellHeight + 2,
                        Math.max(4, cellWidth - 4),
                        Math.max(4, cellHeight - 4)
                );

                g.setColor(Color.BLACK);

                g.drawOval(
                        rabbit.getX() * cellWidth + 2,
                        rabbit.getY() * cellHeight + 2,
                        Math.max(4, cellWidth - 4),
                        Math.max(4, cellHeight - 4)
                );

                g.setColor(new Color(245, 245, 245));
            }
        }

        // Draw wolves.
        g.setColor(new Color(180, 45, 45));

        for (Wolf wolf : world.getWolves()) {

            if (wolf.isAlive()) {
                g.fillOval(
                        wolf.getX() * cellWidth + 2,
                        wolf.getY() * cellHeight + 2,
                        Math.max(4, cellWidth - 4),
                        Math.max(4, cellHeight - 4)
                );
            }
        }
    }
}