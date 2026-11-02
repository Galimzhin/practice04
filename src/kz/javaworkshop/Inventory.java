package kz.javaworkshop;

public class Inventory {
    private final Item[] items;
    private final int maxWeightGrams;
    private int size;

    public Inventory(int capacity, int maxWeightGrams) {
        if (capacity < 1 || capacity > 20) {
            throw new IllegalArgumentException("Вместимость: 1-20");
        }
        if (maxWeightGrams < 1) {
            throw new IllegalArgumentException("Лимит массы должен быть положительным");
        }
        this.items = new Item[capacity];
        this.maxWeightGrams = maxWeightGrams;
    }

    public boolean add(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Предмет не задан");
        }
        if (size >= items.length) {
            return false;
        }
        if (totalWeight() + item.getWeightGrams() > maxWeightGrams) {
            return false;
        }
        items[size++] = item;
        return true;
    }

    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[index];
    }

    public Item remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Item removed = items[index];
        for (int i = index; i < size - 1; i++) {
            items[i] = items[i + 1];
        }
        items[--size] = null;
        return removed;
    }

    public Item[] snapshot() {
        Item[] copy = new Item[size];
        System.arraycopy(items, 0, copy, 0, size);
        return copy;
    }

    public int size() {
        return size;
    }

    public int totalWeight() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += items[i].getWeightGrams();
        }
        return sum;
    }

    public int totalValue() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += items[i].getValue();
        }
        return sum;
    }
}
