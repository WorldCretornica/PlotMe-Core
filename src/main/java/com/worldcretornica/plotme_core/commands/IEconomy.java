package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.api.IPlayer;

public interface IEconomy {

    boolean withdraw(IPlayer player, Number price);

    boolean deposit(IPlayer player, Number price);

    boolean getBalance(IPlayer player);
}
