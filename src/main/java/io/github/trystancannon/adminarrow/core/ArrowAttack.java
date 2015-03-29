package io.github.trystancannon.adminarrow.core;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * @author Trystan
 */
public class ArrowAttack extends JavaPlugin {
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onDisable() {
    }
    
    /**
     * Sends a message to the receiving player with a tag that specifies that
     * the message came from this plugin.
     * 
     * @param receiver
     * @param message 
     */
    private void sendLabeledMessage(Player receiver, String message) {
        receiver.sendMessage(ChatColor.GOLD + "[Admin Arrow] " + ChatColor.RESET + message);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player playerIssuingCommand = (Player) sender;
            String nameOfPlayerTo = args.length >= 1 ? args[0] : null;
            
            if (command.getName().equalsIgnoreCase("arrow")) {
                if (nameOfPlayerTo != null) {
                    // May cause lag!
                    for (Player player : playerIssuingCommand.getWorld().getPlayers()) {
                        if (player.getName().equalsIgnoreCase(nameOfPlayerTo)) {
                            fireArrowFromPlayer(playerIssuingCommand, player);
                            sendLabeledMessage(playerIssuingCommand, ChatColor.GREEN + "Arrow fired at " + player.getName() + "!");
                            
                            break;
                        }
                    }
                } else {
                    sendLabeledMessage(playerIssuingCommand, ChatColor.RED + "Invalid player name! They are either offline, nonexistent, or not in this world.");
                }
                
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Fires an arrow from one player to another if they are in the same
     * world.
     * 
     * @param from Player from which the arrow is fired.
     * @param to Player to which the arrow is fired.
     */
    public void fireArrowFromPlayer(Player from, Player to) {
        if (from.getWorld() == to.getWorld()) {
            Location fromEyeLocation = from.getEyeLocation();
            Location toEyeLocation = to.getEyeLocation();

            Vector offset = new Vector(toEyeLocation.getX() - fromEyeLocation.getX(), 0, toEyeLocation.getZ() - fromEyeLocation.getZ()).normalize();
            fromEyeLocation.add(offset);
            
            Vector arrowDirection = new Vector(
                    toEyeLocation.getX() - fromEyeLocation.getX(),
                    toEyeLocation.getY() - fromEyeLocation.getY(),
                    toEyeLocation.getZ() - fromEyeLocation.getZ()
            );
            
            from.getWorld().spawnArrow(fromEyeLocation, arrowDirection, (float) 5F, 0F);
        }
    }
}