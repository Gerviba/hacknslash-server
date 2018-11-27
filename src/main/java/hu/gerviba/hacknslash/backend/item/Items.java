package hu.gerviba.hacknslash.backend.item;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Item storage and utility functions
 * @author Gergely Szabó
 */
public class Items {
    
    public static final ItemType SKILL_POISON = new ItemType(1, 1, 
            "Poison Skill", ItemCategory.SKILL, 
            -1, -1);
    public static final ItemType SKILL_WEATER_BEAM = new ItemType(2, 2, 
            "Water Beam Skill", ItemCategory.SKILL, 
            -1, -1);
    public static final ItemType SKILL_PURLE_MAGIC = new ItemType(3, 3, 
            "Purple Magic Skill", ItemCategory.SKILL, 
            -1, -1);
    public static final ItemType SKILL_FLAME_CIRCLE = new ItemType(4, 4, 
            "Flame Circle Skill", ItemCategory.SKILL, 
            -1, -1);
    public static final ItemType SKILL_STORM = new ItemType(5, 5, 
            "Storm Skill", ItemCategory.SKILL, 
            -1, -1);
    
    public static final ItemType WEAPON_BLUE_MAGIC_WAND = new ItemType(6, -1, 
            "Plancheite Magic Wand", ItemCategory.WEAPON,
            100, 50, "blue_wand");
    public static final ItemType WEAPON_GOLD_MAGIC_WAND = new ItemType(7, -2, 
            "Gold Magic Wand", ItemCategory.WEAPON,
            300, 150, "gold_wand");
    public static final ItemType WEAPON_BLUE_SWORD = new ItemType(8, -3, 
            "Plancheite Sword", ItemCategory.WEAPON,
            100, 50, "null"); //TODO Add texture
    public static final ItemType WEAPON_GOLD_SWORD = new ItemType(9, -4, 
            "Gold Sword", ItemCategory.WEAPON,
            300, 150, "null"); // TODO Add texture
    
    public static final ItemType POTION_HEALTH_1 = new ItemType(101, 101, 
            "Health Potion I", ItemCategory.POTION, 
            -1, -1);
    public static final ItemType POTION_HEALTH_2 = new ItemType(102, 102, 
            "Health Potion II", ItemCategory.POTION, 
            -1, -1);
    public static final ItemType POTION_HEALTH_3 = new ItemType(103, 103, 
            "Health Potion III", ItemCategory.POTION, 
            -1, -1);

    public static final ItemType POTION_MANA_1 = new ItemType(201, 201, 
            "Mana Potion I", ItemCategory.POTION, 
            -1, -1);
    public static final ItemType POTION_MANA_2 = new ItemType(202, 202, 
            "Mana Potion II", ItemCategory.POTION, 
            -1, -1);
    public static final ItemType POTION_MANA_3 = new ItemType(203, 203, 
            "Mana Potion III", ItemCategory.POTION, 
            -1, -1);
    
    public static final ItemType RING_SILVER = new ItemType(10, 0, 
            "Silver Ring", ItemCategory.RING,
            -1, -1);
    public static final ItemType RING_CARBON = new ItemType(11, 0, 
            "Plancheite Ring", ItemCategory.RING,
            -1, -1);
    public static final ItemType RING_GOLDEN = new ItemType(12, 0, 
            "Golden Ring", ItemCategory.RING,
            -1, -1);
    public static final ItemType RING_RUBY = new ItemType(13, 0, 
            "Ruby Ring", ItemCategory.RING,
            -1, -1);

    public static final ItemType THING_BONE = new ItemType(14, 0, 
            "Bone", ItemCategory.THING,
            -1, -1);
    public static final ItemType THING_EYE = new ItemType(15, 0, 
            "Eye", ItemCategory.THING,
            -1, -1);
    public static final ItemType THING_COPPER_SULPHATE = new ItemType(16, 0, 
            "Plancheite", ItemCategory.THING,
            -1, -1);
    public static final ItemType THING_RUBY = new ItemType(17, 0, 
            "Ruby", ItemCategory.THING,
            -1, -1);

    public static final ItemType HELMET_LEATHER = new ItemType(18, 0, 
            "Leather Helmet", ItemCategory.HELMET,
            50, 25, "leather");
    public static final ItemType HELMET_IRON = new ItemType(19, 0, 
            "Iron Armor", ItemCategory.HELMET,
            100, 50, "iron");
    public static final ItemType HELMET_GOLD = new ItemType(20, 0, 
            "Gold Armor", ItemCategory.HELMET,
            200, 100, "gold");
    
    public static final ItemType ARMOR_LEATHER = new ItemType(21, 0, 
            "Leather Armor", ItemCategory.ARMOR,
            100, 50, "leather");
    public static final ItemType ARMOR_IRON = new ItemType(22, 0, 
            "Iron Armor", ItemCategory.ARMOR,
            200, 100, "iron");
    public static final ItemType ARMOR_GOLD = new ItemType(23, 0, 
            "Gold Armor", ItemCategory.ARMOR,
            400, 200, "gold");

    public static final ItemType BOOTS_LEATHER = new ItemType(24, 0, 
            "Leather Helmet", ItemCategory.BOOTS,
            75, 30, "leather");
    public static final ItemType BOOTS_IRON = new ItemType(25, 0, 
            "Iron Armor", ItemCategory.BOOTS,
            150, 60, "iron");
    public static final ItemType BOOTS_GOLD = new ItemType(26, 0, 
            "Gold Armor", ItemCategory.BOOTS,
            300, 150, "gold");
    

    public static final Map<Integer, ItemType> ALL;
    
    /**
     * Static initialization
     */
    static {
        ALL = Stream.of(Items.class.getFields())
                .filter(field -> field.getType() == ItemType.class)
                .map(field -> {
                    try {
                        return (ItemType) field.get(null);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                    }
                    return null;
                })
                .filter(item -> item != null)
                .collect(Collectors.toMap(entry -> entry.getId(), Function.identity()));
        
    }
    
    /**
     * Item getter by id
     * @param itemId ID of the item
     * @return The item with the specified ID
     */
    public static ItemType get(int itemId) {
        return ALL.get(itemId);
    }
    
}
