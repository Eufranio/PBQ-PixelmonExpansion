package io.github.eufranio.pbqpixelmonexpansion.generations;

import com.pixelmongenerations.api.events.CaptureEvent;
import com.pixelmongenerations.api.events.npc.BeatTrainerEvent;
import com.pixelmongenerations.api.events.npc.NPCChatEvent;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.common.pokedex.Pokedex;
import com.pixelmongenerations.core.storage.NbtKeys;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import io.github.eufranio.pbqpixelmonexpansion.common.CheckMode;
import io.github.eufranio.pbqpixelmonexpansion.common.PixelmonBridge;
import io.github.eufranio.pbqpixelmonexpansion.common.events.Event;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class GenerationsBridge implements PixelmonBridge {

    @Override
    public void init() {}

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
    public boolean hasLevelPokemons(User user, String species, CheckMode speciesMode, int level, CheckMode levelMode, int amount, CheckMode amountMode) {
        PlayerStorage storage = this.getStorage(user);
        long count = Stream.of(storage.partyPokemon).filter(poke -> {
            if (poke == null)
                return false;
            if (speciesMode == CheckMode.EXACT && !poke.getString(NbtKeys.NAME).equals(species))
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
        Entity entity = (Entity) event.npc;
        Event.Chatting triggerEvent = new Event.Chatting((Player) event.player, entity.get(Keys.DISPLAY_NAME).orElse(Text.of(entity.getType().getTranslation())).toPlain().trim());
        Sponge.getEventManager().post(triggerEvent);
    }

    @SubscribeEvent
    public void onBeatTrainer(BeatTrainerEvent event) {
        Entity entity = (Entity) event.getTrainer();
        Event.DefeatTrainer triggerEvent = new Event.DefeatTrainer(
                (Player) event.getPlayer(),
                event.getTrainer().writeToNBT(new NBTTagCompound()).getInteger(NbtKeys.NPCLEVEL),
                entity.get(Keys.DISPLAY_NAME).orElse(Text.of(entity.getType().getTranslation())).toPlain().trim()
        );
        Sponge.getEventManager().post(triggerEvent);
    }

    PlayerStorage getStorage(User user) {
        return PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(user.getUniqueId()).get();
    }
}