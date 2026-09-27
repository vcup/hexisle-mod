package moe.vcup.hexisle;

import java.util.ServiceLoader;

/**
 * The seam between loader-agnostic shared code and the platform modules.
 *
 * <p>Shared code may call these freely; each platform module supplies its own
 * implementation and publishes it through {@link ServiceLoader}, so no loader
 * classes leak into {@code common}.
 */
public interface HexislePlatform {
    /** A human-readable name for the loader this jar targets, e.g. {@code "Fabric"}. */
    String getPlatformName();

    /** The mod version as the loader sees it, e.g. {@code "0.1.0"}. */
    String getModVersion();

    /** Whether a mod with the given id is present at runtime. */
    boolean isModLoaded(String modId);

    /** The live platform implementation, resolved once via {@link ServiceLoader}. */
    static HexislePlatform get() {
        return Holder.INSTANCE;
    }

    /** Convenience accessor for {@code get().getPlatformName()}. */
    static String platformName() {
        return get().getPlatformName();
    }

    /** Convenience accessor for {@code get().getModVersion()}. */
    static String modVersion() {
        return get().getModVersion();
    }

    /** Convenience accessor for {@code get().isModLoaded(modId)}. */
    static boolean modLoaded(String modId) {
        return get().isModLoaded(modId);
    }

    /** Holds the lazily-resolved implementation so lookup cost is paid once. */
    final class Holder {
        private static final HexislePlatform INSTANCE = ServiceLoader
                .load(HexislePlatform.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No HexislePlatform implementation found on the classpath; "
                                + "the platform module's META-INF/services entry is missing"));

        private Holder() {
        }
    }
}
