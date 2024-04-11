import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GForceCalculatorTest {

    @Test
    void calculateTotalGForce_all_direction() {
        // Test case 1: acceleration in all directions
        GForceCalculator calculator1 = new GForceCalculator(3.0, 4.0, 5.0);
        assertEquals(Math.sqrt(3.0 * 3.0 + 4.0 * 4.0 + 5.0 * 5.0) / 9.81, calculator1.calculateTotalGForce(), 0.001);
    }

    @Test
    void calculateTotalGForce_one_direction() {
        // Test case 2: acceleration in only one direction
        GForceCalculator calculator2 = new GForceCalculator(0.0, 0.0, -9.81);
        assertEquals(Math.abs(-9.81) / 9.81, calculator2.calculateTotalGForce(), 0.001);
    }

    @Test
    void calculateTotalGForce_zero_acceleration() {
        // Test case 3: zero acceleration in all directions
        GForceCalculator calculator3 = new GForceCalculator(0.0, 0.0, 0.0);
        assertEquals(0.0, calculator3.calculateTotalGForce(), 0.001);
    }

    @Test
    void calculateDirection_positive_acceleration_all_direction() {
        // Test case 1: Acceleration in all positive directions
        GForceCalculator calculator1 = new GForceCalculator(1.0, 1.0, 1.0);
        assertEquals("Rightward Pitch Upward increase att", calculator1.calculateDirection());
    }

    @Test
    void calculateDirection_negative_acceleration_all_direction() {
        // Test case 2: Acceleration in all negative directions
        GForceCalculator calculator2 = new GForceCalculator(-1.0, -1.0, -1.0);
        assertEquals("Leftward Pitch Downward reduce att", calculator2.calculateDirection());
    }

    @Test
    void calculateDirection_acceleration_mixed_direction() {
        // Test case 3: Acceleration in mixed directions
        GForceCalculator calculator3 = new GForceCalculator(1.0, -1.0, 0.0);
        assertEquals("Rightward Pitch Downward", calculator3.calculateDirection());
    }

    @Test
    void calculateDirection_zero_acceleration_all_direction() {
        // Test case 4: Zero acceleration in all directions
        GForceCalculator calculator4 = new GForceCalculator(0.0, 0.0, 0.0);
        assertEquals("", calculator4.calculateDirection());
    }
}