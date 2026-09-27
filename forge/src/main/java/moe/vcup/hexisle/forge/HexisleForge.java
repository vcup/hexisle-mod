package moe.vcup.hexisle.forge;

import moe.vcup.hexisle.Hexisle;
import moe.vcup.hexisle.HexislePlatform;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge (legacy {@code net.minecraftforge}) entry point, discovered through the
 * {@link Mod} annotation.
 */
@Mod(Hexisle.MOD_ID)
public final class HexisleForge {
    public HexisleForge() {
        Hexisle.init();
    }

    /** Forge's {@link HexislePlatform} implementation, discovered via ServiceLoader. */
    public static final class Platform implements HexislePlatform {
        @Override
        public String getPlatformName() {
            return "Forge";
        }

        @Override
        public String getModVersion() {
            return ModList.get()
                    .getModContainerById(Hexisle.MOD_ID)
                    .map(container -> container.getModInfo().getVersion().toString())
                    .orElse("unknown");
        }

        @Override
        public boolean isModLoaded(String modId) {
            return ModList.get().isLoaded(modId);
        }
    }
}
