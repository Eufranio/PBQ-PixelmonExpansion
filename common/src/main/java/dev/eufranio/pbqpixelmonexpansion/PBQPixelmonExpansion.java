package dev.eufranio.pbqpixelmonexpansion;

import dev.eufranio.pbqpixelmonexpansion.task.HasCaughtFromDexTask;
import dev.eufranio.pbqpixelmonexpansion.task.HasCaughtSpeciesTask;
import dev.eufranio.pbqpixelmonexpansion.task.HasSeenFromDexTask;
import dev.eufranio.pbqpixelmonexpansion.task.HasSeenSpeciesTask;
import dev.eufranio.pbqpixelmonexpansion.task.catching.CatchingTask;
import dev.eufranio.pbqpixelmonexpansion.task.catching.CatchingTaskProvider;
import dev.eufranio.pbqpixelmonexpansion.task.defeat.DefeatWildPokeTask;
import dev.eufranio.pbqpixelmonexpansion.task.defeat.DefeatWildPokeTaskProvider;
import dev.eufranio.pbqpixelmonexpansion.task.fishing.FishingTaskProvider;
import dev.eufranio.pbqpixelmonexpansion.task.fishing.PokemonFishingTask;
import dev.eufranio.pixelbuiltquests.events.PBQEvents;
import dev.eufranio.pixelbuiltquests.task.TaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PBQPixelmonExpansion {

    public static final String MOD_ID = "pbq-pixelmonexpansion";
    public static Logger LOGGER;

    static PBQPixelmonExpansion instance;

    public static TaskType CATCHING = TaskType.builder()
            .id("catching")
            .name("Catching Pokemon")
            .task(CatchingTask.class)
            .listenerProvider(new CatchingTaskProvider())
            .build();

    public static TaskType DEFEAT_WILD_POKE = TaskType.builder()
            .id("defeat_wild_poke")
            .name("Defeat Wild Pokemon")
            .task(DefeatWildPokeTask.class)
            .listenerProvider(new DefeatWildPokeTaskProvider())
            .build();

    public static TaskType HAS_CAUGHT_FROM_DEX = TaskType.builder()
            .id("has_caught_from_dex")
            .name("Has caught pokemon from dex")
            .task(HasCaughtFromDexTask.class)
            .build();

    public static TaskType HAS_CAUGHT_SPECIES = TaskType.builder()
            .id("has_caught_species")
            .name("Has caught pokemon of species")
            .task(HasCaughtSpeciesTask.class)
            .build();

    public static TaskType HAS_SEEN_FROM_DEX = TaskType.builder()
            .id("has_seen_from_dex")
            .name("Has seen pokemon from dex")
            .task(HasSeenFromDexTask.class)
            .build();

    public static TaskType HAS_SEEN_SPECIES = TaskType.builder()
            .id("has_seen_species")
            .name("Has seen pokemon of species")
            .task(HasSeenSpeciesTask.class)
            .build();

    public static TaskType POKEMON_FISHING = TaskType.builder()
            .id("pokemon_fishing")
            .name("Pokemon Fishing")
            .task(PokemonFishingTask.class)
            .listenerProvider(new FishingTaskProvider())
            .build();

    public PBQPixelmonExpansion() {
        LOGGER = LoggerFactory.getLogger(MOD_ID);
        instance = this;
        LOGGER.info("Starting PBQ Pixelmon Expansion");

        registerTasks();

        PBQEvents.RELOAD.register(this::registerTasks);
    }

    void registerTasks() {
        PBQEvents.REGISTER_TYPES.register((type, registry, module) -> {
            if (type.equals(TaskType.class)) {
                LOGGER.info("Registering PBQ - Pixelmon Expansion Tasks");
                module.registerAdditionalCatalog(CATCHING);
                module.registerAdditionalCatalog(DEFEAT_WILD_POKE);
                module.registerAdditionalCatalog(HAS_CAUGHT_FROM_DEX);
                module.registerAdditionalCatalog(HAS_CAUGHT_SPECIES);
                module.registerAdditionalCatalog(HAS_SEEN_FROM_DEX);
                module.registerAdditionalCatalog(HAS_SEEN_SPECIES);
                module.registerAdditionalCatalog(POKEMON_FISHING);
            }
        });
    }

}
