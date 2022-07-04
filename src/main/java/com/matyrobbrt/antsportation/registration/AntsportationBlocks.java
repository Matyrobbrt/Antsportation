package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.AntJarBlock;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AntsportationBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registry.BLOCK_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<Block> ANTJAR_BLOCK = BLOCKS.register("ant_jar", ()->new AntJarBlock(BlockBehaviour.Properties.of(Material.GLASS)));
}
