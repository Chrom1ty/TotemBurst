package totem.burst.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class TotemBurstModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("TotemBurst Settings"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory chromaCategory = builder.getOrCreateCategory(Component.literal("Aberration (Totem)"));

            chromaCategory.addEntry(entryBuilder
                    .startFloatField(Component.literal("Aberration Strength"), TotemBurstConfig.effectStrength)
                    .setDefaultValue(0.03f)
                    .setMin(0.005f)
                    .setMax(0.1f)
                    .setSaveConsumer(val -> TotemBurstConfig.effectStrength = val)
                    .build());

            chromaCategory.addEntry(entryBuilder
                    .startFloatField(Component.literal("Aberration Duration (ms)"), TotemBurstConfig.effectDuration)
                    .setDefaultValue(3000f)
                    .setMin(500f)
                    .setMax(5000f)
                    .setSaveConsumer(val -> {
                        TotemBurstConfig.effectDuration = val;
                        ChromaticAberrationEffect.DURATION = val;
                    })
                    .build());

            ConfigCategory radialCategory = builder.getOrCreateCategory(Component.literal("Radial Blur (Kill Effect)"));

            radialCategory.addEntry(entryBuilder
                    .startFloatField(Component.literal("Blur Strength"), TotemBurstConfig.radialBlurStrength)
                    .setDefaultValue(0.1f)
                    .setMin(0.01f)
                    .setMax(0.2f)
                    .setSaveConsumer(val -> TotemBurstConfig.radialBlurStrength = val)
                    .build());

            radialCategory.addEntry(entryBuilder
                    .startFloatField(Component.literal("Blur Duration (ms)"), TotemBurstConfig.radialBlurDuration)
                    .setDefaultValue(3000f)
                    .setMin(200f)
                    .setMax(3000f)
                    .setSaveConsumer(val -> {
                        TotemBurstConfig.radialBlurDuration = val;
                        RadialBlurEffect.DURATION = val;
                    })
                    .build());

            return builder.build();
        };
    }
}