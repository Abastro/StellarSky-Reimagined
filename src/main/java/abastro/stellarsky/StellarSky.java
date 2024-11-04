package abastro.stellarsky;

import abastro.stellarsky.system.SystemBody;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(StellarSky.MODID)
public class StellarSky {
    public static final String MODID = "stellarsky-reimagined";
    // private static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<Registry<SystemBody>> SYSTEM_BODY_REGISTRY_KEY = ResourceKey
            .createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "system-body"));

    public StellarSky(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        // TODO Fill in the null with CODECs
        event.dataPackRegistry(SYSTEM_BODY_REGISTRY_KEY, null, null);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
