package abastro.stellarsky.system;

import java.util.List;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;

/**
 * Denotes a solar system, especially its orbital properties.
 */
public class SolarSystem {
    public float rootMassFactor;
    public ResourceLocation rootBody;
    public List<Entry> planets;

    /**
     * Denotes location of a body in the system.
     */
    public static record Entry(Orbit orbit, ResourceLocation body, Node node) {
    }

    public static sealed interface Node {
        public Either<Parent, Leaf> toEither();
    }

    public static record Parent(float massFactor, List<Entry> children) implements Node {
        @Override
        public Either<Parent, Leaf> toEither() {
            return Either.left(this);
        }
    }

    public static record Leaf() implements Node {
        @Override
        public Either<Parent, Leaf> toEither() {
            return Either.right(this);
        }
    }

    public static Node eitherToNode(Either<Parent, Leaf> either) {
        return either.map(parent -> parent, leaf -> leaf);
    }

    public static final Codec<Entry> ENTRY_CODEC = Codec.recursive(
            Entry.class.getSimpleName(),
            entryCodec -> {
                var parentCodec = RecordCodecBuilder.create(instParent -> instParent.group(
                        Codec.FLOAT.fieldOf("mass-factor").forGetter(Parent::massFactor),
                        entryCodec.listOf().fieldOf("children").forGetter(Parent::children))
                        .apply(instParent, Parent::new));
                var nodeCodec = Codec.either(
                        parentCodec,
                        Codec.unit(Leaf::new)).xmap(SolarSystem::eitherToNode, Node::toEither);
                return RecordCodecBuilder.create(instEntry -> instEntry.group(
                        Orbit.CODEC.fieldOf("orbit").forGetter(Entry::orbit),
                        ResourceLocation.CODEC.fieldOf("body").forGetter(Entry::body),
                        nodeCodec.fieldOf("node").forGetter(Entry::node))
                        .apply(instEntry, Entry::new));
            });
}
