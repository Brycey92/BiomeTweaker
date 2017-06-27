package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.util.BlockEquivalencePredicate;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;

public class ScriptCommandRegisterVillageBlockReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final BlockStateBuilder<?> toReplace;
	private final BlockStateBuilder<?> replaceWith;

	public ScriptCommandRegisterVillageBlockReplacement(final IBiomePackage pack, final BlockStateBuilder<?> block1, final BlockStateBuilder<?> block2) {
		this.pack = pack;
		this.toReplace = block1;
		this.replaceWith = block2;
	}

	@Override
	public void perform() throws Exception {
		final Iterator<Biome > it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(!BiomeEventHandler.getVillageBlockReplacements().containsKey(Biome.getIdForBiome(gen)))
				BiomeEventHandler.getVillageBlockReplacements().put(Biome.getIdForBiome(gen), Lists.newArrayList());
			final List<Pair<IBlockState, IBlockState>> list = BiomeEventHandler.getVillageBlockReplacements().get(Biome.getIdForBiome(gen));
			final Iterator<Pair<IBlockState, IBlockState>> it2 = list.iterator();
			final IBlockState toReplace = this.toReplace.build();
			final Predicate<IBlockState> pred = new BlockEquivalencePredicate(toReplace);
			while(it2.hasNext()){
				final Pair<IBlockState, IBlockState> pair = it2.next();
				if(pred.apply(pair.getKey())){
					it2.remove();
					break;
				}
			}
			list.add(Pair.of(toReplace, this.replaceWith.build()));
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
		}
	}

}
