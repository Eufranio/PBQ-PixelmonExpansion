package io.github.eufranio.pbqpixelmonexpansion.tasks;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import online.pixelbuilt.pbquests.quest.Quest;
import online.pixelbuilt.pbquests.quest.QuestLine;
import online.pixelbuilt.pbquests.task.BaseTask;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Frani on 28/01/2019.
 */
@ConfigSerializable
public class CatchingTask implements BaseTask<CatchingTask> {

    @Setting
    public String pokemonName = "Pikachu";

    @Setting
    public int count = 3;

    @Setting(comment = "checking mode. 1 = any pokemon, 2 = exact pokemon")
    public int mode = 1;

    public static Map<UUID, Map<EnumSpecies, Integer>> caught = Maps.newHashMap();

    @Override
    public boolean check(Player player, Quest quest, QuestLine line, int questId) {
        EnumSpecies poke = EnumSpecies.getFromNameAnyCase(this.pokemonName);

        if (mode == 1) {
            return caught.getOrDefault(player.getUniqueId(), new HashMap<>()).values().stream().mapToInt(i -> i).sum() >= count;
        } else if (mode == 2) {
            return caught.getOrDefault(player.getUniqueId(), new HashMap<>()).getOrDefault(poke, 0) >= count;
        }

        return false;
    }

    @Override
    public void complete(Player player, Quest quest, QuestLine line, int questId) {

    }
}
