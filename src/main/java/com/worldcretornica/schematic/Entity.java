package com.worldcretornica.schematic;

import java.util.List;

public class Entity extends AbstractSchematicElement {

    private static final long serialVersionUID = 3315103410018232693L;

    private final byte dir;
    private final byte direction;
    private final byte invulnerable;
    private final byte onground;
    private final byte canpickuploot;
    private final byte color;
    private final byte customnamevisible;
    private final byte leashed;
    private final byte persistencerequired;
    private final byte sheared;
    private final byte skeletontype;
    private final byte isbaby;
    private final byte itemrotation;
    private final byte agelocked;
    private final byte invisible;
    private final byte nobaseplate;
    private final byte nogravity;
    private final byte showarms;
    private final byte silent;
    private final byte small;
    private final byte elder;
    private final byte bred;
    private final byte chestedhorse;
    private final byte eatinghaystack;
    private final byte hasreproduced;
    private final byte tame;
    private final byte facing;

    private final double pushx;
    private final double pushz;

    private final Entity riding;

    private final float falldistance;
    private final float absorptionamount;
    private final float healf;
    private final float itemdropchance;

    private final int dimension;
    private final int portalcooldown;
    private final int tilex;
    private final int tiley;
    private final int tilez;
    private final int transfercooldown;
    private final int age;
    private final int inlove;
    private final int tntfuse;
    private final int forcedage;
    private final int hurtbytimestamp;
    private final int morecarrotsticks;
    private final int rabbittype;
    private final int disabledslots;
    private final int temper;
    private final int type;
    private final int variant;

    private final Item item;

    private final Leash leash;

    private final short attacktime;
    private final short deathtime;
    private final short health;
    private final short hurttime;
    private final short air;
    private final short fire;
    private final short fuel;

    private final String id;
    private final String motive;
    private final String customname;
    private final String owneruuid;

    private final List<Double> motion;
    private final List<Double> pos;
    private final List<Float> rotation;
    private final List<Attribute> attributes;
    private final List<Float> dropchances;
    private final Item itemheld;
    private final Item feetarmor;
    private final Item headarmor;
    private final Item chestarmor;
    private final Item legarmor;
    private final List<Item> items;

    private final Pose pose;

    public Entity(byte dir, byte direction, byte invulnerable, byte onground, short air, short fire, int dimension, int portalcooldown,
            int tilex, int tiley, int tilez, float falldistance, String id, String motive, List<Double> motion, List<Double> pos,
            List<Float> rotation,
            byte canpickuploot, byte color, byte customnamevisible, byte leashed, byte persistencerequired, byte sheared, short attacktime,
            short deathtime, short health, short hurttime, int age, int inlove, float absorptionamount, float healf, String customname,
            List<Attribute> attributes,
            List<Float> dropchances, Item itemheld, Item feetarmor, Item headarmor, Item chestarmor, Item legarmor, byte skeletontype, Entity riding,
            Leash leash, Item item, byte isbaby, List<Item> items, int transfercooldown, short fuel, double pushx, double pushz, int tntfuse,
            byte itemrotation,
            float itemdropchance, byte agelocked, byte invisible, byte nobaseplate, byte nogravity, byte showarms, byte silent, byte small,
            byte elder, int forcedage, int hurtbytimestamp, int morecarrotsticks, int rabbittype, int disabledslots,
            Pose pose, byte bred, byte chestedhorse, byte eatinghaystack, byte hasreproduced, byte tame, int temper, int type,
            int variant, String owneruuid, byte facing) {
        this.dir = dir;
        this.direction = direction;
        this.invulnerable = invulnerable;
        this.onground = onground;
        this.air = air;
        this.fire = fire;
        this.dimension = dimension;
        this.portalcooldown = portalcooldown;
        this.tilex = tilex;
        this.tiley = tiley;
        this.tilez = tilez;
        this.falldistance = falldistance;
        this.id = id;
        this.motive = motive;
        this.motion = motion;
        this.pos = pos;
        this.rotation = rotation;
        this.canpickuploot = canpickuploot;
        this.color = color;
        this.customnamevisible = customnamevisible;
        this.leashed = leashed;
        this.persistencerequired = persistencerequired;
        this.sheared = sheared;
        this.attacktime = attacktime;
        this.deathtime = deathtime;
        this.health = health;
        this.hurttime = hurttime;
        this.age = age;
        this.inlove = inlove;
        this.absorptionamount = absorptionamount;
        this.healf = healf;
        this.customname = customname;
        this.attributes = attributes;
        this.dropchances = dropchances;
        this.itemheld = itemheld;
        this.headarmor = headarmor;
        this.legarmor = legarmor;
        this.chestarmor = chestarmor;
        this.feetarmor = feetarmor;
        this.skeletontype = skeletontype;
        this.riding = riding;
        this.leash = leash;
        this.item = item;
        this.isbaby = isbaby;
        this.items = items;
        this.transfercooldown = transfercooldown;
        this.fuel = fuel;
        this.pushx = pushx;
        this.pushz = pushz;
        this.tntfuse = tntfuse;
        this.itemrotation = itemrotation;
        this.itemdropchance = itemdropchance;
        this.agelocked = agelocked;
        this.invisible = invisible;
        this.nobaseplate = nobaseplate;
        this.nogravity = nogravity;
        this.showarms = showarms;
        this.silent = silent;
        this.small = small;
        this.elder = elder;
        this.forcedage = forcedage;
        this.hurtbytimestamp = hurtbytimestamp;
        this.morecarrotsticks = morecarrotsticks;
        this.rabbittype = rabbittype;
        this.disabledslots = disabledslots;
        this.pose = pose;
        this.bred = bred;
        this.chestedhorse = chestedhorse;
        this.eatinghaystack = eatinghaystack;
        this.hasreproduced = hasreproduced;
        this.tame = tame;
        this.temper = temper;
        this.type = type;
        this.variant = variant;
        this.owneruuid = owneruuid;
        this.facing = facing;
    }

    public byte getDir() {
        return dir;
    }

    public byte getDirection() {
        return direction;
    }

    public byte getInvulnerable() {
        return invulnerable;
    }

    public byte getOnGround() {
        return onground;
    }

    public byte getCanPickupLoot() {
        return canpickuploot;
    }

    public byte getColor() {
        return color;
    }

    public byte getCustomNameVisible() {
        return customnamevisible;
    }

    public byte getLeashed() {
        return leashed;
    }

    public byte getPersistenceRequired() {
        return persistencerequired;
    }

    public byte getSheared() {
        return sheared;
    }

    public byte getSkeletonType() {
        return skeletontype;
    }

    public byte getIsBaby() {
        return isbaby;
    }

    public byte getItemRotation() {
        return itemrotation;
    }

    public byte getAgeLocked() {
        return agelocked;
    }

    public byte getInvisible() {
        return invisible;
    }

    public byte getNoBasePlate() {
        return nobaseplate;
    }

    public byte getNoGravity() {
        return nogravity;
    }

    public byte getShowArms() {
        return showarms;
    }

    public byte getSilent() {
        return silent;
    }

    public byte getSmall() {
        return small;
    }

    public byte getElder() {
        return elder;
    }

    public byte getBred() {
        return bred;
    }

    public byte getChestedHorse() {
        return chestedhorse;
    }

    public byte getEatingHaystack() {
        return eatinghaystack;
    }

    public byte getHasReproduced() {
        return hasreproduced;
    }

    public byte getTame() {
        return tame;
    }

    public byte getFacing() {
        return facing;
    }

    public double getPushX() {
        return pushx;
    }

    public double getPushZ() {
        return pushz;
    }

    public Entity getRiding() {
        return riding;
    }

    public float getFallDistance() {
        return falldistance;
    }

    public float getAbsorptionAmount() {
        return absorptionamount;
    }

    public float getHealF() {
        return healf;
    }

    public float getItemDropChance() {
        return itemdropchance;
    }

    public int getDimension() {
        return dimension;
    }

    public int getPortalCooldown() {
        return portalcooldown;
    }

    public int getTileX() {
        return tilex;
    }

    public int getTileY() {
        return tiley;
    }

    public int getTileZ() {
        return tilez;
    }

    public int getAge() {
        return age;
    }

    public int getInLove() {
        return inlove;
    }

    public int getTransferCooldown() {
        return transfercooldown;
    }

    public int getTNTFuse() {
        return tntfuse;
    }

    public int getForcedAge() {
        return forcedage;
    }

    public int getHurtByTimestamp() {
        return hurtbytimestamp;
    }

    public int getMoreCarrotSticks() {
        return morecarrotsticks;
    }

    public int getRabbitType() {
        return rabbittype;
    }

    public int getDisabledSlots() {
        return disabledslots;
    }

    public int getTemper() {
        return temper;
    }

    public int getType() {
        return type;
    }

    public int getVariant() {
        return variant;
    }

    public Item getItem() {
        return item;
    }

    public Item getItemHeld() {
        return itemheld;
    }

    public Item getFeetArmor() {
        return feetarmor;
    }

    public Item getLegArmor() {
        return legarmor;
    }

    public Item getHeadArmor() {
        return headarmor;
    }

    public Item getChestArmor() {
        return chestarmor;
    }

    public Leash getLeash() {
        return leash;
    }

    public Pose getPose() {
        return pose;
    }

    public short getAir() {
        return air;
    }

    public short getFire() {
        return fire;
    }

    public short getAttackTime() {
        return attacktime;
    }

    public short getDeathTime() {
        return deathtime;
    }

    public short getHealth() {
        return health;
    }

    public short getHurtTime() {
        return hurttime;
    }

    public short getFuel() {
        return fuel;
    }

    public String getId() {
        return id;
    }

    public String getMotive() {
        return motive;
    }

    public String getCustomName() {
        return customname;
    }

    public String getOwnerUUID() {
        return owneruuid;
    }

    public List<Double> getMotion() {
        return motion;
    }

    public List<Double> getPos() {
        return pos;
    }

    public List<Float> getRotation() {
        return rotation;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Float> getDropChances() {
        return dropchances;
    }

    public List<Item> getItems() {
        return items;
    }

    public String toString() {
        return "{" + this.getClass().getName() +
                ": dir=" + Sanitize(dir) +
                ", direction=" + Sanitize(direction) +
                ", invulnerable=" + Sanitize(invulnerable) +
                ", onground=" + Sanitize(onground) +
                ", air=" + Sanitize(air) +
                ", fire=" + Sanitize(fire) +
                ", dimension=" + Sanitize(dimension) +
                ", portalcooldown=" + Sanitize(portalcooldown) +
                ", tilex=" + Sanitize(tilex) +
                ", tiley=" + Sanitize(tiley) +
                ", tilez=" + Sanitize(tilez) +
                ", falldistance=" + Sanitize(falldistance) +
                ", id=" + Sanitize(id) +
                ", motive=" + Sanitize(motive) +
                ", motion=" + Sanitize(motion) +
                ", pos=" + Sanitize(pos) +
                ", rotation=" + Sanitize(rotation) +
                ", canpickuploot=" + Sanitize(canpickuploot) +
                ", color=" + Sanitize(color) +
                ", customnamevisible=" + Sanitize(customnamevisible) +
                ", leashed=" + Sanitize(leashed) +
                ", persistencerequired=" + Sanitize(persistencerequired) +
                ", sheared=" + Sanitize(sheared) +
                ", attacktime=" + Sanitize(attacktime) +
                ", deathtime=" + Sanitize(deathtime) +
                ", health=" + Sanitize(health) +
                ", hurttime=" + Sanitize(hurttime) +
                ", age=" + Sanitize(age) +
                ", inlove=" + Sanitize(inlove) +
                ", absorptionamount=" + Sanitize(absorptionamount) +
                ", healf=" + Sanitize(healf) +
                ", customname=" + Sanitize(customname) +
                ", attributes=" + Sanitize(attributes) +
                ", dropchances=" + Sanitize(dropchances) +
                ", itemheld=" + Sanitize(itemheld) +
                ", headarmor=" + Sanitize(headarmor) +
                ", chestarmor=" + Sanitize(chestarmor) +
                ", legarmor=" + Sanitize(legarmor) +
                ", feetarmor=" + Sanitize(feetarmor) +
                ", skeletontype=" + Sanitize(skeletontype) +
                ", riding=" + Sanitize(riding) +
                ", leash=" + Sanitize(leash) +
                ", item=" + Sanitize(item) +
                ", isbaby=" + Sanitize(isbaby) +
                ", items=" + Sanitize(items) +
                ", transfercooldown=" + Sanitize(transfercooldown) +
                ", fuel=" + Sanitize(fuel) +
                ", pushx=" + Sanitize(pushx) +
                ", pushz=" + Sanitize(pushz) +
                ", tntfuse=" + Sanitize(tntfuse) +
                ", itemrotation=" + Sanitize(itemrotation) +
                ", itemdropchance=" + Sanitize(itemdropchance) +
                ", agelocked=" + Sanitize(agelocked) +
                ", invisible=" + Sanitize(invisible) +
                ", nobaseplate=" + Sanitize(nobaseplate) +
                ", nogravity=" + Sanitize(nogravity) +
                ", showarms=" + Sanitize(showarms) +
                ", silent=" + Sanitize(silent) +
                ", small=" + Sanitize(small) +
                ", elder=" + Sanitize(elder) +
                ", forcedage=" + Sanitize(forcedage) +
                ", hurtbytimestamp=" + Sanitize(hurtbytimestamp) +
                ", morecarrotsticks=" + Sanitize(morecarrotsticks) +
                ", rabbittype=" + Sanitize(rabbittype) +
                ", disabledslots=" + Sanitize(disabledslots) +
                ", pose=" + Sanitize(pose) +
                ", bred=" + Sanitize(bred) +
                ", chestedhorse=" + Sanitize(chestedhorse) +
                ", eatinghaystack=" + Sanitize(eatinghaystack) +
                ", hasreproduced=" + Sanitize(hasreproduced) +
                ", tame=" + Sanitize(tame) +
                ", temper=" + Sanitize(temper) +
                ", type=" + Sanitize(type) +
                ", variant=" + Sanitize(variant) +
                ", owneruuid=" + Sanitize(owneruuid) +
                ", facing=" + Sanitize(facing) + "}";
    }
}
