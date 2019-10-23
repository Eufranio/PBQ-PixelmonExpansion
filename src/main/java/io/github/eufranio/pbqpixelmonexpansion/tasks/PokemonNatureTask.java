package io.github.eufranio.pbqpixelmonexpansion.tasks;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.enums.EnumNature;
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
public class PokemonNatureTask implements BaseTask {

    @Setting
    public String nature = "Serious";

    public static Multimap<UUID, EnumNature> natures = ArrayListMultimap.create();

    @Override
    public boolean check(Player player, Quest quest, QuestLine line, int questId) {
        EnumNature n = EnumNature.natureFromString(nature);
        return natures.get(player.getUniqueId()).contains(n);
    }

    @Override
    public void complete(Player player, Quest quest, QuestLine line, int questId) {
        //
    }
}
