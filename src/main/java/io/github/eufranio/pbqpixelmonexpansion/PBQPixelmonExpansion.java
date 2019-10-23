package io.github.eufranio.pbqpixelmonexpansion;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import io.github.eufranio.pbqpixelmonexpansion.tasks.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import online.pixelbuilt.pbquests.task.TaskType;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "pbq-pixelmonexpansion",
        name = "PBQ PixelmonExpansion",
        description = "PixelBuiltQuests expansion for pixelmon tasks/rewards",
        authors = {
                "Eufranio"
        },
        dependencies = {
                @Dependency(id = "pixelbuilt-quests"),
                @Dependency(id = "pixelmon")
        }
)
public class PBQPixelmonExpansion {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Starting PBQ Pixelmon Expansion!");
        Pixelmon.EVENT_BUS.register(new PixelmonEventHandlers());
    }

    @Listener
    public void onRegister(GameRegistryEvent.Register<TaskType> event) {
        logger.info("Registering new PBQ Tasks...");
        event.register(new TaskType("catching", "Catching Pokemons", CatchingTask.class));
        event.register(new TaskType("chatting", "Chatting to NPCs", ChattingTask.class));
        event.register(new TaskType("defeat_trainer", "Defeating Trainers", DefeatTrainerTask.class));
        event.register(new TaskType("has_caught", "Has caught pokemon", HasCaughtTask.class));
        event.register(new TaskType("has_seen", "Has seen pokemon", HasSeenTask.class));
        event.register(new TaskType("pokemon_level", "Pokemon Level", PokemonLevelTask.class));
        event.register(new TaskType("pokemon_nature", "Pokemon Nature", PokemonNatureTask.class));
        event.register(new TaskType("total_pokemon", "Total Pokemon", TotalPokemonTask.class));
    }
}
