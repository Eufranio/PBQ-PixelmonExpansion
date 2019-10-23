package io.github.eufranio.pbqpixelmonexpansion.tasks;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import online.pixelbuilt.pbquests.quest.Quest;
import online.pixelbuilt.pbquests.quest.QuestLine;
import online.pixelbuilt.pbquests.task.BaseTask;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

/**
 * Created by Frani on 28/01/2019.
 */
@ConfigSerializable
public class ChattingTask implements BaseTask<ChattingTask> {

    public static Multimap<UUID, String> npcs = ArrayListMultimap.create();

    @Setting
    public String npcName = "unknown";

    @Setting(comment = "checking mode. 1 = any npc, 2 = a specific npc")
    public int mode = 1;

    @Override
    public boolean check(Player player, Quest quest, QuestLine line, int questId) {
        if (mode == 1) {
            return !npcs.get(player.getUniqueId()).isEmpty();
        } else {
            return npcs.get(player.getUniqueId()).contains(npcName);
        }
    }

    @Override
    public void complete(Player player, Quest quest, QuestLine line, int questId) {
        //
    }
}
