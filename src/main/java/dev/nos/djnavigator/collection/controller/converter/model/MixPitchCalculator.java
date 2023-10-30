package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.model.Track;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Comparator.naturalOrder;

public class MixPitchCalculator {

    public static BigDecimal calculatePitch(Track staticTrack, Track dynamicTrack) {
        try {
            return levelledStaticTrackTempo(staticTrack.getTempo(), dynamicTrack.getTempo())
                    .subtract(dynamicTrack.getTempo())
                    .multiply(valueOf(100))
                    .divide(dynamicTrack.getTempo(), HALF_UP)
                    .setScale(1, HALF_UP);
        } catch (Exception e) {
            return new BigDecimal("0");
        }
    }

    private static BigDecimal levelledStaticTrackTempo(BigDecimal staticVal, BigDecimal dynamicVal) {

        final var defaultRatio = ratio(dynamicVal, staticVal);

        final var multipliedStaticVal = staticVal.multiply(valueOf(2));
        final var multipliedRatio = ratio(dynamicVal, multipliedStaticVal);

        final var dividedStaticVal = staticVal.divide(valueOf(2), HALF_UP);
        final var dividedRatio = ratio(dynamicVal, dividedStaticVal);

        final var minRatio = Stream.of(defaultRatio, multipliedRatio, dividedRatio)
                .min(naturalOrder())
                .get();

        final Map<BigDecimal, BigDecimal> ratioMap = new HashMap<>();
        ratioMap.put(defaultRatio, staticVal);

        if (!ratioMap.containsKey(multipliedRatio)) {
            ratioMap.put(multipliedRatio, multipliedStaticVal);
        }

        if (!ratioMap.containsKey(dividedStaticVal)) {
            ratioMap.put(dividedRatio, dividedStaticVal);
        }

        return ratioMap.get(minRatio);
    }

    private static BigDecimal ratio(BigDecimal val1, BigDecimal val2) {
        return val2.doubleValue() > val1.doubleValue()
                ? val2.divide(val1, HALF_UP)
                : val1.divide(val2, HALF_UP);
    }

}
