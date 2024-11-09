package abastro.stellarsky.system;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Denotes a celestial body in a system.
 */
public record SystemBody(Optional<CoordFrame> frame, Visual visual) {
    // TODO Figure out local coordinates system
    public static record CoordFrame(float axialTilt, float rotationPeriod) {
    }

    public static final Codec<CoordFrame> COORD_CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.FLOAT.fieldOf("axial-tilt").forGetter(CoordFrame::axialTilt),
                Codec.FLOAT.fieldOf("rotation-period").forGetter(CoordFrame::rotationPeriod))
                .apply(instance, CoordFrame::new);
    });

    public static final Codec<SystemBody> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            COORD_CODEC.optionalFieldOf("frame").forGetter(SystemBody::frame),
            Visual.CODEC.fieldOf("visual").forGetter(SystemBody::visual)).apply(instance, SystemBody::new));
}
