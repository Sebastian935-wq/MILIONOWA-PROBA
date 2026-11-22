package pl.fakerangi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

/** Client-only visual voucher. Never modifies player inventory or sends packets. */
public final class FakeVoucher {
    private FakeVoucher() {}
    private static Rank held;
    private static int selectedSlot = -1;
    private static ItemStack original = ItemStack.EMPTY;

    public static boolean holding() { return held != null; }
    public static Rank heldRank() { return held; }

    public static boolean pickup(MinecraftClient client, Rank rank) {
        if (client.player == null) return false;
        ItemStack stack = client.player.getMainHandStack();
        if (stack.isEmpty() || stack.getCount() != 1) return false;
        held = rank;
        selectedSlot = client.player.getInventory().selectedSlot;
        original = stack.copy();
        return true;
    }

    public static boolean valid(MinecraftClient client) {
        if (held == null || client.player == null) return false;
        ItemStack current = client.player.getMainHandStack();
        return client.player.getInventory().selectedSlot == selectedSlot
                && !current.isEmpty() && current.getCount() == 1
                && ItemStack.areItemsAndComponentsEqual(current, original);
    }

    public static boolean use(MinecraftClient client) {
        if (!valid(client)) return false;
        FakeRangiClient.config.rank = held;
        FakeRangiClient.config.enabled = true;
        FakeRangiClient.config.save();
        clear();
        return true;
    }

    public static void clear() {
        held = null;
        original = ItemStack.EMPTY;
        selectedSlot = -1;
    }

    public static void undo() {
        clear();
        FakeRangiClient.config.enabled = false;
        FakeRangiClient.config.save();
    }
}
