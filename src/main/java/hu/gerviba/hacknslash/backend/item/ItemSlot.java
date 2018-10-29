package hu.gerviba.hacknslash.backend.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemSlot {

    private int itemId;
    private int count;
    
    public void update(int itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

    public void update(ItemSlot slot) {
        this.itemId = slot.getItemId();
        this.count = slot.getCount();
    }
    
}
