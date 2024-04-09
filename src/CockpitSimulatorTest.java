import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CockpitSimulatorTest {

    @Test
    void calculateCockpitAngle() {
        CockpitSimulator cockpitSimulator = new CockpitSimulator();

        // Test with G-force of 1
        cockpitSimulator.calculateCockpitAngle(1);
        assertEquals(45, cockpitSimulator.getCockpitAngle(), 0.0001);

        // Test with G-force of 0
        cockpitSimulator.calculateCockpitAngle(0);
        assertEquals(0, cockpitSimulator.getCockpitAngle(), 0.0001);

        // Test with negative G-force
        cockpitSimulator.calculateCockpitAngle(-1);
        assertEquals(-45, cockpitSimulator.getCockpitAngle(), 0.0001);

        // Test with large positive G-force
        cockpitSimulator.calculateCockpitAngle(0.82);
        assertEquals(39.35, cockpitSimulator.getCockpitAngle(), 0.1);

        // Test with large negative G-force
        cockpitSimulator.calculateCockpitAngle(2);
        assertEquals(63.4, cockpitSimulator.getCockpitAngle(), 0.1);

    }
}