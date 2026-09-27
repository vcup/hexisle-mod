package moe.vcup.hexisle;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loader-agnostic entry point shared by every platform module.
 *
 * <p>Each platform's own initializer calls {@link #init()} exactly once during
 * mod construction. Keep this class free of loader types: it is compiled into
 * the Fabric, Quilt, Forge and NeoForge jars alike.
 */
public final class Hexisle {
    /** The mod id, used for every namespaced resource this mod registers. */
    public static final String MOD_ID = "hexisle";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static boolean initialized;

    private Hexisle() {
    }

    /**
     * Runs the platform-independent half of mod construction.
     *
     * <p>Safe to call more than once; later calls are ignored.
     */
    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        LOGGER.info("hexisle initializing on {} ({})",
                HexislePlatform.platformName(), HexislePlatform.modVersion());
    }

    /** Creates a {@link ResourceLocation} in this mod's namespace. */
    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
