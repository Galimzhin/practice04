package kz.javaworkshop;

public abstract class Item {
    private final String name;
    private final int weightGrams;
    private final int value;

    protected Item(String name, int weightGrams, int value) {
        if (name == null) {
            throw new IllegalArgumentException("Имя не задано");
        }
        String trimmed = name.strip();
        if (trimmed.isEmpty() || trimmed.length() > 40) {
            throw new IllegalArgumentException("Имя: 1-40 символов");
        }
        if (weightGrams < 1 || weightGrams > 100000) {
            throw new IllegalArgumentException("Масса: 1-100000 г");
        }
        if (value < 0 || value > 1000000) {
            throw new IllegalArgumentException("Стоимость: 0-1000000");
        }
        this.name = trimmed;
        this.weightGrams = weightGrams;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getWeightGrams() {
        return weightGrams;
    }

    public int getValue() {
        return value;
    }

    public abstract String describe();

    public int attackBonus() {
        return 0;
    }

    public int defenceBonus() {
        return 0;
    }

    protected String tail() {
        return weightGrams + " г; стоимость " + value;
    }
}
