import javax.swing.*;
import java.awt.*;

/**
 * The GForceMeter class represents a graphical component that displays
 * G-force and acceleration information. It includes methods to set
 * acceleration values, calculate total G-force, and draw graphical
 * representations of the G-force meter.
 * The GForceMeter panel displays a circular G-force meter, a coordinate
 * system, and graphs showing the history of longitudinal, vertical, and
 * lateral accelerations. It also provides information about the total
 * G-force, cockpit degree, and orientation degrees.
 * This class is intended to be used in a graphical user interface (GUI)
 * application where real-time acceleration data needs to be visualized.
 */
public class GForceMeter extends JPanel {
    double acceleration_Long;
    double acceleration_Ver;
    double acceleration_Lat;
    double rotationAcceleration_Long;
    double rotationAcceleration_Ver;
    double rotationAcceleration_Lat;
    private double total_gForce;
    private int centerX;
    private int centerY;

    private double velocity_Long; // True body velocity in m/s
    private double bank_degree; //Bank angle in degree, bank to the right with positive degree and to the left with negative degree
    private final int SCALE_FACTOR = 10; // Increased scaling factor
    private double[] accelerationLongBuffer;
    private double[] accelerationVerBuffer;
    private double[] accelerationLatBuffer;
    private double[] rotationAccelerationLongBuffer;
    private double[] rotationAccelerationVerBuffer;
    private double[] rotationAccelerationLatBuffer;
    private double[] velocityBuffer;
    private double[] bankDegreeBuffer;
    private final int BUFFER_SIZE = 100; // Buffer size for storing acceleration history
    private final double GRAVITY = 9.81; //Standard earth gravity


    public GForceMeter() {
        this.setPreferredSize(new Dimension(400, 900)); // Adjusted size for visibility
        this.acceleration_Long = 0;
        this.acceleration_Ver = 0;
        this.acceleration_Lat = 0;
        this.rotationAcceleration_Long = 0;
        this.rotationAcceleration_Ver = 0;
        this.rotationAcceleration_Lat = 0;
        this.total_gForce = 1; //9.81m/s^2

        // Initialize buffers
        accelerationLongBuffer = new double[BUFFER_SIZE];
        accelerationVerBuffer = new double[BUFFER_SIZE];
        accelerationLatBuffer = new double[BUFFER_SIZE];
        rotationAccelerationLongBuffer = new double[BUFFER_SIZE];
        rotationAccelerationVerBuffer = new double[BUFFER_SIZE];
        rotationAccelerationLatBuffer = new double[BUFFER_SIZE];
        velocityBuffer = new double[BUFFER_SIZE];
        bankDegreeBuffer = new double[BUFFER_SIZE];
    }

    public void setAccelerations (double acceleration_Long, double acceleration_Ver, double acceleration_Lat,
                                  double rotationAcceleration_Long, double rotationAcceleration_Ver, double rotationAcceleration_Lat,
                                  double velocity_Long,double bank_degree_rad) {
        this.acceleration_Long = acceleration_Long;
        this.acceleration_Ver = acceleration_Ver;
        this.acceleration_Lat = acceleration_Lat;
        this.rotationAcceleration_Long = rotationAcceleration_Long;
        this.rotationAcceleration_Ver = rotationAcceleration_Ver;
        this.rotationAcceleration_Lat = rotationAcceleration_Lat;
        this.velocity_Long = velocity_Long;
        this.bank_degree = Math.toDegrees(bank_degree_rad);
        this.total_gForce = calculateTotalGForce();

        // Add acceleration values to the buffers
        addToBuffer(accelerationLongBuffer, acceleration_Long);
        addToBuffer(accelerationVerBuffer, acceleration_Ver);
        addToBuffer(accelerationLatBuffer, acceleration_Lat);
        addToBuffer(rotationAccelerationLongBuffer, rotationAcceleration_Long);
        addToBuffer(rotationAccelerationVerBuffer, rotationAcceleration_Ver);
        addToBuffer(rotationAccelerationLatBuffer, rotationAcceleration_Lat);
        addToBuffer(velocityBuffer, velocity_Long);
        addToBuffer(bankDegreeBuffer, bank_degree_rad*10);

        repaint(); // Update the display when accelerations change
    }

    public void setAccelerations (double acceleration_Long, double acceleration_Ver, double acceleration_Lat,
                                  double rotationAcceleration_Long, double rotationAcceleration_Ver, double rotationAcceleration_Lat
                                  ) {
        this.acceleration_Long = acceleration_Long;
        this.acceleration_Ver = acceleration_Ver;
        this.acceleration_Lat = acceleration_Lat;
        this.rotationAcceleration_Long = rotationAcceleration_Long;
        this.rotationAcceleration_Ver = rotationAcceleration_Ver;
        this.rotationAcceleration_Lat = rotationAcceleration_Lat;
        this.total_gForce = calculateTotalGForce();


        // Add acceleration values to the buffers
        addToBuffer(accelerationLongBuffer, acceleration_Long);
        addToBuffer(accelerationVerBuffer, acceleration_Ver);
        addToBuffer(accelerationLatBuffer, acceleration_Lat);
        addToBuffer(rotationAccelerationLongBuffer, rotationAcceleration_Long);
        addToBuffer(rotationAccelerationVerBuffer, rotationAcceleration_Ver);
        addToBuffer(rotationAccelerationLatBuffer, rotationAcceleration_Lat);
        addToBuffer(velocityBuffer, velocity_Long);
        addToBuffer(bankDegreeBuffer, bank_degree * 10);

        repaint(); // Update the display when accelerations change
    }



    @Override
    public void paintComponent(Graphics g) {
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

        //Calculate the position of g Force point in 2D coordinate
        Point g_force_point = calculateFinalCoordinates(x,y,acceleration_Long,acceleration_Ver,acceleration_Lat,
                rotationAcceleration_Long,rotationAcceleration_Ver,rotationAcceleration_Lat,velocity_Long,bank_degree,SCALE_FACTOR);
        x = g_force_point.x;
        y = g_force_point.y;

        //Add label and position name to the g force point in the coordinate
        if(y==centerY-200){
            g.drawString("Neutral", x + 10, y + 10);
        }else if(y > centerY-200) {
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
        g.drawString("Total G-force: " + String.format("%.2f", total_gForce), getWidth() - 150, 20); //calculateTotalGForce()


        g.drawString("Cockpit degree: " + String.format("%.2f", calculateDegree(calculateTotalGForce_no_GRAVITY())), getWidth() - 150, 30);
        if(bank_degree==0){
            g.drawString("Quer degree: " +  String.format("%.2f",calculateDegree(Math.sqrt(Math.pow(acceleration_Lat, 2) + Math.pow(rotationAcceleration_Long, 2) + Math.pow(rotationAcceleration_Ver, 2))/9.81)), getWidth() - 150, 40);
        }else{
            g.drawString("Quer degree: " +  String.format("%.2f",calculateDegree(Math.sqrt(Math.pow(acceleration_Lat, 2) + Math.pow(rotationAcceleration_Long, 2) + Math.pow(calculateAccelerationOfTurn(velocity_Long,Math.toRadians(bank_degree),rotationAcceleration_Ver), 2))/9.81)), getWidth() - 150, 40);
        }
        g.drawString("Laengs degree: " +  String.format("%.2f",calculateDegree(Math.sqrt(Math.pow(acceleration_Long, 2) + Math.pow(acceleration_Ver, 2) + Math.pow(rotationAcceleration_Lat, 2))/9.81)), getWidth() - 150, 50);

        // Draw acceleration graphs
        // Draw acceleration graphs
        drawGraph(g, accelerationLongBuffer, "Longitudinal Acceleration (m/s^2): "+ accelerationLongBuffer[accelerationLongBuffer.length-1],Color.RED, centerY - 50);
        drawGraph(g, accelerationVerBuffer, "Vertical Acceleration (m/s^2): " + accelerationVerBuffer[accelerationVerBuffer.length-1],Color.GREEN, centerY - 100 );
        drawGraph(g, accelerationLatBuffer, "Lateral Acceleration (m/s^2): " + accelerationLatBuffer[accelerationLatBuffer.length-1],Color.BLUE, centerY - 150);

        drawGraph(g, rotationAccelerationLongBuffer, "Rotation Longitudinal Acceleration(ROW m/s^2): "+ rotationAccelerationLongBuffer[rotationAccelerationLongBuffer.length-1],Color.CYAN, centerY - 200);
        drawGraph(g, rotationAccelerationVerBuffer, "Rotation Vertical Acceleration(YAW m/s^2): " + rotationAccelerationVerBuffer[rotationAccelerationVerBuffer.length-1],Color.ORANGE, centerY - 250);
        drawGraph(g, rotationAccelerationLatBuffer, "Rotation Lateral Acceleration(PITCH m/s^2): " + rotationAccelerationLatBuffer[rotationAccelerationLatBuffer.length-1],Color.YELLOW, centerY - 300);

        drawGraph(g, velocityBuffer, "Velocity Longitudinal (m/s): " + velocityBuffer[velocityBuffer.length-1],Color.darkGray,centerY  - 350);
        drawGraph(g, bankDegreeBuffer, "Bank degree (Rad): " + bankDegreeBuffer[bankDegreeBuffer.length-1],Color.magenta,centerY - 400);

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

    public double calculateTotalGForce() {
        // Combine linear and rotational accelerations to calculate total G-force
        double totalAcceleration = 0;
        if(bank_degree == 0) {
            totalAcceleration = Math.sqrt(Math.pow(acceleration_Long, 2) + Math.pow(acceleration_Ver + GRAVITY, 2)
                    + Math.pow(acceleration_Lat, 2) + Math.pow(rotationAcceleration_Long, 2)
                    + Math.pow(rotationAcceleration_Ver, 2) + Math.pow(rotationAcceleration_Lat, 2));
        }else{
            totalAcceleration = Math.sqrt(Math.pow(acceleration_Long, 2) + Math.pow(acceleration_Ver + GRAVITY, 2)
                    + Math.pow(acceleration_Lat, 2) + Math.pow(rotationAcceleration_Long, 2)
                    + Math.pow(calculateAccelerationOfTurn(velocity_Long,Math.toRadians(bank_degree),rotationAcceleration_Ver), 2)
                    + Math.pow(rotationAcceleration_Lat, 2));
        }

        // Convert acceleration to G-force
        return totalAcceleration / 9.81; // Divide by gravitational acceleration to get G-force
    }

    double calculateTotalGForce_no_GRAVITY() {
        // Combine linear and rotational accelerations to calculate total G-force
        double totalAcceleration = 0;
        if(bank_degree==0) {
            totalAcceleration = Math.sqrt(Math.pow(acceleration_Long, 2) + Math.pow(acceleration_Ver, 2)
                    + Math.pow(acceleration_Lat, 2) + Math.pow(rotationAcceleration_Long, 2)
                    + Math.pow(rotationAcceleration_Ver, 2) + Math.pow(rotationAcceleration_Lat, 2));
        }else{
            totalAcceleration = Math.sqrt(Math.pow(acceleration_Long, 2) + Math.pow(acceleration_Ver, 2)
                    + Math.pow(acceleration_Lat, 2) + Math.pow(rotationAcceleration_Long, 2)
                    + Math.pow(calculateAccelerationOfTurn(velocity_Long,Math.toRadians(bank_degree),rotationAcceleration_Ver), 2)
                    + Math.pow(rotationAcceleration_Lat, 2));
        }
        //totalAcceleration = Math.sqrt(Math.pow(totalAcceleration,2) + Math.pow(GRAVITY,2));
        // Convert acceleration to G-force
        return totalAcceleration / 9.81; // Divide by gravitational acceleration to get G-force
    }
    //Calculate the degree of the cockpit with the given g-Force
    private double calculateDegree(double gForce){
        //System.out.println(Math.toDegrees(Math.atan(gForce/GRAVITY*9.81)));
        return Math.toDegrees(Math.atan(gForce/GRAVITY*9.81));
    }

    // Method to calculate the final x and y coordinates in a coordinate system
    public Point calculateFinalCoordinates(int x, int y, double acceleration_Long, double acceleration_Ver,
                                            double acceleration_Lat, double rotationAcceleration_Long,
                                            double rotationAcceleration_Ver, double rotationAcceleration_Lat,
                                            double velocity_Long, double bank_degree,int SCALE_FACTOR) {
        // Convert accelerations to pixel offsets from the center
        int pixelOffsetAcc_Long = (int) (acceleration_Long * SCALE_FACTOR); // Scale factor of 20 pixels per G
        int pixelOffsetAcc_Ver = (int) (acceleration_Ver * SCALE_FACTOR); // Scale factor of 20 pixels per G
        int pixelOffsetAcc_Lat = (int) (acceleration_Lat * SCALE_FACTOR); // Scale factor of 20 pixels per G
        int pixelOffsetRoll_Long = (int) (rotationAcceleration_Long * SCALE_FACTOR);
        int pixelOffsetRoll_Ver = 0;
        int pixelOffSetBank_Degree = 0;
        if(bank_degree==0){
            pixelOffsetRoll_Ver = (int) (rotationAcceleration_Ver * SCALE_FACTOR);
        }
        else{
            pixelOffSetBank_Degree = (int) (calculateAccelerationOfTurn(velocity_Long,Math.toRadians(bank_degree),rotationAcceleration_Ver) * SCALE_FACTOR);
        }
        int pixelOffsetRoll_Lat = (int) (rotationAcceleration_Lat * SCALE_FACTOR);

        // Update x coordinate
        x -= pixelOffsetAcc_Lat;
        x -= pixelOffsetRoll_Long;
        x -= pixelOffsetRoll_Ver;

        // Update y coordinate
        y += pixelOffsetRoll_Lat;
        y += pixelOffsetAcc_Long; // Adjusting for correct direction
        y += pixelOffsetAcc_Ver; // Invert the offset to align with GUI coordinates

        //Because the AccelerationOfTurn with bank_degree always positive
        if(bank_degree>=0){
            x -= pixelOffSetBank_Degree;
        }else{
            x += pixelOffSetBank_Degree;
        }

        // Return the final coordinates as a Point object
        return new Point(x, y);
    }

    double calculateAccelerationOfTurn(double velocity_Long,  double bank_degree_rad, double rotationAcceleration_Ver){
        if(velocity_Long<=0) System.out.println("STALL!!! PULL UP");
        double radius_of_turn = calculateRadiusOfTurn( velocity_Long, bank_degree_rad);
        double velocity_Long_pow2 = Math.pow(velocity_Long, 2);

        return Math.sqrt( Math.pow( (velocity_Long_pow2 / radius_of_turn), 2 ) + Math.pow( (radius_of_turn * rotationAcceleration_Ver) , 2) );
    }

    public double calculateRadiusOfTurn(double velocity_Long, double bank_degree_rad){
        double radius_of_turn =  (Math.pow(velocity_Long,2)) / (9.81*Math.tan(bank_degree_rad)) ;
        return radius_of_turn;
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
                int labelX = 10; // Adjust this value as needed for spacing
                g.drawString(name, labelX,  y1-5);
            }
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("G-Force Meter");
        GForceMeter gForceMeter = new GForceMeter();
        frame.add(gForceMeter);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        System.out.println("Test radius of turn:" + gForceMeter.calculateRadiusOfTurn(15,Math.toRadians(20)));
        System.out.println("g force of turn " + gForceMeter.calculateAccelerationOfTurn(30.86, Math.toRadians(20), 0)/9.81);

        // Define arrays for acceleration values and time delays
        /*
        In this extended set of dummy values, the takeoff phase is represented with increased duration and constant
        acceleration values to simulate the gradual acceleration along the x-axis (forward motion), lift-off along the
        y-axis, and climbing along the z-axis. Adjust the duration and values as needed for your simulation.
         */
        double[] acceleration_Long_Values = {-1, 0, 0, 0, 0, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0};// Acceleration in X-axis (forward motion)
        double[] acceleration_Ver_Values = {-2, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Acceleration in Y-axis (lift-off), standard Gravity 9.81 m/s^2. If the plane increase attitude, then this value also be increased
        double[] acceleration_Lat_Values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Acceleration in Z-axis (wingtip to wingtip)
        double[] rotationAcceleration_Long_Values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Example rotation acceleration in X-axis
        double[] rotationAcceleration_Ver_Values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Example rotation acceleration in Y-axis
        double[] rotationAcceleration_Lat_Values = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Example rotation acceleration in Z-axis
        double[] velocity_values = {0, 20, 0, 0, 0, 3, 6, 9, 12, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 20, 30}; //m/s
        double[] bank_degree_rad_values = {0, 0.6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.2, 0.4, 0.3, 0.4};


        int timeDelay = 500; // Example time delays in milliseconds

        // Iterate through arrays and set acceleration values periodically
        for (int i = 0; i < acceleration_Long_Values.length; i++) {
            double accelerationX = acceleration_Long_Values[i];
            double accelerationY = acceleration_Ver_Values[i];
            double accelerationZ = acceleration_Lat_Values[i];
            double rotationAccelerationX = rotationAcceleration_Long_Values[i];
            double rotationAccelerationY = rotationAcceleration_Ver_Values[i];
            double rotationAccelerationZ = rotationAcceleration_Lat_Values[i];
            double velocity = velocity_values[i];
            double bank_degree_rad = bank_degree_rad_values[i];
            // Schedule setting acceleration values with a time delay
            Timer timer = new Timer(timeDelay, e -> {
                gForceMeter.setAccelerations(accelerationX, accelerationY, accelerationZ,
                        rotationAccelerationX, rotationAccelerationY, rotationAccelerationZ,velocity, bank_degree_rad);
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
