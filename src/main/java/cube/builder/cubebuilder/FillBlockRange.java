package cube.builder.cubebuilder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FillBlockRange {

    private static final Set<Material> AIR_TYPES = new HashSet<>(Arrays.asList(
            Material.AIR, Material.VOID_AIR, Material.CAVE_AIR
    ));

    public int fill(Location start, Location end, Material type) {
        int startX = start.getBlockX();
        int startY = start.getBlockY();
        int startZ = start.getBlockZ();

        int endX = end.getBlockX();
        int endY = end.getBlockY();
        int endZ = end.getBlockZ();

        int count = 0;
        for (int x = startX;x != (startX < endX ? endX + 1 : endX - 1);x += startX < endX ? 1 : -1) {
            for (int y = startY;y != (startY < endY ? endY + 1 : endY - 1);y += startY < endY ? 1 : -1) {
                for (int z = startZ;z != (startZ < endZ ? endZ + 1 : endZ - 1);z += startZ < endZ ? 1 : -1) {
                    Location loc = new Location(start.getWorld(), x, y, z);
                    loc.getBlock().setType(type);
                    count++;
                }
            }
        }
        return count;
    }

    public Location getPoint(Player player) {
        BlockIterator it = new BlockIterator(player);

        Location loc = null;
        int dis = 0;
        while (true) {
            Block block = it.next();
            if (dis > 100) break;
            if (AIR_TYPES.contains(block.getType())) {
                dis++;
                continue;
            }
            loc = block.getLocation();
            break;
        }

        return loc;
    }
}
