package pl.fakerangi;

import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class RankConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("fake-rangi-anarchia.properties");
    public boolean enabled = false;
    public boolean showOverlay = true;
    public boolean showNametag = true;
    public Rank rank = Rank.GVIP;

    public static RankConfig load() {
        RankConfig config = new RankConfig();
        if (!Files.exists(FILE)) return config;
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(FILE)) {
            properties.load(input);
            config.enabled = Boolean.parseBoolean(properties.getProperty("enabled", "false"));
            config.showOverlay = Boolean.parseBoolean(properties.getProperty("overlay", "true"));
            config.showNametag = Boolean.parseBoolean(properties.getProperty("nametag", "true"));
            try { config.rank = Rank.valueOf(properties.getProperty("rank", "GVIP")); }
            catch (IllegalArgumentException ignored) { config.rank = Rank.GVIP; }
        } catch (IOException ex) { System.err.println("Fake Rangi: nie mozna odczytac konfiguracji: " + ex); }
        return config;
    }

    public void save() {
        Properties properties = new Properties();
        properties.setProperty("enabled", Boolean.toString(enabled));
        properties.setProperty("overlay", Boolean.toString(showOverlay));
        properties.setProperty("nametag", Boolean.toString(showNametag));
        properties.setProperty("rank", rank.name());
        try (OutputStream output = Files.newOutputStream(FILE)) { properties.store(output, "Fake Rangi Anarchia.GG"); }
        catch (IOException ex) { System.err.println("Fake Rangi: nie mozna zapisac konfiguracji: " + ex); }
    }
}
