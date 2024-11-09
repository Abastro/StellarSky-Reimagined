package abastro.stellarsky.system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
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

    public static final MapCodec<Point> POINT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(Point::size)).apply(instance, Point::new));

    /**
     * Cubic visual, where size refers to the side length of cube.
     */
    public static record Cube(float size, ResourceLocation image) implements Visual {
    }

    public static final MapCodec<Cube> CUBE_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(Cube::size),
            ResourceLocation.CODEC.fieldOf("image").forGetter(Cube::image))
            .apply(instance, Cube::new));

    // To appease the dispatch codec
    public static enum VisualType {
        POINT("point", POINT_CODEC),
        CUBE("cube", CUBE_CODEC);

        String typeName;
        MapCodec<? extends Visual> codec;

        VisualType(String name, MapCodec<? extends Visual> codec) {
            this.typeName = name;
            this.codec = codec;
        };

        public String getTypeName() {
            return typeName;
        }

        public MapCodec<? extends Visual> getCodec() {
            return codec;
        }
    }

    public static final Codec<VisualType> TYPE_CODEC = Codec.STRING.comapFlatMap(str -> switch (str) {
        case "point" -> DataResult.success(VisualType.POINT);
        case "cube" -> DataResult.success(VisualType.CUBE);
        default -> DataResult.error(() -> str + "is not a valid visual type");
    }, VisualType::getTypeName);

    public static final Codec<Visual> CODEC = TYPE_CODEC.dispatch(visual -> switch (visual) {
        case Point(var size) -> VisualType.POINT;
        case Cube(var size, var image) -> VisualType.CUBE;
    }, VisualType::getCodec);
}
