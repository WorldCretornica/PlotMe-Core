package com.worldcretornica.plotme_core.listener;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotToClear;
import com.worldcretornica.plotme_core.event.PlotWorldLoadEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlotListener implements Listener {

	private PlotMe_Core plugin = null;

	public PlotListener(PlotMe_Core instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event) {
		Block b = event.getBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			Player p = event.getPlayer();
			boolean canbuild = plugin.cPerms(event.getPlayer(), "plotme.admin.buildanywhere");
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			if (id.equals("")) {
				if (!canbuild) {
					p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
					event.setCancelled(true);
				}
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(p.getWorld().getName(), id);

				if (ptc != null) {
					switch (ptc.getReason()) {
						case Clear:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedClear"));
							break;
						case Reset:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedReset"));
							break;
						case Expired:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedExpired"));
							break;
					}
					event.setCancelled(true);
				} else {
					Plot plot = plugin.getPlotMeCoreManager().getMap(p).getPlot(id);

                    if (plot == null || !plot.isAllowed(p.getName())) {
                        if (!canbuild) {
							p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
							event.setCancelled(true);
						}
					} else {
						plot.resetExpire(plugin.getPlotMeCoreManager().getMap(b).getDaysToExpiration());
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event) {
		Block b = event.getBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			Player p = event.getPlayer();
			boolean canbuild = plugin.cPerms(p, "plotme.admin.buildanywhere");
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			if (id.equals("")) {
				if (!canbuild) {
					p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
					event.setCancelled(true);
				}
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(p.getWorld().getName(), id);

				if (ptc != null) {
					switch (ptc.getReason()) {
						case Clear:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedClear"));
							break;
						case Reset:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedReset"));
							break;
						case Expired:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedExpired"));
							break;
					}
					event.setCancelled(true);
				} else {
					Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot == null || !plot.isAllowed(p.getName())) {
                        if (!canbuild) {
							p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
							event.setCancelled(true);
						}
					} else {
						plot.resetExpire(plugin.getPlotMeCoreManager().getMap(b).getDaysToExpiration());
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event) {
		if (!plugin.cPerms(event.getPlayer(), "plotme.admin.buildanywhere")) {
			BlockFace bf = event.getBlockFace();
			Block b = event.getBlockClicked().getLocation().add(bf.getModX(), bf.getModY(), bf.getModZ()).getBlock();
			if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
				String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());
				Player p = event.getPlayer();

				if (id.equals("")) {
					p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
					event.setCancelled(true);
				} else {
					PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(p.getWorld().getName(), id);

					if (ptc != null) {
						switch (ptc.getReason()) {
							case Clear:
								p.sendMessage(plugin.getUtil().C("MsgPlotLockedClear"));
								break;
							case Reset:
								p.sendMessage(plugin.getUtil().C("MsgPlotLockedReset"));
								break;
							case Expired:
								p.sendMessage(plugin.getUtil().C("MsgPlotLockedExpired"));
								break;
						}
						event.setCancelled(true);
					} else {
						Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

						if (plot == null) {
							p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
							event.setCancelled(true);
                        } else if (!plot.isAllowed(p.getName())) {
                            p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerBucketFill(final PlayerBucketFillEvent event) {
		if (!plugin.cPerms(event.getPlayer(), "plotme.admin.buildanywhere")) {
			Block b = event.getBlockClicked();
			if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
				String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());
				Player p = event.getPlayer();

				if (id.equals("")) {
					p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
					event.setCancelled(true);
				} else {
					PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(p.getWorld().getName(), id);

					if (ptc != null) {
						switch (ptc.getReason()) {
							case Clear:
								p.sendMessage(plugin.getUtil().C("MsgPlotLockedClear"));
								break;
							case Reset:
								p.sendMessage(plugin.getUtil().C("MsgPlotLockedReset"));
								break;
							case Expired:
								p.sendMessage(plugin.getUtil().C("MsgPlotLockedExpired"));
								break;
						}
						event.setCancelled(true);
					} else {
						Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

						if (plot == null) {
							p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
							event.setCancelled(true);
                        } else if (!plot.isAllowed(p.getName())) {
                            p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		Block b = event.getClickedBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			Player p = event.getPlayer();

			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(p.getWorld().getName(), id);

			if (ptc != null) {
				switch (ptc.getReason()) {
					case Clear:
						p.sendMessage(plugin.getUtil().C("MsgPlotLockedClear"));
						break;
					case Reset:
						p.sendMessage(plugin.getUtil().C("MsgPlotLockedReset"));
						break;
					case Expired:
						p.sendMessage(plugin.getUtil().C("MsgPlotLockedExpired"));
						break;
				}
				event.setCancelled(true);
			} else {
				boolean canbuild = plugin.cPerms(p, "plotme.admin.buildanywhere");
				PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(b);
				boolean blocked = false;

				if (event.isBlockInHand() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					BlockFace face = event.getBlockFace();
					Block builtblock = b.getWorld().getBlockAt(b.getX() + face.getModX(), b.getY() + face.getModY(), b.getZ() + face.getModZ());

					id = plugin.getPlotMeCoreManager().getPlotId(builtblock.getLocation());

					if (id.equals("")) {
						if (!canbuild) {
							p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
							event.setCancelled(true);
						}
					} else {
						Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

						if (plot == null) {
							if (!canbuild) {
								p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
								event.setCancelled(true);
							}
						} else {
                            if (!plot.isAllowed(p.getName())) {
                                if (!canbuild) {
									p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
									event.setCancelled(true);
								}
							} else {
								plot.resetExpire(plugin.getPlotMeCoreManager().getMap(b).getDaysToExpiration());
							}
						}
					}
				} else {
					if (pmi.isProtectedBlock(b.getTypeId())) {
						if (!plugin.cPerms(p, "plotme.unblock." + b.getTypeId())) {
							blocked = true;
						}
					}

					ItemStack is = event.getItem();

					if (is != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
						int itemid = is.getType().getId();
						byte itemdata = is.getData().getData();

						if (pmi.isPreventedItem("" + itemid)
								|| pmi.isPreventedItem("" + itemid + ":" + itemdata)) {
							if (!plugin.cPerms(p, "plotme.unblock." + itemid)) {
								blocked = true;
							}
						}
					}

					if (blocked) {
						id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

						if (id.equals("")) {
							if (!canbuild) {
								if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
									p.sendMessage(plugin.getUtil().C("ErrCannotUse"));
								}
								event.setCancelled(true);
							}
						} else {
							Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

							if (plot == null) {
								if (!canbuild) {
									if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
										p.sendMessage(plugin.getUtil().C("ErrCannotUse"));
									}
									event.setCancelled(true);
								}
                            } else if (!plot.isAllowed(p.getName())) {
                                if (!canbuild) {
									if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
										p.sendMessage(plugin.getUtil().C("ErrCannotUse"));
									}
									event.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockSpread(final BlockSpreadEvent event) {
		Block b = event.getBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			if (id.equals("")) {
				event.setCancelled(true);
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

				if (ptc != null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockForm(final BlockFormEvent event) {
		Block b = event.getBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			if (id.equals("")) {
				event.setCancelled(true);
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

				if (ptc != null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockDamage(final BlockDamageEvent event) {
		Block b = event.getBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			if (id.equals("")) {
				event.setCancelled(true);
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

				if (ptc != null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockFade(final BlockFadeEvent event) {
		Block b = event.getBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			if (id.equals("")) {
				event.setCancelled(true);
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

				if (ptc != null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockFromTo(final BlockFromToEvent event) {
		Block b = event.getToBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			if (id.equals("")) {
				event.setCancelled(true);
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

				if (ptc != null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockGrow(final BlockGrowEvent event) {
		Block b = event.getBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			if (id.equals("")) {
				event.setCancelled(true);
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

				if (ptc != null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockPistonExtend(final BlockPistonExtendEvent event) {
		if (plugin.getPlotMeCoreManager().isPlotWorld(event.getBlock())) {
			BlockFace face = event.getDirection();

			for (Block b : event.getBlocks()) {
				String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation().add(face.getModX(), face.getModY(), face.getModZ()));

				if (id.equals("")) {
					event.setCancelled(true);
				} else {
					PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

					if (ptc != null) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockPistonRetract(final BlockPistonRetractEvent event) {
		Block b = event.getRetractLocation().getBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b) && event.getBlock().getType() == Material.PISTON_STICKY_BASE) {
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());

			if (id.equals("")) {
				event.setCancelled(true);
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

				if (ptc != null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onStructureGrow(final StructureGrowEvent event) {
		List<BlockState> blocks = event.getBlocks();
		boolean found = false;

		for (int i = 0; i < blocks.size(); i++) {
			if (found || plugin.getPlotMeCoreManager().isPlotWorld(blocks.get(i))) {
				found = true;
				String id = plugin.getPlotMeCoreManager().getPlotId(blocks.get(i).getLocation());

				if (id.equals("")) {
					event.getBlocks().remove(i);
					i--;
				} else {
					PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(blocks.get(i).getWorld().getName(), id);

					if (ptc != null) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityExplode(final EntityExplodeEvent event) {
		Location l = event.getLocation();

		if (l != null) {
			PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(l);

			if (pmi != null && pmi.isDisableExplosion()) {
				event.setCancelled(true);
			} else {
				String id = plugin.getPlotMeCoreManager().getPlotId(l);
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(l.getWorld().getName(), id);

				if (ptc != null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockIgnite(final BlockIgniteEvent event) {
		Block b = event.getBlock();

		if (b != null) {
			PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(b);

			if (pmi != null) {
				if (pmi.isDisableIgnition()) {
					event.setCancelled(true);
				} else {
					String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());
					Player p = event.getPlayer();

					if (id.equals("") || p == null) {
						event.setCancelled(true);
					} else {
						PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

						if (ptc != null) {
							switch (ptc.getReason()) {
								case Clear:
									p.sendMessage(plugin.getUtil().C("MsgPlotLockedClear"));
									break;
								case Reset:
									p.sendMessage(plugin.getUtil().C("MsgPlotLockedReset"));
									break;
								case Expired:
									p.sendMessage(plugin.getUtil().C("MsgPlotLockedExpired"));
									break;
							}
							event.setCancelled(true);
						} else {
							Plot plot = plugin.getPlotMeCoreManager().getPlotById(b, id);

                            if (plot == null || !plot.isAllowed(p.getName())) {
                                event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onHangingPlace(final HangingPlaceEvent event) {
		Block b = event.getBlock();

		if (plugin.getPlotMeCoreManager().isPlotWorld(b)) {
			String id = plugin.getPlotMeCoreManager().getPlotId(b.getLocation());
			Player p = event.getPlayer();
			boolean canbuild = plugin.cPerms(event.getPlayer(), "plotme.admin.buildanywhere");

			if (id.equals("")) {
				if (!canbuild) {
					p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
					event.setCancelled(true);
				}
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(b.getWorld().getName(), id);

				if (ptc != null) {
					switch (ptc.getReason()) {
						case Clear:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedClear"));
							break;
						case Reset:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedReset"));
							break;
						case Expired:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedExpired"));
							break;
					}
					event.setCancelled(true);
				} else {
					Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot == null || !plot.isAllowed(p.getName())) {
                        if (!canbuild) {
							p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
							event.setCancelled(true);
						}
					} else {
						plot.resetExpire(plugin.getPlotMeCoreManager().getMap(b).getDaysToExpiration());
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onHangingBreakByEntity(final HangingBreakByEntityEvent event) {
		Entity entity = event.getRemover();

		if (entity instanceof Player) {
			Player p = (Player) entity;

			boolean canbuild = plugin.cPerms(p, "plotme.admin.buildanywhere");

			Location l = event.getEntity().getLocation();

			if (plugin.getPlotMeCoreManager().isPlotWorld(l)) {
				String id = plugin.getPlotMeCoreManager().getPlotId(l);

				if (id.equals("")) {
					if (!canbuild) {
						p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
						event.setCancelled(true);
					}
				} else {
					PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(l.getWorld().getName(), id);

					if (ptc != null) {
						switch (ptc.getReason()) {
							case Clear:
								p.sendMessage(plugin.getUtil().C("MsgPlotLockedClear"));
								break;
							case Reset:
								p.sendMessage(plugin.getUtil().C("MsgPlotLockedReset"));
								break;
							case Expired:
								p.sendMessage(plugin.getUtil().C("MsgPlotLockedExpired"));
								break;
						}
						event.setCancelled(true);
					} else {
						Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                        if (plot == null || !plot.isAllowed(p.getName())) {
                            if (!canbuild) {
								p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
								event.setCancelled(true);
							}
						} else {
							plot.resetExpire(plugin.getPlotMeCoreManager().getMap(l).getDaysToExpiration());
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
		Location l = event.getRightClicked().getLocation();

		if (plugin.getPlotMeCoreManager().isPlotWorld(l)) {
			Player p = event.getPlayer();
			boolean canbuild = plugin.cPerms(p, "plotme.admin.buildanywhere");
			String id = plugin.getPlotMeCoreManager().getPlotId(l);

			if (id.equals("")) {
				if (!canbuild) {
					p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
					event.setCancelled(true);
				}
			} else {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(l.getWorld().getName(), id);

				if (ptc != null) {
					switch (ptc.getReason()) {
						case Clear:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedClear"));
							break;
						case Reset:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedReset"));
							break;
						case Expired:
							p.sendMessage(plugin.getUtil().C("MsgPlotLockedExpired"));
							break;
					}
					event.setCancelled(true);
				} else {
					Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot == null || !plot.isAllowed(p.getName())) {
                        if (!canbuild) {
							p.sendMessage(plugin.getUtil().C("ErrCannotBuild"));
							event.setCancelled(true);
						}
					} else {
						plot.resetExpire(plugin.getPlotMeCoreManager().getMap(l).getDaysToExpiration());
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerEggThrow(final PlayerEggThrowEvent event) {
		Location l = event.getEgg().getLocation();

		if (plugin.getPlotMeCoreManager().isPlotWorld(l)) {
			Player p = event.getPlayer();
			boolean canbuild = plugin.cPerms(p, "plotme.admin.buildanywhere");
			String id = plugin.getPlotMeCoreManager().getPlotId(l);

			if (id.equals("")) {
				if (!canbuild) {
					p.sendMessage(plugin.getUtil().C("ErrCannotUseEggs"));
					event.setHatching(false);
				}
			} else {
				Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                if (plot == null || !plot.isAllowed(p.getName())) {
                    if (!canbuild) {
						p.sendMessage(plugin.getUtil().C("ErrCannotUseEggs"));
						event.setHatching(false);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onCreatureSpawn(final CreatureSpawnEvent event) {
		Location l = event.getLocation();

		if (plugin.getPlotMeCoreManager().isPlotWorld(l)) {
			String id = plugin.getPlotMeCoreManager().getPlotId(l);

			if (!id.equals("")) {
				PlotToClear ptc = plugin.getPlotMeCoreManager().getPlotLockInfo(l.getWorld().getName(), id);

				if (ptc != null) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler()
	public void onPlotWorldLoad(final PlotWorldLoadEvent event) {
		plugin.getLogger().info("Done loading " + event.getNbPlots() + " plots for world " + event.getWorldName());
	}
}
