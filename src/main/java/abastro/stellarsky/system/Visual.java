package abastro.stellarsky.system;

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

    /**
     * Cubic visual, where size refers to the side length of cube.
     */
    public static record Cube(float size, ResourceLocation image) implements Visual {
    }
}
