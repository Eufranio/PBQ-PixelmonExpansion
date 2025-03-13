package dev.eufranio.pbqpixelmonexpansion.task.defeat;

import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
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
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class DefeatWildPokeTask implements TriggeredTask {

    @Setting
    public String id = "108";

    @Setting
    public String pokemonSpec = "Pikachu lvl:5";

    @Setting
    public int amount = 1;

    @Setting
    @Comment("Checking mode: \"any\" poke or a \"exact\" poke (checks this PokemonSpec)")
    public String mode = "any";

    @Override
    public void handle(QuestReference reference, Object... args) {
        BattleVictoryEvent event = (BattleVictoryEvent) args[0];
        if (!event.getWasWildCapture())
            return;

        var player = event.getBattle().getPlayers().getFirst();

        var battlePoke = event.getLosers().getFirst().getPokemonList().getFirst();
        var poke = battlePoke.getOriginalPokemon();

        PokemonProperties properties = PokemonProperties.Companion.parse(pokemonSpec);
        if (mode.equalsIgnoreCase("any") || properties.matches(poke)) {
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
    public MutableComponent toText() {
        return Component.empty().withStyle(ChatFormatting.GREEN)
                .append("Beat (")
                .append(Component.empty().withStyle(ChatFormatting.AQUA)
                        .append(String.valueOf(this.amount))
                        .append(" Wild ")
                        .append(mode.equalsIgnoreCase("any") ? "Pokemon" : pokemonSpec))
                .append(")");
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.DEFEAT_WILD_POKE;
    }

    @Override
    public String getId() {
        return id;
    }
}