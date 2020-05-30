package io.github.eufranio.pbqpixelmonexpansion;

import com.google.inject.Inject;
import io.github.eufranio.pbqpixelmonexpansion.common.PixelmonBridge;
import io.github.eufranio.pbqpixelmonexpansion.tasks.*;
import online.pixelbuilt.pbquests.task.TaskType;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "pbq-pixelmonexpansion",
        name = "PBQ PixelmonExpansion reforged",
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

    PixelmonBridge bridge;
    static PBQPixelmonExpansion instance;

    public static TaskType CATCHING = new TaskType("catching", "Catching Pokemons", CatchingTask.class);
    public static TaskType CHATTING = new TaskType("chatting", "Chatting to NPCs", ChattingTask.class);
    public static TaskType DEFEAT_TRAINER = new TaskType("defeat_trainer", "Defeating Trainers", DefeatTrainerTask.class);
    public static TaskType HAS_CAUGHT = new TaskType("has_caught", "Has caught pokemon", HasCaughtTask.class);
    public static TaskType HAS_SEEN = new TaskType("has_seen", "Has seen pokemon", HasSeenTask.class);
    public static TaskType POKEMON_LEVEL = new TaskType("pokemon_level", "Pokemon Level", PokemonLevelTask.class);
    public static TaskType TOTAL_POKEMON = new TaskType("total_pokemon", "Total Pokemon", TotalPokemonTask.class);

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        instance = this;
        logger.info("Starting PBQ Pixelmon Expansion");

        try {
            Class.forName("com.pixelmonmod.pixelmon.api.pokemon.Pokemon");
            bridge = (PixelmonBridge) Class.forName("io.github.eufranio.pbqpixelmonexpansion.reforged.ReforgedBridge").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            try {
                bridge = (PixelmonBridge) Class.forName("io.github.eufranio.pbqpixelmonexpansion.generations.GenerationsBridge").newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                logger.error("PBQ Pixelmon Expansion could not load the Pixelmon bridge!");
            }
        }
    }

    @Listener
    public void onRegister(GameRegistryEvent.Register<TaskType> event) {
        logger.info("Registering PBQ - Pixelmon Expansion Tasks");
        event.register(CATCHING);
        event.register(CHATTING);
        event.register(DEFEAT_TRAINER);
        event.register(HAS_CAUGHT);
        event.register(HAS_SEEN);
        event.register(POKEMON_LEVEL);
        event.register(TOTAL_POKEMON);
    }

    public PixelmonBridge getBridge() {
        return bridge;
    }

    public static PBQPixelmonExpansion getInstance() {
        return instance;
    }
}
