package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.AbstractSchematicUtil;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import com.worldcretornica.schematic.Attribute;
import com.worldcretornica.schematic.Display;
import com.worldcretornica.schematic.Ench;
import com.worldcretornica.schematic.Entity;
import com.worldcretornica.schematic.Item;
import com.worldcretornica.schematic.ItemTag;
import com.worldcretornica.schematic.Leash;
import com.worldcretornica.schematic.Modifier;
import com.worldcretornica.schematic.Pattern;
import com.worldcretornica.schematic.Pose;
import com.worldcretornica.schematic.RecordItem;
import com.worldcretornica.schematic.Schematic;
import com.worldcretornica.schematic.TileEntity;
import com.worldcretornica.schematic.jnbt.ByteArrayTag;
import com.worldcretornica.schematic.jnbt.ByteTag;
import com.worldcretornica.schematic.jnbt.CompoundTag;
import com.worldcretornica.schematic.jnbt.DoubleTag;
import com.worldcretornica.schematic.jnbt.FloatTag;
import com.worldcretornica.schematic.jnbt.IntTag;
import com.worldcretornica.schematic.jnbt.ListTag;
import com.worldcretornica.schematic.jnbt.NBTInputStream;
import com.worldcretornica.schematic.jnbt.ShortTag;
import com.worldcretornica.schematic.jnbt.StringTag;
import com.worldcretornica.schematic.jnbt.Tag;
import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Furnace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SchematicUtil extends AbstractSchematicUtil {

    private final PlotMe_Core plugin;

    public SchematicUtil(PlotMe_Core instance) {
        this.plugin = instance;
        blockPlacedLast.add(Material.SAPLING.getId());
        blockPlacedLast.add(Material.BED.getId());
        blockPlacedLast.add(Material.POWERED_RAIL.getId());
        blockPlacedLast.add(Material.DETECTOR_RAIL.getId());
        blockPlacedLast.add(Material.LONG_GRASS.getId());
        blockPlacedLast.add(Material.DEAD_BUSH.getId());
        blockPlacedLast.add(Material.PISTON_EXTENSION.getId());
        blockPlacedLast.add(Material.YELLOW_FLOWER.getId());
        blockPlacedLast.add(Material.RED_ROSE.getId());
        blockPlacedLast.add(Material.BROWN_MUSHROOM.getId());
        blockPlacedLast.add(Material.RED_MUSHROOM.getId());
        blockPlacedLast.add(Material.TORCH.getId());
        blockPlacedLast.add(Material.FIRE.getId());
        blockPlacedLast.add(Material.REDSTONE_WIRE.getId());
        blockPlacedLast.add(Material.CROPS.getId());
        blockPlacedLast.add(Material.LADDER.getId());
        blockPlacedLast.add(Material.RAILS.getId());
        blockPlacedLast.add(Material.LEVER.getId());
        blockPlacedLast.add(Material.STONE_PLATE.getId());
        blockPlacedLast.add(Material.WOOD_PLATE.getId());
        blockPlacedLast.add(Material.REDSTONE_TORCH_OFF.getId());
        blockPlacedLast.add(Material.REDSTONE_TORCH_ON.getId());
        blockPlacedLast.add(Material.STONE_BUTTON.getId());
        blockPlacedLast.add(Material.SNOW.getId());
        blockPlacedLast.add(Material.PORTAL.getId());
        blockPlacedLast.add(Material.DIODE_BLOCK_OFF.getId());
        blockPlacedLast.add(Material.DIODE_BLOCK_ON.getId());
        blockPlacedLast.add(Material.TRAP_DOOR.getId());
        blockPlacedLast.add(Material.VINE.getId());
        blockPlacedLast.add(Material.WATER_LILY.getId());
        blockPlacedLast.add(Material.NETHER_WARTS.getId());
        blockPlacedLast.add(Material.PISTON_BASE.getId());
        blockPlacedLast.add(Material.PISTON_STICKY_BASE.getId());
        blockPlacedLast.add(Material.PISTON_EXTENSION.getId());
        blockPlacedLast.add(Material.PISTON_MOVING_PIECE.getId());
        blockPlacedLast.add(Material.COCOA.getId());
        blockPlacedLast.add(Material.TRIPWIRE_HOOK.getId());
        blockPlacedLast.add(Material.TRIPWIRE.getId());
        blockPlacedLast.add(Material.FLOWER_POT.getId());
        blockPlacedLast.add(Material.CARROT.getId());
        blockPlacedLast.add(Material.POTATO.getId());
        blockPlacedLast.add(Material.WOOD_BUTTON.getId());
        blockPlacedLast.add(Material.SKULL.getId());
        blockPlacedLast.add(Material.GOLD_PLATE.getId());
        blockPlacedLast.add(Material.IRON_PLATE.getId());
        blockPlacedLast.add(Material.REDSTONE_COMPARATOR_OFF.getId());
        blockPlacedLast.add(Material.REDSTONE_COMPARATOR_ON.getId());
        blockPlacedLast.add(Material.ACTIVATOR_RAIL.getId());
    }

    @Override
    public Schematic loadCompiledSchematic(String file) {

        File pluginsFolder = plugin.getServerBridge().getDataFolder().getParentFile();
        File coreFolder = new File(pluginsFolder, "PlotSchematic");
        coreFolder.mkdirs();

        String filename = coreFolder.getAbsolutePath() + File.separatorChar + file + ".plotschematic";

        File file2 = new File(filename);

        Schematic schem = null;
        if (file2.exists()) {
            try (ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                schem = (Schematic) input.readObject();
            } catch (ClassNotFoundException ex) {
                plugin.getLogger().severe("Cannot load '" + file + ".plotschematic'");
            } catch (IOException ex) {
                plugin.getLogger().severe("Cannot read file '" + file + ".plotschematic', verify file permissions");
            }
        } else {
            plugin.getLogger().severe("File '" + file + ".plotschematic' does not exist");
        }

        return schem;
    }

    private Leash getLeash(org.bukkit.entity.Entity leashHolder) {
        Location loc = leashHolder.getLocation();
        return new Leash(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @SuppressWarnings("deprecation")
    private Item getItem(ItemStack is, Byte slot) {
        byte count = (byte) is.getAmount();
        short damage = (short) is.getData().getData();
        short itemid = (short) is.getTypeId();

        ItemTag itemtag = null;

        if (is.hasItemMeta()) {
            List<Ench> enchants = null;

            ItemMeta im = is.getItemMeta();

            Map<Enchantment, Integer> isEnchants = im.getEnchants();
            if (isEnchants != null) {
                enchants = new ArrayList<>();
                for (Enchantment ench : isEnchants.keySet()) {
                    enchants.add(new Ench((short) ench.getId(), isEnchants.get(ench).shortValue()));
                }
            }

            List<String> lore = im.getLore();
            String name = im.getDisplayName();
            Display display = new Display(name, lore);

            String author = null;
            String title = null;
            List<String> pages = null;
            if (im instanceof BookMeta) {
                BookMeta bm = (BookMeta) im;
                author = bm.getAuthor();
                title = bm.getTitle();
                pages = bm.getPages();
            }

            itemtag = new ItemTag(0, enchants, display, author, title, pages);
        }

        return new Item(count, slot, damage, itemid, itemtag);
    }

    @Override
    public void saveCompiledSchematic(Schematic schem, String file) {

        File pluginsFolder = plugin.getServerBridge().getDataFolder().getParentFile();
        File coreFolder = new File(pluginsFolder, "PlotSchematic");
        coreFolder.mkdirs();

        String filename = coreFolder.getAbsolutePath() + File.separatorChar + file + ".plotschematic";

        try (ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            output.writeObject(schem);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save " + filename + ".");
            ex.printStackTrace();
        }
    }

    @Override
    public void pasteSchematic(IWorld world, com.worldcretornica.plotme_core.api.Vector loc, Schematic schem) {
        pasteSchematicBlocks(world, loc, schem, true);
        pasteSchematicEntities(world, loc, schem);
    }

    @SuppressWarnings("deprecation")
    private void pasteSchematicBlocks(IWorld world, com.worldcretornica.plotme_core.api.Vector loc, Schematic schematic, boolean setBlock) {
        int[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        short length = schematic.getLength();
        short width = schematic.getWidth();
        short height = schematic.getHeight();

        for (int y = 0; y < height; ++y) {

            Collection<LastBlock> lastblocks = new HashSet<>();

            for (int x = 0; x < width; ++x) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;

                    IBlock block = world.getBlockAt(x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ());

                    if (!blockPlacedLast.contains(blocks[index])) {
                        try {
                            if (setBlock) {
                                block.setTypeIdAndData((short) blocks[index], blockData[index], false);
                            }
                            block.setData(blockData[index], false);
                        } catch (NullPointerException e) {
                            plugin.getLogger().info("Error pasting block : " + blocks[index] + " of data " + blockData[index]);
                        }

                    } else {
                        lastblocks.add(new LastBlock(block, blocks[index], blockData[index]));
                    }
                }
            }

            for (LastBlock lastblock : lastblocks) {
                try {
                    if (setBlock) {
                        lastblock.block.setTypeIdAndData(lastblock.id, lastblock.data, false);
                    }
                    lastblock.block.setData(lastblock.data, false);
                } catch (NullPointerException e) {
                    plugin.getLogger().info("Error pasting block : " + lastblock.id + " of data " + lastblock.data);
                }
            }

            lastblocks.clear();
        }
    }

    @SuppressWarnings("deprecation")
    private ItemStack getItemStack(Item item) {
        ItemStack is = new ItemStack(item.getId(), item.getCount(), item.getDamage());

        ItemTag itemtag = item.getTag();

        if (itemtag != null) {
            setTag(is, itemtag);
        }

        return is;
    }

    @SuppressWarnings("deprecation")
    private void setTag(ItemStack is, ItemTag itemtag) {
        List<Ench> enchants = itemtag.getEnchants();
        //Integer repaircost = itemtag.getRepairCost();
        List<String> pages = itemtag.getPages();
        String author = itemtag.getAuthor();
        String title = itemtag.getTitle();
        Display display = itemtag.getDisplay();

        ItemMeta itemmeta = is.getItemMeta();

        if (display != null) {
            List<String> lores = display.getLore();
            String name = display.getName();

            itemmeta.setLore(lores);
            itemmeta.setDisplayName(name);
        }

        if (itemmeta instanceof BookMeta) {
            BookMeta bookmeta = (BookMeta) itemmeta;
            bookmeta.setAuthor(author);
            bookmeta.setTitle(title);
            bookmeta.setPages(pages);
        }

        if (itemmeta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchantmentstoragemeta = (EnchantmentStorageMeta) itemmeta;

            for (Ench enchant : enchants) {
                enchantmentstoragemeta.addEnchant(Enchantment.getById(enchant.getId()), enchant.getLvl(), true);
            }
        }

        is.setItemMeta(itemmeta);
    }

    private org.bukkit.entity.Entity getLeash(Leash leash, ILocation loc, int originX, int originY, int originZ) {
        org.bukkit.entity.Entity ent = null;
        World world = ((BukkitWorld) loc.getWorld()).getWorld();


        int x = leash.getX() - originX;
        int y = leash.getY() - originY;
        int z = leash.getZ() - originZ;

        Location etloc = new Location(world, x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ());

        Block block = world.getBlockAt(etloc);

        if (block.getType() == Material.FENCE || block.getType() == Material.NETHER_FENCE) {
            etloc.setX(Math.floor(etloc.getX()));
            etloc.setY(Math.floor(etloc.getY()));
            etloc.setZ(Math.floor(etloc.getZ()));

            ent = world.spawnEntity(etloc, EntityType.LEASH_HITCH);

            List<org.bukkit.entity.Entity> nearbyentities = ent.getNearbyEntities(1, 1, 1);

            for (org.bukkit.entity.Entity nearby : nearbyentities) {
                if (nearby instanceof LeashHitch) {
                    if (nearby.getLocation().distance(ent.getLocation()) == 0) {
                        ent.remove();
                        return nearby;
                    }
                }
            }
        }

        return ent;
    }

    private Leash getLeash(CompoundTag leashelement) {
        Map<String, Tag> leash = leashelement.getValue();
        Integer x = getChildTag(leash, "X", IntTag.class, Integer.class);
        Integer y = getChildTag(leash, "Y", IntTag.class, Integer.class);
        Integer z = getChildTag(leash, "Z", IntTag.class, Integer.class);

        return new Leash(x, y, z);
    }

    private Modifier getModifier(CompoundTag modifierelement) {
        Map<String, Tag> modifier = modifierelement.getValue();
        Integer operation = getChildTag(modifier, "Operation", IntTag.class, Integer.class);
        Double amount = getChildTag(modifier, "Amount", DoubleTag.class, Double.class);
        String name = getChildTag(modifier, "Name", StringTag.class, String.class);

        return new Modifier(operation, amount, name);
    }

    private List<Modifier> getModifiers(Map<String, Tag> attribute) {
        List<?> modifierlist = getChildTag(attribute, "Modifiers", ListTag.class, List.class);

        if (modifierlist != null) {
            List<Modifier> modifiers = new ArrayList<>();

            for (Object modifierelement : modifierlist) {
                if (modifierelement instanceof CompoundTag) {
                    modifiers.add(getModifier((CompoundTag) modifierelement));
                }
            }

            return modifiers;
        } else {
            return null;
        }
    }

    private Item getItem(CompoundTag itemElement) {
        Map<String, Tag> item = itemElement.getValue();
        Byte count = getChildTag(item, "Count", ByteTag.class, Byte.class);
        Byte slot = getChildTag(item, "Slot", ByteTag.class, Byte.class);
        Short damage = getChildTag(item, "Damage", ShortTag.class, Short.class);
        Short itemid = getChildTag(item, "id", ShortTag.class, Short.class);

        ItemTag tag = getItemTag(item);

        return new Item(count, slot, damage, itemid, tag);
    }

    private List<Item> getItems(Map<String, Tag> entity) {
        List<?> itemsList = getChildTag(entity, "Items", ListTag.class, List.class);

        if (itemsList != null) {
            List<Item> items = new ArrayList<>();

            for (Object itemElement : itemsList) {
                if (itemElement instanceof CompoundTag) {
                    items.add(getItem((CompoundTag) itemElement));
                }
            }

            return items;
        } else {
            return null;
        }
    }

    private ItemTag getItemTag(Map<String, Tag> item) {
        CompoundTag itemtagElement = getChildTag(item, "tag", CompoundTag.class);

        if (itemtagElement != null) {
            Map<String, Tag> itemtag = itemtagElement.getValue();
            Integer repaircost = getChildTag(itemtag, "RepairCost", IntTag.class, Integer.class);
            String author = getChildTag(itemtag, "author", StringTag.class, String.class);
            String title = getChildTag(itemtag, "title", StringTag.class, String.class);
            List<String> pages = convert(getChildTag(itemtag, "pages", ListTag.class, List.class), String.class);
            Display display = getDisplay(itemtag);
            List<Ench> enchants = getEnchant(itemtag);

            return new ItemTag(repaircost, enchants, display, author, title, pages);
        } else {
            return null;
        }
    }

    private Display getDisplay(Map<String, Tag> itemtag) {
        CompoundTag displayElement = getChildTag(itemtag, "display", CompoundTag.class);

        if (displayElement != null) {
            Map<String, Tag> display = displayElement.getValue();
            String name = getChildTag(display, "Name", StringTag.class, String.class);
            List<String> lore = convert(getChildTag(display, "Lore", ListTag.class, List.class), String.class);

            return new Display(name, lore);
        } else {
            return null;
        }
    }

    private Ench getEnchant(CompoundTag enchantelement) {
        Map<String, Tag> enchant = enchantelement.getValue();
        Short id = getChildTag(enchant, "id", ShortTag.class, Short.class);
        Short lvl = getChildTag(enchant, "lvl", ShortTag.class, Short.class);
        return new Ench(id, lvl);
    }

    private List<Ench> getEnchant(Map<String, Tag> enchanttag) {
        List<?> enchantList = getChildTag(enchanttag, "ench", ListTag.class, List.class);

        if (enchantList != null) {
            List<Ench> enchants = new ArrayList<>();

            for (Object enchantelement : enchantList) {
                if (enchantelement instanceof CompoundTag) {
                    enchants.add(getEnchant((CompoundTag) enchantelement));
                }
            }

            return enchants;
        } else {
            return null;
        }
    }

    private List<Item> getEquipment(Map<String, Tag> entity) {
        List<?> equipmentlist = getChildTag(entity, "Equipment", ListTag.class, List.class);

        if (equipmentlist != null) {
            List<Item> items = new ArrayList<>();

            for (Object equipmentelement : equipmentlist) {
                if (equipmentelement instanceof CompoundTag) {
                    items.add(getItem((CompoundTag) equipmentelement));
                } else {
                    items.add(null);
                }
            }

            return items;
        } else {
            return null;
        }
    }

    private Attribute getAttribute(CompoundTag attributeelement) {
        Map<String, Tag> attribute = attributeelement.getValue();
        Double base = getChildTag(attribute, "Base", DoubleTag.class, Double.class);
        String name = getChildTag(attribute, "Name", StringTag.class, String.class);
        List<Modifier> modifiers = getModifiers(attribute);
        return new Attribute(base, name, modifiers);
    }

    private List<Attribute> getAttributes(Map<String, Tag> entity) {
        List<?> attributelist = getChildTag(entity, "Attributes", ListTag.class, List.class);

        if (attributelist != null) {
            List<Attribute> attributes = new ArrayList<>();

            for (Object attributeelement : attributelist) {
                if (attributeelement instanceof CompoundTag) {
                    attributes.add(getAttribute((CompoundTag) attributeelement));
                }
            }

            return attributes;
        } else {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    private Entity getEntity(org.bukkit.entity.Entity bukkitentity, int minX, int minY, int minZ) {

        Location entLoc = bukkitentity.getLocation();
        double x = entLoc.getX() - minX;
        double y = entLoc.getY() - minY;
        double z = entLoc.getZ() - minZ;

        byte dir = 0;
        byte direction = 0;
        byte invulnerable = 0;
        byte canpickuploot = 0;
        byte color = 0;
        byte customnamevisible = 0;
        byte persistencerequired = 0;
        byte sheared = 0;
        byte skeletontype = 0;
        byte isbaby = 0;
        byte itemrotation = 0;
        byte leashed = 0;
        byte agelocked = 0;
        byte invisible = 0;
        byte nobaseplate = 0;
        byte nogravity = 0;
        byte showarms = 0;
        byte small = 0;
        byte elder = 0;
        byte bred = 0;
        byte chestedhorse = 0;
        byte eatinghaystack = 0;
        byte hasreproduced = 0;
        byte tame = 0;
        byte facing = 0;

        Entity riding = null;

        float absorptionamount = 0;
        float healf = 0;
        float itemdropchance = 0;

        int dimension = 0;
        int portalcooldown = 0;
        int tilex = 0;
        int tiley = 0;
        int tilez = 0;
        int inlove = 0;
        int transfercooldown = 0;
        int tntfuse = 0;
        int forcedage = 0;
        int hurtbytimestamp = 0;
        int morecarrotsticks = 0;
        int rabbittype = 0;
        int disabledslots = 0;
        int temper = 0;
        int type = 0;
        int variant = 0;

        Item item = null;

        Leash leash = null;

        Pose pose = null;

        short air = 0;
        short attacktime = 0;
        short deathtime = 0;
        short health = 0;
        short hurttime = 0;
        short fuel = 0;

        String id = bukkitentity.getType().getName();
        String motive = null;
        String customname = null;
        String owneruuid = null;

        double pushx = 0;
        double pushz = 0;

        List<Float> rotation = null;
        List<Attribute> attributes = null;
        List<Float> dropchances = null;

        Item itemheld = null;
        Item feetarmor = null;
        Item legarmor = null;
        Item chestarmor = null;
        Item headarmor = null;

        List<Item> items = null;

        if (bukkitentity.getPassenger() != null) {
            riding = getEntity(bukkitentity.getPassenger(), minX, minY, minZ);
        }

        float falldistance = bukkitentity.getFallDistance();
        short fire = (short) bukkitentity.getFireTicks();
        int age = bukkitentity.getTicksLived();

        Vector velocity = bukkitentity.getVelocity();
        List<Double> motion = new ArrayList<>();
        motion.add(velocity.getX());
        motion.add(velocity.getY());
        motion.add(velocity.getZ());

        if (bukkitentity instanceof Item) {
            Item entityitem = (Item) bukkitentity;

            item = new Item(entityitem.getCount(), entityitem.getSlot(), entityitem.getDamage(), entityitem.getId(), null);
        }

        if (bukkitentity instanceof InventoryHolder) {
            InventoryHolder ih = (InventoryHolder) bukkitentity;
            Inventory inventory = ih.getInventory();
            items = new ArrayList<>();

            if (inventory.getSize() > 0) {
                for (byte slot = 0; slot < inventory.getSize(); slot++) {

                    ItemStack is = inventory.getItem(slot);
                    if (is != null) {
                        Item inventoryitem = getItem(is, slot);

                        items.add(inventoryitem);
                    }
                }
            }
        }

        if (bukkitentity instanceof ItemFrame) {
            ItemFrame itemframe = (ItemFrame) bukkitentity;
            itemrotation = (byte) itemframe.getRotation().ordinal();
            item = getItem(itemframe.getItem(), null);
        } else if (bukkitentity instanceof Painting) {
            Painting painting = (Painting) bukkitentity;
            Art art = painting.getArt();
            motive = art.name();

            switch (painting.getFacing()) {
                case EAST:
                    facing = 3;
                    break;
                case WEST:
                    facing = 1;
                    break;
                case NORTH:
                    facing = 2;
                    break;
                case SOUTH:
                    facing = 0;
                    break;
                default:
                    facing = 0;
                    break;
            }

            if (art.getBlockHeight() == 2 || art.getBlockHeight() == 4) {
                y -= 1;
            }
            if (art.getBlockWidth() > 1) {
                if (facing == 1) {
                    z -= 1;
                } else if (facing == 0) {
                    x -= 1;
                }
            }
        }

        if (bukkitentity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) bukkitentity;

            if (livingentity.getCanPickupItems()) {
                canpickuploot = (byte) 1;
            } else {
                canpickuploot = (byte) 0;
            }
            customname = livingentity.getCustomName();
            if (livingentity.isCustomNameVisible()) {
                customnamevisible = (byte) 1;
            } else {
                customnamevisible = (byte) 0;
            }
            health = (short) livingentity.getHealth();
            healf = (float) livingentity.getHealth();
            air = (short) livingentity.getRemainingAir();
            if (livingentity.getRemoveWhenFarAway()) {
                persistencerequired = (byte) 0;
            } else {
                persistencerequired = (byte) 1;
            }

            if (livingentity.isLeashed()) {
                leash = getLeash(livingentity.getLeashHolder());
            }

            EntityEquipment entityequipment = livingentity.getEquipment();

            if (entityequipment != null) {
                ItemStack isHand = entityequipment.getItemInHand();
                if (isHand != null) {
                    itemheld = getItem(isHand, null);
                }

                ItemStack isBoot = entityequipment.getBoots();
                if (isBoot != null) {
                    feetarmor = getItem(isBoot, null);
                }

                ItemStack isLeg = entityequipment.getLeggings();
                if (isLeg != null) {
                    legarmor = getItem(isLeg, null);
                }

                ItemStack isChest = entityequipment.getChestplate();
                if (isChest != null) {
                    chestarmor = getItem(isChest, null);
                }

                ItemStack isHelm = entityequipment.getHelmet();
                if (isHelm != null) {
                    headarmor = getItem(isHelm, null);
                }
            }

            if (livingentity instanceof Ageable) {
                Ageable ageable = (Ageable) livingentity;

                age = ageable.getAge();
                if (ageable.getAgeLock()) {
                    agelocked = (byte) 1;
                } else {
                    agelocked = (byte) 0;
                }
                if (ageable.isAdult()) {
                    isbaby = (byte) 0;
                } else {
                    isbaby = (byte) 1;
                }
            }

            if (livingentity instanceof Tameable) {
                Tameable tameable = (Tameable) livingentity;
                if (tameable.getOwner() != null) {
                    owneruuid = tameable.getOwner().getUniqueId().toString();
                }
                if (tameable.isTamed()) {
                    tame = (byte) 1;
                } else {
                    tame = (byte) 0;
                }
            }

            if (livingentity instanceof Skeleton) {
                Skeleton skeleton = (Skeleton) livingentity;

                switch (skeleton.getSkeletonType()) {
                    case NORMAL:
                        skeletontype = 0;
                        break;
                    case WITHER:
                        skeletontype = 1;
                        break;
                }
            } else if (livingentity instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) livingentity;

                switch (rabbit.getRabbitType()) {
                    case BROWN:
                        rabbittype = 0;
                        break;
                    case WHITE:
                        rabbittype = 1;
                        break;
                    case BLACK:
                        rabbittype = 2;
                        break;
                    case BLACK_AND_WHITE:
                        rabbittype = 3;
                        break;
                    case GOLD:
                        rabbittype = 4;
                        break;
                    case SALT_AND_PEPPER:
                        rabbittype = 5;
                        break;
                    case THE_KILLER_BUNNY:
                        rabbittype = 99;
                        break;
                }

            } else if (livingentity instanceof ArmorStand) {
                ArmorStand armorstand = (ArmorStand) livingentity;

                if (armorstand.hasArms()) {
                    showarms = (byte) 1;
                } else {
                    showarms = (byte) 0;
                }
                if (armorstand.hasBasePlate()) {
                    nobaseplate = (byte) 0;
                } else {
                    nobaseplate = (byte) 1;
                }
                if (armorstand.isVisible()) {
                    invisible = (byte) 0;
                } else {
                    invisible = (byte) 1;
                }
                if (armorstand.hasGravity()) {
                    nogravity = (byte) 0;
                } else {
                    nogravity = (byte) 1;
                }
                if (armorstand.isSmall()) {
                    small = (byte) 1;
                } else {
                    small = (byte) 0;
                }

                List<Float> body = new ArrayList<>();
                List<Float> head = new ArrayList<>();
                List<Float> leftarm = new ArrayList<>();
                List<Float> rightarm = new ArrayList<>();
                List<Float> leftleg = new ArrayList<>();
                List<Float> rightleg = new ArrayList<>();

                EulerAngle bodypose = armorstand.getBodyPose();
                body.add((float) bodypose.getX());
                body.add((float) bodypose.getY());
                body.add((float) bodypose.getZ());

                EulerAngle headpose = armorstand.getHeadPose();
                head.add((float) headpose.getX());
                head.add((float) headpose.getY());
                head.add((float) headpose.getZ());

                EulerAngle leftarmpose = armorstand.getLeftArmPose();
                leftarm.add((float) leftarmpose.getX());
                leftarm.add((float) leftarmpose.getY());
                leftarm.add((float) leftarmpose.getZ());

                EulerAngle rightarmpose = armorstand.getRightArmPose();
                rightarm.add((float) rightarmpose.getX());
                rightarm.add((float) rightarmpose.getY());
                rightarm.add((float) rightarmpose.getZ());

                EulerAngle leftlegpose = armorstand.getLeftLegPose();
                leftleg.add((float) leftlegpose.getX());
                leftleg.add((float) leftlegpose.getY());
                leftleg.add((float) leftlegpose.getZ());

                EulerAngle rightlegpose = armorstand.getRightLegPose();
                rightleg.add((float) rightlegpose.getX());
                rightleg.add((float) rightlegpose.getY());
                rightleg.add((float) rightlegpose.getZ());

                pose = new Pose(body, head, leftarm, rightarm, leftleg, rightleg);

            } else if (livingentity instanceof Guardian) {
                Guardian guardian = (Guardian) livingentity;
                if (guardian.isElder()) {
                    elder = (byte) 1;
                } else {
                    elder = (byte) 0;
                }

            } else if (livingentity instanceof Sheep) {
                Sheep sheep = (Sheep) livingentity;
                if (sheep.isSheared()) {
                    sheared = (byte) 1;
                } else {
                    sheared = (byte) 0;
                }
                color = sheep.getColor().getWoolData();

            } else if (livingentity instanceof Horse) {
                Horse horse = (Horse) livingentity;

                attributes = new ArrayList<>();
                attributes.add(new Attribute(horse.getJumpStrength(), "horse.jumpStrength", null));

                temper = horse.getDomestication();
                if (horse.isCarryingChest()) {
                    chestedhorse = (byte) 1;
                } else {
                    chestedhorse = (byte) 0;
                }

                switch (horse.getVariant()) {
                    case HORSE:
                        variant = 0;
                        break;
                    case DONKEY:
                        variant = 1;
                        break;
                    case MULE:
                        variant = 2;
                        break;
                    case UNDEAD_HORSE:
                        variant = 3;
                        break;
                    case SKELETON_HORSE:
                        variant = 4;
                        break;
                }

                switch (horse.getStyle()) {
                    case NONE:
                        type = 0;
                        break;
                    case WHITE:
                        type = 256;
                        break;
                    case WHITEFIELD:
                        type = 512;
                        break;
                    case WHITE_DOTS:
                        type = 768;
                        break;
                    case BLACK_DOTS:
                        type = 1024;
                        break;
                }

                switch (horse.getColor()) {
                    case CREAMY:
                        type += 1;
                        break;
                    case CHESTNUT:
                        type += 2;
                        break;
                    case BROWN:
                        type += 3;
                        break;
                    case BLACK:
                        type += 4;
                        break;
                    case GRAY:
                        type += 5;
                        break;
                    case DARK_BROWN:
                        type += 6;
                        break;
                    default:
                        break;
                }
            }
        }

        List<Double> positions = new ArrayList<>();

        positions.add(x);
        positions.add(y);
        positions.add(z);

        return new Entity(dir, direction, invulnerable, onground, air, fire, dimension, portalcooldown,
                tilex, tiley, tilez, falldistance, id, motive, motion, positions, rotation, canpickuploot,
                color, customnamevisible, leashed, persistencerequired, sheared, attacktime, deathtime, health,
                hurttime, age, inlove, absorptionamount, healf, customname, attributes, dropchances,
                itemheld, feetarmor, legarmor, chestarmor, headarmor,
                skeletontype, riding, leash, item, isbaby, items, transfercooldown, fuel, pushx, pushz, tntfuse,
                itemrotation, itemdropchance, agelocked, invisible, nobaseplate, nogravity, showarms, (byte) 0, small,
                elder, forcedage, hurtbytimestamp, morecarrotsticks, rabbittype, disabledslots, pose,
                bred, chestedhorse, eatinghaystack, hasreproduced, tame, temper, type, variant, owneruuid, facing);
    }

    @Override
    public Schematic loadSchematic(File file) throws IOException, IllegalArgumentException {

        Schematic schem = loadCompiledSchematic(file.getName());

        if (schem == null) {

            try (NBTInputStream nbtStream = new NBTInputStream(new FileInputStream(file))) {

                CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
                if (!"Schematic".equals(schematicTag.getName())) {
                    throw new IllegalArgumentException("Tag \"Schematic\" does not exist or is not first");
                }

                Map<String, Tag> schematic = schematicTag.getValue();
                if (!schematic.containsKey("Blocks")) {
                    throw new IllegalArgumentException("Schematic file is missing a \"Blocks\" tag");
                }

                Short width = getChildTag(schematic, "Width", ShortTag.class, Short.class);
                Short length = getChildTag(schematic, "Length", ShortTag.class, Short.class);
                Short height = getChildTag(schematic, "Height", ShortTag.class, Short.class);
                String roomauthor = getChildTag(schematic, "RoomAuthor", StringTag.class, String.class);

                Integer originx = getChildTag(schematic, "WEOriginX", IntTag.class, Integer.class);
                Integer originy = getChildTag(schematic, "WEOriginY", IntTag.class, Integer.class);
                Integer originz = getChildTag(schematic, "WEOriginZ", IntTag.class, Integer.class);

                String materials = getChildTag(schematic, "Materials", StringTag.class, String.class);
                if (!"Alpha".equals(materials)) {
                    throw new IllegalArgumentException("Schematic file is not an Alpha schematic");
                }

                byte[] rawblocks = getChildTag(schematic, "Blocks", ByteArrayTag.class, byte[].class);
                int[] blocks = new int[rawblocks.length];

                for (int ctr = 0; ctr < rawblocks.length; ctr++) {
                    int blockid = rawblocks[ctr] & 0xff;

                    //if(blockid < 0) blockid = 256 + blockid;

                    blocks[ctr] = blockid;
                }

                byte[] blockData = getChildTag(schematic, "Data", ByteArrayTag.class, byte[].class);

                List<Entity> entities = null;
                List<TileEntity> tileentities = null;

                // Load Entities
                List<?> entitiesList = getChildTag(schematic, "Entities", ListTag.class, List.class);

                if (entitiesList != null) {
                    entities = new ArrayList<>();

                    for (Object tag : entitiesList) {
                        if (tag instanceof CompoundTag) {
                            entities.add(getEntity((CompoundTag) tag));
                        }
                    }
                }

                // Load TileEntities
                List<?> tileentitiesList = getChildTag(schematic, "TileEntities", ListTag.class, List.class);

                if (tileentitiesList != null) {
                    tileentities = new ArrayList<>();

                    for (Object entityElement : tileentitiesList) {
                        if (entityElement instanceof CompoundTag) {
                            Map<String, Tag> tileentity = ((CompoundTag) entityElement).getValue();

                            Byte rot = getChildTag(tileentity, "Rot", ByteTag.class, Byte.class);
                            Byte skulltype = getChildTag(tileentity, "SkullType", ByteTag.class, Byte.class);
                            Byte note = getChildTag(tileentity, "note", ByteTag.class, Byte.class);

                            Integer x = getChildTag(tileentity, "x", IntTag.class, Integer.class);
                            Integer y = getChildTag(tileentity, "y", IntTag.class, Integer.class);
                            Integer z = getChildTag(tileentity, "z", IntTag.class, Integer.class);
                            Integer record = getChildTag(tileentity, "Record", IntTag.class, Integer.class);
                            Integer outputsignal = getChildTag(tileentity, "OutputSignal", IntTag.class, Integer.class);
                            Integer transfercooldown = getChildTag(tileentity, "TransferCooldown", IntTag.class, Integer.class);
                            Integer levels = getChildTag(tileentity, "Levels", IntTag.class, Integer.class);
                            Integer primary = getChildTag(tileentity, "Primary", IntTag.class, Integer.class);
                            Integer secondary = getChildTag(tileentity, "Secondary", IntTag.class, Integer.class);
                            Integer base = getChildTag(tileentity, "Base", IntTag.class, Integer.class);

                            RecordItem recorditem = null;

                            Short delay = getChildTag(tileentity, "Delay", ShortTag.class, Short.class);
                            Short maxnearbyentities = getChildTag(tileentity, "MaxNearbyEntities", ShortTag.class, Short.class);
                            Short maxspawndelay = getChildTag(tileentity, "MaxSpawnDelay", ShortTag.class, Short.class);
                            Short minspawndelay = getChildTag(tileentity, "MinSpawnDelay", ShortTag.class, Short.class);
                            Short requiredplayerrange = getChildTag(tileentity, "RequiredPlayerRange", ShortTag.class, Short.class);
                            Short spawncount = getChildTag(tileentity, "SpawnCount", ShortTag.class, Short.class);
                            Short spawnrange = getChildTag(tileentity, "SpawnRange", ShortTag.class, Short.class);
                            Short burntime = getChildTag(tileentity, "BurnTime", ShortTag.class, Short.class);
                            Short cooktime = getChildTag(tileentity, "CookTime", ShortTag.class, Short.class);
                            Short brewtime = getChildTag(tileentity, "BrewTime", ShortTag.class, Short.class);

                            String entityid = getChildTag(tileentity, "EntityId", StringTag.class, String.class);
                            String customname = getChildTag(tileentity, "CustomName", StringTag.class, String.class);
                            String id = getChildTag(tileentity, "id", StringTag.class, String.class);
                            String text1 = getChildTag(tileentity, "Text1", StringTag.class, String.class);
                            String text2 = getChildTag(tileentity, "Text2", StringTag.class, String.class);
                            String text3 = getChildTag(tileentity, "Text3", StringTag.class, String.class);
                            String text4 = getChildTag(tileentity, "Text4", StringTag.class, String.class);
                            String command = getChildTag(tileentity, "Command", StringTag.class, String.class);

                            List<Item> items = getItems(tileentity);
                            List<Pattern> patterns = getPatterns(tileentity);

                            if (tileentity.containsKey("RecordItem")) {
                                Map<String, Tag> recorditemtag = getChildTag(tileentity, "RecordItem", CompoundTag.class).getValue();
                                Byte count = getChildTag(recorditemtag, "Count", ByteTag.class, Byte.class);
                                Short damage = getChildTag(recorditemtag, "Damage", ShortTag.class, Short.class);
                                Short recorditemid = getChildTag(recorditemtag, "id", ShortTag.class, Short.class);
                                recorditem = new RecordItem(count, damage, recorditemid);
                            }

                            tileentities.add(new TileEntity(x, y, z, customname, id, items, rot, skulltype, delay, maxnearbyentities,
                                    maxspawndelay, minspawndelay, requiredplayerrange, spawncount, spawnrange, entityid,
                                    burntime, cooktime,
                                    text1, text2, text3, text4, note, record, recorditem, brewtime, command, outputsignal,
                                    transfercooldown, levels, primary, secondary, patterns, base));
                        }
                    }
                }

                schem =
                        new Schematic(blocks, blockData, materials, width, length, height, entities, tileentities, roomauthor, originx,
                                originy, originz);

                saveCompiledSchematic(schem, file.getName());
            }
        }

        return schem;
    }

    @SuppressWarnings("deprecation")
    private void pasteSchematicEntities(IWorld world, com.worldcretornica.plotme_core.api.Vector loc, Schematic schematic) {
        List<Entity> entities = schematic.getEntities();
        List<TileEntity> tileentities = schematic.getTileEntities();
        int originX = schematic.getOriginX();
        int originY = schematic.getOriginY();
        int originZ = schematic.getOriginZ();

        try {
            for (Entity e : entities) {
                createEntity(world, e, loc, originX, originY, originZ);
            }
        } catch (Exception e) {
            //plugin.getLogger().warning("err:" + e.getMessage());
            e.printStackTrace();
        }

        for (TileEntity te : tileentities) {

            IBlock block = world.getBlockAt(te.getX() + loc.getBlockX(), te.getY() + loc.getBlockY(), te.getZ() + loc.getBlockZ());
            List<Item> items = te.getItems();
            // Commented are unused

            // Short maxnearbyentities = te.getMaxNearbyEntities();
            // Short maxspawndelay = te.getMaxSpawnDelay();
            // Short minspawndelay = te.getMinSpawnDelay();
            // Short requiredplayerrange = te.getRequiredPlayerRange();
            // Short spawncount = te.getSpawnCount();
            // Short spawnrange = te.getSpawnRange();
            // String customname = te.getCustomName();

            // Comparator
            // Integer outputsignal = te.getOutputSignal();

            // Hopper
            // Integer transfercooldown = te.getTransferCooldown();

            // Beacon
            //Integer levels = te.getLevels();
            //Integer primary = te.getPrimary();
            //Integer secondary = te.getSecondary();

            BlockState bs = block.getState();

            if (bs instanceof Skull) {
                Skull skull = (Skull) bs;

                BlockFace bf = BlockFace.NORTH;
                byte rot = te.getRot();
                if (rot == 0) {
                    bf = BlockFace.NORTH;
                } else if (rot == 1) {
                    bf = BlockFace.NORTH_NORTH_EAST;
                } else if (rot == 2) {
                    bf = BlockFace.NORTH_EAST;
                } else if (rot == 3) {
                    bf = BlockFace.EAST_NORTH_EAST;
                } else if (rot == 4) {
                    bf = BlockFace.EAST;
                } else if (rot == 5) {
                    bf = BlockFace.EAST_SOUTH_EAST;
                } else if (rot == 6) {
                    bf = BlockFace.SOUTH_EAST;
                } else if (rot == 7) {
                    bf = BlockFace.SOUTH_SOUTH_EAST;
                } else if (rot == 8) {
                    bf = BlockFace.SOUTH;
                } else if (rot == 9) {
                    bf = BlockFace.SOUTH_SOUTH_WEST;
                } else if (rot == 10) {
                    bf = BlockFace.SOUTH_WEST;
                } else if (rot == 11) {
                    bf = BlockFace.WEST_SOUTH_WEST;
                } else if (rot == 12) {
                    bf = BlockFace.WEST;
                } else if (rot == 13) {
                    bf = BlockFace.WEST_NORTH_WEST;
                } else if (rot == 14) {
                    bf = BlockFace.NORTH_WEST;
                } else if (rot == 15) {
                    bf = BlockFace.NORTH_NORTH_WEST;
                }

                skull.setSkullType(SkullType.values()[te.getSkullType()]);
                skull.setRotation(bf);
                skull.update(true, false);
            } else if (bs instanceof CreatureSpawner) {
                CreatureSpawner spawner = (CreatureSpawner) bs;
                spawner.setCreatureTypeByName(te.getEntityId());
                spawner.setDelay(te.getDelay());
                spawner.update(true, false);
            } else if (bs instanceof Furnace) {
                Furnace furnace = (Furnace) bs;
                furnace.setBurnTime(te.getBurnTime());
                furnace.setCookTime(te.getCookTime());
                furnace.update(true, false);
            } else if (bs instanceof Sign) {
                Sign sign = (Sign) bs;
                sign.setLine(0, te.getText1());
                sign.setLine(1, te.getText2());
                sign.setLine(2, te.getText3());
                sign.setLine(3, te.getText4());
                sign.update(true, false);
            } else if (bs instanceof CommandBlock) {
                CommandBlock cb = (CommandBlock) bs;
                cb.setCommand(te.getCommand());
                cb.update(true, false);
            } else if (bs instanceof BrewingStand) {
                BrewingStand brew = (BrewingStand) bs;
                brew.setBrewingTime(te.getBrewTime());
                brew.update(true, false);
            } else if (bs instanceof Jukebox) {
                Jukebox jb = (Jukebox) bs;
                jb.setPlaying(Material.getMaterial(te.getRecord()));
                jb.update(true, false);
            } else if (bs instanceof NoteBlock) {
                NoteBlock nb = (NoteBlock) bs;
                nb.setRawNote(te.getNote());
                nb.update(true, false);
            } else if (bs instanceof Banner) {
                Banner banner = (Banner) bs;
                banner.setBaseColor(DyeColor.getByDyeData((byte) te.getBase()));
                for (Pattern pattern : te.getPatterns()) {
                    DyeColor dc = DyeColor.getByDyeData((byte) pattern.getColor());
                    PatternType pt = PatternType.getByIdentifier(pattern.getPattern());
                    org.bukkit.block.banner.Pattern pat = new org.bukkit.block.banner.Pattern(dc, pt);
                    banner.addPattern(pat);
                }
                banner.update(true, false);
            }

            if (bs instanceof InventoryHolder && items != null && !items.isEmpty()) {
                InventoryHolder ih = (InventoryHolder) bs;
                Inventory inventory = ih.getInventory();

                for (Item item : items) {

                    ItemStack is = getItemStack(item);

                    inventory.setItem(item.getSlot(), is);
                }
            }
        }
    }

    private org.bukkit.entity.Entity createEntity(IWorld world, Entity e, com.worldcretornica.plotme_core.api.Vector loc, int originX, int originY,
            int originZ) {
        try {
            @SuppressWarnings("deprecation")
            EntityType entitytype = EntityType.fromName(e.getId());
            org.bukkit.entity.Entity ent = null;

            if (entitytype != null && e.getPos() != null && e.getPos().size() == 3) {
                List<Double> positions = e.getPos();

                double x = positions.get(0) - originX;
                double y = positions.get(1) - originY;
                double z = positions.get(2) - originZ;

                //Set properties, unused are commented out

                Byte dir = e.getDir();
                //Byte direction = e.getDirection();
                //Byte invulnerable = e.getInvulnerable();
                //Byte onground = e.getOnGround();
                byte canpickuploot = e.getCanPickupLoot();
                byte color = e.getColor();
                byte customnamevisible = e.getCustomNameVisible();
                //Byte leashed = e.getLeashed();
                byte persistencerequired = e.getPersistenceRequired();
                byte sheared = e.getSheared();
                byte skeletontype = e.getSkeletonType();
                byte isbaby = e.getIsBaby();
                byte itemrotation = e.getItemRotation();
                byte agelocked = e.getAgeLocked();
                byte invisible = e.getInvisible();
                byte nobaseplate = e.getNoBasePlate();
                byte nogravity = e.getNoGravity();
                byte showarms = e.getShowArms();
                //Byte silent = e.getSilent();
                byte small = e.getSmall();
                byte elder = e.getElder();
                //Byte bred = e.getBred();
                byte chestedhorse = e.getChestedHorse();
                //Byte eatinghaystack = e.getEatingHaystack();
                //Byte hasreproduced = e.getHasReproduced();
                byte tame = e.getTame();
                byte facing = e.getFacing();

                //Double pushx = e.getPushX();
                //Double pushz = e.getPushZ();

                Entity riding = e.getRiding();

                float falldistance = e.getFallDistance();
                //float absorptionamount = e.getAbsorptionAmount();
                float healf = e.getHealF();
                //float itemdropchance = e.getItemDropChance();

                //Integer dimension = e.getDimension();
                //Integer portalcooldown = e.getPortalCooldown();
                //Integer tilex = e.getTileX();
                //Integer tiley = e.getTileY();
                //Integer tilez = e.getTileZ();
                int age = e.getAge();
                //Integer inlove = e.getInLove();
                //Integer transfercooldown = e.getTransferCooldown();
                //Integer tntfuse = e.getTNTFuse();
                //Integer forcedage = e.getForcedAge();
                int hurtbytimestamp = e.getHurtByTimestamp();
                //Integer morecarrotsticks = e.getMoreCarrotSticks();
                int rabbittype = e.getRabbitType();
                //Integer disabledslots = e.getDisabledSlots();
                int temper = e.getTemper();
                int type = e.getType();
                int variant = e.getVariant();

                Item item = e.getItem();

                Leash leash = e.getLeash();

                Pose pose = e.getPose();

                short air = e.getAir();
                short fire = e.getFire();
                //Short attacktime = e.getAttackTime();
                //Short deathtime = e.getDeathTime();
                //Short health = e.getHealth();
                //Short hurttime = e.getHurtTime();
                //Short fuel = e.getFuel()

                //String id = e.getId();
                String motive = e.getMotive();
                String customname = e.getCustomName();
                String owneruuid = e.getOwnerUUID();

                List<Double> motion = e.getMotion();
                //List<Float> rotation = e.getRotation();
                List<Attribute> attributes = e.getAttributes();
                //List<Float> dropchances = e.getDropChances();

                Item itemheld = e.getItemHeld();
                Item feetarmor = e.getFeetArmor();
                Item legarmor = e.getLegArmor();
                Item chestarmor = e.getChestArmor();
                Item headarmor = e.getHeadArmor();

                List<Item> items = e.getItems();

                Location etloc = new Location(world, x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ());

                if (entitytype == EntityType.ITEM_FRAME) {
                    etloc.setX(Math.floor(etloc.getX()));
                    etloc.setY(Math.floor(etloc.getY()));
                    etloc.setZ(Math.floor(etloc.getZ()));
                    ent = world.spawnEntity(etloc, EntityType.ITEM_FRAME);
                } else if (entitytype == EntityType.PAINTING) {
                    etloc.setX(Math.floor(etloc.getX()));
                    etloc.setY(Math.floor(etloc.getY()));
                    etloc.setZ(Math.floor(etloc.getZ()));

                    ent = world.spawnEntity(etloc, EntityType.PAINTING);
                    Painting painting = (Painting) ent;

                    BlockFace bf = BlockFace.SOUTH;

                    switch (facing) {
                        case 0:
                            bf = BlockFace.SOUTH;
                            break;
                        case 1:
                            bf = BlockFace.WEST;
                            break;
                        case 2:
                            bf = BlockFace.NORTH;
                            break;
                        case 3:
                            bf = BlockFace.EAST;
                            break;
                    }

                    painting.setArt(Art.getByName(motive), true);
                    painting.setFacingDirection(bf, true);

                } else if (entitytype == EntityType.LEASH_HITCH) {
                    /*etloc.setX(Math.floor(etloc.getX()));
                    etloc.setY(Math.floor(etloc.getY()));
                    etloc.setZ(Math.floor(etloc.getZ()));

                    ent = world.spawnEntity(etloc, entitytype);*/
                    return null;
                } else if (entitytype == EntityType.DROPPED_ITEM) {
                    if (item == null) {
                        return null;
                    } else {
                        @SuppressWarnings("deprecation")
                        ItemStack is = new ItemStack(item.getId(), item.getCount());
                        ItemTag itemtag = item.getTag();

                        if (itemtag != null) {
                            setTag(is, itemtag);
                        }

                        ent = world.dropItem(etloc, is);
                    }
                } else {
                    ent = world.spawnEntity(etloc, entitytype);
                }

                if (riding != null) {
                    ent.setPassenger(createEntity(world, riding, loc, originX, originY, originZ));
                }
                ent.setFallDistance(falldistance);
                ent.setFireTicks(fire);
                ent.setTicksLived(age);

                if (motion != null && motion.size() == 3) {
                    Vector velocity = new Vector(motion.get(0), motion.get(1), motion.get(2));
                    ent.setVelocity(velocity);
                }

                if (ent instanceof InventoryHolder) {
                    InventoryHolder ih = (InventoryHolder) ent;

                    Set<ItemStack> newitems = new HashSet<>();

                    if (items != null && !items.isEmpty()) {
                        for (Item newitem : items) {
                            ItemStack is = getItemStack(newitem);
                            ItemTag itemtag = newitem.getTag();

                            if (itemtag != null) {
                                setTag(is, itemtag);
                            }

                            newitems.add(is);
                        }
                    }

                    ih.getInventory().setContents(newitems.toArray(new ItemStack[newitems.size()]));
                }

                if (ent instanceof ItemFrame) {
                    ItemFrame itemframe = (ItemFrame) ent;
                    itemframe.setRotation(Rotation.values()[itemrotation]);

                    ItemStack is = getItemStack(item);
                    ItemTag itemtag = item.getTag();

                    if (itemtag != null) {
                        setTag(is, itemtag);
                    }

                    itemframe.setItem(is);
                }

                if (ent instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity) ent;

                    livingentity.setCanPickupItems(canpickuploot != 0);
                    if (customname != null) {
                        livingentity.setCustomName(customname);
                    }
                    livingentity.setCustomNameVisible(customnamevisible != 0);

                    livingentity.setRemainingAir(air);
                    livingentity.setRemoveWhenFarAway(persistencerequired == 0);
                    if (leash != null) {
                        org.bukkit.entity.Entity leashentity = getLeash(leash, loc, originX, originY, originZ);
                        if (leashentity != null) {
                            livingentity.setLeashHolder(leashentity);
                        }
                    }
                    livingentity.setNoDamageTicks(hurtbytimestamp);

                    if (livingentity.getMaxHealth() < healf) {
                        livingentity.setMaxHealth(healf);
                    }
                    livingentity.setHealth(healf);

                    EntityEquipment entityequipment = livingentity.getEquipment();

                    if (itemheld != null) {
                        entityequipment.setItemInHand(getItemStack(itemheld));
                    }
                    if (feetarmor != null) {
                        entityequipment.setBoots(getItemStack(feetarmor));
                    }
                    if (legarmor != null) {
                        entityequipment.setLeggings(getItemStack(legarmor));
                    }
                    if (chestarmor != null) {
                        entityequipment.setChestplate(getItemStack(chestarmor));
                    }
                    if (headarmor != null) {
                        entityequipment.setHelmet(getItemStack(headarmor));
                    }

                    if (livingentity instanceof Ageable) {
                        Ageable ageable = (Ageable) livingentity;
                        ageable.setAge(age);
                        ageable.setAgeLock(agelocked != 0);
                        if (isbaby != 0) {
                            ageable.setBaby();
                        } else {
                            ageable.setAdult();
                        }
                    }

                    if (livingentity instanceof Tameable) {
                        Tameable tameable = (Tameable) livingentity;
                        if (owneruuid != null) {
                            tameable.setOwner(Bukkit.getOfflinePlayer(UUID.fromString(owneruuid)));
                        }
                        tameable.setTamed(tame != 0);
                    }

                    if (livingentity instanceof Skeleton) {
                        Skeleton skeleton = (Skeleton) livingentity;

                        SkeletonType st;

                        if (skeletontype == 0) {
                            st = SkeletonType.NORMAL;
                        } else {
                            st = SkeletonType.WITHER;
                        }

                        skeleton.setSkeletonType(st);
                    } else if (livingentity instanceof Rabbit) {
                        Rabbit rabbit = (Rabbit) livingentity;

                        switch (rabbittype) {
                            case 0:
                                rabbit.setRabbitType(Rabbit.Type.BROWN);
                                break;
                            case 1:
                                rabbit.setRabbitType(Rabbit.Type.WHITE);
                                break;
                            case 2:
                                rabbit.setRabbitType(Rabbit.Type.BLACK);
                                break;
                            case 3:
                                rabbit.setRabbitType(Rabbit.Type.BLACK_AND_WHITE);
                                break;
                            case 4:
                                rabbit.setRabbitType(Rabbit.Type.GOLD);
                                break;
                            case 5:
                                rabbit.setRabbitType(Rabbit.Type.SALT_AND_PEPPER);
                                break;
                            case 99:
                                rabbit.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
                                break;
                        }
                    } else if (livingentity instanceof ArmorStand) {
                        ArmorStand armorstand = (ArmorStand) livingentity;
                        armorstand.setArms(showarms != 0);
                        armorstand.setBasePlate(nobaseplate == 0);
                        armorstand.setVisible(invisible == 0);
                        armorstand.setGravity(nogravity == 0);
                        armorstand.setSmall(small != 0);

                        if (pose != null) {
                            List<Float> body = pose.getBody();
                            List<Float> head = pose.getHead();
                            List<Float> leftarm = pose.getLeftArm();
                            List<Float> rightarm = pose.getRightArm();
                            List<Float> leftleg = pose.getLeftLeg();
                            List<Float> rightleg = pose.getRightLeg();

                            if (body != null && body.size() == 3) {
                                armorstand.setBodyPose(new EulerAngle(body.get(0), body.get(1), body.get(2)));
                            }
                            if (head != null && head.size() == 3) {
                                armorstand.setHeadPose(new EulerAngle(head.get(0), head.get(1), head.get(2)));
                            }
                            if (leftarm != null && leftarm.size() == 3) {
                                armorstand.setLeftArmPose(new EulerAngle(leftarm.get(0), leftarm.get(1), leftarm.get(2)));
                            }
                            if (rightarm != null && rightarm.size() == 3) {
                                armorstand.setRightArmPose(new EulerAngle(rightarm.get(0), rightarm.get(1), rightarm.get(2)));
                            }
                            if (leftleg != null && leftleg.size() == 3) {
                                armorstand.setLeftLegPose(new EulerAngle(leftleg.get(0), leftleg.get(1), leftleg.get(2)));
                            }
                            if (rightleg != null && rightleg.size() == 3) {
                                armorstand.setRightLegPose(new EulerAngle(rightleg.get(0), rightleg.get(1), rightleg.get(2)));
                            }
                        }
                    } else if (livingentity instanceof Guardian) {
                        Guardian guardian = (Guardian) livingentity;
                        guardian.setElder(elder != 0);
                    } else if (livingentity instanceof Sheep) {
                        Sheep sheep = (Sheep) livingentity;
                        sheep.setSheared(sheared != 0);
                        @SuppressWarnings("deprecation")
                        DyeColor dyecolor = DyeColor.getByWoolData(color);
                        if (dyecolor != null) {
                            sheep.setColor(dyecolor);
                        }
                    } else if (livingentity instanceof Horse) {
                        Horse horse = (Horse) livingentity;

                        if (attributes != null) {
                            for (Attribute attribute : attributes) {
                                if ("horse.jumpStrength".equalsIgnoreCase(attribute.getName())) {
                                    horse.setJumpStrength(attribute.getBase());
                                }
                            }
                        }

                        horse.setCarryingChest(chestedhorse != 0);
                        horse.setDomestication(temper);

                        switch (variant) {
                            case 0:
                                horse.setVariant(Variant.HORSE);
                                break;
                            case 1:
                                horse.setVariant(Variant.DONKEY);
                                break;
                            case 2:
                                horse.setVariant(Variant.MULE);
                                break;
                            case 3:
                                horse.setVariant(Variant.UNDEAD_HORSE);
                                break;
                            case 4:
                                horse.setVariant(Variant.SKELETON_HORSE);
                                break;
                        }

                        if (type < 256) {
                            horse.setStyle(Style.NONE);
                        } else if (type < 512) {
                            horse.setStyle(Style.WHITE);
                        } else if (type < 768) {
                            horse.setStyle(Style.WHITEFIELD);
                        } else if (type < 1024) {
                            horse.setStyle(Style.WHITE_DOTS);
                        } else {
                            horse.setStyle(Style.BLACK_DOTS);
                        }

                        switch ((int) ((double) type) % 256) {
                            case 0:
                                horse.setColor(Horse.Color.WHITE);
                                break;
                            case 1:
                                horse.setColor(Horse.Color.CREAMY);
                                break;
                            case 2:
                                horse.setColor(Horse.Color.CHESTNUT);
                                break;
                            case 3:
                                horse.setColor(Horse.Color.BROWN);
                                break;
                            case 4:
                                horse.setColor(Horse.Color.BLACK);
                                break;
                            case 5:
                                horse.setColor(Horse.Color.GRAY);
                                break;
                            case 6:
                                horse.setColor(Horse.Color.DARK_BROWN);
                                break;
                        }
                    }
                }
            }

            if (ent == null) {
                plugin.getLogger().info("null entity");
            }

            return ent;
        } catch (IllegalArgumentException ex) {
            plugin.getLogger().info("failed to create entity ");
            ex.printStackTrace();
            return null;
        }
    }

    private Entity getEntity(CompoundTag tag) {
        Map<String, Tag> entity = tag.getValue();

        Byte dir = getChildTag(entity, "Dir", ByteTag.class, Byte.class);
        Byte direction = getChildTag(entity, "Direction", ByteTag.class, Byte.class);
        Byte invulnerable = getChildTag(entity, "Invulnerable", ByteTag.class, Byte.class);
        Byte onground = getChildTag(entity, "OnGround", ByteTag.class, Byte.class);
        Byte canpickuploot = getChildTag(entity, "CanPickUpLoot", ByteTag.class, Byte.class);
        Byte color = getChildTag(entity, "Color", ByteTag.class, Byte.class);
        Byte customnamevisible = getChildTag(entity, "CustomNameVisible", ByteTag.class, Byte.class);
        Byte leashed = getChildTag(entity, "Leashed", ByteTag.class, Byte.class);
        Byte persistencerequired = getChildTag(entity, "PersistenceRequired", ByteTag.class, Byte.class);
        Byte sheared = getChildTag(entity, "Sheared", ByteTag.class, Byte.class);
        Byte skeletontype = getChildTag(entity, "SkeletonType", ByteTag.class, Byte.class);
        Byte isbaby = getChildTag(entity, "IsBaby", ByteTag.class, Byte.class);
        Byte itemrotation = getChildTag(entity, "ItemRotation", ByteTag.class, Byte.class);
        Byte agelocked = getChildTag(entity, "AgeLocked", ByteTag.class, Byte.class);
        Byte invisible = getChildTag(entity, "Invisible", ByteTag.class, Byte.class);
        Byte nobaseplate = getChildTag(entity, "NoBasePlate", ByteTag.class, Byte.class);
        Byte nogravity = getChildTag(entity, "NoGravity", ByteTag.class, Byte.class);
        Byte showarms = getChildTag(entity, "ShowArms", ByteTag.class, Byte.class);
        Byte silent = getChildTag(entity, "Silent", ByteTag.class, Byte.class);
        Byte small = getChildTag(entity, "Small", ByteTag.class, Byte.class);
        Byte elder = getChildTag(entity, "Elder", ByteTag.class, Byte.class);
        Byte bred = getChildTag(entity, "Bred", ByteTag.class, Byte.class);
        Byte chestedhorse = getChildTag(entity, "ChestHorse", ByteTag.class, Byte.class);
        Byte eatinghaystack = getChildTag(entity, "EatingHaystack", ByteTag.class, Byte.class);
        Byte hasreproduced = getChildTag(entity, "HasReproduced", ByteTag.class, Byte.class);
        Byte tame = getChildTag(entity, "Tame", ByteTag.class, Byte.class);
        Byte facing = getChildTag(entity, "Facing", ByteTag.class, Byte.class);

        Double pushx = getChildTag(entity, "PushX", DoubleTag.class, Double.class);
        Double pushz = getChildTag(entity, "PushZ", DoubleTag.class, Double.class);

        Float falldistance = getChildTag(entity, "FallDistance", FloatTag.class, Float.class);
        Float absorptionamount = getChildTag(entity, "AbsorptionAmount", FloatTag.class, Float.class);
        Float healf = getChildTag(entity, "HealF", FloatTag.class, Float.class);
        Float itemdropchance = getChildTag(entity, "ItemDropChance", FloatTag.class, Float.class);

        Integer dimension = getChildTag(entity, "Dimension", IntTag.class, Integer.class);
        Integer portalcooldown = getChildTag(entity, "PortalCooldown", IntTag.class, Integer.class);
        Integer tilex = getChildTag(entity, "TileX", IntTag.class, Integer.class);
        Integer tiley = getChildTag(entity, "TileY", IntTag.class, Integer.class);
        Integer tilez = getChildTag(entity, "TileZ", IntTag.class, Integer.class);
        Integer inlove = getChildTag(entity, "InLove", IntTag.class, Integer.class);
        Integer transfercooldown = getChildTag(entity, "TransferCooldown", IntTag.class, Integer.class);
        Integer tntfuse = getChildTag(entity, "TNTFuse", IntTag.class, Integer.class);
        Integer forcedage = getChildTag(entity, "ForcedAge", IntTag.class, Integer.class);
        Integer hurtbytimestamp = getChildTag(entity, "HurtByTimestamp", IntTag.class, Integer.class);
        Integer morecarrotsticks = getChildTag(entity, "MoreCarrotTicks", IntTag.class, Integer.class);
        Integer rabbittype = getChildTag(entity, "RabbitType", IntTag.class, Integer.class);
        Integer disabledslots = getChildTag(entity, "DisabledSlots", IntTag.class, Integer.class);
        Integer temper = getChildTag(entity, "Temper", IntTag.class, Integer.class);
        Integer type = getChildTag(entity, "Type", IntTag.class, Integer.class);
        Integer variant = getChildTag(entity, "Variant", IntTag.class, Integer.class);

        Short air = getChildTag(entity, "Air", ShortTag.class, Short.class);
        Short fire = getChildTag(entity, "Fire", ShortTag.class, Short.class);
        Short attacktime = getChildTag(entity, "AttachTime", ShortTag.class, Short.class);
        Short deathtime = getChildTag(entity, "DeathTime", ShortTag.class, Short.class);
        Short health = getChildTag(entity, "Health", ShortTag.class, Short.class);
        Short hurttime = getChildTag(entity, "HurtTime", ShortTag.class, Short.class);
        Short fuel = getChildTag(entity, "Fuel", ShortTag.class, Short.class);

        String id = getChildTag(entity, "id", StringTag.class, String.class);
        String motive = getChildTag(entity, "Motive", StringTag.class, String.class);
        String customname = getChildTag(entity, "CustomName", StringTag.class, String.class);
        String owneruuid = getChildTag(entity, "OwnerUUID", StringTag.class, String.class);

        List<Double> motion = convert(getChildTag(entity, "Motion", ListTag.class, List.class), Double.class);
        List<Double> pos = convert(getChildTag(entity, "Pos", ListTag.class, List.class), Double.class);
        List<Float> rotation = convert(getChildTag(entity, "Rotation", ListTag.class, List.class), float.class);
        List<Attribute> attributes = getAttributes(entity);
        List<Float> dropchances = convert(getChildTag(entity, "DropChances", ListTag.class, List.class), float.class);
        List<Item> equipments = getEquipment(entity);
        Item itemheld = null;
        Item feetarmor = null;
        Item legarmor = null;
        Item chestarmor = null;
        Item headarmor = null;

        if (equipments != null) {
            itemheld = equipments.get(0);
            feetarmor = equipments.get(1);
            legarmor = equipments.get(2);
            chestarmor = equipments.get(3);
            headarmor = equipments.get(4);
        }

        List<Item> items = getItems(entity);

        Integer age = null; //Handled lower
        try {
            age = getChildTag(entity, "Age", IntTag.class, Integer.class);
        } catch (IllegalArgumentException e) {
            Short shortAge = getChildTag(entity, "Age", ShortTag.class, Short.class);

            if (shortAge != null) {
                age = shortAge.intValue();
            }
        }

        CompoundTag itemtag = getChildTag(entity, "Item", CompoundTag.class);
        Item item = null;
        if (itemtag != null) {
            item = getItem(itemtag);
        }

        Entity riding = null;
        if (entity.containsKey("Riding")) {
            riding = getEntity(getChildTag(entity, "Riding", CompoundTag.class));
        }

        Leash leash = null;
        if (entity.containsKey("Leash")) {
            leash = getLeash(getChildTag(entity, "Leash", CompoundTag.class));
        }

        Pose pose = null;
        if (entity.containsKey("Pose")) {
            pose = getPose(getChildTag(entity, "Pose", CompoundTag.class));
        }

        return new Entity(dir, direction, invulnerable, onground, air, fire, dimension, portalcooldown, tilex, tiley, tilez, falldistance, id, motive,
                motion, pos, rotation,
                canpickuploot, color, customnamevisible, leashed, persistencerequired, sheared, attacktime, deathtime, health, hurttime,
                age, inlove, absorptionamount,
                healf, customname, attributes, dropchances, itemheld, feetarmor, legarmor, chestarmor, headarmor,
                skeletontype, riding, leash, item, isbaby, items, transfercooldown, fuel, pushx, pushz, tntfuse,
                itemrotation, itemdropchance, agelocked, invisible, nobaseplate, nogravity, showarms, silent, small, elder, forcedage,
                hurtbytimestamp,
                morecarrotsticks, rabbittype, disabledslots, pose,
                bred, chestedhorse, eatinghaystack, hasreproduced, tame, temper, type, variant, owneruuid,
                facing);
    }

    private Pose getPose(CompoundTag poseelement) {
        Map<String, Tag> pose = poseelement.getValue();
        List<Float> body = convert(getChildTag(pose, "body", ListTag.class, List.class), float.class);
        List<Float> head = convert(getChildTag(pose, "head", ListTag.class, List.class), Float.class);
        List<Float> leftarm = convert(getChildTag(pose, "leftarm", ListTag.class, List.class), Float.class);
        List<Float> rightarm = convert(getChildTag(pose, "rightarm", ListTag.class, List.class), Float.class);
        List<Float> leftleg = convert(getChildTag(pose, "leftleg", ListTag.class, List.class), Float.class);
        List<Float> rightleg = convert(getChildTag(pose, "rightleg", ListTag.class, List.class), Float.class);

        return new Pose(body, head, leftarm, rightarm, leftleg, rightleg);
    }

    private List<Pattern> getPatterns(Map<String, Tag> entity) {
        List<?> patternsList = getChildTag(entity, "Patterns", ListTag.class, List.class);

        if (patternsList != null) {
            List<Pattern> patterns = new ArrayList<>();

            for (Object patternElement : patternsList) {
                if (patternElement instanceof CompoundTag) {
                    patterns.add(getPattern((CompoundTag) patternElement));
                }
            }

            return patterns;
        } else {
            return null;
        }
    }

    private Pattern getPattern(CompoundTag patternElement) {
        Map<String, Tag> patternmap = patternElement.getValue();
        Integer color = getChildTag(patternmap, "Color", IntTag.class, Integer.class);
        String pattern = getChildTag(patternmap, "Pattern", StringTag.class, String.class);

        return new Pattern(color, pattern);
    }

    @SuppressWarnings("deprecation")
    public Schematic createCompiledSchematic(IWorld world, com.worldcretornica.plotme_core.api.Vector loc1, com.worldcretornica.plotme_core.api
            .Vector loc2) {

        Schematic schem;

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        short length = (short) (maxZ - minZ + 1);
        short width = (short) (maxX - minX + 1);
        short height = (short) (maxY - minY + 1);

        int[] blocks = new int[length * width * height];
        byte[] blockData = new byte[length * width * height];

        List<Entity> entities = new ArrayList<>();
        List<TileEntity> tileentities = new ArrayList<>();

        for (int x = 0; x < width; ++x) {
            for (int z = 0; z < length; ++z) {
                for (int y = 0; y < height; ++y) {
                    int index = y * width * length + z * width + x;

                    IBlock block = world.getBlockAt(x + minX, y + minY, z + minZ);

                    blocks[index] = block.getTypeId();
                    blockData[index] = block.getData();

                    boolean isTileEntity = false;

                    BlockState bs = block.getState();

                    byte rot = 0;
                    byte skulltype = 0;
                    byte note = 0;

                    int record = 0;
                    int outputsignal = 0;
                    int transfercooldown = 0;
                    int levels = 0;
                    int primary = 0;
                    int secondary = 0;
                    int base = 0;

                    RecordItem recorditem = null;

                    short delay = 0;
                    short maxnearbyentities = 0;
                    short maxspawndelay = 0;
                    short minspawndelay = 0;
                    short requiredplayerrange = 0;
                    short spawncount = 0;
                    short spawnrange = 0;
                    short burntime = 0;
                    short cooktime = 0;
                    short brewtime = 0;

                    String entityid = null;
                    String customname = null;
                    String id = null;
                    String text1 = null;
                    String text2 = null;
                    String text3 = null;
                    String text4 = null;
                    String command = null;

                    List<Item> items = null;
                    List<Pattern> patterns = null;

                    if (bs instanceof Skull) {
                        Skull skull = (Skull) bs;

                        switch (skull.getRotation()) {
                            case NORTH:
                                rot = 0;
                                break;
                            case NORTH_NORTH_EAST:
                                rot = 1;
                                break;
                            case UP:
                                break;
                            case DOWN:
                                break;
                            case NORTH_EAST:
                                rot = 2;
                                break;
                            case EAST_NORTH_EAST:
                                rot = 3;
                                break;
                            case EAST:
                                rot = 4;
                                break;
                            case EAST_SOUTH_EAST:
                                rot = 5;
                                break;
                            case SOUTH_EAST:
                                rot = 6;
                                break;
                            case SOUTH_SOUTH_EAST:
                                rot = 7;
                                break;
                            case SOUTH:
                                rot = 8;
                                break;
                            case SOUTH_SOUTH_WEST:
                                rot = 9;
                                break;
                            case SOUTH_WEST:
                                rot = 10;
                                break;
                            case WEST_SOUTH_WEST:
                                rot = 11;
                                break;
                            case WEST:
                                rot = 12;
                                break;
                            case WEST_NORTH_WEST:
                                rot = 13;
                                break;
                            case NORTH_WEST:
                                rot = 14;
                                break;
                            case NORTH_NORTH_WEST:
                                rot = 15;
                                break;
                            case SELF:
                                break;
                            default:
                                rot = 0;
                                break;
                        }

                        skulltype = (byte) skull.getSkullType().ordinal();

                        isTileEntity = true;
                    }

                    if (bs instanceof CreatureSpawner) {
                        CreatureSpawner spawner = (CreatureSpawner) bs;

                        entityid = spawner.getCreatureTypeName();
                        delay = (short) spawner.getDelay();

                        isTileEntity = true;
                    }

                    if (bs instanceof Furnace) {
                        Furnace furnace = (Furnace) bs;

                        burntime = furnace.getBurnTime();
                        cooktime = furnace.getCookTime();

                        isTileEntity = true;
                    }

                    if (bs instanceof Sign) {
                        Sign sign = (Sign) bs;
                        text1 = sign.getLine(0);
                        text2 = sign.getLine(1);
                        text3 = sign.getLine(2);
                        text4 = sign.getLine(3);

                        isTileEntity = true;
                    }

                    if (bs instanceof CommandBlock) {
                        CommandBlock cb = (CommandBlock) bs;

                        command = cb.getCommand();

                        isTileEntity = true;
                    }

                    if (bs instanceof BrewingStand) {
                        BrewingStand brew = (BrewingStand) bs;

                        brewtime = (short) brew.getBrewingTime();

                        isTileEntity = true;
                    }

                    if (bs instanceof Jukebox) {
                        Jukebox jb = (Jukebox) bs;

                        record = jb.getPlaying().getId();

                        isTileEntity = true;
                    }

                    if (bs instanceof NoteBlock) {
                        NoteBlock nb = (NoteBlock) bs;

                        note = nb.getRawNote();

                        isTileEntity = true;
                    }

                    if (bs instanceof InventoryHolder) {

                        InventoryHolder ih = (InventoryHolder) bs;
                        Inventory inventory = ih.getInventory();

                        if (inventory.getSize() > 0) {
                            items = new ArrayList<>();

                            for (byte slot = 0; slot < inventory.getSize(); slot++) {
                                ItemStack is = inventory.getItem(slot);
                                if (is != null) {
                                    Item item = getItem(is, slot);
                                    items.add(item);
                                }
                            }
                        }

                        isTileEntity = true;
                    }

                    if (bs instanceof Banner) {
                        Banner banner = (Banner) bs;
                        patterns = new ArrayList<>();
                        base = (int) banner.getBaseColor().getDyeData();

                        for (org.bukkit.block.banner.Pattern pattern : banner.getPatterns()) {
                            patterns.add(new Pattern((int) pattern.getColor().getDyeData(), pattern.getPattern().getIdentifier()));
                        }

                        isTileEntity = true;
                    }

                    if (isTileEntity) {
                        TileEntity te = new TileEntity(x, y, z, customname, id, items, rot, skulltype, delay, maxnearbyentities, maxspawndelay,
                                minspawndelay, requiredplayerrange, spawncount, spawnrange, entityid, burntime, cooktime,
                                text1, text2, text3, text4, note, record, recorditem, brewtime, command, outputsignal,
                                transfercooldown, levels, primary, secondary, patterns, base);
                        tileentities.add(te);
                    }
                }
            }
        }

        for (org.bukkit.entity.Entity bukkitentity : world.getEntities()) {
            Location entloc = bukkitentity.getLocation();

            if (entloc.getX() >= minX && entloc.getX() <= maxX &&
                    entloc.getY() >= minY && entloc.getY() <= maxY &&
                    entloc.getZ() >= minZ && entloc.getZ() <= maxZ &&
                    !(bukkitentity instanceof Player)) {
                entities.add(getEntity(bukkitentity, minX, minY, minZ));
            }
        }

        schem = new Schematic(blocks, blockData, "Alpha", width, length, height, entities, tileentities, "", 0, 0, 0);

        return schem;
    }

    private class LastBlock {

        final Block block;
        final int id;
        final byte data;

        public LastBlock(IBlock block, int id, byte data) {
            this.block = block;
            this.id = id;
            this.data = data;
        }
    }
}
