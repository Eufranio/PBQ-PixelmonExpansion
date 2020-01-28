package io.github.eufranio.pbqpixelmonexpansion.tasks;

import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import online.pixelbuilt.pbquests.quest.Quest;
import online.pixelbuilt.pbquests.quest.QuestLine;
import online.pixelbuilt.pbquests.task.BaseTask;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Frani on 28/01/2019.
 */
@ConfigSerializable
public class HasCaughtTask implements BaseTask<HasCaughtTask> {

    @Setting
    public String pokemon = "Pikachu";

    @Override
    public boolean check(Player player, Quest quest, QuestLine line, int questId) {
        PlayerStorage storage = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(player.getUniqueId()).get();
        return storage.pokedex.hasCaught(Pokedex.nameToID(pokemon));
    }

    @Override
    public void complete(Player player, Quest quest, QuestLine line, int questId) {
        //
    }
}
