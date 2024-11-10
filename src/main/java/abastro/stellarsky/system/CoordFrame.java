package abastro.stellarsky.system;

import org.joml.Matrix3d;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface CoordFrame {
    /**
     * @param seconds current total time in seconds
     * @return Conversion matrix from Ecliptic coordinates to this coordinates.
     */
    Matrix3d fromEcliptic(float seconds);

    public static class Ecliptic implements CoordFrame {
        @Override
        public Matrix3d fromEcliptic(float seconds) {
            return new Matrix3d(); // Identity
        }
    }

    public static class Equatorial implements CoordFrame {
        @Override
        public Matrix3d fromEcliptic(float seconds) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'fromEcliptic'");
        }
    }

    public static final CoordFrame ECLIPTIC = new Ecliptic();
    public static final CoordFrame EQUATORIAL = new Equatorial();

    public static final Codec<Rotating> ROTATING_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("axial-tilt").forGetter(Rotating::axialTilt),
            Codec.FLOAT.fieldOf("pole-longitude").forGetter(Rotating::poleLongitude),
            Codec.FLOAT.fieldOf("rotation-period").forGetter(Rotating::rotationPeriod))
            .apply(instance, Rotating::new));

    // TODO Check out if this is appropriate
    public static record Rotating(float axialTilt, float poleLongitude, float rotationPeriod) implements CoordFrame {
        @Override
        public Matrix3d fromEcliptic(float seconds) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'fromEcliptic'");
        }
    }
}
