package io.github.eufranio.pbqpixelmonexpansion.tasks;

import com.google.common.collect.Streams;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.*;
import net.minecraft.entity.player.EntityPlayerMP;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import online.pixelbuilt.pbquests.quest.Quest;
import online.pixelbuilt.pbquests.quest.QuestLine;
import online.pixelbuilt.pbquests.task.BaseTask;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;

/**
 * Created by Frani on 28/01/2019.
 */
@ConfigSerializable
public class PokemonLevelTask implements BaseTask {

    @Setting
    public int level = 10;

    @Setting(comment = "checking mode. 1 = any pokemon, 2 = specific pokemon")
    public int mode = 1;

    @Setting
    public String pokemon = "Pikachu";

    @Override
    public boolean check(Player player, Quest quest, QuestLine line, int questId) {
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
        if (mode == 1) {
            return Streams.concat(Arrays.stream(storage.getAll()),
                    Arrays.stream(Pixelmon.storageManager.getPCForPlayer(player.getUniqueId()).getAll()))
                    .anyMatch(poke -> poke.getLevel() >= level);
        } else {
            return Streams.concat(Arrays.stream(storage.getAll()),
                    Arrays.stream(Pixelmon.storageManager.getPCForPlayer(player.getUniqueId()).getAll()))
                    .anyMatch(poke -> poke.getLevel() >= level && poke.getSpecies().getPokemonName().equalsIgnoreCase(pokemon));
        }
    }

    @Override
    public void complete(Player player, Quest quest, QuestLine line, int questId) {
        //
    }
}
