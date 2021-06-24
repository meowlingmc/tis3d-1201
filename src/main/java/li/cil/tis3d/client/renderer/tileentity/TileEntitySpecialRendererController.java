package li.cil.tis3d.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import li.cil.tis3d.common.tileentity.TileEntityController;
import li.cil.tis3d.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;

import java.util.Objects;

public final class TileEntitySpecialRendererController extends TileEntityRenderer<TileEntityController> {
    public TileEntitySpecialRendererController(final TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(final TileEntityController controller, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer bufferFactory, final int light, final int overlay) {
        final TileEntityController.ControllerState state = controller.getState();
        if (!state.isError) {
            return;
        }

        final RayTraceResult hit = renderDispatcher.cameraHitResult;
        if (hit instanceof BlockRayTraceResult) {
            final BlockRayTraceResult blockHit = (BlockRayTraceResult) hit;
            if (Objects.equals(blockHit.getPos(), controller.getPos())) {
                renderState(matrixStack, bufferFactory, state);
            }
        }
    }

    private void renderState(final MatrixStack matrixStack, final IRenderTypeBuffer bufferFactory, final TileEntityController.ControllerState state) {
        matrixStack.push();
        matrixStack.translate(0.5, 1.4, 0.5);
        matrixStack.rotate(renderDispatcher.renderInfo.getRotation());
        matrixStack.scale(-0.025f, -0.025f, 0.025f);

        final ITextComponent message = state.message;
        final FontRenderer fontRenderer = renderDispatcher.getFontRenderer();
        final float x = -fontRenderer.getStringPropertyWidth(message) / 2.0f;
        final Matrix4f matrix = matrixStack.getLast().getMatrix();
        final int backgroundColor = Minecraft.getInstance().gameSettings.getTextBackgroundColor(0.25f);
        final int maxBrightness = LightTexture.packLight(0xF, 0xF);
        fontRenderer.func_243247_a(message, x, 0, Color.withAlpha(Color.WHITE, 0.125f), false, matrix, bufferFactory, true, backgroundColor, maxBrightness);
        fontRenderer.func_243247_a(message, x, 0, Color.WHITE, false, matrix, bufferFactory, false, 0, maxBrightness);

        matrixStack.pop();
    }
}
