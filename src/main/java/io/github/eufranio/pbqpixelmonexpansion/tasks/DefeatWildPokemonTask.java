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

/**
 * Created by Frani on 18/07/2020.
 */
@ConfigSerializable
public class DefeatWildPokemonTask implements TriggeredTask<Event.DefeatWildPokemon> {

    @Setting
    public int id = 108;

    @Setting
    public String pokemonSpec = "Pikachu lvl:5";

    @Setting
    public int amount = 1;

    @Setting(comment = "Checking mode: \"any\" poke or a \"exact\" poke (checks this PokemonSpec)")
    public CheckMode mode = CheckMode.ANY;

    @Override
    public Class<Event.DefeatWildPokemon> getEventClass() {
        return Event.DefeatWildPokemon.class;
    }

    @Override
    public void handle(QuestLine line, Quest quest, Event.DefeatWildPokemon event) {
        if (mode == CheckMode.ANY || event.matches(pokemonSpec)) {
            PlayerData data = PixelBuiltQuests.getStorage().getData(event.getTargetEntity().getUniqueId());
            if (!this.isCompleted(data, line, quest)) {
                QuestStatus status = data.getStatus(this, line, quest);
                this.increase(data, status, 1);
            }
        }
    }

    @Override
    public int getTotal() {
        return amount;
    }

    @Override
    public Text toText() {
        return Text.of(TextColors.GREEN, "Beat ", TextColors.AQUA, amount, " Wild ", mode == CheckMode.ANY ? "Pokemon" : pokemonSpec);
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.DEFEAT_WILD_POKE;
    }

    @Override
    public int getId() {
        return id;
    }
}
