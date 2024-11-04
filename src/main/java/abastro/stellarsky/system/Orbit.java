package abastro.stellarsky.system;

import org.joml.Vector3d;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Denotes an elliptic orbit using orbital elements.
 * <p>
 * The units are
 * - distance in AU
 * - time in year
 * - angle in degrees
 * 
 * @param a        semi-major axis
 * @param ecc      eccentricity
 * @param incl     inclination
 * @param longNode longitude of node
 * @param longPeri longitude of periapsis
 * @param meanLong (initial) mean longitude
 */
public record Orbit(
        float a,
        float ecc,
        float incl,
        float longNode,
        float longPeri,
        float meanLong) {

    /**
     * Computes position in ecliptic coordinates.
     * 
     * @param parentMassFactor mass factor of parent.
     * @param year             the time as year in fractions.
     * @return position at the year.
     */
    public Vector3d positionAtYear(float parentMassFactor, float year) {
        // Kepler's third law: a^3 / T^2 = massFactor
        var period = Math.sqrt(a * a * a / parentMassFactor);
        var argPeri = longPeri - longNode;
        // Current mean anomaly
        var curMeanAnomaly = meanLong - longPeri + 360.0 * year / period;
        // Mean anomaly in radians, in (-PI, PI) range
        var meanAnomalyRad = Math.IEEEremainder(curMeanAnomaly, 2 * Math.PI);
        var eccAnomalyRad = eccAnomalyFor(meanAnomalyRad);
        var relPos = new Vector3d(
                a * (Math.cos(eccAnomalyRad) - ecc), a * Math.sqrt(1 - ecc * ecc) * Math.sin(eccAnomalyRad), 0);
        // TODO Check the coordinates
        return relPos.rotateZ(-Math.toRadians(argPeri))
                .rotateX(-Math.toRadians(incl))
                .rotateZ(-Math.toRadians(longNode));
    }

    /**
     * Eccentric anomaly(E) given mean anomaly(M).
     * Satisfies M = E - ecc * sin(E), where ecc is the eccentricity.
     */
    private double eccAnomalyFor(double meanAnomaly) {
        return 0;
    }

    public static final Codec<Orbit> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("semi-major").forGetter(Orbit::a),
            Codec.FLOAT.fieldOf("eccentricity").forGetter(Orbit::ecc),
            Codec.FLOAT.fieldOf("inclination").forGetter(Orbit::incl),
            Codec.FLOAT.fieldOf("longitude-asc-node").forGetter(Orbit::longNode),
            Codec.FLOAT.fieldOf("longitude-periapsis").forGetter(Orbit::longPeri),
            Codec.FLOAT.fieldOf("mean-longitude").forGetter(Orbit::meanLong))
            .apply(instance, Orbit::new));

    public static final float YEAR_IN_DAY = 365.25f;
    public static final float DAY_IN_TICK = 24000f;
    public static final float AU_IN_KM = 1.5e8f;
}
