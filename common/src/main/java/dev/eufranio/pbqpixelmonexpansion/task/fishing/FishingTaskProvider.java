package dev.eufranio.pbqpixelmonexpansion.task.fishing;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.fishing.BobberSpawnPokemonEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import dev.eufranio.pbqpixelmonexpansion.PBQPixelmonExpansion;
import dev.eufranio.pixelbuiltquests.listeners.TaskListenerProvider;
import dev.eufranio.pixelbuiltquests.task.TaskType;
import dev.eufranio.pixelbuiltquests.task.TriggeredTask;

public class FishingTaskProvider extends TaskListenerProvider {

    ObservableSubscription<BobberSpawnPokemonEvent.Post> subscription;

    @Override
    public TaskType taskType() {
        return PBQPixelmonExpansion.POKEMON_FISHING;
    }

    @Override
    public void registerListeners() {
        subscription = CobblemonEvents.BOBBER_SPAWN_POKEMON_POST.subscribe(Priority.NORMAL, (BobberSpawnPokemonEvent.Post event) -> {
            listeningTasks().forEach(questTask ->
                    ((TriggeredTask) questTask.getTask()).handle(questTask, event));
            return null;
        });
    }

    @Override
    public void unregisterListeners() {
        CobblemonEvents.BOBBER_SPAWN_POKEMON_POST.unsubscribe(subscription);
    }

}
