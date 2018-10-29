package hu.gerviba.hacknslash.backend.item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Inventory {

    private static final Map<Integer, List<ItemCategory>> CATEGORY_RESTRICTION = new ConcurrentHashMap<>();
    private static final List<ItemCategory> NO_RESTRICTION = Arrays.asList(ItemCategory.values());
    
    public static final int SLOT_ID_WEAPON = 0;
    public static final int SLOT_ID_RING = 1;
    public static final int SLOT_ID_HELMET = 2;
    public static final int SLOT_ID_ARMOR = 3;
    public static final int SLOT_ID_BOOTS = 4;
    
    
    static {
        CATEGORY_RESTRICTION.put(SLOT_ID_WEAPON, Arrays.asList(ItemCategory.WEAPON));
        CATEGORY_RESTRICTION.put(SLOT_ID_RING, Arrays.asList(ItemCategory.RING));
        CATEGORY_RESTRICTION.put(SLOT_ID_HELMET, Arrays.asList(ItemCategory.HELMET));
        CATEGORY_RESTRICTION.put(SLOT_ID_ARMOR, Arrays.asList(ItemCategory.ARMOR));
        CATEGORY_RESTRICTION.put(SLOT_ID_BOOTS, Arrays.asList(ItemCategory.BOOTS));
        
        for (int i = 5; i < 10; ++i)
            CATEGORY_RESTRICTION.put(i, Arrays.asList());
        
        for (int i = 1; i <= 7; ++i)
            CATEGORY_RESTRICTION.put(i + 9, 
                    Arrays.asList(ItemCategory.SKILL, ItemCategory.POTION, ItemCategory.WEAPON));

        for (int i = 17; i < 22; ++i)
            CATEGORY_RESTRICTION.put(i, Arrays.asList());
    }
    
    @Getter
    private Map<Integer, ItemSlot> itemSlot;

    public ItemSlot getSlot(int slotId) {
        return itemSlot.get(slotId);
    }
    
    public void putItemToSlot(int slot, int itemId, int count) {
        if (itemSlot.containsKey(slot))
            itemSlot.get(slot).update(itemId, count);
        else
            itemSlot.put(slot, new ItemSlot(itemId, count));
    }
    
    public void putItemToSlot(int slot, ItemSlot item) {
        if (itemSlot.containsKey(slot))
            itemSlot.get(slot).update(item.getItemId(), item.getCount());
        else
            itemSlot.put(slot, new ItemSlot(item.getItemId(), item.getCount()));
    }
    
    public boolean addItem(int itemId, int count) {
        for (int i = 22; i <= 62; ++i) {
            if (!itemSlot.containsKey(i)) {
                itemSlot.put(i, new ItemSlot(itemId, count));
                return true;
            }
        }
        return false;
    }
    
    public boolean change(int from, int to) {
        if (!checkSide(to, getSlot(from)) || !checkSide(from, getSlot(to)))
            return false;

        if (getSlot(from) == null) {
            if (getSlot(to) == null)
                return true;
            itemSlot.put(from, getSlot(to));
            itemSlot.remove(to);
            return true;
        } else if (getSlot(to) == null) {
            itemSlot.put(to, getSlot(from));
            itemSlot.remove(from);
            return true;
        }
        
        ItemSlot temp = getSlot(from);
        int itemId = temp.getItemId();
        int count = temp.getCount();
        getSlot(from).update(getSlot(to));
        getSlot(to).update(itemId, count);
        
        return true;
    }

    private boolean checkSide(int slotId, ItemSlot slotSource) {
        return slotSource == null
                || CATEGORY_RESTRICTION.getOrDefault(slotId, NO_RESTRICTION)
                        .contains(Items.get(slotSource.getItemId()).getType());
    }

    public String getWeapon() {
        if (getSlot(SLOT_ID_WEAPON) == null)
            return "null";
        return Items.get(getSlot(SLOT_ID_WEAPON).getItemId()).getTextureName();
    }

    public String getRing() {
        if (getSlot(SLOT_ID_RING) == null)
            return "null";
        return Items.get(getSlot(SLOT_ID_RING).getItemId()).getTextureName();
    }

    public String getHelmet() {
        if (getSlot(SLOT_ID_HELMET) == null)
            return "null";
        return Items.get(getSlot(SLOT_ID_HELMET).getItemId()).getTextureName();
    }
    
    public String getArmor() {
        if (getSlot(SLOT_ID_ARMOR) == null)
            return "null";
        return Items.get(getSlot(SLOT_ID_ARMOR).getItemId()).getTextureName();
    }
    
    public String getBoots() {
        if (getSlot(SLOT_ID_BOOTS) == null)
            return "null";
        return Items.get(getSlot(SLOT_ID_BOOTS).getItemId()).getTextureName();
    }
    
}
