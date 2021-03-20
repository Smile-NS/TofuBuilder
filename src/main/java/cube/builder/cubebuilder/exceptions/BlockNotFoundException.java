package cube.builder.cubebuilder.exceptions;

import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.RED;

public class BlockNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;
    private static final String msg = "ブロックが見つからなかったか、遠すぎます";

    public BlockNotFoundException(Player sender){
        super(msg);
        sender.sendMessage(RED + msg);
    }
}
