package io.github.eufranio.pbqpixelmonexpansion;

import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.NPCChatEvent;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.github.eufranio.pbqpixelmonexpansion.tasks.CatchingTask;
import io.github.eufranio.pbqpixelmonexpansion.tasks.ChattingTask;
import io.github.eufranio.pbqpixelmonexpansion.tasks.DefeatTrainerTask;
import io.github.eufranio.pbqpixelmonexpansion.tasks.PokemonNatureTask;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frani on 28/01/2019.
 */
public class PixelmonEventHandlers {

    @SubscribeEvent
    public void onCatch(CaptureEvent.SuccessfulCapture event) {
        Map<EnumSpecies, Integer> map = CatchingTask.caught.computeIfAbsent(event.player.getUniqueID(), uuid -> new HashMap<>());
        map.compute(event.getPokemon().getPokemonData().getSpecies(), (poke, value) -> {
            if (value == null) {
                return 1;
            } else {
                return ++value;
            }
        });
        PokemonNatureTask.natures.put(event.player.getUniqueID(), event.getPokemon().getPokemonData().getNature());
    }

    @SubscribeEvent
    public void onChat(NPCChatEvent event) {
        ChattingTask.npcs.put(event.player.getUniqueID(), event.npc.getName());
    }

    @SubscribeEvent
    public void onBeatTrainer(BeatTrainerEvent e) {
        DefeatTrainerTask.players.add(e.player.getUniqueID());
    }

}
