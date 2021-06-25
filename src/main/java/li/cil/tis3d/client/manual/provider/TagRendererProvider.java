package li.cil.tis3d.client.manual.provider;

import li.cil.tis3d.api.manual.RendererProvider;
import li.cil.tis3d.api.manual.ContentRenderer;
import li.cil.tis3d.client.manual.Strings;
import li.cil.tis3d.client.manual.segment.render.ItemStackContentRenderer;
import li.cil.tis3d.client.manual.segment.render.MissingItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public final class TagRendererProvider extends ForgeRegistryEntry<RendererProvider> implements RendererProvider {
    private static final String PREFIX = "tag:";

    @Override
    public boolean matches(final String path) {
        return path.startsWith(PREFIX);
    }

    @Override
    public ContentRenderer getRenderer(final String path) {
        final String data = path.substring(PREFIX.length());
        final ITag<Item> tag = ItemTags.getAllTags().getTag(new ResourceLocation(data));
        if (tag == null || tag.getValues().isEmpty()) {
            return new MissingItemRenderer(Strings.WARNING_TAG_MISSING);
        }
        return new ItemStackContentRenderer(tag
            .getValues().stream()
            .map(ItemStack::new)
            .toArray(ItemStack[]::new));
    }
}