package li.cil.tis3d.api.prefab.manual;

import li.cil.tis3d.api.manual.InteractiveImageRenderer;
import net.minecraft.util.text.ITextComponent;

/**
 * Simple base implementation of {@link li.cil.tis3d.api.manual.InteractiveImageRenderer}.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AbstractInteractiveImageRenderer implements InteractiveImageRenderer {
    @Override
    public ITextComponent getTooltip(final ITextComponent tooltip) {
        return tooltip;
    }

    @Override
    public boolean onMouseClick(final double mouseX, final double mouseY) {
        return false;
    }
}
