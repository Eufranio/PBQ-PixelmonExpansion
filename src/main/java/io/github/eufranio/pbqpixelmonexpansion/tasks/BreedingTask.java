package io.github.eufranio.pbqpixelmonexpansion.tasks;

import io.github.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import io.github.eufranio.pbqpixelmonexpansion.common.CheckMode;
import io.github.eufranio.pbqpixelmonexpansion.common.events.BreedingEvent;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import online.pixelbuilt.pbquests.PixelBuiltQuests;
import online.pixelbuilt.pbquests.quest.Quest;
import online.pixelbuilt.pbquests.quest.QuestLine;
import online.pixelbuilt.pbquests.storage.sql.PlayerData;
import online.pixelbuilt.pbquests.storage.sql.QuestStatus;
import online.pixelbuilt.pbquests.task.TaskType;
import online.pixelbuilt.pbquests.task.TriggeredTask;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Frani on 03/07/2020.
 */
@ConfigSerializable
public class BreedingTask implements TriggeredTask<BreedingEvent> {

    @Setting
    public int id = 99;

    @Setting
    public CheckMode eggCheckMode = CheckMode.EXACT;

    @Setting
    public String eggPokemonSpec = "Pikachu lvl:5";

    @Setting
    public CheckMode parent1CheckMode = CheckMode.EXACT;

    @Setting
    public String parent1PokemonSpec = "Pikachu lvl:5";

    @Setting
    public CheckMode parent2CheckMode = CheckMode.ANY;

    @Setting
    public String parent2PokemonSpec = "Pikachu";

    @Setting
    public int amount = 1;

    @Override
    public Class<BreedingEvent> getEventClass() {
        return BreedingEvent.class;
    }

    @Override
    public void handle(QuestLine line, Quest quest, BreedingEvent event) {
        if (eggCheckMode == CheckMode.EXACT && !event.matchesEgg(eggPokemonSpec))
            return;

        // exact + any
        if (parent1CheckMode == CheckMode.EXACT && parent2CheckMode == CheckMode.ANY) {
            // none of the parents are parent1
            if (!event.matchesParent1(parent1PokemonSpec) && !event.matchesParent2(parent1PokemonSpec))
                return;
        }

        // any + exact
        if (parent1CheckMode == CheckMode.ANY && parent2CheckMode == CheckMode.EXACT) {
            // none of the parents are parent2
            if (!event.matchesParent1(parent2PokemonSpec) && !event.matchesParent2(parent2PokemonSpec))
                return;
        }

        // exact + exact
        if (parent1CheckMode == CheckMode.EXACT && parent2CheckMode == CheckMode.EXACT) {
            // doesn't match parent1 + parent2 nor parent2 + parent1
            if (!((event.matchesParent1(parent1PokemonSpec) && event.matchesParent2(parent2PokemonSpec)) ||
                    (event.matchesParent1(parent2PokemonSpec) && event.matchesParent2(parent1PokemonSpec))))
                return;
        }

        PlayerData data = PixelBuiltQuests.getStorage().getData(event.getPlayer());
        if (!this.isCompleted(data, line, quest)) {
            QuestStatus status = data.getStatus(this, line, quest);
            this.increase(data, status, 1);
        }
    }

    @Override
    public int getTotal() {
        return this.amount;
    }

    @Override
    public Text toText() {
        return Text.of(TextColors.GREEN,
                "Breed ", TextColors.AQUA,
                amount, " ",
                eggCheckMode == CheckMode.ANY ? "Pokemon" : eggPokemonSpec,
                (parent1CheckMode != CheckMode.ANY || parent2CheckMode != CheckMode.ANY) ?
                        Text.of(" ",
                                parent1CheckMode == CheckMode.ANY ? "(Any + " : "(" + parent1PokemonSpec + " + ",
                                parent2CheckMode == CheckMode.ANY ? "Any)" : parent2PokemonSpec + ")"
                        )
                        : ""
        );
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.BREEDING;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
