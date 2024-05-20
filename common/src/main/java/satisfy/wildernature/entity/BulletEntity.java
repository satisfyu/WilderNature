package satisfy.wildernature.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import satisfy.wildernature.item.BulletItem;
import satisfy.wildernature.network.EntityPacketHandler;
import satisfy.wildernature.registry.EntityRegistry;

public class BulletEntity extends Fireball {
	private static final double STOP_TRESHOLD = 0.01;
	private double damage = 1;
	private boolean ignoreInvulnerability = false;
	private double knockbackStrength = 0;
	private int ticksSinceFired;

	public BulletEntity(EntityType<? extends Fireball> entityType, Level level) {
		super(entityType, level);
	}

	public BulletEntity(Level worldIn, LivingEntity shooter) {
		this(worldIn, shooter, 0, 0, 0);
		setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
	}

	public BulletEntity(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(EntityRegistry.BULLET.get(), shooter, accelX, accelY, accelZ, worldIn);
	}

	@Override
	public void tick() {
		ticksSinceFired++;
		if (ticksSinceFired > 100 || getDeltaMovement().lengthSqr() < STOP_TRESHOLD) {
			remove(RemovalReason.KILLED);
		}
		super.tick();
	}

	@Override
	protected void onHitEntity(EntityHitResult raytrace) {
		super.onHitEntity(raytrace);
		if (!level().isClientSide) {
			Entity target = raytrace.getEntity();
			Entity shooter = getOwner();
			BulletItem bullet = (BulletItem) getItemRaw().getItem();

			if (isOnFire()) target.setSecondsOnFire(5);
			int lastHurtResistant = target.invulnerableTime;
			if (ignoreInvulnerability) target.invulnerableTime = 0;
			boolean damaged = target.hurt(level().damageSources().indirectMagic(this, shooter), (float) bullet.modifyDamage(damage, this, target, shooter, level()));

			if (damaged && target instanceof LivingEntity livingTarget) {
				if (knockbackStrength > 0) {
					Vec3 vec = getDeltaMovement().multiply(1, 0, 1).normalize().scale(knockbackStrength);
					if (vec.lengthSqr() > 0) livingTarget.push(vec.x, 0.1, vec.z);
				}
				if (shooter instanceof LivingEntity) doEnchantDamageEffects((LivingEntity) shooter, target);
				BulletItem.onLivingEntityHit(livingTarget, shooter, level());
			} else if (!damaged && ignoreInvulnerability) target.invulnerableTime = lastHurtResistant;
		}
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (!level().isClientSide && (!noPhysics || result.getType() != HitResult.Type.BLOCK)) remove(RemovalReason.KILLED);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("tickssincefired", ticksSinceFired);
		compound.putDouble("damage", damage);
		if (ignoreInvulnerability) compound.putBoolean("ignoreinvulnerability", true);
		if (knockbackStrength != 0) compound.putDouble("knockback", knockbackStrength);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		ticksSinceFired = compound.getInt("tickssincefired");
		damage = compound.getDouble("damage");
		ignoreInvulnerability = compound.getBoolean("ignoreinvulnerability");
		knockbackStrength = compound.getDouble("knockback");
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getDamage() {
		return damage;
	}

	public void setIgnoreInvulnerability(boolean ignoreInvulnerability) {
		this.ignoreInvulnerability = ignoreInvulnerability;
	}

	@Override
	public void shootFromRotation(Entity shooter, float xRot, float yRot, float p_37255_, float speed, float spread) {
		float f = -Mth.sin(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
		float f1 = -Mth.sin((xRot + p_37255_) * ((float) Math.PI / 180F));
		float f2 = Mth.cos(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
		shoot(f, f1, f2, speed, spread);
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	@Override
	protected float getInertia() {
		return 1;
	}

	@Override
	public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
		return EntityPacketHandler.createAddEntityPacket(this);
	}
}