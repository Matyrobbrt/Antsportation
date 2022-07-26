package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.AntHillBlock;
import com.matyrobbrt.antsportation.block.AntJarBlock;
import com.matyrobbrt.antsportation.block.AntNestBlock;
import com.matyrobbrt.antsportation.block.BoxerBlock;
import com.matyrobbrt.antsportation.block.MarkerBlock;
import com.matyrobbrt.antsportation.block.UnboxerBlock;
import com.matyrobbrt.antsportation.block.entity.AntHillBE;
import com.matyrobbrt.antsportation.block.entity.AntJarBE;
import com.matyrobbrt.antsportation.block.entity.AntNestBE;
import com.matyrobbrt.antsportation.block.entity.MarkerBE;
import com.matyrobbrt.antsportation.block.entity.boxing.BoxerBE;
import com.matyrobbrt.antsportation.block.entity.boxing.UnboxerBE;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class AntsportationBlocks {
    public static final Map<Supplier<? extends Block>, MineData> MINE_DATA = new HashMap<>();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registry.BLOCK_REGISTRY, Antsportation.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registry.BLOCK_ENTITY_TYPE_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<BoxerBlock> BOXER = registerWithItem("boxer", Mineable.PICKAXE, ToolTier.IRON, () -> new BoxerBlock(BlockBehaviour.Properties.of(Material.METAL)
            .color(MaterialColor.COLOR_BLACK)
            .strength(5.0f)
    ));
    public static final RegistryObject<UnboxerBlock> UNBOXER = registerWithItem("unboxer", Mineable.PICKAXE, ToolTier.IRON, () -> new UnboxerBlock(BlockBehaviour.Properties.of(Material.METAL)
            .color(MaterialColor.COLOR_BLACK)
            .strength(5.0f)
    ));
    public static final RegistryObject<AntNestBlock> ANT_NEST = registerWithItem("ant_nest",Mineable.SHOVEL,ToolTier.WOODEN, ()-> new AntNestBlock(BlockBehaviour.Properties.of(Material.DIRT)
            .color(MaterialColor.COLOR_BROWN)
            .strength(1.0f)
    ));
    public static final RegistryObject<AntHillBlock> ANT_HILL = registerWithItem("ant_hill",Mineable.SHOVEL,ToolTier.WOODEN, ()-> new AntHillBlock(BlockBehaviour.Properties.of(Material.DIRT)
            .color(MaterialColor.COLOR_BROWN)
            .strength(1.0f)
            .sound(SoundType.ROOTED_DIRT)
    ));
    public static final RegistryObject<AntJarBlock> ANT_JAR = register("ant_jar", Mineable.PICKAXE, ToolTier.WOODEN,
            () -> new AntJarBlock(BlockBehaviour.Properties.of(Material.GLASS).requiresCorrectToolForDrops()));

    public static final RegistryObject<MarkerBlock> MARKER = register("marker", Mineable.SHOVEL, ToolTier.WOODEN, MarkerBlock::new);
    public static final RegistryObject<BlockEntityType<BoxerBE>> BOXER_BE = BLOCK_ENTITIES.register("boxer", () ->
            BlockEntityType.Builder.of(BoxerBE::new, BOXER.get()).build(null));
    public static final RegistryObject<BlockEntityType<UnboxerBE>> UNBOXER_BE = BLOCK_ENTITIES.register("unboxer", () ->
            BlockEntityType.Builder.of(UnboxerBE::new, UNBOXER.get()).build(null));

    public static final RegistryObject<BlockEntityType<AntNestBE>> ANT_NEST_BE = BLOCK_ENTITIES.register("ant_nest", () ->
            BlockEntityType.Builder.of(AntNestBE::new, ANT_NEST.get()).build(null));

    public static final RegistryObject<BlockEntityType<AntHillBE>> ANT_HILL_BE = BLOCK_ENTITIES.register("ant_hill", () ->
            BlockEntityType.Builder.of(AntHillBE::new, ANT_HILL.get()).build(null));
    public static final RegistryObject<BlockEntityType<AntJarBE>> ANT_JAR_BE = BLOCK_ENTITIES.register("ant_jar", ()->
            BlockEntityType.Builder.of(AntJarBE::new, ANT_JAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<MarkerBE>> MARKER_BE = BLOCK_ENTITIES.register("marker", () -> BlockEntityType.Builder.of(MarkerBE::new, MARKER.get()).build(null));
    private static <T extends Block> RegistryObject<T> register(String name, Mineable mineable, ToolTier tier, Supplier<T> factory) {
        final var reg = BLOCKS.register(name, factory);
        MINE_DATA.put(reg, new MineData(mineable, tier));
        return reg;
    }

    private static <T extends Block> RegistryObject<T> registerWithItem(String name, Mineable mineable, ToolTier tier, Supplier<T> factory) {
        final var block = register(name, mineable, tier, factory);
        AntsportationItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(Antsportation.TAB)));
        return block;
    }

    public record MineData(Mineable mineable, ToolTier toolTier) {
    }

    public enum Mineable {
        SHOVEL(BlockTags.MINEABLE_WITH_SHOVEL),
        PICKAXE(BlockTags.MINEABLE_WITH_PICKAXE);
        public final TagKey<Block> tag;

        Mineable(TagKey<Block> tag) {
            this.tag = tag;
        }
    }

    public enum ToolTier {
        IRON(BlockTags.NEEDS_IRON_TOOL),
        WOODEN(Tags.Blocks.NEEDS_WOOD_TOOL);
        public final TagKey<Block> tag;

        ToolTier(TagKey<Block> tag) {
            this.tag = tag;
        }
    }

}
