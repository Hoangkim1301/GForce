public class GForceCalculator {
    private double accelerationX;
    private double accelerationY;
    private double accelerationZ;

    public GForceCalculator(double accelerationX, double accelerationY, double accelerationZ) {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
        this.accelerationZ = accelerationZ;
    }

    public double calculateTotalGForce() {
        double totalAcceleration = Math.sqrt(Math.pow(accelerationX, 2) + Math.pow(accelerationY, 2) + Math.pow(accelerationZ, 2));
        double gForce = totalAcceleration / 9.81; // Divide by gravitational acceleration to get G-force
        return gForce;
    }

    public String calculateDirection() {
        // Determine the direction based on the signs of acceleration along each axis
        StringBuilder direction = new StringBuilder();
        if (accelerationX > 0) {
            direction.append("Rightward ");
        } else if (accelerationX < 0) {
            direction.append("Leftward ");
        }

        if (accelerationY > 0) {
            direction.append("Pitch Upward ");
        } else if (accelerationY < 0) {
            direction.append("Pitch Downward ");
        }

        if (accelerationZ > 0) {
            direction.append("increase att");
        } else if (accelerationZ < 0) {
            direction.append("reduce att");
        }

        return direction.toString().trim();
    }

    public static void main(String[] args) {
        // Example usage:
        // Assuming accelerationX, accelerationY, and accelerationZ are obtained from Microsoft Flight Simulator
        // You need to replace these with actual values obtained from the simulator

        double accelerationX = 10; // making a right turn
        double accelerationY = 5; // pitch up
        double accelerationZ = 20; // increase attitude

        GForceCalculator calculator = new GForceCalculator(accelerationX, accelerationY, accelerationZ);
        double totalGForce = calculator.calculateTotalGForce();
        String direction = calculator.calculateDirection();

        System.out.println("Total G-force: " + String.format("%.2f", totalGForce) + " G");
        System.out.println("Direction: " + direction);
    }
}
