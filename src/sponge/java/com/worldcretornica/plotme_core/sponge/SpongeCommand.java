package com.worldcretornica.plotme_core.sponge;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.CommandExBase;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

//TODO Write command class
public class SpongeCommand extends CommandExBase implements CommandCallable {

    private final PlotMeCoreManager manager;
    private final Game game;

    public SpongeCommand(PlotMe_Sponge plugin) {
        super(plugin.getAPI());
        manager = PlotMeCoreManager.getInstance();
        game = plugin.getGame();
    }

    /**
     * Execute the command based on input arguments.
     *
     * <p>The implementing class must perform the necessary permission
     * checks.</p>
     *
     * @param source The caller of the command
     * @param arguments The raw arguments for this command
     * @param parents A stack of parent commands, where the first entry is
     *                the root command
     * @return Whether a command was processed
     * @throws CommandException Thrown on a command error
     */
    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
        return source instanceof Player;
    }

    /**
     * Test whether this command can probably be executed by the given source.
     *
     * <p>If implementations are unsure if the command can be executed by
     * the source, {@code true} should be returned. Return values of this method
     * may be used to determine whether this command is listed in command
     * listings.</p>
     *
     * @param source The caller of the command
     * @return Whether permission is (probably) granted
     */
    @Override
    public boolean testPermission(CommandSource source) {
        return false;
    }

    @Override
    public String getShortDescription(CommandSource commandSource) {
        return null;
    }

    @Override
    public Text getHelp(CommandSource commandSource) {
        return null;
    }

    @Override
    public String getUsage(CommandSource commandSource) {
        return null;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }
}
