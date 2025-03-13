package dev.eufranio.pbqpixelmonexpansion.task.fishing;


import com.cobblemon.mod.common.api.events.fishing.BobberSpawnPokemonEvent;
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import dev.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import dev.eufranio.pixelbuiltquests.PixelBuiltQuests;
import dev.eufranio.pixelbuiltquests.quest.QuestReference;
import dev.eufranio.pixelbuiltquests.task.QuestTaskReference;
import dev.eufranio.pixelbuiltquests.task.TaskType;
import dev.eufranio.pixelbuiltquests.task.TriggeredTask;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class PokemonFishingTask implements TriggeredTask {

    @Setting
    public String id = "107";

    @Setting
    public int amount = 1;

    @Setting
    public String pokemonSpec = "Pikachu lvl:5";

    @Setting
    @Comment("Pokemon check mode: \"any\" poke or an \"exact\" poke (that mets the pokemon spec)")
    public String mode = "any";

    @Override
    public MutableComponent toText() {
        return Component.empty().withStyle(ChatFormatting.GREEN)
                .append("Fish (")
                .append(Component.empty().withStyle(ChatFormatting.AQUA)
                        .append(String.valueOf(this.amount))
                        .append(" ")
                        .append(mode.equalsIgnoreCase("any") ? "Pokemon" : pokemonSpec))
                .append(")");
    }

    @Override
    public void handle(QuestReference reference, Object... args) {
        BobberSpawnPokemonEvent.Post event = (BobberSpawnPokemonEvent.Post) args[0];
        Player player = event.getBobber().getPlayerOwner();
        PokemonProperties properties = PokemonProperties.Companion.parse(pokemonSpec);

        if (mode.equalsIgnoreCase("any") || properties.matches(event.getPokemon())) {
            if (this.isCompleted(player.getUUID(), reference))
                return;
            PixelBuiltQuests.storage()
                    .getStatus(player.getUUID(), QuestTaskReference.of(reference, this))
                    .ifPresent(status ->
                            this.increase(player.getUUID(), status, 1));
        }
    }

    @Override
    public int getTotal() {
        return amount;
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.POKEMON_FISHING;
    }

    @Override
    public String getId() {
        return id;
    }
}
