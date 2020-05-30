package io.github.eufranio.pbqpixelmonexpansion.common;

import org.spongepowered.api.entity.living.player.User;

import javax.annotation.Nullable;

public interface PixelmonBridge {

    void init();

    default boolean hasCaught(User user, String species) {
        return hasCaught(user, species, 1);
    }

    boolean hasCaught(User user, @Nullable String species, int amount);

    boolean hasSeen(User user, String species);

    boolean hasLevelPokemons(User user,
                             String species,
                             CheckMode speciesMode,
                             int level,
                             CheckMode levelMode,
                             int amount,
                             CheckMode amountMode);

}
