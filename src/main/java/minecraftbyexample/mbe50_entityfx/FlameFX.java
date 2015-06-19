///*
// ** 2012 Januar 1
// **
// ** The author disclaims copyright to this source code.  In place of
// ** a legal notice, here is a blessing:
// **    May you do good and not evil.
// **    May you find forgiveness for yourself and forgive others.
// **    May you share freely, never taking more than you give.
// */
//
//package minecraftbyexample.mbe50_entityfx;
//
//import info.ata4.minecraft.MathF;
//import java.util.List;
//import net.minecraft.src.*;
//
///**
// * Fluid flame particle effect.
// *
// * @author Nico Bergemann <barracuda415 at yahoo.de>
// */
//public class FlameFX extends EntityFX {
//
//    private Entity owner;
//
//    protected float particleMaxSize;
//
//    public float smokeChance = 0.1f;
//    public float largeSmokeChance = 0.3f;
//    public boolean igniteBlocks = true;
//    public boolean igniteEntities = true;
//    public int igniteDamage = 2;
//    public int igniteDuration = 5;
//    public float igniteChance = 0.12f;
//
//    public FlameFX(World world, double x, double y, double z, double a, double b, double c) {
//        this(world, x, y, z, a, b, c, 4, 40);
//    }
//
//    public FlameFX(World world, double x, double y, double z, double a, double b, double c, float size, int age) {
//        super(world, x, y, z, a, b, c);
//
//        motionX = a * 0.1 + rand.nextGaussian() * 0.01;
//        motionY = b * 0.1 + rand.nextGaussian() * 0.01;
//        motionZ = c * 0.1 + rand.nextGaussian() * 0.01;
//
//        Block block = Block.fire;
//
//        setParticleTextureIndex(block.getBlockTextureFromSideAndMetadata(0, 0));
//        particleGravity = block.blockParticleGravity;
//        particleMaxSize = size + (float)rand.nextGaussian() * (size / 2f);
//        particleMaxAge = age + (int) (rand.nextFloat() * (age / 2f));
//    }
//
//    public FlameFX(World world, double x, double y, double z, double a, double b, double c, FlameEmitter ft) {
//        this(world, x, y, z, a, b, c, ft.flameSize, ft.flameLifetime);
//
//        owner = ft.getOwner();
//
//        smokeChance = ft.smokeChance;
//        largeSmokeChance = ft.largeSmokeChance;
//        igniteBlocks = ft.igniteBlocks;
//        igniteEntities = ft.igniteEntities;
//        igniteDamage = ft.igniteDamage;
//        igniteDuration = ft.igniteDuration;
//        igniteChance = ft.igniteChance;
//    }
//
//    @Override
//    public int getFXLayer() {
//        return 1;
//    }
//
//    @Override
//    public int getEntityBrightnessForRender(float f) {
//        return 0xf00000;
//    }
//
//    @Override
//    public void onUpdate() {
//        prevPosX = posX;
//        prevPosY = posY;
//        prevPosZ = posZ;
//
//        float lifetimeRate = (float) particleAge / (float) particleMaxAge;
//        particleScale = 1 + MathF.sinL(lifetimeRate * (float)Math.PI) * particleMaxSize;
//        setSize(0.5f * particleScale, 0.5f * particleScale);
//        yOffset = height / 2f;
//
//        // spawn a smoke trail after some time
//        if (smokeChance != 0 && rand.nextFloat() < lifetimeRate && rand.nextFloat() <= smokeChance) {
//            worldObj.spawnParticle(getSmokeParticleName(), posX, posY, posZ, motionX * 0.5, motionY * 0.5, motionZ * 0.5);
//        }
//
//        if (particleAge++ >= particleMaxAge) {
//            setEntityDead();
//            return;
//        }
//
//        // extinguish when hitting water
//        if (handleWaterMovement()) {
//            worldObj.spawnParticle(getSmokeParticleName(), posX, posY, posZ, 0, 0, 0);
//
//            setEntityDead();
//            return;
//        }
//
//        motionY += 0.02;
//
//        moveEntity(motionX, motionY, motionZ);
//
//        if (posY == prevPosY) {
//            motionX *= 1.1;
//            motionZ *= 1.1;
//        }
//
//        motionX *= 0.96;
//        motionY *= 0.96;
//        motionZ *= 0.96;
//
//        if (onGround) {
//            motionX *= 0.7;
//            motionZ *= 0.7;
//        }
//
//        // collision ages particles faster
//        if (isCollided) {
//            particleAge += 5;
//        }
//
//        // ignite environment
//        if ((igniteEntities || igniteBlocks) && rand.nextFloat() <= igniteChance) {
//            igniteEnvironment();
//        }
//    }
//
//    @Override
//    public boolean handleWaterMovement() {
//        return worldObj.handleMaterialAcceleration(boundingBox, Material.water, this);
//    }
//
//    @Override
//    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
//        tessellator.setBrightness(240);
//        super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
//    }
//
//    protected void igniteEnvironment() {
//        Vec3D v1 = Vec3D.createVector(posX, posY, posZ);
//        Vec3D v2 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
//
//        MovingObjectPosition target = worldObj.rayTraceBlocks(v1, v2);
//
//        v1 = Vec3D.createVector(posX, posY, posZ);
//        v2 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
//
//        if (target != null) {
//            v2 = Vec3D.createVector(target.hitVec.xCoord, target.hitVec.yCoord, target.hitVec.zCoord);
//        }
//
//        Entity touchedEntity = null;
//        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
//                boundingBox.addCoord(motionX, motionY, motionZ).expand(1, 1, 1));
//        double minDist = 0;
//
//        for (int j = 0; j < list.size(); j++) {
//            Entity ent = (Entity) list.get(j);
//
//            if (!ent.canBeCollidedWith()) {
//                continue;
//            }
//
//            float aabbOfs = 0.3f;
//            AxisAlignedBB aabb = ent.boundingBox.expand(aabbOfs, aabbOfs, aabbOfs);
//            MovingObjectPosition entTarget = aabb.calculateIntercept(v1, v2);
//
//            if (entTarget == null) {
//                continue;
//            }
//
//            double dist = v1.distanceTo(entTarget.hitVec);
//
//            if (dist < minDist || minDist == 0) {
//                touchedEntity = ent;
//                minDist = dist;
//            }
//        }
//
//        if (touchedEntity != null && touchedEntity != owner) {
//            target = new MovingObjectPosition(touchedEntity);
//        }
//
//        if (target != null) {
//            igniteTarget(target);
//        }
//    }
//
//    protected void igniteTarget(MovingObjectPosition target) {
//        if (igniteEntities && target.typeOfHit == EnumMovingObjectType.ENTITY && !target.entityHit.isImmuneToFire()) {
//            if (owner != null) {
//                if (target.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, owner), igniteDamage)) {
//                    target.entityHit.setFire(igniteDuration);
//                }
//            } else {
//                if (target.entityHit.attackEntityFrom(DamageSource.onFire, igniteDamage)) {
//                    target.entityHit.setFire(igniteDuration);
//                }
//            }
//        }
//
//        if (igniteBlocks && target.typeOfHit == EnumMovingObjectType.TILE) {
//            int bx = target.blockX;
//            int by = target.blockY;
//            int bz = target.blockZ;
//
//            switch (target.sideHit) {
//                case 0:
//                    by--;
//                    break;
//
//                case 1:
//                    by++;
//                    break;
//
//                case 2:
//                    bz--;
//                    break;
//
//                case 3:
//                    bz++;
//                    break;
//
//                case 4:
//                    bx--;
//                    break;
//
//                case 5:
//                    bx++;
//                    break;
//            }
//
//            if (worldObj.isAirBlock(bx, by, bz)) {
//                worldObj.setBlockWithNotify(bx, by, bz, Block.fire.blockID);
//                if (Block.fire.canBlockCatchFire(worldObj, target.blockX, target.blockY, target.blockZ)) {
//                    worldObj.spawnParticle("lava", bx, by, bz, 0, 0, 0);
//                }
//            }
//        }
//    }
//
//    protected String getSmokeParticleName() {
//        if (largeSmokeChance != 0 && rand.nextFloat() <= largeSmokeChance) {
//            return "largesmoke";
//        } else {
//            return "smoke";
//        }
//    }
//}
