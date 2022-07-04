package com.matyrobbrt.antsportation.item;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.menu.BoxMenu;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BoxItem extends Item {
    public static final String TAG_ITEMS = "Items";
    private final BoxTier tier;
    public BoxItem(Properties pProperties, BoxTier tier) {
        super(pProperties);
        this.tier = tier;
    }

    public static Stream<ItemStackInstance> getStoredItems(ItemStack box) {
        if (!box.getOrCreateTag().contains(TAG_ITEMS, Tag.TAG_LIST))
            return Stream.empty();
        final var list = box.getOrCreateTag().getList(TAG_ITEMS, Tag.TAG_COMPOUND);
        return IntStream.range(0, list.size()).mapToObj(index -> new ItemStackInstance(list.getCompound(index), list::remove));
    }

    public static ItemStackInstance bookNewSlot(ItemStack box) {
        final var list = box.getOrCreateTag().getList(TAG_ITEMS, Tag.TAG_COMPOUND);
        final var compound = new CompoundTag();
        list.add(compound);
        box.getOrCreateTag().put(TAG_ITEMS, list);
        return new ItemStackInstance(compound, list::remove);
    }

    public static int getStoredCount(ItemStack box) {
        if (!box.getOrCreateTag().contains(TAG_ITEMS, Tag.TAG_LIST))
            return 0;
        final var list = box.getOrCreateTag().getList(TAG_ITEMS, Tag.TAG_COMPOUND);
        int count = 0;
        for (final var tag : list) {
            count += deserialize((CompoundTag) tag).getCount();
        }
        return count;
    }

    public static int getStoredTypes(ItemStack box) {
        if (!box.getOrCreateTag().contains(TAG_ITEMS, Tag.TAG_LIST))
            return 0;
        final var list = box.getOrCreateTag().getList(TAG_ITEMS, Tag.TAG_COMPOUND);
        return list.size();
    }

    @CanIgnoreReturnValue
    public static ItemStack load(ItemStack box, ItemStack toLoad) {
        if (!(box.getItem() instanceof BoxItem boxItem)) return toLoad;
        final var max = boxItem.tier.space;
        final var stored = getStoredCount(box);
        final var toInsert = Math.min(max - stored, toLoad.getCount());
        if (toInsert > 0) {
            final var canBook = getStoredTypes(box) < boxItem.tier.types;
            final var slot = getStoredItems(box)
                    .filter(in -> ItemStack.isSameItemSameTags(in.getStack(), toLoad))
                    .findFirst()
                    .or(() -> {
                        if (canBook)
                            return Optional.of(bookNewSlot(box));
                        return Optional.empty();
                    });
            if (slot.isPresent()) {
                final var nonOpt = slot.get();
                nonOpt.setStack(Utils.withCount(toLoad, nonOpt.getStack().getCount() + toInsert));
                return Utils.withCount(toLoad, toLoad.getCount() - toInsert);
            } else {
                return toLoad;
            }
        }
        return toLoad;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()) {
            NetworkHooks.openGui((ServerPlayer) pPlayer, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return pPlayer.getItemInHand(pUsedHand).getHoverName().copy().withStyle(ChatFormatting.BLACK);
                }

                @Override
                public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                    return new BoxMenu(pContainerId, pInventory, pPlayer.getItemInHand(pUsedHand));
                }
            }, e -> e.writeEnum(pUsedHand));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        load(stack, Registry.ITEM.byId(new Random().nextInt(Registry.ITEM.size())).getDefaultInstance());
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        if (getStoredItems(pStack).findAny().isEmpty())
            return Optional.empty();
        return Optional.of(new Tooltip(getStoredItems(pStack)
                .map(ItemStackInstance::getStack).toList()));
    }

    public enum BoxTier implements ItemLike {
        BASIC(256, 16, 0xffffff, Rarity.RARE);

        public final int space;
        public final int types;
        public final int colour;
        public final Rarity rarity;
        private RegistryObject<BoxItem> item;

        BoxTier(int space, int types, int colour, Rarity rarity) {
            this.space = space;
            this.types = types;
            this.colour = colour;
            this.rarity = rarity;
        }

        public void registerItem(Item.Properties properties) {
            item = AntsportationItems.ITEMS.register(name().toLowerCase(Locale.ROOT) + "_box", () -> new BoxItem(properties, this));
        }

        public String getTranslationKey() {
            return "box_tier." + Antsportation.MOD_ID + "." + name().toLowerCase(Locale.ROOT);
        }

        public MutableComponent translate() {
            return new TranslatableComponent(getTranslationKey());
        }

        @Override
        public BoxItem asItem() {
            return item.get();
        }
    }

    public record ItemStackInstance(CompoundTag tag, Consumer<CompoundTag> remover) {
        public ItemStack getStack() {
            return deserialize(tag);
        }
        public void extract(int count) {
            final var stack = getStack();
            stack.grow(-count);
            if (stack.getCount() <= 0) {
                remover.accept(tag);
            } else {
                setStack(stack);
            }
        }
        public void setStack(ItemStack newStack) {
            Utils.clearTag(tag);
            final var ser = serialize(newStack);
            ser.getAllKeys().forEach(k -> tag.put(k, Objects.requireNonNull(ser.get(k))));
        }
    }

    public static ItemStack deserialize(CompoundTag tag) {
        final var stack = ItemStack.of(tag);
        stack.setCount(tag.getInt("ExtraCount"));
        return stack;
    }

    public static CompoundTag serialize(ItemStack stack) {
        final var tag = stack.serializeNBT();
        tag.putInt("ExtraCount", stack.getCount());
        return tag;
    }

    public record Tooltip(List<ItemStack> stacks) implements TooltipComponent {}
}
