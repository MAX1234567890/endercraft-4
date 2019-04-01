package net.supachat.mods;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ender4EventHandler {
//    private ItemStack cache;
//
//    @SubscribeEvent
//    public void worldTick(TickEvent.WorldTickEvent event) {
//
//    }

    private static Map<String, Kit> kits;
    static {
        kits = new HashMap<>();
        ItemStack knightHelmet = new ItemStack(Items.CHAINMAIL_HELMET);
        ItemStack knightChestplate = new ItemStack(Items.CHAINMAIL_CHESTPLATE);
        ItemStack knightLeggings = new ItemStack(Items.CHAINMAIL_LEGGINGS);
        ItemStack knightBoots = new ItemStack(Items.CHAINMAIL_BOOTS);
        ItemStack knightSword = new ItemStack(Items.IRON_SWORD);
        knightSword.addEnchantment(Enchantments.KNOCKBACK, 4);
        knightSword.addEnchantment(Enchantments.SHARPNESS, 2);
        kits.put(
                "Knight",
                new Kit(
                        2,
                        knightHelmet,
                        knightChestplate,
                        knightLeggings,
                        knightBoots,
                        knightSword
                )
        );

        ItemStack archerHelmet = new ItemStack(Items.LEATHER_HELMET);
        Items.LEATHER_HELMET.setColor(archerHelmet, 6192150);
        ItemStack archerChestplate = new ItemStack(Items.LEATHER_CHESTPLATE);
        Items.LEATHER_CHESTPLATE.setColor(archerChestplate, 6192150);
        ItemStack archerLeggings = new ItemStack(Items.LEATHER_LEGGINGS);
        Items.LEATHER_LEGGINGS.setColor(archerLeggings, 6192150);
        ItemStack archerBoots = new ItemStack(Items.LEATHER_BOOTS);
        Items.LEATHER_BOOTS.setColor(archerBoots, 6192150);
        ItemStack archerBow = new ItemStack(Items.BOW);
        archerBow.addEnchantment(Enchantments.PUNCH, 4);
        archerBow.addEnchantment(Enchantments.POWER, 2);
        ItemStack archerArrows = new ItemStack(Items.SPECTRAL_ARROW, 64);
        kits.put(
                "Archer",
                new Kit(
                        2,
                        archerHelmet,
                        archerChestplate,
                        archerLeggings,
                        archerBoots,
                        archerBow,
                        archerArrows
                )
        );

        ItemStack trollHelmet = new ItemStack(Items.DIAMOND_HELMET);
        trollHelmet.addEnchantment(Enchantments.PROTECTION, 50);
        trollHelmet.addEnchantment(Enchantments.THORNS, 50);
        ItemStack trollChestplate = new ItemStack(Items.DIAMOND_CHESTPLATE);
        trollChestplate.addEnchantment(Enchantments.PROTECTION, 50);
        trollChestplate.addEnchantment(Enchantments.THORNS, 50);
        ItemStack trollLeggings = new ItemStack(Items.DIAMOND_LEGGINGS);
        trollLeggings.addEnchantment(Enchantments.PROTECTION, 50);
        trollLeggings.addEnchantment(Enchantments.THORNS, 50);
        ItemStack trollBoots = new ItemStack(Items.DIAMOND_BOOTS);
        trollBoots.addEnchantment(Enchantments.PROTECTION, 50);
        trollBoots.addEnchantment(Enchantments.THORNS, 50);
        ItemStack trollBow = new ItemStack(Items.BOW);
        trollBow.addEnchantment(Enchantments.PUNCH, 50);
        trollBow.addEnchantment(Enchantments.POWER, 50);
        ItemStack trollAxe = new ItemStack(Items.DIAMOND_AXE);
        trollAxe.addEnchantment(Enchantments.KNOCKBACK, 50);
        trollAxe.addEnchantment(Enchantments.SHARPNESS, 50);
        ItemStack trollArrows = new ItemStack(Items.SPECTRAL_ARROW, 64);
        kits.put(
                "Troll",
                new Kit(
                        128,
                        trollHelmet,
                        trollChestplate,
                        trollLeggings,
                        trollBoots,
                        trollAxe,
                        trollBow,
                        trollArrows
                )
        );
    }

    private static final boolean tpToEnd = true;

    @SubscribeEvent
    public void onPlayerUseItem(LivingEntityUseItemEvent.Finish e) {
        if(e.getItem().getItem().equals(Items.SPECKLED_MELON)) {
            if(e.getEntity() instanceof EntityPlayer) {
                ((EntityPlayer) (e.getEntity())).addPotionEffect(new PotionEffect(MobEffects.HASTE, 20*20, 5, false, true)); // add haste
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if(e.getEntity() instanceof EntityItem) {
            e.setCanceled(this.buy(e.getWorld().getClosestPlayerToEntity(e.getEntity(), 10), (EntityItem)e.getEntity()));
        }
    }

    /**
     * standard docstring
     * @param p troll
     * @param i wat
     * @return whether to destroy the entity
     */
    public boolean buy(EntityPlayer p, EntityItem i) {
        if(p == null || i == null) {
            return false;
        }

        String name = i
                .getItem()
                .serializeNBT()
                .getCompoundTag("tag")
                .getCompoundTag("display")
                .getString("Name");

        if (!StringUtils.isEmpty(name)) {
            int cost;

            try {
                cost = Integer.parseInt(name);
            } catch (NumberFormatException err) {
                return false;
            }

            if (!this.deductCost(p, cost)) {
//                e.pickedUp.changeDimension(-1);
                i.changeDimension(1);
//                e.player.dropItemAndGetStack(e.pickedUp);
//                e.player.inventory.clearMatchingItems(
//                        e.pickedUp.getItem().getItem(),
//                        e.pickedUp.getItem().getMetadata(),
//                        e.pickedUp.getItem().getCount(),
//                        e.pickedUp.getItem().serializeNBT()
//                );
                p.sendMessage(new TextComponentString("<Endercorp> You don't have enough to pay for this. It was sent to the End."));
                return false;
//                return true;
//                e.pickedUp.dropItem(e.pickedUp.getItem().getItem(), e.pickedUp.getItem().getCount());
            }
        }

        return false;
    }

    private boolean deductCost(EntityPlayer player, int cost) {
        try {
            InventoryPlayer inv = player.inventory;
            inv.getItemStack();
        } catch(NullPointerException e) {
//            e.printStackTrace();
        }

        return false;
    }

    @SubscribeEvent
    public void punchZombieEvent(LivingDamageEvent e) {
        if(e.getEntityLiving() instanceof EntityHusk) { // TODO: kit class
            EntityHusk zomb = (EntityHusk)e.getEntityLiving();
            String kitName = zomb.serializeNBT().getString("CustomName");
                // /summon Husk ~ ~ ~ {ZombieType:6,NoAI:1,CustomName:"<kit>"}
            System.out.println(kitName);

            if(!kitName.isEmpty()) {
                e.setCanceled(true);

                Entity source = e.getSource().getTrueSource();
                if(source instanceof EntityPlayer) {
                    EntityPlayer trueSource = (EntityPlayer)source;
                    Kit kit = kits.get(kitName);

                    kit.apply(trueSource);
                    kit.applyToHusk(zomb);
                }
            }
        }
    }
}
