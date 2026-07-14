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
                    .startIntSlider(Component.literal("Aberration Strength"), (int)(TotemBurstConfig.effectStrength * 1000), 5, 100)
                    .setDefaultValue((int)(0.03f * 1000))
                    .setTextGetter(value -> Component.literal(String.format("%.3f", value / 1000f)))
                    .setSaveConsumer(val -> TotemBurstConfig.effectStrength = val / 1000f)
                    .build());

            chromaCategory.addEntry(entryBuilder
                    .startIntSlider(Component.literal("Aberration Duration (ms)"), (int)(TotemBurstConfig.effectDuration / 500), 1, 10)
                    .setDefaultValue((int)(3000f / 500))
                    .setTextGetter(value -> Component.literal((value * 500) + " ms"))
                    .setSaveConsumer(val -> {
                        float fVal = val * 500f;
                        TotemBurstConfig.effectDuration = fVal;
                        ChromaticAberrationEffect.DURATION = fVal;
                    })
                    .build());

            ConfigCategory radialCategory = builder.getOrCreateCategory(Component.literal("Radial Blur (Kill Effect)"));

            radialCategory.addEntry(entryBuilder
                    .startIntSlider(Component.literal("Blur Strength"), (int)(TotemBurstConfig.radialBlurStrength * 1000), 10, 100)
                    .setDefaultValue((int)(0.1f * 1000))
                    .setTextGetter(value -> Component.literal(String.format("%.3f", value / 1000f)))
                    .setSaveConsumer(val -> TotemBurstConfig.radialBlurStrength = val / 1000f)
                    .build());

            radialCategory.addEntry(entryBuilder
                    .startIntSlider(Component.literal("Blur Duration (ms)"), (int)(TotemBurstConfig.radialBlurDuration / 500), 0, 10)
                    .setDefaultValue((int)(3000f / 500))
                    .setTextGetter(value -> Component.literal((value * 500) + " ms"))
                    .setSaveConsumer(val -> {
                        float fVal = val * 500f;
                        TotemBurstConfig.radialBlurDuration = fVal;
                        RadialBlurEffect.DURATION = fVal;
                    })
                    .build());

            ConfigCategory comboCategory = builder.getOrCreateCategory(Component.literal("Combo (Multi-Kill Screen Contrast)"));

            comboCategory.addEntry(entryBuilder
                    .startIntSlider(Component.literal("Contrast Strength"), (int)(TotemBurstConfig.comboContrastStrength * 100), 10, 200)
                    .setDefaultValue((int)(1.0f * 100))
                    .setTextGetter(value -> Component.literal(String.format("%.2f", value / 100f)))
                    .setSaveConsumer(val -> TotemBurstConfig.comboContrastStrength = val / 100f)
                    .build());

            comboCategory.addEntry(entryBuilder
                    .startIntSlider(Component.literal("Contrast Duration (ms)"), (int)(TotemBurstConfig.comboContrastDuration / 500), 1, 10)
                    .setDefaultValue((int)(1200f / 500))
                    .setTextGetter(value -> Component.literal((value * 500) + " ms"))
                    .setSaveConsumer(val -> {
                        float fVal = val * 500f;
                        TotemBurstConfig.comboContrastDuration = fVal;
                        ComboEffect.DURATION = fVal;
                    })
                    .build());

            comboCategory.addEntry(entryBuilder
                    .startIntSlider(Component.literal("Per-Kill Escalation Step"), (int)(TotemBurstConfig.comboStepIntensity * 100), 5, 100)
                    .setDefaultValue((int)(0.25f * 100))
                    .setTextGetter(value -> Component.literal(String.format("%.2f", value / 100f)))
                    .setSaveConsumer(val -> {
                        float fVal = val.floatValue() / 100f;
                        TotemBurstConfig.comboStepIntensity = fVal;
                        ComboEffect.PER_COMBO_STEP = fVal;
                    })
                    .build());

            comboCategory.addEntry(entryBuilder
                    .startLongSlider(Component.literal("Combo Chain Window (ms)"), KillTracker.COMBO_CHAIN_WINDOW_MS / 500L, 1L, 16L)
                    .setDefaultValue(3000L / 500L)
                    .setTextGetter(value -> Component.literal((value * 500L) + " ms"))
                    .setSaveConsumer(val -> KillTracker.COMBO_CHAIN_WINDOW_MS = val * 500L)
                    .build());

            return builder.build();
        };
    }
}