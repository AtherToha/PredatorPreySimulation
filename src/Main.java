import simulation.ui.SimulationWindow;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            SimulationWindow window = new SimulationWindow();

            window.setVisible(true);
        });
    }
}