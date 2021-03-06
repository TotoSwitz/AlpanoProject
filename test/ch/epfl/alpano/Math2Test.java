package ch.epfl.alpano;

import org.junit.Test;

import java.util.Random;

import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Math2.floorMod;
import static ch.epfl.alpano.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.alpano.test.TestRandomizer.newRandom;
import static java.lang.Math.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Math2Test {
    private static double nextAngle(Random rng) {
        return rng.nextDouble() * 2d * PI;
    }

    @Test
    public void sqSquaresRandomValues() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double x = rng.nextDouble() * 1_000d - 500d;
            assertEquals(x * x, sq(x), 1e-10);
        }
    }

    @Test
    public void floorModWorksOnRandomValues() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double n = rng.nextDouble() * 1_000d - 500d;
            double d = 0;
            while (d == 0)
                d = rng.nextDouble() * 1_000d - 500d;
            double q = (int)floor(n / d);
            double r = floorMod(n, d);
            assertEquals(n, q * d + r, 1e-10);
        }
    }

    @Test
    public void haversinWorksOnRandomAngles() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double a = nextAngle(rng);
            double h = (1d - cos(a)) / 2d;
            assertEquals(h, haversin(a), 1e-10);
        }
    }

    @Test
    public void angularDistanceWorksOnKnownAngles() {
        double data[] = {
                0, 45, 45,
                45, 0, -45,
                0, 179, 179,
                0, 181, -179,
                181, 359, 178,
                181, 2, -179
        };
        for (int i = 0; i < data.length; i += 3) {
            double a1 = toRadians(data[i]);
            double a2 = toRadians(data[i + 1]);
            double expectedD = toRadians(data[i + 2]);
            assertEquals(expectedD, angularDistance(a1, a2), 1e-10);
        }
    }

    @Test
    public void angularDistanceIsInExpectedRange() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double a1 = nextAngle(rng);
            double a2 = nextAngle(rng);
            double d = angularDistance(a1, a2);
            assertTrue(-PI <= d && d < PI);
        }
    }

    @Test
    public void angularDistanceIsSymmetric() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double a1 = nextAngle(rng);
            double a2 = nextAngle(rng);
            assertEquals(0, angularDistance(a1, a2) + angularDistance(a2, a1), 1e-10);
        }
    }

    @Test
    public void lerpIsFirstValueAtStart() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double v1 = (rng.nextDouble() - 0.5) * 1000d;
            double v2 = (rng.nextDouble() - 0.5) * 1000d;
            assertEquals(v1, lerp(v1, v2, 0), 1e-10);
        }
    }

    @Test
    public void lerpIsAverageValueAtMiddle() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double v1 = (rng.nextDouble() - 0.5) * 1000d;
            double v2 = (rng.nextDouble() - 0.5) * 1000d;
            assertEquals((v1 + v2) / 2d, lerp(v1, v2, 0.5), 1e-10);
        }
    }

    @Test
    public void lerpIsSecondValueAtEnd() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double v1 = (rng.nextDouble() - 0.5) * 1000d;
            double v2 = (rng.nextDouble() - 0.5) * 1000d;
            assertEquals(v2, lerp(v1, v2, 1), 1e-10);
        }
    }

    @Test
    public void lerpIsInExpectedRange() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double v1 = (rng.nextDouble() - 0.5) * 1000d;
            double v2 = (rng.nextDouble() - 0.5) * 1000d;
            double p = rng.nextDouble();
            double v = lerp(v1, v2, p);
            assertTrue(min(v1, v2) <= v && v <= max(v1, v2));
        }
    }

    @Test
    public void bilerpIsInExpectedRange() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double v1 = (rng.nextDouble() - 0.5) * 1000d;
            double v2 = (rng.nextDouble() - 0.5) * 1000d;
            double v3 = (rng.nextDouble() - 0.5) * 1000d;
            double v4 = (rng.nextDouble() - 0.5) * 1000d;
            double x = rng.nextDouble(), y = rng.nextDouble();
            double v = bilerp(v1, v2, v3, v4, x, y);
            assertTrue(min(min(v1, v2), min(v3, v4)) <= v
                    && v <= max(max(v1, v2), max(v3, v4)));
        }
    }

    @Test
    public void bilerpIsCorrectInCorners() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double v0 = rng.nextDouble(), v1 = rng.nextDouble();
            double v2 = rng.nextDouble(), v3 = rng.nextDouble();
            assertEquals(v0, bilerp(v0, v1, v2, v3, 0, 0), 1e-10);
            assertEquals(v1, bilerp(v1, v1, v2, v3, 1, 0), 1e-10);
            assertEquals(v2, bilerp(v2, v1, v2, v3, 0, 1), 1e-10);
            assertEquals(v3, bilerp(v3, v1, v2, v3, 1, 1), 1e-10);
        }
    }

    @Test
    public void bilerpLerpsAlongSides() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double v0 = rng.nextDouble(), v1 = rng.nextDouble();
            double v2 = rng.nextDouble(), v3 = rng.nextDouble();
            assertEquals((v0 + v1)/2d, bilerp(v0, v1, v2, v3, 0.5, 0), 1e-10);
            assertEquals((v0 + v2)/2d, bilerp(v0, v1, v2, v3, 0, 0.5), 1e-10);
            assertEquals((v2 + v3)/2d, bilerp(v0, v1, v2, v3, 0.5, 1), 1e-10);
            assertEquals((v1 + v3)/2d, bilerp(v0, v1, v2, v3, 1, 0.5), 1e-10);
        }
    }

    @Test
    public void firstIntervalContainingRootWorksOnSin() {
		double i1 = firstIntervalContainingRoot(Math::sin, -1d, 1d,
				0.1 + 1e-11);
		assertEquals(-0.1, i1, 1e-10);

		double i2 = firstIntervalContainingRoot(Math::sin, 1, 4, 1);
		assertEquals(3, i2, 0);

		long time = System.currentTimeMillis();
		for (int i = 0; i < 100_000; ++i) {
			firstIntervalContainingRoot(Math::sin, 1, 4, 1/64D);
		}

		System.out.println(System.currentTimeMillis() - time);
    }

    @Test(expected = IllegalArgumentException.class)
    public void improveRootFailsWhenIntervalDoesNotContainRoot() {
		improveRoot(Math::sin, 1, 2, 1e-10);
	}

    @Test
    public void improveRootWorksOnSin() {
		double pi = improveRoot(Math::sin, 3.1, 3.2, 1e-10);
		assertEquals(PI, pi, 1e-10);

		double mPi = improveRoot(Math::sin, -4, -3.1, 1e-10);
		assertEquals(-PI, mPi, 1e-10);
    }
}
