package com.vitalarc.workout.domain;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AcwrCalculatorTest {

    @Test
    void testCalculateRatio() {
        // Acute (Last 7 days): 100 per day = 700
        // Chronic (Last 28 days): 100 per day = 2800. Avg = 700.
        // 700 / 700 = 1.0 ratio
        var acute = Arrays.asList(100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0);
        var chronic = Arrays.asList(100.0, 100.0, 100.0, 100.0);

        double result = AcwrCalculator.calculate(acute, chronic);
        assertEquals(1.0, result, 0.01);
    }
}
