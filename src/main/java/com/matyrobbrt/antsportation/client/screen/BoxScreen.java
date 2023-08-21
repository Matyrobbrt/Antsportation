package com.matyrobbrt.antsportation.client.screen;

import com.google.common.collect.Lists;
import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.client.tooltip.BoxTooltipClient;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.menu.BoxMenu;
import com.matyrobbrt.antsportation.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BoxScreen extends BaseContainerScreen<BoxMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Antsportation.MOD_ID, "textures/gui/box.png");
    public BoxScreen(BoxMenu pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, title);
        imageHeight = 222;
        imageWidth = 186;
        this.inventoryLabelY = this.imageHeight - 94;
        backgroundTexture = CONTAINER_BACKGROUND;
    }

    private SelectionList list;

    @Override
    protected void init() {
        super.init();
        list = new SelectionList(minecraft,  9 * 18, height + 6 * 20, topPos + 17, topPos + 17 + 6 * 18);
        list.setRenderBackground(false);
        addRenderableWidget(list);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void render(@NotNull GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        {
            final var entry = list.getEntryAtPos(pMouseX, pMouseY);
            if (entry != null) {
                final var tooltip = entry.getTooltip(pMouseX - entry.x);
                if (tooltip != null)
                    pPoseStack.renderTooltip(Minecraft.getInstance().font, List.of(tooltip), Optional.empty(), entry.getStack(pMouseX - entry.x), pMouseX, pMouseY);
            }
        }
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    public record StackWithPose(ItemStack stack, Rect2i area) {}
    public Optional<StackWithPose> resolveStack(int mouseX, int mouseY) {
        final var entry = list.getEntryAtPos(mouseX, mouseY);
        if (entry != null) {
            final var index = (mouseX - entry.x) / 18;
            if (entry.stacks.size() > index && isWithinBounds(entry.y, entry.y + 20)) {
                return Optional.of(new StackWithPose(entry.stacks.get(index), new Rect2i(entry.x + index * 18 + 1, 18, entry.y + 1, 18)));
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    private class SelectionList extends AbstractSelectionList<Entry> {

        public SelectionList(Minecraft pMinecraft, int pWidth, int pHeight, int pY0, int pY1) {
            super(pMinecraft, pWidth, pHeight, pY0, pY1, 20);
            x0 = BoxScreen.this.leftPos + 5;
            x1 = x0 + pWidth + 5;
            setRenderBackground(false);
            setRenderTopAndBottom(false);

            Lists.partition(BoxItem.getStoredItems(menu.stack)
                    .map(BoxItem.ItemStackInstance::getStack)
                    .toList(), 9).forEach(stacks -> addEntry(new BoxScreen.Entry(stacks)));
        }

        protected int getY0() {
            return y0;
        }
        protected int getY1() {
            return y1;
        }

        @Override
        public int getRowWidth() {
            return 9 * 18;
        }

        @Override
        protected int getScrollbarPosition() {
            return leftPos + 177;
        }

        @Override
        public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
            pNarrationElementOutput.add(NarratedElementType.TITLE, BoxScreen.this.title);
        }

        @Nullable
        public BoxScreen.Entry getEntryAtPos(double mouseX, double mouseY) {
            return getEntryAtPosition(mouseX, mouseY);
        }
    }

    private class Entry extends AbstractSelectionList.Entry<Entry> {
        private final List<ItemStack> stacks;
        public int x;
        public int y;

        private Entry(List<ItemStack> stacks) {
            this.stacks = stacks;
        }

        @Override
        public void render(@NotNull GuiGraphics pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
            this.x = pLeft;
            this.y = pTop;
            for (int i = 0; i < stacks.size(); i++) {
                final var x = pLeft + (i * 18);
                if (isWithinBounds(pTop, pTop + 20)) {
                    final var itemstack = stacks.get(i);
                    BoxTooltipClient.blit(pPoseStack, x, pTop, BoxTooltipClient.Texture.SLOT);
                    pPoseStack.renderItem(itemstack, x + 1, pTop + 1, i);
                    pPoseStack.renderItemDecorations(font, itemstack, x + 1, pTop + 1, Utils.getCompressedCount(itemstack.getCount()));
                }
            }
        }

        @Nullable
        public Component getTooltip(int relativeX) {
            final var index = relativeX / 18;
            if (stacks.size() > index && isWithinBounds(this.y, this.y + 20)) {
                return stacks.get(index).getHoverName();
            }
            return null;
        }

        @Nullable
        public ItemStack getStack(int relativeX) {
            final var index = relativeX / 18;
            if (stacks.size() > index && isWithinBounds(this.y, this.y + 20)) {
                return stacks.get(index);
            }
            return null;
        }
    }

    public boolean isWithinBounds(int y0, int y1) {
        return y0 >= list.getY0() && y0 <= list.getY1() && y1 >= list.getY0() && y1 <= list.getY1();
    }
}
