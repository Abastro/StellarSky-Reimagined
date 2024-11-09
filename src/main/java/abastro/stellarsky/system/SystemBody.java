package abastro.stellarsky.system;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Denotes a celestial body in a system.
 */
public record SystemBody(Optional<CoordFrame.Rotating> frame, Visual visual) {
    public static final Codec<SystemBody> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CoordFrame.ROTATING_CODEC.optionalFieldOf("frame").forGetter(SystemBody::frame),
            Visual.CODEC.fieldOf("visual").forGetter(SystemBody::visual)).apply(instance, SystemBody::new));
}
