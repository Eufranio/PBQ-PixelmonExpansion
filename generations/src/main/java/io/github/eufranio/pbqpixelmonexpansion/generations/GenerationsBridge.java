package io.github.eufranio.pbqpixelmonexpansion.generations;

import com.pixelmongenerations.api.events.BeatWildPixelmonEvent;
import com.pixelmongenerations.api.events.BreedEvent;
import com.pixelmongenerations.api.events.CaptureEvent;
import com.pixelmongenerations.api.events.FishingEvent;
import com.pixelmongenerations.api.events.npc.BeatTrainerEvent;
import com.pixelmongenerations.api.events.npc.NPCChatEvent;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.common.battle.controller.participants.PixelmonWrapper;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.pokedex.Pokedex;
import com.pixelmongenerations.core.storage.NbtKeys;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import io.github.eufranio.pbqpixelmonexpansion.common.CheckMode;
import io.github.eufranio.pbqpixelmonexpansion.common.PixelmonBridge;
import io.github.eufranio.pbqpixelmonexpansion.common.events.BreedingEvent;
import io.github.eufranio.pbqpixelmonexpansion.common.events.Event;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class GenerationsBridge implements PixelmonBridge {

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hasCaught(User user, @Nullable String species, int amount) {
        PlayerStorage storage = this.getStorage(user);
        if (species == null)
            return storage.pokedex.countCaught() >= amount;
        return storage.pokedex.hasCaught(Pokedex.nameToID(species));
    }

    @Override
    public boolean hasSeen(User user, String species) {
        return this.getStorage(user).pokedex.hasSeen(Pokedex.nameToID(species));
    }

    @Override
    public boolean hasLevelPokemons(User user, String pokemonSpec, CheckMode pokeMode, int level, CheckMode levelMode, int amount, CheckMode amountMode) {
        PlayerStorage storage = this.getStorage(user);
        long count = Stream.of(storage.partyPokemon).filter(poke -> {
            if (poke == null)
                return false;
            if (pokeMode == CheckMode.EXACT && !(new PokemonSpec(pokemonSpec).matches(poke)))
                return false;
            if (levelMode == CheckMode.MIN && poke.getInteger(NbtKeys.LEVEL) < level ||
                    levelMode == CheckMode.EXACT && poke.getInteger(NbtKeys.LEVEL) != level ||
                    levelMode == CheckMode.MAX && poke.getInteger(NbtKeys.LEVEL) > level) {
                return false;
            }
            return true;
        }).count();

        if (amountMode == CheckMode.MIN && count < amount ||
            amountMode == CheckMode.MAX && count > amount) {
            return false;
        }

        return true;
    }

    @SubscribeEvent
    public void onCatch(CaptureEvent.SuccessfulCaptureEvent event) {
        Event.Catching triggerEvent = new Event.Catching((Player) event.getPlayer(), spec -> new PokemonSpec(spec).matches(event.getPokemon()));
        Sponge.getEventManager().post(triggerEvent);
    }

    @SubscribeEvent
    public void onChat(NPCChatEvent event) {
        Event.Chatting triggerEvent = new Event.Chatting((Player) event.player, event.npc.getName("en/us"));
        Sponge.getEventManager().post(triggerEvent);
    }

    @SubscribeEvent
    public void onBeatTrainer(BeatTrainerEvent event) {
        Event.DefeatTrainer triggerEvent = new Event.DefeatTrainer(
                (Player) event.getPlayer(),
                event.getTrainer().writeToNBT(new NBTTagCompound()).getInteger(NbtKeys.NPCLEVEL),
                event.getTrainer().getName("en/us")
        );
        Sponge.getEventManager().post(triggerEvent);
    }

    @SubscribeEvent
    public void onFish(FishingEvent.FishingReelEvent event) {
        if (event.isPokemon()) {
            Event.PokemonFishing triggerEvent = new Event.PokemonFishing((Player) event.getPlayer(), spec -> new PokemonSpec(spec).matches((EntityPixelmon) event.getEntity().get()));
            Sponge.getEventManager().post(triggerEvent);
        }
    }

    @SubscribeEvent
    public void onBreed(BreedEvent.MakeEggEvent event) {
        BreedingEvent triggerEvent = new BreedingEvent(event.getOwner(),
                spec -> new PokemonSpec(spec).matches(event.getParentOne()),
                spec -> new PokemonSpec(spec).matches(event.getParentTwo()),
                spec -> new PokemonSpec(spec).matches(event.getEgg())
        );
        Sponge.getEventManager().post(triggerEvent);
    }

    @SubscribeEvent
    public void onBeatWildPokemon(BeatWildPixelmonEvent event) {
        PixelmonWrapper wrapper = event.getWildPokemon().getPokemon();
        if (wrapper != null && wrapper.pokemon != null) {
            Event.DefeatWildPokemon triggerEvent = new Event.DefeatWildPokemon((Player) event.getPlayer(), spec -> new PokemonSpec(spec).matches(wrapper.pokemon));
            Sponge.getEventManager().post(triggerEvent);
        }
    }

    PlayerStorage getStorage(User user) {
        return PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(user.getUniqueId()).get();
    }
}