package kz.javaworkshop;

public class Hero {
    private final String name;
    private final Inventory inventory;
    private final int maxHp;
    private int hp;

    public Hero(String name, int capacity, int maxWeightGrams, int maxHp) {
        if (name == null || name.strip().isEmpty() || name.strip().length() > 40) {
            throw new IllegalArgumentException("Имя героя: 1-40 символов");
        }
        if (maxHp < 1 || maxHp > 1000) {
            throw new IllegalArgumentException("Здоровье: 1-1000");
        }
        this.name = name.strip();
        this.inventory = new Inventory(capacity, maxWeightGrams);
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public String getName() {
        return name;
    }

    public boolean pickUp(Item item) {
        return inventory.add(item);
    }

    public int itemCount() {
        return inventory.size();
    }

    public Item[] items() {
        return inventory.snapshot();
    }

    public int totalWeight() {
        return inventory.totalWeight();
    }

    public int totalValue() {
        return inventory.totalValue();
    }

    public int attackPower() {
        int sum = 0;
        for (Item item : inventory.snapshot()) {
            sum += item.attackBonus();
        }
        return sum;
    }

    public int defencePower() {
        int sum = 0;
        for (Item item : inventory.snapshot()) {
            sum += item.defenceBonus();
        }
        return sum;
    }

    public int takeHit(int damage) {
        if (damage < 0 || damage > 1000) {
            throw new IllegalArgumentException("Удар: 0-1000");
        }
        int loss = Math.max(0, damage - defencePower());
        loss = Math.min(loss, hp);
        hp -= loss;
        return loss;
    }

    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Лечение не может быть отрицательным");
        }
        hp = Math.min(maxHp, hp + amount);
    }

    public boolean useItem(int index) {
        Item item = inventory.get(index);
        if (item instanceof Usable usable) {
            usable.use(this);
            inventory.remove(index);
            return true;
        }
        return false;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public String summary() {
        int n = itemCount();
        return name + ": HP " + hp + "/" + maxHp
                + ", атака " + attackPower()
                + ", защита " + defencePower()
                + ", " + n + " " + plural(n)
                + ", " + totalWeight() + " г, " + totalValue() + " монет";
    }

    private static String plural(int n) {
        int mod100 = n % 100;
        int mod10 = n % 10;
        if (mod100 >= 11 && mod100 <= 14) {
            return "предметов";
        }
        if (mod10 == 1) {
            return "предмет";
        }
        if (mod10 >= 2 && mod10 <= 4) {
            return "предмета";
        }
        return "предметов";
    }
}
