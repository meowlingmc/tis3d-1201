package li.cil.tis3d.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

/**
 * Base item for all modules.
 */
public class ItemModule extends ModItem {
    public ItemModule() {
    }

    public ItemModule(final Properties properties) {
        super(properties);
    }

    // --------------------------------------------------------------------- //
    // Item

    @Nullable
    @Override
    public CompoundNBT getShareTag(final ItemStack stack) {
        return null;
    }
}
