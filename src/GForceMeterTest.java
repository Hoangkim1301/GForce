import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.swing.*;
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

        gForceMeter.setAccelerations(accelerationX, accelerationY, accelerationZ,
                rotationAccelerationX, rotationAccelerationY, rotationAccelerationZ);

        assertEquals(accelerationX, gForceMeter.acceleration_Long);
        assertEquals(accelerationY, gForceMeter.acceleration_Ver);
        assertEquals(accelerationZ, gForceMeter.acceleration_Lat);
        assertEquals(rotationAccelerationX, gForceMeter.rotationAcceleration_Long);
        assertEquals(rotationAccelerationY, gForceMeter.rotationAcceleration_Ver);
        assertEquals(rotationAccelerationZ, gForceMeter.rotationAcceleration_Lat);
    }

    //When airplane accelerate
    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_1(){
        gForceMeter.setAccelerations(5,0,0,0,0,0);
        double gForce_no_GRAVITY = 0.5;
        assertEquals(gForce_no_GRAVITY, gForceMeter.calculateTotalGForce_no_GRAVITY(), 0.01);
    }

    //when airplane accelerate (with GRAVITY)
    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_2(){
        gForceMeter.setAccelerations(5,0,0,0,0,0);
        double gForce_with_gravity = 1.12;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    //Test from here always with GRAVITY

    //When takeoff and with vertical acceleration = 3m/s^2
    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_3(){
        gForceMeter.setAccelerations(5,3,0,0,0,0);
        double gForce_with_gravity = 1.401;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_4(){
        gForceMeter.setAccelerations(0,0,0,0,0,0);
        double gForce_with_gravity = 1;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }

    @Test
    public void testCockpitDegreeWithLongitudinalAccelerate_5(){
        gForceMeter.setAccelerations(-1,-2,0,0,0,0);
        double gForce_with_gravity = 0.8;
        assertEquals(gForce_with_gravity, gForceMeter.calculateTotalGForce(), 0.01);
    }


    @Test
    public void testFeetToMeter() {
        GForceMeter gForceMeter = new GForceMeter();

        // Test with a feet value
        double feetValue = 10.0;
        double expectedMeterValue = 3.048; // 10 feet = 3.048 meters
        double result = gForceMeter.feet_to_meter(feetValue);
        assertEquals(expectedMeterValue, result, 0.001); // Using delta for double comparison

        // Test with a feet value
        feetValue = 20.0;
        expectedMeterValue = 6.096; // 20 feet = 6.096 meters
        result = gForceMeter.feet_to_meter(feetValue);
        assertEquals(expectedMeterValue, result, 0.001); // Using delta for double comparison

        // Add more test cases as needed
    }


}