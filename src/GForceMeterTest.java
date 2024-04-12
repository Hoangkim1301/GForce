import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GForceMeterTest {
    private static GForceMeter gForceMeter;

    @BeforeAll
    public static void setup() {
        JFrame frame = new JFrame("Test Frame");
        gForceMeter = new GForceMeter();
        frame.add(gForceMeter);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Test
    public void testSetAccelerations() {
        double accelerationX = 2.5;
        double accelerationY = 1.5;
        double accelerationZ = 3.0;
        double rotationAccelerationX = 0.5;
        double rotationAccelerationY = -0.5;
        double rotationAccelerationZ = 1.0;
        double velocity_long = 20.0;
        double bank_degree = -10.0;

        gForceMeter.setAccelerations(accelerationX, accelerationY, accelerationZ,
                rotationAccelerationX, rotationAccelerationY, rotationAccelerationZ,velocity_long, bank_degree);

        assertEquals(accelerationX, gForceMeter.acceleration_Long);
        assertEquals(accelerationY, gForceMeter.acceleration_Ver);
        assertEquals(accelerationZ, gForceMeter.acceleration_Lat);
        assertEquals(rotationAccelerationX, gForceMeter.rotationAcceleration_Long);
        assertEquals(rotationAccelerationY, gForceMeter.rotationAcceleration_Ver);
        assertEquals(rotationAccelerationZ, gForceMeter.rotationAcceleration_Lat);
        assertEquals(velocity_long,gForceMeter.velocity_Long);
        assertEquals(Math.toDegrees(bank_degree),gForceMeter.bank_degree);
    }

    //When airplane accelerate
    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_1(){
        gForceMeter.setAccelerations(5,0,0,0,0,0,0,0);
        double gForce_no_GRAVITY = 0.5;
        assertEquals(gForce_no_GRAVITY, gForceMeter.calculateTotalGForce_no_GRAVITY(), 0.01);
    }

    //when airplane accelerate (with GRAVITY)
    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_2(){
        gForceMeter.setAccelerations(5,0,0,0,0,0,0,0);
        double gForce_with_gravity = 1.12;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    //Test from here always with GRAVITY

    //When takeoff and with vertical acceleration = 3m/s^2
    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_3(){
        gForceMeter.setAccelerations(5,3,0,0,0,0,0,0);
        double gForce_with_gravity = 1.401;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    //When airplane stand still only have 1g from GRAVITY
    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_4(){
        gForceMeter.setAccelerations(0,0,0,0,0,0,0,0);
        double gForce_with_gravity = 1;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    //when airplane reduce velocity and attitude
    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_5(){
        gForceMeter.setAccelerations(-1,-2,0,0,0,0,0,0);
        double gForce_with_gravity = 0.8;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    @Test
    public void testCockpitDegreeWhileLanding_1(){
        // Acceleration in Long-axis (forward motion - deceleration during landing)
        // Acceleration in Ver-axis (lift-off - reduced due to landing)
        // Acceleration in Z-axis (wingtip to wingtip) - slight side movement possible
        // Example rotation acceleration in X-axis - minor pitch change
        // Example rotation acceleration in Y-axis - some roll during touchdown
        // Example rotation acceleration in Z-axis - potential yaw for course correction
        gForceMeter.setAccelerations(-2,-1.5,0.2,0.1,0.3,0.5,0,0);
        double gForce_with_gravity = 0.87;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    //Yaw Maneuver to right
    @Test
    public void testCockpitDegreeWhileMaking_U_turn_1(){
        gForceMeter.setAccelerations(-0.2,0,2,1,0.5,0,0,0);
        double gForce_with_gravity = 1.03;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    //Yaw Maneuver to left
    @Test
    public void testCockpitDegreeWhileMaking_U_turn_2(){
        gForceMeter = new GForceMeter();
        gForceMeter.setAccelerations(0,0,0,0,0,0, 30, -0.4);
        double gForce_with_gravity = 1.09;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    @Test
    public void testFeetToMeter() {
        GForceMeter gForceMeter = new GForceMeter();

        // Test with feet value
        double feetValue = 10.0;
        double expectedMeterValue = 3.048; // 10 feet = 3.048 meters
        double result = gForceMeter.feet_to_meter(feetValue);
        assertEquals(expectedMeterValue, result, 0.001); // Using delta for double comparison

        // Test with feet value
        feetValue = 20.0;
        expectedMeterValue = 6.096; // 20 feet = 6.096 meters
        result = gForceMeter.feet_to_meter(feetValue);
        assertEquals(expectedMeterValue, result, 0.001); // Using delta for double comparison
    }

    @Test
    public void testCalculateTotalGForce_zero_acceleration() {
        // Test case 1: All accelerations zero
        GForceMeter meter1 = new GForceMeter();
        assertEquals(1.0, meter1.calculateTotalGForce(), 0.001);
    }

    @Test
    public void testCalculateTotalGForce_one_direction() {
        // Test case 2: Acceleration only in one direction
        GForceMeter meter2 = new GForceMeter();
        meter2.setAccelerations(5.0, 0.0, 0.0, 0.0, 0.0, 0.0,0,0);
        assertEquals( 1.1224, meter2.calculateTotalGForce(), 0.001);
    }

    @Test
    public void testCalculateTotalGForce_multiple_directions() {
        // Test case 3: Acceleration in multiple directions
        GForceMeter meter3 = new GForceMeter();
        meter3.setAccelerations(3.0, 4.0, 5.0, 1.0, 2.0, 3.0,0,0);
        assertEquals(1.574,meter3.calculateTotalGForce(), 0.001);
    }

    @Test
    public void testCalculateTotalGForce_no_GRAVITY_no_acceleration() {
        // Test case 1: All accelerations zero
        GForceMeter meter1 = new GForceMeter();
        meter1.setAccelerations(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
        assertEquals(0.0, meter1.calculateTotalGForce_no_GRAVITY(), 0.001);
    }

    @Test
    public void testCalculateTotalGForce_no_GRAVITY_one_direction() {
        // Test case 2: Acceleration only in one direction
        GForceMeter meter2 = new GForceMeter();
        meter2.setAccelerations(5.0, 0.0, 0.0, 0.0, 0.0, 0.0,0,0);
        assertEquals(0.5097, meter2.calculateTotalGForce_no_GRAVITY(), 0.001);
    }

    @Test
    public void testCalculateTotalGForce_no_GRAVITY_multiple_directions() {
        // Test case 3: Acceleration in multiple directions
        GForceMeter meter3 = new GForceMeter();
        meter3.setAccelerations(3.0, 4.0, 5.0, 1.0, 2.0, 3.0,0,0);
        assertEquals(0.815,
                meter3.calculateTotalGForce_no_GRAVITY(), 0.001);
    }

    @Test
    public void calculateFinalCoordinates_zero_acceleration() {
        // Test case 1: All accelerations zero
        GForceMeter meter = new GForceMeter();
        Point result1 = meter.calculateFinalCoordinates(100, 100, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 20);
        assertEquals(new Point(100, 100), result1);
    }

    @Test
    public void calculateFinalCoordinates_non_zero_acceleration_bank_degree_0() {
        // Test case 2: Non-zero accelerations and rotation with bank degree of 0
        GForceMeter meter = new GForceMeter();
        Point result2 = meter.calculateFinalCoordinates(100, 100, 10.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 20);
        int expectedX2 = 100;
        int expectedY2 = 300;
        assertEquals(new Point(expectedX2, expectedY2), result2);
    }

    @Test
    public void calculateFinalCoordinates_non_zero_acceleration_bank_degree_non_zero() {
        // Test case 3: Non-zero accelerations and rotation with bank degree not equal to 0
        GForceMeter meter = new GForceMeter();
        Point result3 = meter.calculateFinalCoordinates(100, 100, 10.0, 0.0, 0.0, 10.0, 0.0, 0.0, 10.0, 30.0, 20);
        int expectedX3 = -213;
        int expectedY3 = 300;
        assertEquals(new Point(expectedX3, expectedY3), result3);
    }

    @Test
    public void calculateFinalCoordinates_different_coordinates_and_velocity() {
        // Test case 4: Different coordinates and velocity
        GForceMeter meter = new GForceMeter();
        Point result4 = meter.calculateFinalCoordinates(50, 50, 2.0, 1.0, -1.0, 0.5, -1.0, -0.5, 15.0, -15.0, 20);
        int expectedX4 = 1772;
        int expectedY4 = 100;
        assertEquals(new Point(expectedX4, expectedY4), result4);
    }

    //Test the centripetal acceleration of turn method
    @Test
    public void calculateAccelerationOfTurn_Test_01(){
        double centripetal_acceleration = 3.5; // m/s^2
        double centripetal_gForce = centripetal_acceleration/9.8;

        assertEquals(3.5, gForceMeter.calculateAccelerationOfTurn(30.86, Math.toRadians(20), 0), 0.1);
        assertEquals(0.36, gForceMeter.calculateAccelerationOfTurn(30.86, Math.toRadians(20), 0)/9.81, 0.1);
    }

    //Test the Radius and diameter of turn for an airplane with given velocity and bank-angle
    //Right horizontal turn in
    @Test
    public void calculateRadiusOfTurn_Test_01(){
        double velocity = 15; // m/s
        double bank_degree = 20; // degree
        double radius_of_turn = 63.01; //meter
        double diameter_of_turn = 126.02; //meter

        assertEquals(radius_of_turn,gForceMeter.calculateRadiusOfTurn(velocity,Math.toRadians(bank_degree)),0.1); // Radius test
        assertEquals(diameter_of_turn, 2 * gForceMeter.calculateRadiusOfTurn(velocity,Math.toRadians(bank_degree)),0.1); // Diameter test
    }

    //Test the Radius and diameter of turn for an airplane with given velocity and bank-angle
    //Left horizontal turn in
    @Test
    public void calculateRadiusOfTurn_Test_02(){
        double velocity = 50; // m/s
        double bank_degree = 10; // degree
        double radius_of_turn = 1445.2; //meter
        double diameter_of_turn = 2890.5; //meter

        assertEquals(radius_of_turn,gForceMeter.calculateRadiusOfTurn(velocity,Math.toRadians(bank_degree)),0.1); // Radius test
        assertEquals(diameter_of_turn, 2 * gForceMeter.calculateRadiusOfTurn(velocity,Math.toRadians(bank_degree)),0.1); // Diameter test
    }
}