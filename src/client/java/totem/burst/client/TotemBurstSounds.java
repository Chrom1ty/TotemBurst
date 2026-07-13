package totem.burst.client;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class TotemBurstSounds {
    public static final ResourceLocation KILL_BLAST_ID =
            ResourceLocation.fromNamespaceAndPath("totem-burst", "kill_blast");

    public static final SoundEvent KILL_BLAST =
            SoundEvent.createVariableRangeEvent(KILL_BLAST_ID);

    public static void register() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, KILL_BLAST_ID, KILL_BLAST);
    }
}
