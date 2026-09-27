package moe.vcup.hexisle.quilt;

import moe.vcup.hexisle.Hexisle;
import moe.vcup.hexisle.HexislePlatform;
import net.fabricmc.api.ModInitializer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;

/**
 * Quilt entry point, referenced from {@code quilt.mod.json}.
 *
 * <p>Quilt 0.19.2's entrypoint interface is Fabric's {@link ModInitializer}:
 * Quilt ships {@code net.fabricmc.api.ModInitializer} itself and has no
 * {@code org.quiltmc} equivalent, so this class implements that interface while
 * the loader-facing queries below use Quilt's own API.
 */
public final class HexisleQuilt implements ModInitializer {
    @Override
    public void onInitialize() {
        Hexisle.init();
    }

    /** Quilt's {@link HexislePlatform} implementation, discovered via ServiceLoader. */
    public static final class Platform implements HexislePlatform {
        @Override
        public String getPlatformName() {
            return "Quilt";
        }

        @Override
        public String getModVersion() {
            return QuiltLoader.getModContainer(Hexisle.MOD_ID)
                    .map(ModContainer::metadata)
                    .map(metadata -> metadata.version().raw())
                    .orElse("unknown");
        }

        @Override
        public boolean isModLoaded(String modId) {
            return QuiltLoader.isModLoaded(modId);
        }
    }
}
