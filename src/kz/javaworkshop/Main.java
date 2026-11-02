package kz.javaworkshop;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        controlRun();
        System.out.println();
        boundaryChecks();
    }

    // Шаг 6. Контрольный проход, вариант А · Воин
    private static void controlRun() {
        System.out.println("=== Контрольный проход (вариант А · Воин) ===");

        Hero aliya = new Hero("Алия", 4, 6000, 120);
        Item sword = new Weapon("Меч", 2500, 100, 12);
        Item shield = new Armor("Щит", 3000, 80, 8);
        Item potion = new Potion("Зелье", 500, 20, 30);

        System.out.println("Алия подбирает Меч: " + aliya.pickUp(sword));
        System.out.println("Алия подбирает Щит: " + aliya.pickUp(shield));
        System.out.println("Алия подбирает Зелье: " + aliya.pickUp(potion));
        printItems(aliya);
        System.out.println(aliya.summary());

        int lost = aliya.takeHit(50);
        System.out.println("Алия получает удар 50, потеряно HP: " + lost);
        System.out.println(aliya.summary());

        System.out.println("Алия использует предмет 0: " + aliya.useItem(0));
        System.out.println("Алия использует предмет 2: " + aliya.useItem(2));
        System.out.println("Алия использует предмет 2: " + aliya.useItem(2));
        System.out.println(aliya.summary());

        Hero daniyar = new Hero("Данияр", 2, 6000, 100);
        System.out.println("Данияр подбирает Факел: " + daniyar.pickUp(new Weapon("Факел", 200, 10, 3)));
        System.out.println("Данияр подбирает Хлеб: " + daniyar.pickUp(new Food("Хлеб", 300, 5, 10)));
        printItems(daniyar);
        System.out.println("Данияр получает удар 5, потеряно HP: " + daniyar.takeHit(5));
        System.out.println("Данияр использует предмет 1: " + daniyar.useItem(1));
        System.out.println(daniyar.summary());
    }

    private static void printItems(Hero hero) {
        Item[] items = hero.items();
        for (int i = 0; i < items.length; i++) {
            System.out.println("  " + i + ". " + items[i].describe());
        }
    }

    // Шаг 7. Проверка границ
    private static void boundaryChecks() {
        System.out.println("=== Проверка границ ===");

        // 1. Пустой рюкзак
        Hero empty = new Hero("Пустой", 3, 5000, 50);
        System.out.println("[1] useItem(0) у пустого: " + empty.useItem(0)
                + ", useItem(-1): " + empty.useItem(-1)
                + ", HP " + empty.getHp() + "/" + empty.getMaxHp()
                + ", предметов " + empty.itemCount());

        // 2. Зелье при полном здоровье
        Hero full = new Hero("Полный", 3, 5000, 50);
        full.pickUp(new Potion("Зелье", 500, 20, 30));
        boolean used = full.useItem(0);
        System.out.println("[2] Зелье при полном HP: " + used
                + ", HP " + full.getHp() + "/" + full.getMaxHp()
                + ", предметов " + full.itemCount());

        // 3. Удар слабее защиты
        Hero guarded = new Hero("Защищённый", 3, 5000, 50);
        guarded.pickUp(new Armor("Щит", 3000, 80, 8));
        int weak = guarded.takeHit(5);
        System.out.println("[3] Удар 5 при защите " + guarded.defencePower()
                + ": потеря " + weak + ", HP " + guarded.getHp() + "/" + guarded.getMaxHp());

        // 4. Удар 1000
        Hero victim = new Hero("Жертва", 3, 5000, 50);
        int bigLoss = victim.takeHit(1000);
        System.out.println("[4] Удар 1000: потеря " + bigLoss + ", HP " + victim.getHp());

        // 5. Лечение героя с нулевым здоровьем
        victim.heal(15);
        System.out.println("[5] Лечение на 15 при HP 0: HP " + victim.getHp());

        // 6. useItem() для брони
        Hero armored = new Hero("Броненосец", 3, 5000, 50);
        armored.pickUp(new Armor("Куртка", 2000, 80, 4));
        String before = Arrays.toString(describeAll(armored));
        boolean armorUsed = armored.useItem(0);
        String after = Arrays.toString(describeAll(armored));
        System.out.println("[6] useItem для брони: " + armorUsed
                + ", рюкзак прежний: " + before.equals(after));

        // 7. Значения 0 и 101 в подклассах
        expectIllegal("[7] Weapon урон 0", () -> new Weapon("Меч", 100, 1, 0));
        expectIllegal("[7] Weapon урон 101", () -> new Weapon("Меч", 100, 1, 101));
        expectIllegal("[7] Armor защита 0", () -> new Armor("Щит", 100, 1, 0));
        expectIllegal("[7] Armor защита 101", () -> new Armor("Щит", 100, 1, 101));
        expectIllegal("[7] Potion лечение 0", () -> new Potion("Зелье", 100, 1, 0));
        expectIllegal("[7] Potion лечение 101", () -> new Potion("Зелье", 100, 1, 101));
        expectIllegal("[7] Food лечение 0", () -> new Food("Хлеб", 100, 1, 0));
        expectIllegal("[7] Food лечение 101", () -> new Food("Хлеб", 100, 1, 101));

        // 8. maxHp 0 / 1001, удар -1
        expectIllegal("[8] maxHp 0", () -> new Hero("Тест", 2, 1000, 0));
        expectIllegal("[8] maxHp 1001", () -> new Hero("Тест", 2, 1000, 1001));
        expectIllegal("[8] удар -1", () -> new Hero("Тест", 2, 1000, 10).takeHit(-1));

        // 9. Имя из пробелов в Food
        expectIllegal("[9] Food с именем из пробелов", () -> new Food("   ", 100, 1, 10));

        // Item абстрактен: проверку компиляции см. в README
    }

    private static String[] describeAll(Hero hero) {
        Item[] items = hero.items();
        String[] result = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            result[i] = items[i].describe();
        }
        return result;
    }

    private static void expectIllegal(String label, Runnable action) {
        try {
            action.run();
            System.out.println(label + ": ИСКЛЮЧЕНИЕ НЕ ВЫБРОШЕНО (ошибка!)");
        } catch (IllegalArgumentException e) {
            System.out.println(label + ": IllegalArgumentException (" + e.getMessage() + ")");
        }
    }
}
