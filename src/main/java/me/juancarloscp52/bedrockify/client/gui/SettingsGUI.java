package me.juancarloscp52.bedrockify.client.gui;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.BedrockifySettings;
import me.juancarloscp52.bedrockify.mixin.featureManager.MixinFeatureManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.*;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Map;

public class SettingsGUI {

    BedrockifySettings settings = Bedrockify.getInstance().settings;

    public Screen getConfigScreen(Screen parent, boolean isTransparent){
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.translatable("bedrockify.options.settings"));
        builder.setSavingRunnable(()-> {
            Bedrockify.getInstance().saveSettings();
            MixinFeatureManager.saveMixinSettings();
        });
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigCategory mixins = builder.getOrCreateCategory(Text.literal("Mixins"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        SubCategoryBuilder bedrockOverlay = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.bedrockOverlay"));
        bedrockOverlay.add(entryBuilder.startTextDescription(Text.translatable("bedrockify.options.subCategory.bedrockOverlay.description")).build());
        bedrockOverlay.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.showCoordinates"), settings.showPositionHUD).setDefaultValue(true).setSaveConsumer(newValue -> settings.showPositionHUD=newValue).build());
        bedrockOverlay.add(entryBuilder.startSelector(Text.translatable("bedrockify.options.showFPS"), new Byte []{0,1,2},settings.FPSHUD).setDefaultValue((byte) 0).setNameProvider((value)-> switch (value) {
            case 0 -> Text.translatable("bedrockify.options.off");
            case 1 -> Text.translatable("bedrockify.options.withPosition");
            default -> Text.translatable("bedrockify.options.underPosition");
        }).setSaveConsumer((newValue)->settings.FPSHUD=newValue).build());
        bedrockOverlay.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.coordinatesPosition"),settings.positionHUDHeight,0,100).setDefaultValue(50).setSaveConsumer((newValue)->settings.positionHUDHeight=newValue).build());
        bedrockOverlay.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.showPaperDoll"), settings.showPaperDoll).setDefaultValue(true).setSaveConsumer(newValue -> settings.showPaperDoll=newValue).build());
        bedrockOverlay.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.showSavingOverlay"), settings.savingOverlay).setDefaultValue(true).setSaveConsumer(newValue -> settings.savingOverlay=newValue).build());
        general.addEntry(bedrockOverlay.build());
        SubCategoryBuilder guiImprovements = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.visualImprovements"));
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.biggerItems"), settings.biggerIcons).setDefaultValue(true).setSaveConsumer(newValue -> settings.biggerIcons=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.transparentToolBar"), settings.transparentHotBar).setDefaultValue(true).setSaveConsumer(newValue -> settings.transparentHotBar=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.expTextStyle"), settings.expTextStyle).setDefaultValue(true).setSaveConsumer(newValue -> settings.expTextStyle=newValue).setYesNoTextSupplier((value)->value ? Text.translatable("bedrockify.options.chatStyle.bedrock") : Text.translatable("bedrockify.options.chatStyle.vanilla")).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.chatStyle"), settings.bedrockChat).setDefaultValue(true).setSaveConsumer(newValue -> settings.bedrockChat=newValue).setYesNoTextSupplier((value)->value ? Text.translatable("bedrockify.options.chatStyle.bedrock") : Text.translatable("bedrockify.options.chatStyle.vanilla")).build());
        guiImprovements.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.screenSafeArea"),settings.screenSafeArea,0,30).setDefaultValue(0).setSaveConsumer((newValue)->settings.screenSafeArea=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.ignoreBorder"), settings.overlayIgnoresSafeArea).setDefaultValue(false).setSaveConsumer(newValue -> settings.overlayIgnoresSafeArea=newValue).build());
        guiImprovements.add(entryBuilder.startSelector(Text.translatable("bedrockify.options.tooltips"), new Byte []{0,1,2},settings.heldItemTooltip).setDefaultValue((byte) 2).setNameProvider((value)-> switch (value) {
            case 0 -> Text.translatable("bedrockify.options.off");
            case 1 -> Text.translatable("bedrockify.options.on");
            default -> Text.translatable("bedrockify.options.withBackground");
        }).setSaveConsumer((newValue)->settings.heldItemTooltip=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.inventoryHighlight"), settings.slotHighlight).setDefaultValue(true).setSaveConsumer(newValue -> settings.slotHighlight=newValue).build());
        guiImprovements.add(entryBuilder.startAlphaColorField(Text.translatable("bedrockify.options.inventoryHighlight.color1"),settings.highLightColor1).setDefaultValue((255 << 8) + (255) + (255 << 16) + (255 << 24)).setSaveConsumer(newValue -> settings.highLightColor1=newValue).build());
        guiImprovements.add(entryBuilder.startAlphaColorField(Text.translatable("bedrockify.options.inventoryHighlight.color2"),settings.highLightColor2).setDefaultValue(64 + (170 << 8) + (109 << 16) + (255 << 24)).setSaveConsumer(newValue -> settings.highLightColor2=newValue).build());
        guiImprovements.add(entryBuilder.startSelector(Text.translatable("bedrockify.options.idleAnimation"), new Float []{0.0f,0.5f,1.0f,1.5f,2.0f,2.5f,3.0f,4.0f},settings.idleAnimation).setDefaultValue(1.0f).setNameProvider((value)-> Text.literal("x"+ value)).setSaveConsumer((newValue)->settings.idleAnimation=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.eatingAnimations"), settings.eatingAnimations).setDefaultValue(true).setSaveConsumer(newValue -> settings.eatingAnimations=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.pickupAnimations"), settings.pickupAnimations).setTooltip(wrapLines(Text.translatable("bedrockify.options.pickupAnimations.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settings.pickupAnimations=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.loadingScreen"), settings.loadingScreen).setDefaultValue(true).setSaveConsumer(newValue -> settings.loadingScreen=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.bedrockShading"), settings.bedrockShading).setDefaultValue(true).setSaveConsumer(newValue -> {
            settings.bedrockShading=newValue;
            MinecraftClient.getInstance().worldRenderer.reload();
        }).build());
        general.addEntry(guiImprovements.build());
        SubCategoryBuilder reachAround = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.Reach-Around"));
        reachAround.add(entryBuilder.startTextDescription(Text.translatable("bedrockify.options.subCategory.Reach-Around.description")).build());
        reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround"), settings.reacharound).setDefaultValue(true).setSaveConsumer(newValue -> settings.reacharound=newValue).build());
        reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround.multiplayer"), settings.reacharoundMultiplayer).setDefaultValue(true).setSaveConsumer(newValue -> settings.reacharoundMultiplayer=newValue).build());
        reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround.sneaking"), settings.reacharoundSneaking).setDefaultValue(true).setSaveConsumer(newValue -> settings.reacharoundSneaking=newValue).build());
        reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround.indicator"), settings.reacharoundIndicator).setDefaultValue(true).setSaveConsumer(newValue -> settings.reacharoundIndicator=newValue).build());
        reachAround.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.reachAround.pitch"),settings.reacharoundPitchAngle, 0,90).setDefaultValue(25).setSaveConsumer(newValue -> settings.reacharoundPitchAngle=newValue).build());
        reachAround.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.reachAround.distance"), MathHelper.floor(settings.reacharoundBlockDistance*100), 0,100).setTextGetter((integer -> Text.literal(String.valueOf(integer/100d)))).setDefaultValue(75).setSaveConsumer(newValue -> settings.reacharoundBlockDistance=newValue/100d).build());
        general.addEntry(reachAround.build());
        SubCategoryBuilder otherSettings = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.other"));
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.loadingScreenChunkMap"), settings.showChunkMap).setTooltip(wrapLines(Text.translatable("bedrockify.options.loadingScreenChunkMap.tooltip"))).setDefaultValue(false).setSaveConsumer(newValue -> settings.showChunkMap=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.rotationalBackground"), settings.cubeMapBackground).setDefaultValue(true).setTooltip(wrapLines(Text.translatable("bedrockify.options.rotationalBackground.tooltip"))).setSaveConsumer(newValue -> settings.cubeMapBackground=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.showBedrockIfyButton"), settings.bedrockIfyButton).setDefaultValue(true).setTooltip(wrapLines(Text.translatable("bedrockify.options.showBedrockIfyButton.tooltip"))).setSaveConsumer(newValue -> settings.bedrockIfyButton=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.quickArmorSwap"), settings.quickArmorSwap).setDefaultValue(true).setSaveConsumer(newValue -> settings.quickArmorSwap=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.dyingTrees"), settings.dyingTrees).setDefaultValue(true).setSaveConsumer(newValue -> settings.dyingTrees=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.fireAspectLight"), settings.fireAspectLight).setTooltip(wrapLines(Text.translatable("bedrockify.options.fireAspectLight.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settings.fireAspectLight=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.disableFlyingMomentum"), settings.disableFlyingMomentum).setDefaultValue(true).setSaveConsumer(newValue -> settings.disableFlyingMomentum =newValue).build());

        //        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.sneakingShield"), settings.sneakingShield).setDefaultValue(true).setSaveConsumer(newValue -> settings.sneakingShield=newValue).build());


        general.addEntry(otherSettings.build());
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.recipes"), settings.bedrockRecipes).setTooltip(wrapLines(Text.translatable("bedrockify.options.recipes.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settings.bedrockRecipes=newValue).build());
        mixins.addEntry(entryBuilder.startTextDescription(Text.translatable("bedrockify.options.mixins.description")).build());
        for(Map.Entry<String, Boolean> elem : MixinFeatureManager.features.entrySet()){
            mixins.addEntry(entryBuilder.startBooleanToggle(Text.translatable(elem.getKey()), elem.getValue()).setDefaultValue(true).setSaveConsumer(newValue -> MixinFeatureManager.features.put(elem.getKey(),newValue)).build());
        }

        return builder.setTransparentBackground(isTransparent).build();
    }

    public Text[] wrapLines(Text text){
        List<StringVisitable> lines = MinecraftClient.getInstance().textRenderer.getTextHandler().wrapLines(text,Math.max(MinecraftClient.getInstance().getWindow().getScaledWidth()/2 - 43,170), Style.EMPTY);
        lines.get(0).getString();
        Text[] textLines = new Text[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            textLines[i]=Text.literal(lines.get(i).getString());
        }
        return textLines;
    }

}
