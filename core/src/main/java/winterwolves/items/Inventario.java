package winterwolves.items;

import winterwolves.personajes.InventarioHud;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private final List<Item> items = new ArrayList<>();

    public void agregarItem(Item item) {
        items.add(item);
    }

    public void quitarItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }
}
