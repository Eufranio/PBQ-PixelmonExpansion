package io.github.eufranio.pbqpixelmonexpansion.tasks;

import io.github.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import io.github.eufranio.pbqpixelmonexpansion.common.CheckMode;
import io.github.eufranio.pbqpixelmonexpansion.common.events.Event;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import online.pixelbuilt.pbquests.PixelBuiltQuests;
import online.pixelbuilt.pbquests.quest.Quest;
import online.pixelbuilt.pbquests.quest.QuestLine;
import online.pixelbuilt.pbquests.storage.sql.PlayerData;
import online.pixelbuilt.pbquests.storage.sql.QuestStatus;
import online.pixelbuilt.pbquests.task.TaskType;
import online.pixelbuilt.pbquests.task.TriggeredTask;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

@ConfigSerializable
public class PokemonFishingTask implements TriggeredTask<Event.PokemonFishing> {

    @Setting
    public int id = 107;

    @Setting
    public int amount = 1;

    @Setting
    public String pokemonSpec = "Pikachu lvl:5";

    @Setting(comment = "Pokemon check mode: \"any\" poke or an \"exact\" poke (that mets the pokemon spec)")
    public CheckMode mode = CheckMode.ANY;

    @Override
    public Class<Event.PokemonFishing> getEventClass() {
        return Event.PokemonFishing.class;
    }

    @Override
    public void handle(QuestLine line, Quest quest, Event.PokemonFishing event) {
        if (mode == CheckMode.ANY || event.matches(this.pokemonSpec)) {
            PlayerData data = PixelBuiltQuests.getStorage().getData(event.getTargetEntity().getUniqueId());
            if (!this.isCompleted(data, line, quest)) {
                data.getStatus(this, line, quest)
                        .ifPresent(status -> this.increase(data, status, 1));
            }
        }
    }

    @Override
    public int getTotal() {
        return amount;
    }

    @Override
    public Text toText() {
        return Text.of(TextColors.GREEN, "Fish ", TextColors.AQUA, amount, " ", mode == CheckMode.ANY ? "Pokemon" : pokemonSpec);
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.POKEMON_FISHING;
    }

    @Override
    public int getId() {
        return id;
    }
}
