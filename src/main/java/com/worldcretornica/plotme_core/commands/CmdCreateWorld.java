package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;

public class CmdCreateWorld extends PlotCommand {

    public CmdCreateWorld(PlotMe_Core instance) {
        super(instance);
    }
/*

    public boolean execute(ICommandSender sender, String[] args) {
        if (plugin.creationbuffer.containsKey(sender.getName())) {
            if (args.length == 1) {
                //try to create world
                Map<String, String> parameters = plugin.creationbuffer.get(sender.getName());

                InternalPlotWorldCreateEvent event = serverBridge.getEventFactory().callPlotWorldCreateEvent(parameters.get("worldname"), parameters);

                if (!event.isCancelled()) {
                    if (serverBridge.createPlotWorld(parameters.get("worldname"), parameters.get("generator"), parameters)) {
                        sender.sendMessage(C("MsgWorldCreationSuccess"));
                    }
                }
            } else if (args.length >= 2) {
                //settings
                if (args[1].equalsIgnoreCase(C("CommandCreateWorld-Setting"))) {
                    if (args.length == 4) {
                        String key = args[2];
                        String value = args[3];

                        Map<String, String> parameters = plugin.creationbuffer.get(sender.getName());

                        if (parameters != null) {
                            for (String ckey : parameters.keySet()) {
                                if (key.equalsIgnoreCase(ckey)) {
                                    parameters.remove(ckey);
                                    parameters.put(ckey, value);

                                    sender.sendMessage(C("MsgSettingChanged") + " " + ckey + "=" + value);

                                    return true;
                                }
                            }

                            showCurrentSettings(sender, parameters);
                            return true;
                        }
                    }
                }

                sender.sendMessage(C("WordUsage") + ": ");
                sender.sendMessage("/plotme createworld " + C("CommandCreateWorld-Setting") + "<" + C("WordConfig") + "> <" + C("WordValue") + "> "
                 + C("MsgCreateWorldParameters4"));
                sender.sendMessage("/plotme createworld " + C("CommandCreateWorld-Cancel") + " " + C("MsgCreateWorldParameters5"));
            }
        } else {
            //Usage
            if (args.length == 1) {
                sender.sendMessage(C("WordUsage") + ": /plotme createworld <" + C("WordWorld") + "> [" + C("WordGenerator") + "]");
                sender.sendMessage(C("MsgCreateWorldHelp"));
            } else {

                Map<String, String> parameters = new HashMap<>();

                //Prepare creation
                if (args.length >= 2) {
                    parameters.put("worldname", args[1]);
                }

                if (args.length >= 3) {
                    parameters.put("generator", args[2]);
                } else {
                    parameters.put("generator", "PlotMe-DefaultGenerator");
                }

                //Check if world exists
                if (serverBridge.worldExists(parameters.get("worldname"))) {
                    sender.sendMessage(C("ErrWorldExists") + " '" + parameters.get("worldname") + "'");
                    return false;
                }

                //Find generator
                IPlotMe_ChunkGenerator generator = serverBridge.getPlotMeGenerator(parameters.get("worldname"));

                Map<String, String> genparameters;
                if (generator != null) {
                    //Get the generator configurations
                    genparameters = generator.getManager().getDefaultGenerationConfig();

                    if (genparameters == null) {
                        sender.sendMessage(C("ErrCannotCreateGen1") + " '" + parameters.get("generator") + "' " + C("ErrCannotCreateGen2"));
                        return false;
                    }
                } else {
                    sender.sendMessage(C("ErrCannotCreateGen1") + " '" + parameters.get("generator") + "' " + C("ErrCannotCreateGen3"));
                    return false;
                }

                parameters.put("PlotAutoLimit", "1000");
                parameters.put("DaysToExpiration", "7");
                parameters.put("ProtectedWallBlockId", "44:4");
                parameters.put("ForSaleWallBlockId", "44:1");
                parameters.put("AutoLinkPlots", "true");
                parameters.put("DisableExplosion", "true");
                parameters.put("DisableIgnition", "true");
                parameters.put("UseEconomy", "false");
                parameters.put("CanPutOnSale", "false");
                parameters.put("RefundClaimPriceOnReset", "false");
                parameters.put("RefundClaimPriceOnSetOwner", "false");
                parameters.put("ClaimPrice", "0");
                parameters.put("ClearPrice", "0");
                parameters.put("AddPlayerPrice", "0");
                parameters.put("DenyPlayerPrice", "0");
                parameters.put("RemovePlayerPrice", "0");
                parameters.put("UndenyPlayerPrice", "0");
                parameters.put("PlotHomePrice", "0");
                parameters.put("SellToPlayerPrice", "0");
                parameters.put("BiomeChangePrice", "0");
                parameters.put("ProtectPrice", "0");
                parameters.put("DisposePrice", "0");
                parameters.put("UseProgressiveClear", "false");

                sender.sendMessage(C("MsgCreateWorldParameters1"));
                sender.sendMessage(C("MsgCreateWorldParameters2"));

                //Show default configurations
                showCurrentSettings(sender, parameters);
                sender.sendMessage(C("MsgCreateWorldParameters3"));

                showCurrentSettings(sender, genparameters);

                parameters.putAll(genparameters);

                sender.sendMessage("/plotme createworld " + C("CommandCreateWorld-Setting") + "<" + C("WordConfig") + "> <" + C("WordValue") + "> "
                 + C("MsgCreateWorldParameters4"));

                sender.sendMessage("/plotme createworld " + C("CommandCreateWorld-Cancel") + " " + C("MsgCreateWorldParameters5"));

                plugin.creationbuffer.put(sender.getName(), parameters);
            }
        }
        return true;
    }

    private void showCurrentSettings(ICommandSender cs, Map<String, String> parameters) {
        String buffer = " ";

        for (String key : parameters.keySet()) {
            if (MinecraftFontWidthCalculator.getStringWidth(serverBridge.stripColor(buffer + key + "=" + parameters.get(key) + " ")) >= 1250) {
                cs.sendMessage(buffer);
                buffer = " ";
            }

            buffer += key + "=" + parameters.get(key) + "  ";
        }
        cs.sendMessage(buffer);
    }

*/
}
