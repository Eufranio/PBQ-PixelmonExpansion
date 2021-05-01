package io.github.eufranio.pbqpixelmonexpansion.reforged;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.eufranio.pbqpixelmonexpansion.common.CheckMode;
import io.github.eufranio.pbqpixelmonexpansion.common.PixelmonBridge;
import io.github.eufranio.pbqpixelmonexpansion.common.events.BreedingEvent;
import io.github.eufranio.pbqpixelmonexpansion.common.events.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;

public class ReforgedBridge implements PixelmonBridge {

    @Override
    public void init() {
        Pixelmon.EVENT_BUS.register(this);
    }

    @Override
    public boolean hasCaught(User user, @Nullable String species, int amount) {
        PlayerPartyStorage storage = this.getStorage(user);
        if (species == null)
            return storage.pokedex.countCaught() >= amount;
        return storage.pokedex.hasCaught(Pokedex.nameToID(species));
    }

    @Override
    public boolean hasSeen(User user, String species) {
        return this.getStorage(user).pokedex.hasSeen(Pokedex.nameToID(species));
    }

    @Override
    public boolean hasLevelPokemons(User user, String pokemonSpec, CheckMode pokemonMode, int level, CheckMode levelMode, int amount, CheckMode amountMode) {
        PlayerPartyStorage storage = this.getStorage(user);
        int count = storage.findAll(poke -> {
            if (pokemonMode == CheckMode.EXACT && !(new PokemonSpec(pokemonSpec).matches(poke)))
                return false;
            if (levelMode == CheckMode.MIN && poke.getLevel() < level ||
                levelMode == CheckMode.EXACT && poke.getLevel() != level ||
                levelMode == CheckMode.MAX && poke.getLevel() > level) {
                return false;
            }
            return true;
        }).size();

        if (amountMode == CheckMode.MIN && count < amount ||
            amountMode == CheckMode.MAX && count > amount) {
            return false;
        }

        return true;
    }

    @SubscribeEvent
    public void onCatch(CaptureEvent.SuccessfulCapture event) {
        Event.Catching triggerEvent = new Event.Catching((Player) event.player, spec -> new PokemonSpec(spec).matches(event.getPokemon().getPokemonData()));
        Sponge.getEventManager().post(triggerEvent);
    }

    @SubscribeEvent
    public void onChat(NPCChatEvent event) {
        Event.Chatting triggerEvent = new Event.Chatting((Player) event.player, ((NPCChatting) event.npc).getName("en/us"));
        Sponge.getEventManager().post(triggerEvent);
    }

    @SubscribeEvent
    public void onBeatTrainer(BeatTrainerEvent event) {
        Event.DefeatTrainer triggerEvent = new Event.DefeatTrainer((Player) event.player, event.trainer.level, event.trainer.getName("en/us"));
        Sponge.getEventManager().post(triggerEvent);
    }

    @SubscribeEvent
    public void onFish(FishingEvent.Reel event) {
        if (event.isPokemon()) {
            Event.PokemonFishing triggerEvent = new Event.PokemonFishing((Player) event.player, spec -> new PokemonSpec(spec).matches((EntityPixelmon) event.optEntity.get()));
            Sponge.getEventManager().post(triggerEvent);
        }
    }

    @SubscribeEvent
    public void onBreed(BreedEvent.MakeEgg event) {
        BreedingEvent triggerEvent = new BreedingEvent(event.owner,
                spec -> new PokemonSpec(spec).matches(event.parent1),
                spec -> new PokemonSpec(spec).matches(event.parent2),
                spec -> new PokemonSpec(spec).matches(event.getEgg())
        );
        Sponge.getEventManager().post(triggerEvent);
    }

    @SubscribeEvent
    public void onBeatWildPokemon(BeatWildPixelmonEvent event) {
        if (event.wpp.controlledPokemon.isEmpty())
            return;
        PixelmonWrapper wrapper = event.wpp.controlledPokemon.get(0);
        if (wrapper != null && wrapper.entity != null) {
            Event.DefeatWildPokemon triggerEvent = new Event.DefeatWildPokemon((Player) event.player, spec -> new PokemonSpec(spec).matches(wrapper.entity));
            Sponge.getEventManager().post(triggerEvent);
        }
    }

    PlayerPartyStorage getStorage(User user) {
        return Pixelmon.storageManager.getParty(user.getUniqueId());
    }
}
