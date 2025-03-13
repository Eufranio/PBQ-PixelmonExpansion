package dev.eufranio.pbqpixelmonexpansion.task.catching;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import dev.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import dev.eufranio.pixelbuiltquests.listeners.TaskListenerProvider;
import dev.eufranio.pixelbuiltquests.task.TaskType;
import dev.eufranio.pixelbuiltquests.task.TriggeredTask;

public class CatchingTaskProvider extends TaskListenerProvider {

    ObservableSubscription<PokemonCapturedEvent> subscription;

    @Override
    public TaskType taskType() {
        return PBQPixelmonExpansion.CATCHING;
    }

    @Override
    public void registerListeners() {
        subscription = CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, (PokemonCapturedEvent event) -> {
            listeningTasks().forEach(questTask ->
                    ((TriggeredTask) questTask.getTask()).handle(questTask, event));
            return null;
        });
    }

    @Override
    public void unregisterListeners() {
        CobblemonEvents.POKEMON_CAPTURED.unsubscribe(subscription);
    }

}