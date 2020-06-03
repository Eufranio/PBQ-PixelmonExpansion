package io.github.eufranio.pbqpixelmonexpansion.common.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent;
import org.spongepowered.api.event.impl.AbstractEvent;

import java.util.function.Predicate;

public class Event extends AbstractEvent implements TargetPlayerEvent {

    Player player;

    public Event(Player player) {
        this.player = player;
    }

    @Override
    public Player getTargetEntity() {
        return this.player;
    }

    @Override
    public Cause getCause() {
        return Sponge.getCauseStackManager().getCurrentCause();
    }

    public static class Catching extends Event {

        Predicate<String> pokemonSpecMatcher;

        public Catching(Player player, Predicate<String> pokemonSpecMatcher) {
            super(player);
            this.pokemonSpecMatcher = pokemonSpecMatcher;
        }

        public boolean matches(String pokemonSpec) {
            return this.pokemonSpecMatcher.test(pokemonSpec);
        }
    }

    public static class Chatting extends Event {

        String npc;

        public Chatting(Player player, String npc) {
            super(player);
            this.npc = npc;
        }

        public String getNPC() {
            return npc;
        }
    }

    public static class DefeatTrainer extends Event {

        int level;
        String npc;

        public DefeatTrainer(Player player, int level, String npc) {
            super(player);
            this.level = level;
            this.npc = npc;
        }

        public String getNpc() {
            return npc;
        }

        public int getLevel() {
            return level;
        }
    }

}
