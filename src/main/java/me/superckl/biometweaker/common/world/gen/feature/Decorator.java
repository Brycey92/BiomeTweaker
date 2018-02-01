package me.superckl.biometweaker.common.world.gen.feature;

import java.util.List;
import java.util.Random;

import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorWrapper;
import me.superckl.biometweaker.common.world.gen.PlacementStage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Decorator {

	public static void runDecoration(final PlacementStage stage, final World world, final Random rand, final ChunkPos pos){
		final DecorationManager manager = DecorationManager.getManagerForWorld(world.provider.getDimension());
		if(!manager.hasReplacements(stage))
			return;
		final BlockPos bPos = new BlockPos((pos.chunkXPos << 4), 0, (pos.chunkZPos << 4));
		final int id = Biome.getIdForBiome(world.getBiome(bPos.add(8, 0, 8)));
		if(!manager.hasReplacements(id, stage))
			return;
		final List<WorldGeneratorWrapper<?>> gens = manager.findDecorationList(id, stage);
		for(final WorldGeneratorWrapper<?> gen:gens)
			gen.generate(world, rand, bPos);
	}

}
