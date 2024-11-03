package abastro.stellarsky;

import java.util.Map;

import org.joml.Vector3d;

import abastro.stellarsky.system.SolarSystem;
import net.minecraft.resources.ResourceLocation;

public class CurrentState {
    private Map<ResourceLocation, Positioning> positions;

    public Map<ResourceLocation, Positioning> getPositions() {
      return positions;
    }

    public void updatePositions(SolarSystem system, float year) {
        var basePos = new Vector3d(0, 0, 0);
        for (var planet : system.planets) {
            updatePosFor(planet, system.rootMassFactor, year, basePos);
        }
    }

    private void updatePosFor(SolarSystem.Entry entry, float parentMass, float year, Vector3d parentPos) {
        var pos = entry.orbit().positionAtYear(parentMass, year).add(parentPos);
        // TODO How to update position?
        switch (entry.node()) {
            case SolarSystem.Parent(var mass, var children):
                for (var child : children)
                    updatePosFor(child, mass, year, pos);
                break;
            case SolarSystem.Leaf():
                break;
        }
    }

}
