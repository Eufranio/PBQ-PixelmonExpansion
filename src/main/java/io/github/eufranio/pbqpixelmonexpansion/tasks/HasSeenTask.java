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
public class HasSeenTask implements BaseTask {

    @Setting
    public int id = 104;

    @Setting
    public String species = "Pikachu";

    @Override
    public Text toText() {
        return Text.of(TextColors.GREEN, "Have seen a ", TextColors.AQUA, species);
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.HAS_SEEN;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isCompleted(PlayerData data, QuestLine line, Quest quest) {
        return PBQPixelmonExpansion.getInstance().getBridge().hasSeen(data.getUser(), species);
    }
}
