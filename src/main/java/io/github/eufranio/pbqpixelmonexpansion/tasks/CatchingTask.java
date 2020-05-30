package io.github.eufranio.pbqpixelmonexpansion.tasks;

import io.github.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import io.github.eufranio.pbqpixelmonexpansion.events.Event;
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
 * Created by Frani on 28/01/2019.
 */
@ConfigSerializable
public class CatchingTask implements TriggeredTask<Event.Catching> {

    @Setting
    public int id = 100;

    @Setting
    public String pokemonSpec = "Pikachu lvl:5";

    @Setting
    public int amount = 1;

    @Setting(comment = "Checking mode: \"any\" poke or a \"specific\" poke")
    public String mode = "any";

    @Override
    public Class<Event.Catching> getEventClass() {
        return Event.Catching.class;
    }

    @Override
    public void handle(QuestLine line, Quest quest, Event.Catching event) {
        if (mode.toLowerCase().equals("any") || event.matches(pokemonSpec)) {
            PlayerData data = PixelBuiltQuests.getStorage().getData(event.getTargetEntity().getUniqueId());
            QuestStatus status = data.getStatus(this, line, quest);
            this.increase(data, status, 1);
        }
    }

    @Override
    public int getTotal() {
        return this.amount;
    }

    @Override
    public Text getDisplay() {
        return Text.of(TextColors.GREEN, "Catch ", TextColors.AQUA, amount, mode.toLowerCase().equals("any") ? " Pokemon" : pokemonSpec);
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.CATCHING;
    }

    @Override
    public int getId() {
        return id;
    }
}
