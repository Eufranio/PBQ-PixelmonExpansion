package dev.eufranio.pbqpixelmonexpansion.task.catching;

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
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CatchingTask implements TriggeredTask {

    @Setting
    public String id = "100";

    @Setting
    public String pokemonSpec = "Pikachu lvl:5";

    @Setting
    public int amount = 1;

    @Setting
    @Comment("Checking mode: \"any\" poke or a \"specific\" poke")
    public String mode = "any";

    @Override
    public void handle(QuestReference reference, Object... args) {
        PokemonCapturedEvent event = (PokemonCapturedEvent) args[0];

        PokemonProperties properties = PokemonProperties.Companion.parse(pokemonSpec);
        if (mode.equalsIgnoreCase("any") || properties.matches(event.getPokemon())) {
            if (this.isCompleted(event.getPlayer().getUUID(), reference))
                return;
            PixelBuiltQuests.storage()
                    .getStatus(event.getPlayer().getUUID(), QuestTaskReference.of(reference, this))
                    .ifPresent(status ->
                            this.increase(event.getPlayer().getUUID(), status, 1));
        }
    }

    @Override
    public int getTotal() {
        return this.amount;
    }

    @Override
    public MutableComponent toText() {
        return Component.empty().withStyle(ChatFormatting.GREEN)
                .append("Catch (")
                .append(Component.empty().withStyle(ChatFormatting.AQUA)
                        .append(String.valueOf(this.amount))
                        .append(" ")
                        .append(mode.equalsIgnoreCase("any") ? " Pokemon" : pokemonSpec))
                .append(")");
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.CATCHING;
    }

    @Override
    public String getId() {
        return id;
    }
}