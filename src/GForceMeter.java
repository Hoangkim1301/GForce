import javax.swing.*;
import java.awt.*;

public class GForceMeter extends JPanel {
    private double accelerationX;
    private double accelerationY;
    private double accelerationZ;
    private double gForce;
    private int centerX;
    private int centerY;
    private final int SCALE_FACTOR = 20; // Increased scaling factor

    public GForceMeter() {
        this.setPreferredSize(new Dimension(600, 600)); // Adjusted size for visibility
        this.accelerationX = 0;
        this.accelerationY = 0;
        this.accelerationZ = 0;
        this.gForce = 0;
    }

    public void setAccelerations(double accelerationX, double accelerationY, double accelerationZ) {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
        this.accelerationZ = accelerationZ;
        this.gForce = calculateTotalGForce();
        repaint(); // Update the display when accelerations change
    }

    private double calculateTotalGForce() {
        // Calculate total acceleration magnitude
        double totalAcceleration = Math.sqrt(Math.pow(accelerationX, 2) + Math.pow(accelerationY, 2) + Math.pow(accelerationZ, 2));
        // Convert acceleration to G-force
        return totalAcceleration / 9.81; // Divide by gravitational acceleration to get G-force
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate center coordinates
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        // Draw circular G-force meter
        int radius = Math.min(getWidth(), getHeight()) / 2 - 100; // Adjusted radius to fit within the panel
        //g.setColor(Color.LIGHT_GRAY);
        g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        // Draw coordinate axes
        g.drawLine(20, centerY, getWidth() - 20, centerY); // X-axis
        g.drawLine(centerX, 20, centerX, getHeight() - 20); // Y-axis

        // Draw coordinate labels
        g.drawString("0", centerX - 5, centerY + 15); // Label for origin (0,0)
        g.drawString("+G", centerX - 10, getHeight() - 10); // Label for positive G-axis
        g.drawString("+G", centerX - 10, 30); // Label for negative G-axis
        g.drawString("+G", getWidth() - 25, centerY + 5); // Label for positive G-axis
        g.drawString("+G", 5, centerY + 5); // Label for negative G-axis

        // Draw G-force point
        int x = centerX;
        int y = centerY;

        // Convert G-force to pixel offset from the center
        int pixelOffsetX = (int) (accelerationX * SCALE_FACTOR); // Scale factor of 20 pixels per G
        int pixelOffsetY = (int) (accelerationY * SCALE_FACTOR); // Scale factor of 20 pixels per G
        int pixelOffsetZ = (int) (accelerationZ * SCALE_FACTOR);
        x -= pixelOffsetY; // Adjusting for correct direction
        y += pixelOffsetX; // Invert the offset to align with GUI coordinates

        // Draw a circle representing the G-force point
        g.setColor(Color.RED);
        g.fillOval(x - 5, y - 5, 10, 10);
        g.drawString("Total G-force: " + String.format("%.2f", gForce), getWidth() - 150, 20);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("G-Force Meter");
        GForceMeter gForceMeter = new GForceMeter();
        frame.add(gForceMeter);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Define arrays for acceleration values and time delays
        /*
        In this extended set of dummy values, the takeoff phase is represented with increased duration and constant
        acceleration values to simulate the gradual acceleration along the x-axis (forward motion), lift-off along the
        y-axis, and climbing along the z-axis. Adjust the duration and values as needed for your simulation.
         */
        double[] accelerationXValues = {0, 2, 4, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7}; // Acceleration in X-axis (forward motion)
        double[] accelerationYValues = {0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, -2, -2, 1, 0.5, 1, 0, 0, 0}; // Acceleration in Y-axis (lift-off)
        double[] accelerationZValues = {0, 0, 5, 5, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 5, 5}; // Acceleration in Z-axis (climbing)

        int timeDelay = 500; // Example time delays in milliseconds

        // Iterate through arrays and set acceleration values periodically
        for (int i = 0; i < accelerationXValues.length; i++) {
            double accelerationX = accelerationXValues[i];
            double accelerationY = accelerationYValues[i];
            double accelerationZ = accelerationZValues[i];

            // Schedule setting acceleration values with a time delay
            Timer timer = new Timer(timeDelay, e -> {
                gForceMeter.setAccelerations(accelerationX, accelerationY, accelerationZ);
            });
            timer.setRepeats(false); // Set to execute only once
            timer.start();

            // Sleep to wait for the current timer to finish before scheduling the next one
            try {
                Thread.sleep(timeDelay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }
}
