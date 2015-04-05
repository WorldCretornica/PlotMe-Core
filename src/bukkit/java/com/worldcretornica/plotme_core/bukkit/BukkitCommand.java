package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.api.CommandExBase;
import com.worldcretornica.plotme_core.bukkit.api.BukkitCommandSender;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.commands.PlotCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitCommand extends CommandExBase implements CommandExecutor {


    private final PlotMe_CorePlugin plugin;

    public BukkitCommand(PlotMe_CorePlugin instance) {
        super(instance.getAPI());
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                try {
                    return commandMap.get("help").execute(plugin.wrapPlayer((Player) sender), args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            PlotCommand _command = commandMap.get(args[0]);
            if (_command == null) {
                sender.sendMessage("PlotMe does not have a command by that name.");
                return true;
            } else {
                try {
                    return _command.execute(new BukkitPlayer((Player) sender), args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            return handleConsoleCommands(sender, args);
        }
        return false;
    }

    private boolean handleConsoleCommands(CommandSender sender, String[] args) {
        if (args.length == 0) {
            PlotCommand command = commandMap.get("reload");
            if (command != null) {
                try {
                    return command.execute(new BukkitCommandSender(sender), args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            sender.sendMessage("You can only reload plotme from the console.");
        }
        if ("reload".equalsIgnoreCase(args[0])) {
            PlotCommand command = commandMap.get("reload");
            if (command != null) {
                try {
                    return command.execute(new BukkitCommandSender(sender), args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
