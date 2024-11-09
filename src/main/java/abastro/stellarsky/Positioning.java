package abastro.stellarsky;

import java.util.Optional;

import org.joml.Matrix3d;
import org.joml.Matrix3dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import com.mojang.datafixers.util.Pair;

/**
 * Positioning of the object.
 * Coordinates is optionally given.
 */
public record Positioning(Pair<Vector3dc, Vector3dc> pos, Optional<Pair<Matrix3dc, Matrix3dc>> coord) {
    public Vector3d interpPos(float partial) {
        var current = pos.getFirst();
        var next = pos.getSecond();
        var diff = next.sub(current, new Vector3d());
        return diff.mulAdd(partial, current);
    }

    public Optional<Matrix3d> interpCoord(float partial) {
        return coord.map((crd) -> {
            var current = crd.getFirst();
            var next = crd.getSecond();
            var diff = next.sub(current, new Matrix3d());
            return diff.scale(partial).add(current);
        });
    }
}
