package net.strauss.kitpvpmod.item;

public enum MinecraftColor {
    BLACK("0"),
    DARK_BLUE("1"),
    DARK_GREEN("2"),
    DARK_AQUA("3"),
    DARK_RED("4"),
    DARK_PURPLE("5"),
    GOLD("6"),
    GRAY("7"),
    DARK_GRAY("8"),
    BLUE("9"),
    GREEN("a"),
    AQUA("b"),
    RED("c"),
    LIGHT_PURPLE("d"),
    YELLOW("e"),
    WHITE("f"),
    MINECOIN_GOLD("g"),
    MINECOIN_QUARTZ("h"),
    MATERIAL_IRON("i"),
    MATERIAL_NETHERITE("j"),
    MATERIAL_REDSTONE("m"),
    MATERIAL_COPPER("n"),
    MATERIAL_GOLD("p"),
    MATERIAL_EMERALD("q"),
    MATERIAL_DIAMOND("s"),
    MATERIAL_LAPIS("t"),
    MATERIAL_AMETHYST("u");

    private final String value;

    // Конструктор
    MinecraftColor(String value) {
        this.value = value;
    }

    // Геттер
    public String getValue() {
        return value;
    }
}
