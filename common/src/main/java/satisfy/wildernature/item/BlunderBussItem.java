package satisfy.wildernature.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import satisfy.wildernature.entity.BulletEntity;
import satisfy.wildernature.registry.ObjectRegistry;
import satisfy.wildernature.registry.SoundRegistry;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlunderBussItem extends ProjectileWeaponItem {
	protected int bonusDamage;
	protected double damageMultiplier;
	protected int fireDelay;
	protected double inaccuracy;
	protected double projectileSpeed = 3;
	protected boolean ignoreInvulnerability = false;
	protected Supplier<Ingredient> repairMaterial = () -> Ingredient.of(net.minecraft.world.item.Items.IRON_INGOT);

	public BlunderBussItem(Properties properties, int bonusDamage, double damageMultiplier, int fireDelay, double inaccuracy) {
		super(properties);
		this.bonusDamage = bonusDamage;
		this.damageMultiplier = damageMultiplier;
		this.fireDelay = fireDelay;
		this.inaccuracy = inaccuracy;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack gun = player.getItemInHand(hand);
		ItemStack ammo = player.getProjectile(gun);

		if (!ammo.isEmpty() || player.getAbilities().instabuild) {
			if (ammo.isEmpty()) ammo = new ItemStack(ObjectRegistry.AMMO_BAG.get());

			BulletItem bulletItem = (BulletItem) (ammo.getItem() instanceof BulletItem ? ammo.getItem() : ObjectRegistry.AMMO_BAG.get());
			if (!world.isClientSide) {
				boolean bulletFree = player.getAbilities().instabuild || !shouldConsumeAmmo();

				ItemStack shotAmmo = ammo.getItem() instanceof BulletItem ? ammo : new ItemStack(ObjectRegistry.AMMO_BAG.get());
				shoot(world, player, shotAmmo, bulletItem);

				gun.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
				if (!bulletFree) bulletItem.consume(ammo, player);
			}

			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegistry.BLUNDERBUSS_SHOOT.get(), SoundSource.PLAYERS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
			player.awardStat(Stats.ITEM_USED.get(this));

			player.getCooldowns().addCooldown(this, getFireDelay());
			return InteractionResultHolder.consume(gun);
		}
		else return InteractionResultHolder.fail(gun);
	}

	protected void shoot(Level world, Player player, ItemStack ammo, BulletItem bulletItem) {
		BulletEntity shot = bulletItem.createProjectile(world, ammo, player);
		shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, (float)getProjectileSpeed(), (float)getInaccuracy());
		shot.setDamage((shot.getDamage() + getBonusDamage()) * getDamageMultiplier());
		shot.setIgnoreInvulnerability(ignoreInvulnerability);

		world.addFreshEntity(shot);
	}

	public boolean shouldConsumeAmmo() {
		return true;
	}

	public double getBonusDamage() {
		return bonusDamage;
	}

	public double getDamageMultiplier() {
		return damageMultiplier;
	}

	public int getFireDelay() {
		return fireDelay;
	}

	public double getInaccuracy() {
		return inaccuracy;
	}

	public double getProjectileSpeed() {
		return projectileSpeed;
	}

	@Override
	public @NotNull UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	private static final Predicate<ItemStack> BULLETS = (stack) -> stack.getItem() instanceof BulletItem && ((BulletItem)stack.getItem()).hasAmmo(stack);

	@Override
	public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
		return BULLETS;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 15;
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return (repairMaterial != null && repairMaterial.get().test(repair)) || super.isValidRepairItem(toRepair, repair);
	}
}