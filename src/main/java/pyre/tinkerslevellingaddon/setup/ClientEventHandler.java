package pyre.tinkerslevellingaddon.setup;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import pyre.tinkerslevellingaddon.ImprovementModifier;
import pyre.tinkerslevellingaddon.TinkersLevellingAddon;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = TinkersLevellingAddon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {

    public static final String TOOLTIP_LEVEL_KEY = "modifier.tinkerslevellingaddon.improvement.tooltip.level";
    public static final String TOOLTIP_XP_KEY = "modifier.tinkerslevellingaddon.improvement.tooltip.xp";

    @SubscribeEvent
    static void onTooltipEvent(ItemTooltipEvent event) {
        if (KeyModifier.getActiveModifier() != KeyModifier.NONE) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if(!(stack.getItem() instanceof IModifiable)) {
            return;
        }

        ToolStack tool = ToolStack.from(stack);
        if (tool.getModifierLevel(Registration.improvement.get()) > 0) {
            ModDataNBT data = tool.getPersistentData();
            int xp = data.getInt(ImprovementModifier.EXPERIENCE_KEY);
            int level = data.getInt(ImprovementModifier.LEVEL_KEY);
            int experienceNeeded = ImprovementModifier.getXpForLevel(level + 1);

            TranslatableComponent levelTooltip = new TranslatableComponent(TOOLTIP_LEVEL_KEY,
                    new TextComponent(Integer.toString(level)).withStyle(ChatFormatting.GOLD));
            TranslatableComponent xpTooltip = new TranslatableComponent(TOOLTIP_XP_KEY, xp, experienceNeeded);
            //add tooltips under tool durability
            event.getToolTip().add(2, levelTooltip);
            event.getToolTip().add(3, xpTooltip);
        }
    }
}