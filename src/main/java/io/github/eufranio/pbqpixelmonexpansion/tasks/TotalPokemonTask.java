package io.github.eufranio.pbqpixelmonexpansion.tasks;

import io.github.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import online.pixelbuilt.pbquests.quest.Quest;
import online.pixelbuilt.pbquests.quest.QuestLine;
import online.pixelbuilt.pbquests.storage.sql.PlayerData;
import online.pixelbuilt.pbquests.task.BaseTask;
import online.pixelbuilt.pbquests.task.TaskType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Frani on 28/01/2019.
 */
@ConfigSerializable
public class TotalPokemonTask implements BaseTask {

    @Setting
    public int id = 106;

    @Setting
    public int count = 10;

    @Override
    public Text toText() {
        return Text.of(TextColors.GREEN, "Have caught at least ", TextColors.AQUA, count, " Pokemon");
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.TOTAL_POKEMON;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isCompleted(PlayerData data, QuestLine line, Quest quest) {
        return PBQPixelmonExpansion.getInstance().getBridge().hasCaught(data.getUser(), null, count);
    }
}
