public class CentripetalAccelerationGForce {

    // Gravity constant in meters per second squared
    private static final double GRAVITY = 9.81;

    // Calculate G-force from rotation acceleration around the Z-axis
    public static double calculateGForce(double rotationAccelerationZ) {
        // Convert rotation acceleration to G-force
        double gForce = rotationAccelerationZ / GRAVITY;

        return gForce;
    }

    public static void main(String[] args) {
        // Example rotation acceleration along the Z-axis from MFS (in radians per second squared)
        double rotationAccelerationZ = 3; // Sample value, replace with actual data from MFS

        // Calculate G-force
        double gForce = calculateGForce(rotationAccelerationZ);

        // Print the calculated G-force
        System.out.println("G-force experienced by the passenger during the turn: " + gForce);
    }
}
