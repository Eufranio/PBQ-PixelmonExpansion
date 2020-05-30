package io.github.eufranio.pbqpixelmonexpansion.tasks;

import io.github.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import io.github.eufranio.pbqpixelmonexpansion.common.events.Event;
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
public class ChattingTask implements TriggeredTask<Event.Chatting> {

    @Setting
    public int id = 101;

    @Setting
    public String npc = "Test";

    @Setting(comment = "Checking mode: \"any\" NPC or a \"specific\" named NPC")
    public String mode = "any";

    @Override
    public Class<Event.Chatting> getEventClass() {
        return Event.Chatting.class;
    }

    @Override
    public void handle(QuestLine line, Quest quest, Event.Chatting event) {
        if (mode.toLowerCase().equals("any") || event.getNPC().equals(npc)) {
            PlayerData data = PixelBuiltQuests.getStorage().getData(event.getTargetEntity().getUniqueId());
            QuestStatus status = data.getStatus(this, line, quest);
            this.increase(data, status, 1);
        }
    }

    @Override
    public int getTotal() {
        return 1;
    }

    @Override
    public Text getDisplay() {
        return Text.of(TextColors.GREEN, "Talk to ", TextColors.AQUA, mode.toLowerCase().equals("any") ? "an NPC" : npc);
    }

    @Override
    public TaskType getType() {
        return PBQPixelmonExpansion.CHATTING;
    }

    @Override
    public int getId() {
        return id;
    }
}
