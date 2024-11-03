package abastro.stellarsky;

import java.util.Optional;

import org.joml.Matrix3f;
import org.joml.Matrix3fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mojang.datafixers.util.Pair;

/**
 * Positioning of the object.
 * Coordinates is optionally given.
 */
public record Positioning(Pair<Vector3fc, Vector3fc> pos, Optional<Pair<Matrix3fc, Matrix3fc>> coord) {
    public Vector3f interpPos(float partial) {
        var current = pos.getFirst();
        var next = pos.getSecond();
        var diff = next.sub(current, new Vector3f());
        return diff.mulAdd(partial, current);
    }

    public Optional<Matrix3f> interpCoord(float partial) {
        return coord.map((crd) -> {
            var current = crd.getFirst();
            var next = crd.getSecond();
            var diff = next.sub(current, new Matrix3f());
            return diff.scale(partial).add(current);
        });
    }
}
