package moe.vcup.hexisle.fabric;

import moe.vcup.hexisle.Hexisle;
import moe.vcup.hexisle.HexislePlatform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Fabric entry point, referenced from {@code fabric.mod.json}.
 *
 * <p>It wires the loader-specific side of {@link HexislePlatform}, then hands
 * off to the shared {@link Hexisle#init()}.
 */
public final class HexisleFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Hexisle.init();
    }

    /** Fabric's {@link HexislePlatform} implementation, discovered via ServiceLoader. */
    public static final class Platform implements HexislePlatform {
        @Override
        public String getPlatformName() {
            return "Fabric";
        }

        @Override
        public String getModVersion() {
            return FabricLoader.getInstance()
                    .getModContainer(Hexisle.MOD_ID)
                    .map(container -> container.getMetadata().getVersion().getFriendlyString())
                    .orElse("unknown");
        }

        @Override
        public boolean isModLoaded(String modId) {
            return FabricLoader.getInstance().isModLoaded(modId);
        }
    }
}
