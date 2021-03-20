package cube.builder.cubebuilder;

import cube.builder.cubebuilder.exceptions.BlockNotFoundException;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;
import static org.bukkit.event.block.Action.*;

public class Controller {

    public static Map<Player, Location[]> rangeMap = new HashMap<>();
    public static Map<Player, BukkitRunnable> timerMap = new HashMap<>();
    public static Map<Player, Color> colorMap = new HashMap<>();

    private PlayerInteractEvent event;
    private final Player player;
    private final FillBlockRange blockRange = new FillBlockRange();

    public Controller(PlayerInteractEvent event) {
        this.event = event;
        this.player = event.getPlayer();
    }

    public Controller(Player player) {
        this.player = player;
    }

    public void onLeftClick() throws Exception {
        if (player.getInventory().getItemInMainHand().getType() != Material.CLOCK) return;
        Action action = event.getAction();

        if (!(action == LEFT_CLICK_AIR || action == LEFT_CLICK_BLOCK)) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        event.setCancelled(true);

        Location loc = blockRange.getPoint(player);
        if (loc == null) throw new BlockNotFoundException(player);
        Location[] array = new Location[2];
        array[0] = loc;

        rangeMap.put(player, array);

        player.sendMessage(BLUE + "" + UNDERLINE + "始点" + RESET + GRAY + "を ["
                + loc.getBlockX() + ", "  + loc.getBlockY() + ", " + loc.getBlockZ() + "] に設定しました");
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);

        colorMap.put(player, Color.RED);
    }

    public void onRightClick() throws Exception {
        if (player.getInventory().getItemInMainHand().getType() != Material.CLOCK) return;
        Action action = event.getAction();

        if (!(action == RIGHT_CLICK_AIR || action == RIGHT_CLICK_BLOCK)) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        event.setCancelled(true);

        if (!rangeMap.containsKey(player)) {
            player.sendMessage(RED+ "始点から指定してください");
            return;
        }

        Location loc = blockRange.getPoint(player);
        if (loc == null) throw new BlockNotFoundException(player);
        Location[] array = rangeMap.get(player);
        array[1] = loc;

        rangeMap.put(player, array);

        player.sendMessage(RED + "" + UNDERLINE + "終点" + RESET + GRAY + "を ["
                + loc.getBlockX() + ", "  + loc.getBlockY() + ", " + loc.getBlockZ() + "] に設定しました");
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
    }

    public void showCursor() {
        BukkitRunnable timer = new BukkitRunnable() {

            public void run() {
                Location loc = blockRange.getPoint(player);
                if (loc != null) {
                    if (!colorMap.containsKey(player))
                        colorMap.put(player, Color.BLUE);
                    makeBox(loc, colorMap.get(player), 2);
                }
            }
        };
        timerMap.put(player, timer);
        timer.runTaskTimer(getServer().getPluginManager().getPlugin("TofuBuilder"), 0L, 1L);
    }

    private void makeBox(Location loc, Color color, int count) {
        Particle.DustOptions dust = new Particle.DustOptions(color, 1);

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        player.spawnParticle(Particle.REDSTONE, x, y + 0.5, z, count,
                0, 0.25, 0, dust);
        player.spawnParticle(Particle.REDSTONE, x + 1, y + 0.5, z, count,
                0, 0.25, 0, dust);

        player.spawnParticle(Particle.REDSTONE, x, y + 0.5, z + 1, count,
                0, 0.25, 0, dust);
        player.spawnParticle(Particle.REDSTONE, x + 1, y + 0.5, z + 1, count,
                0, 0.25, 0, dust);

        player.spawnParticle(Particle.REDSTONE, x + 0.5, y, z, count,
                0.25, 0, 0, dust);
        player.spawnParticle(Particle.REDSTONE, x + 0.5, y + 1, z, count,
                0.25, 0, 0, dust);

        player.spawnParticle(Particle.REDSTONE, x + 0.5, y, z + 1, count,
                0.25, 0, 0, dust);
        player.spawnParticle(Particle.REDSTONE, x + 0.5, y + 1, z + 1, count,
                0.25, 0, 0, dust);

        player.spawnParticle(Particle.REDSTONE, x, y, z + 0.5, count,
                0, 0, 0.25, dust);
        player.spawnParticle(Particle.REDSTONE, x + 1, y, z + 0.5, count,
                0, 0, 0.25, dust);

        player.spawnParticle(Particle.REDSTONE, x, y + 1, z + 0.5, count,
                0, 0, 0.25, dust);
        player.spawnParticle(Particle.REDSTONE, x + 1, y + 1, z + 0.5, count,
                0, 0, 0.25, dust);
    }
}
