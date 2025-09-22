package winterwolves.items;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private final List<ItemEquipable> items = new ArrayList<>();

    public void agregarItem(ItemEquipable item) {
        items.add(item);
    }

    public void quitarItem(ItemEquipable item) {
        items.remove(item);
    }

    public List<ItemEquipable> getItems() {
        return items;
    }

    public ItemEquipable getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }
}
