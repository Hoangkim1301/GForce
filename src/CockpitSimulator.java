public class CockpitSimulator {
    // Assume the cockpit is represented by an angle relative to the vertical axis
    private double cockpitAngle;

    public CockpitSimulator() {
        this.cockpitAngle = 0; // Start with the cockpit level
    }

    // Update the cockpit angle based on the calculated G-force
    public void calculateCockpitAngle(double gForce) {
        // Convert G-force to tilt angle (this is a simplified calculation)
        // You may need to adjust this conversion based on the specifics of your simulator
        cockpitAngle = Math.toDegrees(Math.atan(gForce)); // Using simple trigonometry to determine angle
    }

    // Getter method to retrieve the current cockpit angle
    public double getCockpitAngle() {
        return cockpitAngle;
    }

    public static void main(String[] args) {
        // Create an instance of CockpitSimulator
        CockpitSimulator cockpitSimulator = new CockpitSimulator();

        // Calculate G-force (example value)
        double gForce = 1;

        // Update cockpit angle based on G-force
        cockpitSimulator.calculateCockpitAngle(gForce);

        // Retrieve and print the current cockpit angle
        double currentAngle = cockpitSimulator.getCockpitAngle();

        System.out.println("Current Cockpit Angle: " + currentAngle + " degrees");
    }
}