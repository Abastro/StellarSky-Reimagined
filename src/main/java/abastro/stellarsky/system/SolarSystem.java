package abastro.stellarsky.system;

import java.util.List;

import net.minecraft.resources.ResourceLocation;

/**
 * Denotes a solar system, especially its orbital properties.
 */
public class SolarSystem {
    public float rootMassFactor;
    public ResourceLocation rootBody;
    public List<Entry> planets;

    public static record Entry(Orbit orbit, ResourceLocation body, Node node) {
    }

    public static sealed interface Node {
    }
    public static record Parent(float massFactor, List<Entry> children) implements Node {
    }
    public static record Leaf() implements Node {
    }
}
