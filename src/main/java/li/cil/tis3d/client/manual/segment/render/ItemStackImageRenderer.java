package li.cil.tis3d.client.manual.segment.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import li.cil.tis3d.api.manual.ImageRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector4f;

public final class ItemStackImageRenderer implements ImageRenderer {
    /**
     * How long to show individual stacks, in milliseconds, before switching to the next.
     */
    private static final int CYCLE_SPEED = 1000;

    private final ItemStack[] stacks;

    public ItemStackImageRenderer(final ItemStack... stacks) {
        this.stacks = stacks;
    }

    @Override
    public int getWidth() {
        return 32;
    }

    @Override
    public int getHeight() {
        return 32;
    }

    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY) {
        final Minecraft mc = Minecraft.getInstance();
        final int index = (int) (System.currentTimeMillis() % (CYCLE_SPEED * stacks.length)) / CYCLE_SPEED;
        final ItemStack stack = stacks[index];

        final float scaleX = getWidth() / 16f;
        final float scaleY = getHeight() / 16f;

        // This is *nasty*, but sadly there's no renderItemAndEffectIntoGUI() variant that
        // takes a MatrixStack. Yet.

        GlStateManager.pushMatrix();
        GlStateManager.scalef(scaleX, scaleY, 1);

        final Vector4f position = new Vector4f(0, 0, 0, 1);
        position.transform(matrixStack.getLast().getMatrix());
        mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, (int) (position.getX() / scaleX), (int) (position.getY() / scaleY));

        GlStateManager.popMatrix();

        // Unfuck GL state.
        RenderSystem.enableBlend();
    }
}
