package satisfy.wildernature.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import satisfy.wildernature.entity.BulletEntity;

@SuppressWarnings("unused")
public class BulletItem extends Item {
	private final int damage;

	public BulletItem(Properties properties, int damage) {
		super(properties);
		this.damage = damage;
	}

	public BulletEntity createProjectile(Level world, ItemStack stack, LivingEntity shooter) {
		BulletEntity entity = new BulletEntity(world, shooter);
		entity.setItem(stack);
		entity.setDamage(damage);
		return entity;
	}

	public void consume(ItemStack stack, Player player) {
		stack.shrink(1);
		if (stack.isEmpty()) {
			player.getInventory().removeItem(stack);
		}
	}

	public boolean hasAmmo(ItemStack stack) {
		return !stack.isEmpty();
	}

	public static void onLivingEntityHit(LivingEntity target, @Nullable Entity shooter, Level world) {}

	public double modifyDamage(double damage, BulletEntity projectile, Entity target, @Nullable Entity shooter, Level world) {
		return damage;
	}
}
