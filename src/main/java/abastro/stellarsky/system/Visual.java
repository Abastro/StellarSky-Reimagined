package abastro.stellarsky.system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;

/**
 * The way a celestial body is is visible.
 */
public sealed interface Visual permits Visual.Point, Visual.Cube {
    /**
     * Point visual, where size is to compute brightness.
     */
    public static record Point(float size) implements Visual {
    }

    public static final Codec<Point> POINT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(Point::size)).apply(instance, Point::new));

    /**
     * Cubic visual, where size refers to the side length of cube.
     */
    public static record Cube(float size, ResourceLocation image) implements Visual {
    }

    public static final Codec<Cube> CUBE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(Cube::size),
            ResourceLocation.CODEC.fieldOf("image").forGetter(Cube::image))
            .apply(instance, Cube::new));
}
