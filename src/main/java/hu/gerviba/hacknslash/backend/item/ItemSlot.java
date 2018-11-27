package hu.gerviba.hacknslash.backend.item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Item slot representation
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class ItemSlot {

    private int itemId;
    private int count;
    
    /**
     * Value setter
     * @param itemId Item id
     * @param count Count
     */
    public void update(int itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

    /**
     * Value setter
     * @param slot Slot
     */
    public void update(ItemSlot slot) {
        this.itemId = slot.getItemId();
        this.count = slot.getCount();
    }
    
}
