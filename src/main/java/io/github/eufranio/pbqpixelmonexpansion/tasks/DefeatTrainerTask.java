package io.github.eufranio.pbqpixelmonexpansion.tasks;

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import online.pixelbuilt.pbquests.quest.Quest;
import online.pixelbuilt.pbquests.quest.QuestLine;
import online.pixelbuilt.pbquests.task.BaseTask;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;
import java.util.UUID;

/**
 * Created by Frani on 28/01/2019.
 */
@ConfigSerializable
public class DefeatTrainerTask implements BaseTask<DefeatTrainerTask> {

    public static List<UUID> players = Lists.newArrayList();

    @Override
    public boolean check(Player player, Quest quest, QuestLine line, int questId) {
        return players.contains(player.getUniqueId());
    }

    @Override
    public void complete(Player player, Quest quest, QuestLine line, int questId) {
        //
    }
}
