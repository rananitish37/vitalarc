package com.vitalarc.workout.domain;

import java.util.List;

public class AcwrCalculator {

    public static double calculate(List<Double> last7DaysLoad, List<Double> last28DaysLoad) {
        double acuteLoad = last7DaysLoad.stream().mapToDouble(Double::doubleValue).sum();
        double chronicLoad = last28DaysLoad.stream().mapToDouble(Double::doubleValue).sum() / 4.0;

        if (chronicLoad == 0) return 0.0;
        return acuteLoad / chronicLoad;
    }
}