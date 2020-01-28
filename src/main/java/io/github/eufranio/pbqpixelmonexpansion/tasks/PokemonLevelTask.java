package io.github.eufranio.pbqpixelmonexpansion.tasks;

import com.pixelmonmod.pixelmon.storage.*;
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
        PlayerStorage storage = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(player.getUniqueId()).get();
        if (mode == 1) {
            return Arrays.stream(storage.partyPokemon)
                    .anyMatch(nbt -> nbt.getInteger(NbtKeys.LEVEL) >= level);
        } else {
            return Arrays.stream(storage.partyPokemon)
                    .anyMatch(nbt -> nbt.getInteger(NbtKeys.LEVEL) >= level && nbt.getString(NbtKeys.NAME).equalsIgnoreCase(pokemon));
        }
    }

    @Override
    public void complete(Player player, Quest quest, QuestLine line, int questId) {
        //
    }
}
