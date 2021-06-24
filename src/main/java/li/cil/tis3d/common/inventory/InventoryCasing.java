package li.cil.tis3d.common.inventory;

import li.cil.tis3d.api.machine.Face;
import li.cil.tis3d.api.machine.Port;
import li.cil.tis3d.api.module.Module;
import li.cil.tis3d.api.module.ModuleProvider;
import li.cil.tis3d.api.module.traits.Rotatable;
import li.cil.tis3d.common.block.BlockCasing;
import li.cil.tis3d.common.network.Network;
import li.cil.tis3d.common.network.message.CasingInventoryMessage;
import li.cil.tis3d.common.provider.ModuleProviders;
import li.cil.tis3d.common.tileentity.TileEntityCasing;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Inventory implementation for casings, having six slots for modules, one per face.
 */
public final class InventoryCasing extends Inventory implements ISidedInventory {
    private final TileEntityCasing tileEntity;

    public InventoryCasing(final TileEntityCasing tileEntity) {
        super(Face.VALUES.length);
        this.tileEntity = tileEntity;
    }

    // Copy-paste of parent setInventorySlotContents, but allows passing along module facing.
    public void setInventorySlotContents(final int index, final ItemStack stack, final Port facing) {
        if (items[index] == stack) {
            return;
        }

        if (!items[index].isEmpty()) {
            onItemRemoved(index);
        }

        items[index] = stack;

        if (!items[index].isEmpty()) {
            onItemAdded(index, facing);
        }

        markDirty();
    }

    // --------------------------------------------------------------------- //
    // IInventory

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        tileEntity.markDirty();
        final World world = tileEntity.getTileEntityWorld();
        if (!world.isRemote()) {
            BlockState state = tileEntity.getBlockState();
            for (final Face face : Face.VALUES) {
                final BooleanProperty property = BlockCasing.FACE_TO_PROPERTY.get(face);
                state = state.with(property, !items[face.ordinal()].isEmpty());
            }
            world.setBlockState(tileEntity.getPos(), state);
        }
    }

    // --------------------------------------------------------------------- //
    // ISidedInventory

    @Override
    public int[] getSlotsForFace(final Direction side) {
        return new int[side.ordinal()];
    }

    @Override
    public boolean canInsertItem(final int index, final ItemStack stack, @Nullable final Direction side) {
        return side != null && side.ordinal() == index &&
               getStackInSlot(index).isEmpty() &&
               tileEntity.getModule(Face.fromDirection(side)) == null && // Handles virtual modules.
               canInstall(stack, Face.fromDirection(side));
    }

    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final Direction side) {
        return side.ordinal() == index && stack == getStackInSlot(index);
    }

    private boolean canInstall(final ItemStack stack, final Face face) {
        return ModuleProviders.getProviderFor(stack, tileEntity, face) != null;
    }

    // --------------------------------------------------------------------- //
    // Inventory

    @Override
    protected void onItemAdded(final int index) {
        onItemAdded(index, Port.UP);
    }

    private void onItemAdded(final int index, final Port facing) {
        final ItemStack stack = getStackInSlot(index);
        if (stack.isEmpty()) {
            return;
        }

        final Face face = Face.VALUES[index];
        final ModuleProvider provider = ModuleProviders.getProviderFor(stack, tileEntity, face);
        if (provider == null) {
            return;
        }

        final Module module = provider.createModule(stack, tileEntity, face);

        if (module instanceof Rotatable) {
            ((Rotatable) module).setFacing(facing);
        }

        if (!tileEntity.getCasingWorld().isRemote()) {
            // Grab module data from newly created module, if any, don't rely on stack.
            // Rationale: module may initialize data from stack while contents of stack
            // are not synchronized to client, or do some fancy server-side only setup
            // based on the stack. The possibilities are endless. This is robust.
            final CompoundNBT moduleData;
            if (module != null) {
                module.onInstalled(stack);
                module.writeToNBT(moduleData = new CompoundNBT());
            } else {
                moduleData = null;
            }

            final CasingInventoryMessage message = new CasingInventoryMessage(tileEntity, index, stack, moduleData);
            Network.INSTANCE.send(Network.getTracking(tileEntity), message);
        }

        tileEntity.setModule(Face.VALUES[index], module);
    }

    @Override
    protected void onItemRemoved(final int index) {
        final Face face = Face.VALUES[index];
        final Module module = tileEntity.getModule(face);
        tileEntity.setModule(face, null);
        if (!tileEntity.getCasingWorld().isRemote()) {
            if (module != null) {
                module.onUninstalled(getStackInSlot(index));
                module.onDisposed();
            }

            final CasingInventoryMessage message = new CasingInventoryMessage(tileEntity, index, ItemStack.EMPTY, null);
            Network.INSTANCE.send(Network.getTracking(tileEntity), message);
        }
    }
}
