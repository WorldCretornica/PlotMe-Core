package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.api.event.InternalPlotWorldCreateEvent;
import com.worldcretornica.plotme_core.api.IPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.utils.MinecraftFontWidthCalculator;

import java.util.HashMap;
import java.util.Map;

public class CmdCreateWorld extends PlotCommand {

    public CmdCreateWorld(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(ICommandSender cs, String[] args) {
        if (plugin.cPerms(cs, "PlotMe.admin.createworld")) {
            if (plugin.creationbuffer.containsKey(cs.getName())) {
                if (args.length == 1) {
                    //try to create world
                    Map<String, String> parameters = plugin.creationbuffer.get(cs.getName());

                    InternalPlotWorldCreateEvent event = sob.getEventFactory().callPlotWorldCreateEvent(parameters.get("worldname"), cs, parameters);

                    if (!event.isCancelled()) {
                        if (sob.CreatePlotWorld(cs, parameters.get("worldname"), parameters.get("generator"), parameters)) {
                            cs.sendMessage(C("MsgWorldCreationSuccess"));
                        }
                    }
                } else {
                    if (args.length >= 2) {
                        //cancel
                        if (args[1].equalsIgnoreCase(C("CommandCreateWorld-Cancel"))) {
                            plugin.creationbuffer.remove(cs.getName());
                            return true;
                        } //settings
                        else if (args[1].equalsIgnoreCase(C("CommandCreateWorld-Setting"))) {
                            if (args.length == 4) {
                                String key = args[2];
                                String value = args[3];

                                Map<String, String> parameters = plugin.creationbuffer.get(cs.getName());

                                if (parameters != null) {
                                    for (String ckey : parameters.keySet()) {
                                        if (key.equalsIgnoreCase(ckey)) {
                                            parameters.remove(ckey);
                                            parameters.put(ckey, value);

                                            cs.sendMessage(C("MsgSettingChanged") + " " + GREEN + ckey + RESET + "=" + AQUA + value);

                                            return true;
                                        }
                                    }

                                    showCurrentSettings(cs, parameters);
                                    return true;
                                }
                            }
                        }

                        cs.sendMessage(C("WordUsage") + ": ");
                        cs.sendMessage("/plotme " + C("CommandCreateWorld") + " " + C("CommandCreateWorld-Setting")
                                + "<" + C("WordConfig") + ">" + " " + "<" + C("WordValue") + "> "
                                + C("MsgCreateWorldParameters4"));
                        cs.sendMessage("/plotme " + C("CommandCreateWorld") + " " + C("CommandCreateWorld-Cancel") + " "
                                + C("MsgCreateWorldParameters5"));
                    }
                }
            } else {
                //Usage
                if (args.length == 1) {
                    cs.sendMessage(C("WordUsage") + ": " + RED + "/plotme " + C("CommandCreateWorld") + " <" + C("WordWorld") + "> [" + C("WordGenerator") + "]");
                    cs.sendMessage("  " + C("MsgCreateWorldHelp"));
                } else {
                    
                    Map<String, String> parameters = new HashMap<>();
                    Map<String, String> genparameters;

                    //Prepare creation
                    if (args.length >= 2) {
                        parameters.put("worldname", args[1]);

                        if (!sob.checkWorldName(args[1])) {
                            cs.sendMessage("[" + plugin.getName() + "] " + C("ErrInvalidWorldName") + " '" + parameters.get("worldname") + "'");
                            return true;
                        }
                    }

                    if (args.length >= 3) {
                        parameters.put("generator", args[2]);
                    } else {
                        parameters.put("generator", "PlotMe-DefaultGenerator");
                    }

                    //Check if world exists
                    if (sob.worldExists(parameters.get("worldname"))) {
                        cs.sendMessage("[" + plugin.getName() + "] " + C("ErrWorldExists") + " '" + parameters.get("worldname") + "'");
                        return false;
                    }

                    //Find generator
                    IPlotMe_ChunkGenerator generator = sob.getPlotMeGenerator(parameters.get("worldname"));

                    if (generator != null) {
                        //Get the generator configurations
                        genparameters = generator.getManager().getDefaultGenerationConfig();

                        if (genparameters == null) {
                            cs.sendMessage("[" + plugin.getName() + "] " + C("ErrCannotCreateGen1") + " '" + parameters.get("generator") + "' " + C("ErrCannotCreateGen2"));
                            return false;
                        }
                    } else {
                        cs.sendMessage("[" + plugin.getName() + "] " + C("ErrCannotCreateGen1") + " '" + parameters.get("generator") + "' " + C("ErrCannotCreateGen3"));
                        return false;
                    }

                    parameters.put("PlotAutoLimit", "1000");
                    parameters.put("DaysToExpiration", "7");
                    parameters.put("ProtectedWallBlockId", "44:4");
                    parameters.put("ForSaleWallBlockId", "44:1");
                    parameters.put("AuctionWallBlockId", "44:1");
                    parameters.put("AutoLinkPlots", "true");
                    parameters.put("DisableExplosion", "true");
                    parameters.put("DisableIgnition", "true");
                    parameters.put("UseEconomy", "false");
                    parameters.put("CanPutOnSale", "false");
                    parameters.put("CanSellToBank", "false");
                    parameters.put("RefundClaimPriceOnReset", "false");
                    parameters.put("RefundClaimPriceOnSetOwner", "false");
                    parameters.put("ClaimPrice", "0");
                    parameters.put("ClearPrice", "0");
                    parameters.put("AddPlayerPrice", "0");
                    parameters.put("DenyPlayerPrice", "0");
                    parameters.put("RemovePlayerPrice", "0");
                    parameters.put("UndenyPlayerPrice", "0");
                    parameters.put("PlotHomePrice", "0");
                    parameters.put("CanCustomizeSellPrice", "false");
                    parameters.put("SellToPlayerPrice", "0");
                    parameters.put("SellToBankPrice", "0");
                    parameters.put("BuyFromBankPrice", "0");
                    parameters.put("AddCommentPrice", "0");
                    parameters.put("BiomeChangePrice", "0");
                    parameters.put("ProtectPrice", "0");
                    parameters.put("DisposePrice", "0");
                    parameters.put("UseProgressiveClear", "false");

                    cs.sendMessage(C("MsgCreateWorldParameters1"));
                    cs.sendMessage(C("MsgCreateWorldParameters2"));

                    //Show default configurations
                    showCurrentSettings(cs, parameters);
                    cs.sendMessage(C("MsgCreateWorldParameters3"));

                    showCurrentSettings(cs, genparameters);

                    parameters.putAll(genparameters);

                    cs.sendMessage("/plotme " + C("CommandCreateWorld") + " " + C("CommandCreateWorld-Setting")
                            + "<" + C("WordConfig") + ">" + " " + "<" + C("WordValue") + "> "
                            + C("MsgCreateWorldParameters4"));

                    cs.sendMessage("/plotme " + C("CommandCreateWorld") + " " + C("CommandCreateWorld-Cancel") + " "
                            + C("MsgCreateWorldParameters5"));

                    plugin.creationbuffer.put(cs.getName(), parameters);
                }
            }
        } else {
            cs.sendMessage(RED + C("MsgPermissionDenied"));
        }
        return true;
    }

    private void showCurrentSettings(ICommandSender cs, Map<String, String> parameters) {
        String buffer = " ";

        for (String key : parameters.keySet()) {
            if (MinecraftFontWidthCalculator.getStringWidth(sob.stripColor(buffer + key + "=" + parameters.get(key) + " ")) >= 1250) {
                cs.sendMessage(buffer);
                buffer = " ";
            }

            buffer += GREEN + key + RESET + "=" + AQUA + parameters.get(key) + "  ";
        }
        cs.sendMessage(buffer);
    }
}
