package com.worldcretornica.schematic;

import java.util.List;

public class Entity extends AbstractSchematicElement {

    private static final long serialVersionUID = 3315103410018232693L;

    private final Byte dir;
    private final Byte direction;
    private final Byte invulnerable;
    private final Byte onground;
    private final Byte canpickuploot;
    private final Byte color;
    private final Byte customnamevisible;
    private final Byte leashed;
    private final Byte persistencerequired;
    private final Byte sheared;
    private final Byte skeletontype;
    private final Byte isbaby;
    private final Byte itemrotation;
    private final Byte agelocked;
    private final Byte invisible;
    private final Byte nobaseplate;
    private final Byte nogravity;
    private final Byte showarms;
    private final Byte silent;
    private final Byte small;
    private final Byte elder;
    private final Byte bred;
    private final Byte chestedhorse;
    private final Byte eatinghaystack;
    private final Byte hasreproduced;
    private final Byte tame;
    private final Byte facing;

    private final Double pushx;
    private final Double pushz;

    private final Entity riding;

    private final Float falldistance;
    private final Float absorptionamount;
    private final Float healf;
    private final Float itemdropchance;

    private final Integer dimension;
    private final Integer portalcooldown;
    private final Integer tilex;
    private final Integer tiley;
    private final Integer tilez;
    private final Integer transfercooldown;
    private final Integer age;
    private final Integer inlove;
    private final Integer tntfuse;
    private final Integer forcedage;
    private final Integer hurtbytimestamp;
    private final Integer morecarrotsticks;
    private final Integer rabbittype;
    private final Integer disabledslots;
    private final Integer temper;
    private final Integer type;
    private final Integer variant;

    private final Item item;

    private final Leash leash;

    private final Short attacktime;
    private final Short deathtime;
    private final Short health;
    private final Short hurttime;
    private final Short air;
    private final Short fire;
    private final Short fuel;

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

    public Entity(Byte dir, Byte direction, Byte invulnerable, Byte onground, Short air, Short fire, Integer dimension, Integer portalcooldown, Integer tilex, 
            Integer tiley, Integer tilez, Float falldistance, String id, String motive, List<Double> motion, List<Double> pos, List<Float> rotation,
            Byte canpickuploot, Byte color, Byte customnamevisible, Byte leashed, Byte persistencerequired, Byte sheared, Short attacktime, Short deathtime, 
            Short health, Short hurttime, Integer age, Integer inlove, Float absorptionamount, Float healf, String customname, List<Attribute> attributes, 
            List<Float> dropchances, Item itemheld, Item feetarmor, Item headarmor, Item chestarmor, Item legarmor, Byte skeletontype, Entity riding, Leash leash, 
            Item item, Byte isbaby, List<Item> items, Integer transfercooldown, Short fuel, Double pushx, Double pushz, Integer tntfuse, Byte itemrotation,
            Float itemdropchance, Byte agelocked, Byte invisible, Byte nobaseplate, Byte nogravity, Byte showarms, Byte silent, Byte small,
            Byte elder, Integer forcedage, Integer hurtbytimestamp, Integer morecarrotsticks, Integer rabbittype, Integer disabledslots,
            Pose pose, Byte bred, Byte chestedhorse, Byte eatinghaystack, Byte hasreproduced, Byte tame, Integer temper, Integer type, Integer variant,
            String owneruuid, Byte facing) {
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

    public Byte getDir() { return dir; }
    public Byte getDirection() { return direction; }
    public Byte getInvulnerable() { return invulnerable; }
    public Byte getOnGround() { return onground; }
    public Byte getCanPickupLoot() { return canpickuploot; }
    public Byte getColor() { return color; }
    public Byte getCustomNameVisible() { return customnamevisible; }
    public Byte getLeashed() { return leashed; }
    public Byte getPersistenceRequired() { return persistencerequired; }
    public Byte getSheared() { return sheared; }
    public Byte getSkeletonType() { return skeletontype; }
    public Byte getIsBaby() { return isbaby; }
    public Byte getItemRotation() { return itemrotation; }
    public Byte getAgeLocked() { return agelocked; }
    public Byte getInvisible() { return invisible; }
    public Byte getNoBasePlate() { return nobaseplate; }
    public Byte getNoGravity() { return nogravity; }
    public Byte getShowArms() { return showarms; }
    public Byte getSilent() { return silent; }
    public Byte getSmall() { return small; }
    public Byte getElder() { return elder; }
    public Byte getBred() { return bred; }
    public Byte getChestedHorse() { return chestedhorse; }
    public Byte getEatingHaystack() { return eatinghaystack; }
    public Byte getHasReproduced() { return hasreproduced; }
    public Byte getTame() { return tame; }
    public Byte getFacing() { return facing; }

    public Double getPushX() { return pushx; }
    public Double getPushZ() { return pushz; }

    public Entity getRiding() { return riding; }

    public Float getFallDistance() { return falldistance; }
    public Float getAbsorptionAmount() { return absorptionamount; }
    public Float getHealF() { return healf; }
    public Float getItemDropChance() { return itemdropchance; }

    public Integer getDimension() { return dimension; }
    public Integer getPortalCooldown() { return portalcooldown; }
    public Integer getTileX() { return tilex; }
    public Integer getTileY() { return tiley; }
    public Integer getTileZ() { return tilez; }
    public Integer getAge() { return age; }
    public Integer getInLove() { return inlove; }
    public Integer getTransferCooldown() { return transfercooldown; }
    public Integer getTNTFuse() { return tntfuse; }
    public Integer getForcedAge() { return forcedage; }
    public Integer getHurtByTimestamp() { return hurtbytimestamp; }
    public Integer getMoreCarrotSticks() { return morecarrotsticks; }
    public Integer getRabbitType() { return rabbittype; }
    public Integer getDisabledSlots() { return disabledslots; }
    public Integer getTemper() { return temper; }
    public Integer getType() { return type; }
    public Integer getVariant() { return variant; }

    public Item getItem() { return item; }
    
    public Item getItemHeld() { return itemheld; }
    public Item getFeetArmor() { return feetarmor; }
    public Item getLegArmor() { return legarmor; }
    public Item getHeadArmor() { return headarmor; }
    public Item getChestArmor() { return chestarmor; }

    public Leash getLeash() { return leash; }
    
    public Pose getPose() { return pose; }

    public Short getAir() { return air; }
    public Short getFire() { return fire; }
    public Short getAttackTime() { return attacktime; }
    public Short getDeathTime() { return deathtime; }
    public Short getHealth() { return health; }
    public Short getHurtTime() { return hurttime; }
    public Short getFuel() { return fuel; }

    public String getId() { return id; }
    public String getMotive() { return motive; }
    public String getCustomName() { return customname; }
    public String getOwnerUUID() { return owneruuid; }

    public List<Double> getMotion() { return motion; }
    public List<Double> getPos() { return pos; }
    public List<Float> getRotation() { return rotation; }
    public List<Attribute> getAttributes() { return attributes; }
    public List<Float> getDropChances() { return dropchances; }
    public List<Item> getItems() { return items; }
    
    public String toString()
    {
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
