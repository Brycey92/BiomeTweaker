package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.Biome;

public class AllBiomesPackage extends BiomePackage{

	@Override
	public Iterator<Biome> getIterator() {
		final List<Biome> list = new ArrayList<>();
		final Iterator<Biome> it = Biome.REGISTRY.iterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(gen != null)
				list.add(gen);
		}
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> list = new ArrayList<>();
		final Iterator<Biome> it = Biome.REGISTRY.iterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(gen != null)
				list.add(Biome.getIdForBiome(gen));
		}
		return list;
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return false;
	}

}
