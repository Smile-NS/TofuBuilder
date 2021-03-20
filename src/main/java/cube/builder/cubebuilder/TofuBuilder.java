package cube.builder.cubebuilder;

import cube.builder.cubebuilder.exceptions.IllegalArgsException;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.ChatColor.*;

public final class TofuBuilder extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        Controller ctrl = new Controller(event);
        try {
            ctrl.onLeftClick();
            ctrl.onRightClick();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void  PlayerItemHeldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (item == null || item.getType() != Material.CLOCK){
            if (Controller.timerMap.containsKey(player)) {
                Controller.timerMap.get(player).cancel();
                Controller.timerMap.remove(player);
            }
        } else if (item.getType() == Material.CLOCK)
            new Controller(player).showCursor();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)) {
            System.out.println("このコマンドはプレイヤー用です");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("tofu")) {
            try {
                sentCmd(player, args);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    private void sentCmd(Player sender, String[] args) throws Exception{
        if (args.length != 1) throw new IllegalArgsException(sender);

        if (!Controller.rangeMap.containsKey(sender)) {
            sender.sendMessage(RED + "始点が指定されていません");
            return;
        }

        Location[] array = Controller.rangeMap.get(sender);
        Location start = array[0];
        Location end = array[1];

        if (end == null) {
            sender.sendMessage(RED + "終点が指定されていません");
            return;
        }

        Material type = Material.getMaterial(args[0]);
        if (type == null) throw new IllegalArgsException(sender);

        sender.sendMessage(GRAY + "" + new FillBlockRange().fill(start, end, type) + "個のブロックで豆腐を生成しました");

        Controller.rangeMap.remove(sender);
        Controller.colorMap.put(sender, Color.BLUE);
    }
}
