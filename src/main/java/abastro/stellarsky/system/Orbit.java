package abastro.stellarsky.system;

import org.joml.Vector3d;

/**
 * Denotes an elliptic orbit using orbital parameters.
 * <p>
 * The units are in
 * - AU for distance
 * - year for time
 */
public record Orbit(float semiMajor, float eccentricity) {
    /**
     * @param parentMassFactor mass factor of parent.
     * @param year the time as year in fractions.
     * @return position at the year.
     */
    public Vector3d positionAtYear(float parentMassFactor, float year) {
        // TODO Implement
        return null;
    }
}
