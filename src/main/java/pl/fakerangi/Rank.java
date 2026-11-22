package pl.fakerangi;

import net.minecraft.util.Formatting;

public enum Rank {
    VIP(Formatting.YELLOW, 0xFFFF55),
    SVIP(Formatting.GOLD, 0xFFAA00),
    GVIP(Formatting.AQUA, 0x55FFFF),
    VOUCHER(Formatting.LIGHT_PURPLE, 0xFF55FF);
    private final Formatting formatting;
    private final int rgb;
    Rank(Formatting formatting, int rgb) { this.formatting = formatting; this.rgb = rgb; }
    public Formatting formatting() { return formatting; }
    public int rgb() { return rgb; }
}
