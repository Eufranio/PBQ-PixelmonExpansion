package dev.eufranio.pbqpixelmonexpansion.task;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress;
import com.cobblemon.mod.common.api.pokedex.PokedexManager;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import dev.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import dev.eufranio.pixelbuiltquests.quest.QuestReference;
import dev.eufranio.pixelbuiltquests.task.BaseTask;
import dev.eufranio.pixelbuiltquests.task.TaskType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.UUID;

@ConfigSerializable
public class HasCaughtSpeciesTask implements BaseTask {

    @Setting
    public String id = "203";

    @Setting
    public String species = "Pikachu";

    @Override
    public MutableComponent toText() {
        return Component.empty().withStyle(ChatFormatting.GREEN)
                .append("Have Caught a ")
                .append(Component.empty().withStyle(ChatFormatting.AQUA)
                        .append(species))
                .append(" ever");
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.HAS_CAUGHT_SPECIES;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isCompleted(UUID player, QuestReference reference) {
        Species species = PokemonSpecies.INSTANCE.getByName(this.species.toLowerCase());
        if (species == null) {
            PBQPixelmonExpansion.LOGGER.error("Invalid species: {} for task {}", this.species, this.id);
            return false;
        }

        PokedexManager pokedex = Cobblemon.INSTANCE.getPlayerDataManager()
                .getPokedexData(player);
        return pokedex.getKnowledgeForSpecies(species.getResourceIdentifier()) == PokedexEntryProgress.CAUGHT;
    }
}
