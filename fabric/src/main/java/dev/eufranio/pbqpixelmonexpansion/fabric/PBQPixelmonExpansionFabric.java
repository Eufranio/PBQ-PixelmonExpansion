package dev.eufranio.pbqpixelmonexpansion.fabric;

import dev.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import net.fabricmc.api.ModInitializer;

public final class PBQPixelmonExpansionFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        new PBQPixelmonExpansion();
    }

}
