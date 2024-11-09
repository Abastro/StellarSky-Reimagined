package abastro.stellarsky;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        var curTickYr = tick / Constants.YEAR_IN_TICK;
        var nextTickYr = (tick + 1) / Constants.YEAR_IN_TICK;
        for (var planet : system.planets) {
            updatePosFor(planet, system.rootMassFactor, curTickYr, nextTickYr, Pair.of(basePos, basePos));
        }
    }

    private void updatePosFor(SolarSystem.Entry entry, float parentMass, float curTickYr, float nextTickYr,
            Pair<Vector3dc, Vector3dc> parentPos) {
        Vector3dc curPos = entry.orbit().positionAtYear(parentMass, curTickYr).add(parentPos.getFirst());
        Vector3dc nextPos = entry.orbit().positionAtYear(parentMass, nextTickYr).add(parentPos.getSecond());
        var posPair = Pair.of(curPos, nextPos);

        var body = bodyRegistry.get(entry.body());
        // TODO How to update coordinates?
        positions.put(entry.body(), new Positioning(posPair, Optional.empty()));

        switch (entry.node()) {
            case SolarSystem.Parent(var mass, var children):
                for (var child : children)
                    updatePosFor(child, mass, curTickYr, nextTickYr, posPair);
                break;
            case SolarSystem.Leaf():
                break;
        }
    }

}
