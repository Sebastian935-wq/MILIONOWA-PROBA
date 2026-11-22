package pl.fakerangi;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public final class RankScreen extends Screen {
    private static final Identifier[] BOOKS = {
        Identifier.of("fakerangianarchia", "textures/gui/vip.png"),
        Identifier.of("fakerangianarchia", "textures/gui/svip.png"),
        Identifier.of("fakerangianarchia", "textures/gui/gvip.png"),
        Identifier.of("fakerangianarchia", "textures/gui/gvip.png")
    };
    private final Rank[] ranks = Rank.values();
    private int left, top;
    private boolean inventoryTab = true;

    public RankScreen() { super(Text.literal("Fake Rangi - Anarchia.GG")); }

    @Override protected void init() {
        left = width / 2 - 148;
        top = height / 2 - 100;
        addDrawableChild(ButtonWidget.builder(Text.literal("RANGA EKWIPUNEK"), b -> {
            inventoryTab = true; rebuild();
        }).dimensions(left + 5, top + 28, 136, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("USTAWIENIA"), b -> {
            inventoryTab = false; rebuild();
        }).dimensions(left + 5, top + 53, 136, 20).build());
        addDrawableChild(ButtonWidget.builder(toggleLabel(), b -> {
            FakeRangiClient.config.enabled = !FakeRangiClient.config.enabled; rebuild();
        }).dimensions(left + 148, top + 141, 140, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("COFNIJ / PRZYWROC"), b -> {
            FakeVoucher.undo(); rebuild();
        }).dimensions(left + 5, top + 144, 136, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Zamknij"), b -> close())
            .dimensions(left + 148, top + 166, 140, 20).build());
        if (!inventoryTab) {
            addDrawableChild(ButtonWidget.builder(Text.literal("HUD: " + (FakeRangiClient.config.showOverlay ? "ON" : "OFF")), b -> {
                FakeRangiClient.config.showOverlay = !FakeRangiClient.config.showOverlay; rebuild();
            }).dimensions(left + 148, top + 57, 140, 20).build());
            addDrawableChild(ButtonWidget.builder(Text.literal("Napis nad glowa: " + (FakeRangiClient.config.showNametag ? "ON" : "OFF")), b -> {
                FakeRangiClient.config.showNametag = !FakeRangiClient.config.showNametag; rebuild();
            }).dimensions(left + 148, top + 84, 140, 20).build());
        }
    }

    private Text toggleLabel() {
        return Text.literal("FAKE RANGI: " + (FakeRangiClient.config.enabled ? "ON" : "OFF"))
            .formatted(FakeRangiClient.config.enabled ? Formatting.GREEN : Formatting.RED);
    }

    private void rebuild() { FakeRangiClient.config.save(); clearAndInit(); }
    private boolean hasSingleItem() {
        if (client == null || client.player == null) return false;
        // Jedna sztuka dowolnego przedmiotu w aktywnej rece; nie konsumujemy przedmiotu.
        return !client.player.getMainHandStack().isEmpty()
                && client.player.getMainHandStack().getCount() == 1;
    }

    private void pickup(Rank rank) {
        if (client != null && FakeVoucher.pickup(client, rank)) rebuild();
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Lewy klik: lokalnie wez voucher; PPM na voucherze / w grze: uzyj.
        if (inventoryTab && mouseX >= left + 166 && mouseX < left + 291 && mouseY >= top + 53 && mouseY < top + 97) {
            for (int i = 0; i < ranks.length; i++) {
                int x = left + 168 + i * 30;
                if (mouseX >= x && mouseX < x + 28 && mouseY >= top + 57 && mouseY < top + 89) {
                    if (button == 0) { pickup(ranks[i]); return true; }
                    if (button == 1) { if (client != null && FakeVoucher.heldRank() == ranks[i]) {
                        FakeVoucher.use(client); rebuild();
                    } return true; }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(left, top, left + 296, top + 196, 0xE8100E19);
        context.fill(left + 1, top + 1, left + 295, top + 24, 0xFF30243C);
        context.drawCenteredTextWithShadow(textRenderer, "FAKE RANGI | ANARCHIA.GG", left + 148, top + 8, 0xFFFFFFFF);
        context.fill(left + 146, top + 27, left + 147, top + 136, 0xFF654474);
        context.drawCenteredTextWithShadow(textRenderer, inventoryTab ? "RANGA EKWIPUNEK" : "USTAWIENIA", left + 217, top + 32, 0xFFFFD85A);

        if (inventoryTab) {
            Rank selected = FakeRangiClient.config.rank;
            for (int i = 0; i < ranks.length; i++) {
                Rank rank = ranks[i];
                int x = left + 168 + i * 30;
                int y = top + 57;
                context.fill(x, y, x + 28, y + 34, rank == selected && FakeRangiClient.config.enabled ? 0xFF786037 : 0xFF333039);
                context.fill(x + 1, y + 1, x + 27, y + 31, 0xFF19151D);
                context.drawTexture(RenderLayer::getGuiTextured, BOOKS[i], x + 4, y + 2, 0, 0, 20, 20, 22, 22);
                context.drawCenteredTextWithShadow(textRenderer, (rank == Rank.VOUCHER ? "VCHR" : rank.name()), x + 14, y + 23, rank.rgb());
                if (mouseX >= x && mouseX < x + 28 && mouseY >= y && mouseY < y + 34) {
                    Text[] tooltip = RankVouchers.tooltip(rank);
                    // Tooltip rysujemy po przyciskach, na wierzchu.
                }
            }
            context.drawCenteredTextWithShadow(textRenderer, "LPM = WEZ VOUCHER", left + 218, top + 111, 0xFF55FF55);
            context.drawCenteredTextWithShadow(textRenderer,
                    "WYMAGANA 1 SZT. PRZEDMIOTU", left + 218, top + 101, 0xFFFF5555);
            context.drawCenteredTextWithShadow(textRenderer,
                    FakeVoucher.holding() ? "Voucher: " + FakeVoucher.heldRank().name() + " (PPM uzyj)" :
                    hasSingleItem() ? "Warunek spelniony" : "Trzymaj 1 szt. w rece!",
                    left + 218, top + 123, hasSingleItem() ? 0xFF55FF55 : 0xFFFF5555);
        } else {
            context.drawCenteredTextWithShadow(textRenderer, "Zmien wyswietlanie rangi", left + 218, top + 44, 0xFFBBBBBB);
            context.drawCenteredTextWithShadow(textRenderer, "Tylko na Twoim kliencie", left + 218, top + 119, 0xFF999999);
        }
        context.drawCenteredTextWithShadow(textRenderer, "COFNIJ = przywroc widok", left + 73, top + 134, 0xFFCCCCCC);
        context.drawCenteredTextWithShadow(textRenderer, "Wybrana: " + FakeRangiClient.config.rank.name(), left + 73, top + 104, FakeRangiClient.config.rank.rgb());
        context.drawCenteredTextWithShadow(textRenderer, FakeRangiClient.config.enabled ? "FAKE RANGA AKTYWNA" : "PRAWDZIWA RANGA", left + 73, top + 118, FakeRangiClient.config.enabled ? 0xFF55FF55 : 0xFFFF5555);
        super.render(context, mouseX, mouseY, delta);
        if (inventoryTab) {
            for (int i = 0; i < ranks.length; i++) {
                int x = left + 168 + i * 30, y = top + 57;
                if (mouseX >= x && mouseX < x + 28 && mouseY >= y && mouseY < y + 34) {
                    context.drawTooltip(textRenderer, java.util.Arrays.asList(RankVouchers.tooltip(ranks[i])), mouseX, mouseY);
                    break;
                }
            }
        }
    }

    @Override public void close() {
        FakeRangiClient.config.save();
        if (client != null) client.setScreen(null);
    }
}
