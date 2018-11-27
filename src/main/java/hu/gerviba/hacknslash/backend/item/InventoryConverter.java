package hu.gerviba.hacknslash.backend.item;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Inventory converter to database format
 * @author Gergely Szabó
 */
@Converter
public class InventoryConverter implements AttributeConverter<Inventory, String> {

    /**
     * Encode
     */
    @Override
    public String convertToDatabaseColumn(Inventory attribute) {
        return attribute
                .getItemSlot()
                .entrySet()
                .stream()
                .map(x -> x.getKey() + "," + x.getValue().getItemId() + "," + x.getValue().getCount())
                .collect(Collectors.joining(";"));
    }

    /**
     * Decode
     */
    @Override
    public Inventory convertToEntityAttribute(String dbData) {
        return new Inventory(Stream.of(
                dbData.split(";"))
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toMap(
                        entry -> Integer.parseInt(entry.split(",", 2)[0]),
                        entry -> {
                            String[] params = entry.split(",");
                            return new ItemSlot(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        })));
    }

}
