package moe.vcup.hexisle.neoforge;

import moe.vcup.hexisle.Hexisle;
import moe.vcup.hexisle.HexislePlatform;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/**
 * NeoForge entry point.
 *
 * <p>On 1.20.1 NeoForge is a Forge 47.x republish, so the entry point uses the
 * same {@code net.minecraftforge.fml} types as the legacy Forge module. It lives
 * in its own class only so the two jars can be built and loaded independently.
 */
@Mod(Hexisle.MOD_ID)
public final class HexisleNeoForge {
    public HexisleNeoForge() {
        Hexisle.init();
    }

    /** NeoForge's {@link HexislePlatform} implementation, discovered via ServiceLoader. */
    public static final class Platform implements HexislePlatform {
        @Override
        public String getPlatformName() {
            return "NeoForge";
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
