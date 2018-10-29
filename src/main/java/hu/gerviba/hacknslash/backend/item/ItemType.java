package hu.gerviba.hacknslash.backend.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemType {

    private final int id;
    private final int skillUid;
    private final String name;
    private final ItemCategory type;
    private final int sellCost;
    private final int buyCost;
    private String textureName = "null";
    
}
