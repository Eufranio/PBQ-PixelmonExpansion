package io.github.eufranio.pbqpixelmonexpansion.tasks;

import io.github.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import io.github.eufranio.pbqpixelmonexpansion.common.CheckMode;
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
public class PokemonLevelTask implements BaseTask {

    @Setting
    public int id = 105;

    @Setting
    public int pokemonAmount = 1;

    @Setting(comment = "Pokemon check mode: Player has \"min\" X pokes or \"max\" X pokes with the desired level")
    public CheckMode pokemonAmountMode = CheckMode.MIN;

    @Setting
    public int level = 10;

    @Setting(comment = "Level check mode: Pokemon has \"min\" level X, \"exact\"" +
            " X level or an \"max\" of X level")
    public CheckMode levelMode = CheckMode.MAX;

    @Setting
    public String species = "Pikachu";

    @Setting(comment = "Species check mode: Pokemon with an \"exact\" species or \"any\" species")
    public CheckMode speciesMode = CheckMode.EXACT;

    @Override
    public Text toText() {
        return Text.of(TextColors.GREEN, "Have ", TextColors.AQUA,
                pokemonAmountMode == CheckMode.MIN ? "at least " : ( pokemonAmountMode == CheckMode.EXACT ? "exactly " : "max. "), pokemonAmount, " ",
                levelMode == CheckMode.MIN ? "min lvl. " : levelMode == CheckMode.MAX ? "max lvl. " : "lvl. ", level, " ",
                speciesMode == CheckMode.EXACT ? species : "Pokemon"
                );
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.POKEMON_LEVEL;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isCompleted(PlayerData data, QuestLine line, Quest quest) {
        return PBQPixelmonExpansion.getInstance().getBridge().hasLevelPokemons(
                data.getUser(),
                species,
                speciesMode,
                level,
                levelMode,
                pokemonAmount,
                pokemonAmountMode
        );
    }
}
