package dev.eufranio.pbqpixelmonexpansion.task.defeat;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import dev.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import dev.eufranio.pixelbuiltquests.listeners.TaskListenerProvider;
import dev.eufranio.pixelbuiltquests.task.TaskType;
import dev.eufranio.pixelbuiltquests.task.TriggeredTask;

public class DefeatWildPokeTaskProvider extends TaskListenerProvider {

    ObservableSubscription<BattleVictoryEvent> subscription;

    @Override
    public TaskType taskType() {
        return PBQPixelmonExpansion.DEFEAT_WILD_POKE;
    }

    @Override
    public void registerListeners() {
        subscription = CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, (BattleVictoryEvent event) -> {
            listeningTasks().forEach(questTask ->
                    ((TriggeredTask) questTask.getTask()).handle(questTask, event));
            return null;
        });
    }

    @Override
    public void unregisterListeners() {
        CobblemonEvents.BATTLE_VICTORY.unsubscribe(subscription);
    }

}