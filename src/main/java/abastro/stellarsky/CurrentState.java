package abastro.stellarsky;

import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix3dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import com.mojang.datafixers.util.Pair;

import abastro.stellarsky.system.SolarSystem;
import abastro.stellarsky.system.SystemBody;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;

public class CurrentState {
    private Registry<SystemBody> bodyRegistry;
    private Map<ResourceLocation, Positioning> positions;

    public CurrentState(RegistryAccess registries) {
        bodyRegistry = registries.registryOrThrow(StellarSky.SYSTEM_BODY_REGISTRY_KEY);
        positions = new HashMap<>();
    }

    public Map<ResourceLocation, Positioning> getPositions() {
        return positions;
    }

    public void updatePositions(SolarSystem system, int tick) {
        var basePos = new Vector3d(0, 0, 0);
        var curSec = tick / Constants.SECOND_IN_TICK;
        // Next tick in seconds
        var nextSec = (tick + 1) / Constants.SECOND_IN_TICK;
        for (var planet : system.planets()) {
            updatePosFor(planet, system.rootMassFactor(), curSec, nextSec, Pair.of(basePos, basePos));
        }
    }

    private void updatePosFor(SolarSystem.Entry entry, float parentMass, float curSec, float nextSec,
            Pair<Vector3dc, Vector3dc> parentPos) {
        Vector3dc curPos = entry.orbit().positionAtYear(parentMass, curSec / Constants.YEAR_IN_SEC)
                .add(parentPos.getFirst());
        Vector3dc nextPos = entry.orbit().positionAtYear(parentMass, nextSec / Constants.YEAR_IN_SEC)
                .add(parentPos.getSecond());
        var posPair = Pair.of(curPos, nextPos);

        var body = bodyRegistry.get(entry.body());
        var optCoord = body.frame().map(frame -> {
            Matrix3dc curCoord = frame.fromEcliptic(curSec);
            Matrix3dc nextCoord = frame.fromEcliptic(nextSec);
            return Pair.of(curCoord, nextCoord);
        });
        positions.put(entry.body(), new Positioning(posPair, optCoord));

        switch (entry.node()) {
            case SolarSystem.Parent(var mass, var children):
                for (var child : children)
                    updatePosFor(child, mass, curSec, nextSec, posPair);
                break;
            case SolarSystem.Leaf():
                break;
        }
    }

}
