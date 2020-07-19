package io.github.eufranio.pbqpixelmonexpansion.tasks;

import io.github.eufranio.pbqpixelmonexpansion.common.events.Event;
import io.github.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
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
 * Created by Frani on 28/01/2019.
 */
@ConfigSerializable
public class DefeatTrainerTask implements TriggeredTask<Event.DefeatTrainer> {

    @Setting
    public int id = 102;

    @Setting(comment = "If not empty, check if the trainer name is \"name\"")
    public String name = "Default";

    @Setting(comment = "Checking mode: \"any\" trainer or trainers with a least \"level\" level")
    public String mode = "any";

    @Setting
    public int level = 10;

    @Setting
    public int amount = 1;

    @Override
    public Class<Event.DefeatTrainer> getEventClass() {
        return Event.DefeatTrainer.class;
    }

    @Override
    public void handle(QuestLine line, Quest quest, Event.DefeatTrainer event) {
        if (!name.isEmpty() && !event.getNpc().equals(name))
            return;
        if (mode.toLowerCase().equals("any") || event.getLevel() >= level) {
            PlayerData data = PixelBuiltQuests.getStorage().getData(event.getTargetEntity().getUniqueId());
            if (!this.isCompleted(data, line, quest)) {
                QuestStatus status = data.getStatus(this, line, quest);
                this.increase(data, status, 1);
            }
        }
    }

    @Override
    public int getTotal() {
        return amount;
    }

    @Override
    public Text toText() {
        return Text.of(TextColors.GREEN, "Defeat ", TextColors.AQUA, amount, " ",
                mode.toLowerCase().equals("any") ? "Trainer(s)" : ("Lvl " + level + "+ Trainer(s)"));
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.DEFEAT_TRAINER;
    }

    @Override
    public int getId() {
        return id;
    }
}
