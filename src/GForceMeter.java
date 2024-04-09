import javax.swing.*;
import java.awt.*;


public class GForceMeter extends JPanel {
    double acceleration_Long;
    double acceleration_Ver;
    double acceleration_Lat;
    double rotationAcceleration_Long;
    double rotationAcceleration_Ver;
    double rotationAcceleration_Lat;
    private double gForce;
    private int centerX;
    private int centerY;
    private final int SCALE_FACTOR = 10; // Increased scaling factor
    private double[] accelerationLongBuffer;
    private double[] accelerationVerBuffer;
    private double[] accelerationLatBuffer;
    private final int BUFFER_SIZE = 100; // Buffer size for storing acceleration history


    /*

     */
    public GForceMeter() {
        this.setPreferredSize(new Dimension(400, 700)); // Adjusted size for visibility
        this.acceleration_Long = 0;
        this.acceleration_Ver = 0;
        this.acceleration_Lat = 0;
        this.rotationAcceleration_Long = 0;
        this.rotationAcceleration_Ver = 0;
        this.rotationAcceleration_Lat = 0;
        this.gForce = 0;

        // Initialize buffers
        accelerationLongBuffer = new double[BUFFER_SIZE];
        accelerationVerBuffer = new double[BUFFER_SIZE];
        accelerationLatBuffer = new double[BUFFER_SIZE];
    }

    public void setAccelerations (double accelerationX, double accelerationY, double accelerationZ,
    double rotationAccelerationX, double rotationAccelerationY, double rotationAccelerationZ) {
        this.acceleration_Long = accelerationX;
        this.acceleration_Ver = accelerationY;
        this.acceleration_Lat = accelerationZ;
        this.rotationAcceleration_Long = rotationAccelerationX;
        this.rotationAcceleration_Ver = rotationAccelerationY;
        this.rotationAcceleration_Lat = rotationAccelerationZ;
        this.gForce = calculateTotalGForce();

        // Add acceleration values to the buffers
        addToBuffer(accelerationLongBuffer, accelerationX);
        addToBuffer(accelerationVerBuffer, accelerationY);
        addToBuffer(accelerationLatBuffer, accelerationZ);

        repaint(); // Update the display when accelerations change
    }

    private void addToBuffer(double[] buffer, double value) {
        // Shift elements in the buffer to make space for the new value
        for (int i = 0; i < BUFFER_SIZE - 1; i++) {
            buffer[i] = buffer[i + 1];
        }
        // Add the new value to the end of the buffer
        buffer[BUFFER_SIZE - 1] = value;
    }

    //Convert Feet per second square to meter per second square
    public double feet_to_meter(double velocity){
        double FEET_TO_METERS = 0.3048;
        return velocity * FEET_TO_METERS;
    }

    private double calculateTotalGForce() {
        // Calculate total linear acceleration magnitude
        double totalLinearAcceleration = Math.sqrt(Math.pow(acceleration_Long, 2) + Math.pow(acceleration_Ver, 2) + Math.pow(acceleration_Lat, 2));
        // Calculate total rotational acceleration magnitude
        double totalRotationAcceleration = Math.sqrt(Math.pow(rotationAcceleration_Long, 2) + Math.pow(rotationAcceleration_Ver, 2) + Math.pow(rotationAcceleration_Lat, 2));
        // Combine linear and rotational accelerations to calculate total G-force
        double totalAcceleration = Math.sqrt(Math.pow(acceleration_Long, 2) + Math.pow(acceleration_Ver, 2) + Math.pow(acceleration_Lat, 2) + Math.pow(rotationAcceleration_Long, 2) + Math.pow(rotationAcceleration_Ver, 2) + Math.pow(rotationAcceleration_Lat, 2));
        // Convert acceleration to G-force
        return totalAcceleration / 9.81; // Divide by gravitational acceleration to get G-force
    }

    private void drawGraph(Graphics g, double[] buffer,String name, Color color, int yOffset) {
        g.setColor(color);
        int graphHeight = getHeight() / 20;
        int graphWidth = getWidth() - 20;
        int scaleX = graphWidth / BUFFER_SIZE;
        int scaleY = graphHeight / 10; // Scale the graph to fit within the panel

        for (int i = 0; i < BUFFER_SIZE - 1; i++) {
            int x1 = i * scaleX + 10;
            int y1 = getHeight() - (int) (buffer[i] * scaleY) - 10 - yOffset;
            int x2 = (i + 1) * scaleX + 10;
            int y2 = getHeight() - (int) (buffer[i + 1] * scaleY) - 10 - yOffset;

            g.drawLine(x1, y1, x2, y2);
            // Draw label for the buffer name
            if (i == BUFFER_SIZE/2 ) { // Draw the label roughly in the middle of the line
                FontMetrics fm = g.getFontMetrics();
                int labelHeight = fm.getHeight();
                int labelX = 10; // Adjust this value as needed for spacing
                int labelY = y1 + labelHeight / 2;
                g.drawString(name, labelX,  y1-5);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate center coordinates
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        // Draw circular G-force meter
        int radius = 100; // Adjusted radius to fit within the panel
        //g.setColor(Color.LIGHT_GRAY);
        g.drawOval(centerX - radius, centerY - radius -200, 2 * radius, 2 * radius);

        // Draw coordinate axes
        g.drawLine(20, centerY-200, getWidth() - 20, centerY-200); // X-axis
        g.drawLine(centerX, 20, centerX, getHeight()/2 + 20); // Y-axis

        // Draw coordinate labels
        g.drawString("0", centerX - 5, centerY -200 +15); // Label for origin (0,0)
        g.drawString("+G", centerX - 10, getHeight() /2 + 15); // Label for positive G-axis
        g.drawString("+G", centerX - 10, 30); // Label for negative G-axis
        g.drawString("+G", getWidth() - 25, centerY -200 + 5); // Label for positive G-axis
        g.drawString("+G", 5, centerY -200  + 5); // Label for negative G-axis

        // Draw G-force point
        int x = centerX;
        int y = centerY-200;

        // Convert G-force to pixel offset from the center
        int pixelOffsetAcc_Long = (int) (acceleration_Long * SCALE_FACTOR); // Scale factor of 20 pixels per G
        int pixelOffsetAcc_Ver = (int) (acceleration_Ver * SCALE_FACTOR); // Scale factor of 20 pixels per G
        int pixelOffsetAcc_Lat = (int) (acceleration_Lat * SCALE_FACTOR); // Scale factor of 20 pixels per G
        int pixelOffsetRoll_Long = (int) (rotationAcceleration_Long * SCALE_FACTOR);
        int pixelOffsetRoll_Ver = (int) (rotationAcceleration_Ver * SCALE_FACTOR);
        int pixelOffsetRoll_Lat = (int) (rotationAcceleration_Lat * SCALE_FACTOR);

        x -= pixelOffsetAcc_Lat;
        x -= pixelOffsetRoll_Long;
        x -= pixelOffsetRoll_Ver;

        y -= pixelOffsetRoll_Lat;
        y += pixelOffsetAcc_Long; // Adjusting for correct direction
        y += pixelOffsetAcc_Ver; // Invert the offset to align with GUI coordinates

        if(y==centerY){
            g.drawString("Neutral", x + 10, y + 10);
        }else if(y > centerY) {
            g.drawString("Lean Back", x + 10, y + 10);
        } else {
            g.drawString("Lean Forward", x + 10, y + 10);
        }
        if(x==centerX){
            g.drawString("Neutral", x + 10, y + 25);
        }
        else if (x > centerX) {
            g.drawString("Lean Right", x + 10, y + 25);
        } else {
            g.drawString("Lean Left", x + 10, y + 25);
        }

        // Draw a circle representing the G-force point
        g.setColor(Color.RED);
        g.fillOval(x - 5, y - 5, 10, 10);
        g.drawString("Total G-force: " + String.format("%.2f", gForce), getWidth() - 150, 20);
        g.drawString("Cockpit degree: " + String.format("%.2f", Math.toDegrees(Math.atan(gForce))), getWidth() - 150, 30);
        g.drawString("X degree: " +  String.format("%.2f",Math.toDegrees(Math.atan((Math.sqrt(Math.pow(acceleration_Lat, 2) + Math.pow(rotationAcceleration_Long, 2) + Math.pow(rotationAcceleration_Ver, 2)))/9.81))), getWidth() - 150, 40);
        g.drawString("Y degree: " +  String.format("%.2f",Math.toDegrees(Math.atan((Math.sqrt(Math.pow(acceleration_Long, 2) + Math.pow(acceleration_Ver, 2) + Math.pow(rotationAcceleration_Lat, 2)))/9.81))), getWidth() - 150, 50);

        // Draw acceleration graphs
        // Draw acceleration graphs
        drawGraph(g, accelerationLongBuffer, "Acceleration: "+ accelerationLongBuffer[accelerationLongBuffer.length-1],Color.RED, centerY - 150);
        drawGraph(g, accelerationVerBuffer, "Attitude: " + accelerationVerBuffer[accelerationVerBuffer.length-1],Color.GREEN, centerY - 200);
        drawGraph(g, accelerationLatBuffer, "Lateral: " + accelerationLatBuffer[accelerationVerBuffer.length-1],Color.BLUE, centerY - 250);
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
        double[] acceleration_Long_Values = {0, 2, 4, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 6, 6.5, 2, 1, 0, -2, -2.3, 1}; // Acceleration in X-axis (forward motion)
        double[] acceleration_Ver_Values = {0, 0, 0, 0, 0, 0, 0, 2, 2, 3, 4, 4, 4, 5, 4, 4, 4, 4, 3, 3, 3, 0}; // Acceleration in Y-axis (lift-off)
        double[] acceleration_Lat_Values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0}; // Acceleration in Z-axis (wingtip to wingtip)
        double[] rotationAcceleration_Long_Values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Example rotation acceleration in X-axis
        double[] rotationAcceleration_Ver_Values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}; // Example rotation acceleration in Y-axis
        double[] rotationAcceleration_Lat_Values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 3, 3, 0, 0}; // Example rotation acceleration in Z-axis

        int timeDelay = 500; // Example time delays in milliseconds

        // Iterate through arrays and set acceleration values periodically
        for (int i = 0; i < acceleration_Long_Values.length; i++) {
            double accelerationX = acceleration_Long_Values[i];
            double accelerationY = acceleration_Ver_Values[i];
            double accelerationZ = acceleration_Lat_Values[i];
            double rotationAccelerationX = rotationAcceleration_Long_Values[i];
            double rotationAccelerationY = rotationAcceleration_Ver_Values[i];
            double rotationAccelerationZ = rotationAcceleration_Lat_Values[i];


            // Schedule setting acceleration values with a time delay
            Timer timer = new Timer(timeDelay, e -> {
                gForceMeter.setAccelerations(accelerationX, accelerationY, accelerationZ,
                        rotationAccelerationX, rotationAccelerationY, rotationAccelerationZ);
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
