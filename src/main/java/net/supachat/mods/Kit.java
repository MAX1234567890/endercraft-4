package net.supachat.mods;

import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kit {
    public int cost;
    public ItemStack helmet;
    public ItemStack chestplate;
    public ItemStack leggings;
    public ItemStack boots;

    public List<ItemStack> others;

    public Kit() {

    }

    /**
     *
     * @param cost cost in diamonds
     * @param helmet helmet
     * @param chestplate chests
     * @param leggings legs
     * @param boots boot equipt.
     * @param others weapon = others[0]
     */
    public Kit(int cost, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack... others) {
        this.cost = cost;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.others = new ArrayList<>(Arrays.asList(others));
    }

    public void apply(EntityPlayer p) {
        if(!Ender4EventHandler.deductCost(p, this.cost, Item.getItemFromBlock(Blocks.DIAMOND_BLOCK))) {
            return;
        }

        if(this.helmet != null) {
            p.inventory.armorInventory.set(3, this.helmet);
        }

        if(this.chestplate != null) {
            p.inventory.armorInventory.set(2, this.chestplate);
        }

        if(this.leggings != null) {
            p.inventory.armorInventory.set(1, this.leggings);
        }

        if(this.boots != null) {
            p.inventory.armorInventory.set(0, this.boots);
        }

        if(!this.others.isEmpty()) {
            for(ItemStack i : this.others)
                p.inventory.addItemStackToInventory(i);
        }
    }

    public void applyToHusk(EntityHusk husk) {
        if(this.helmet != null) {
            husk.setItemStackToSlot(EntityEquipmentSlot.HEAD, this.helmet);
        }

        if(this.chestplate != null) {
            husk.setItemStackToSlot(EntityEquipmentSlot.CHEST, this.chestplate);
        }

        if(this.leggings != null) {
            husk.setItemStackToSlot(EntityEquipmentSlot.LEGS, this.leggings);
        }

        if(this.boots != null) {
            husk.setItemStackToSlot(EntityEquipmentSlot.FEET, this.boots);
        }

        if(!this.others.isEmpty()) {
            husk.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.others.get(0));
            if(this.others.size() > 1) {
                husk.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, this.others.get(1));
            }
        }
    }
}
