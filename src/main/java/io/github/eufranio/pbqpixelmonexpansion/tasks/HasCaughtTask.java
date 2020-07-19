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
public class HasCaughtTask implements BaseTask {

    @Setting
    public int id = 103;

    @Setting
    public String species = "Pikachu";

    @Setting
    public int count = 1;

    @Override
    public Text toText() {
        return Text.of(TextColors.GREEN, "Have caught ", TextColors.AQUA, count, " ", species);
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.HAS_CAUGHT;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isCompleted(PlayerData data, QuestLine line, Quest quest) {
        return PBQPixelmonExpansion.getInstance().getBridge().hasCaught(data.getUser(), species, count);
    }
}
