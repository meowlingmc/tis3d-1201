package li.cil.tis3d.common.provider;

import li.cil.tis3d.api.API;
import li.cil.tis3d.api.machine.Face;
import li.cil.tis3d.api.module.RedstoneInputProvider;
import li.cil.tis3d.api.module.traits.Redstone;
import li.cil.tis3d.common.provider.redstone.MinecraftRedstoneInputProvider;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public final class RedstoneInputProviders {
    private static final DeferredRegister<RedstoneInputProvider> REDSTONE_INPUT_PROVIDERS = DeferredRegister.create(RedstoneInputProvider.class, API.MOD_ID);

    // --------------------------------------------------------------------- //

    public static final Supplier<IForgeRegistry<RedstoneInputProvider>> REDSTONE_INPUT_PROVIDER_REGISTRY = REDSTONE_INPUT_PROVIDERS.makeRegistry("redstone_inputs", RegistryBuilder::new);

    // --------------------------------------------------------------------- //

    public static void initialize() {
        REDSTONE_INPUT_PROVIDERS.register("minecraft", MinecraftRedstoneInputProvider::new);

        REDSTONE_INPUT_PROVIDERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static int getRedstoneInput(final Redstone module) {
        int maxSignal = 0;
        for (final RedstoneInputProvider provider : REDSTONE_INPUT_PROVIDER_REGISTRY.get()) {
            final int signal = provider.getInput(module.getCasing().getCasingWorld(), module.getCasing().getPosition(), Face.toDirection(module.getFace()));
            if (signal > maxSignal) {
                maxSignal = signal;
            }
        }
        return maxSignal;
    }
}
