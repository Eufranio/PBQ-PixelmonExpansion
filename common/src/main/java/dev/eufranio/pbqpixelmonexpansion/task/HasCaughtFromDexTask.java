package dev.eufranio.pbqpixelmonexpansion.task;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokedex.Dexes;
import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress;
import com.cobblemon.mod.common.api.pokedex.PokedexManager;
import com.cobblemon.mod.common.api.pokedex.entry.PokedexEntry;
import dev.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import dev.eufranio.pixelbuiltquests.quest.QuestReference;
import dev.eufranio.pixelbuiltquests.task.BaseTask;
import dev.eufranio.pixelbuiltquests.task.TaskType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;
import java.util.UUID;

@ConfigSerializable
public class HasCaughtFromDexTask implements BaseTask {

    @Setting
    public String id = "103";

    @Setting
    public String dexId = "cobblemon:national";

    @Setting
    public int count = 1;

    @Override
    public MutableComponent toText() {
        return Component.empty().withStyle(ChatFormatting.GREEN)
                .append("Have Caught ")
                .append(Component.empty().withStyle(ChatFormatting.AQUA)
                        .append(String.valueOf(this.count))
                        .append(" ")
                        .append(dexId)
                        .append(" Pokemon")
                );
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.HAS_CAUGHT_FROM_DEX;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isCompleted(UUID player, QuestReference reference) {
        ResourceLocation dexId = ResourceLocation.parse(this.dexId);
        if (!Dexes.INSTANCE.getDexEntryMap().containsKey(dexId)) {
            PBQPixelmonExpansion.LOGGER.error("Invalid dex id: {} for task {}", this.dexId, this.id);
            return false;
        }

        PokedexManager pokedex = Cobblemon.INSTANCE.getPlayerDataManager()
                .getPokedexData(player);
        var knowledge = PokedexEntryProgress.CAUGHT;

        List<PokedexEntry> allDexEntries = Dexes.INSTANCE.getDexEntryMap().get(dexId).getEntries();
        long count = allDexEntries.stream()
                .filter(entry -> pokedex.getKnowledgeForSpecies(entry.getSpeciesId()).ordinal() >= knowledge.ordinal())
                .count();
        return count >= this.count;
    }
}
