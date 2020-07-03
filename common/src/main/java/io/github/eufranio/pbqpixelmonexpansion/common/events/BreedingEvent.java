package io.github.eufranio.pbqpixelmonexpansion.common.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

import java.util.UUID;
import java.util.function.Predicate;

public class BreedingEvent extends AbstractEvent {

    UUID player;
    Predicate<String> parent1SpecMatcher;
    Predicate<String> parent2SpecMatcher;
    Predicate<String> eggSpecMatcher;

    public BreedingEvent(UUID player,
                    Predicate<String> parent1SpecMatcher,
                    Predicate<String> parent2SpecMatcher,
                    Predicate<String> eggSpecMatcher) {
        this.player = player;
        this.parent1SpecMatcher = parent1SpecMatcher;
        this.parent2SpecMatcher = parent2SpecMatcher;
        this.eggSpecMatcher = eggSpecMatcher;
    }

    public UUID getPlayer() {
        return player;
    }

    public boolean matchesEgg(String pokemonSpec) {
        return this.eggSpecMatcher.test(pokemonSpec);
    }

    public boolean matchesParent1(String pokemonSpec) {
        return this.parent1SpecMatcher.test(pokemonSpec);
    }

    public boolean matchesParent2(String pokemonSpec) {
        return this.parent2SpecMatcher.test(pokemonSpec);
    }

    @Override
    public Cause getCause() {
        return Sponge.getCauseStackManager().getCurrentCause();
    }
}
