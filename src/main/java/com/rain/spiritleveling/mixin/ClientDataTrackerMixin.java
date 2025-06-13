package com.rain.spiritleveling.mixin;

import com.rain.spiritleveling.client.IClientSpiritEnergyPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class ClientDataTrackerMixin extends LivingEntity implements IClientSpiritEnergyPlayer {
	// keys for current and max spirit energy tracking
	@Unique
	private static final TrackedData<Integer> MAX_SPIRIT_ENERGY = DataTracker.registerData(ClientDataTrackerMixin.class, TrackedDataHandlerRegistry.INTEGER);
	@Unique
	private static final TrackedData<Integer> CURRENT_SPIRIT_ENERGY = DataTracker.registerData(ClientDataTrackerMixin.class, TrackedDataHandlerRegistry.INTEGER);
	@Unique
	private static final TrackedData<Integer> SPIRIT_LEVEL = DataTracker.registerData(ClientDataTrackerMixin.class, TrackedDataHandlerRegistry.INTEGER);
	@Unique
	private static final TrackedData<Boolean> MINOR_BOTTLENECK = DataTracker.registerData(ClientDataTrackerMixin.class, TrackedDataHandlerRegistry.BOOLEAN);

	protected ClientDataTrackerMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("TAIL"), method = "initDataTracker")
	private void initCustomDataTrackers(CallbackInfo info) {
		this.dataTracker.startTracking(MAX_SPIRIT_ENERGY, 0);
		this.dataTracker.startTracking(CURRENT_SPIRIT_ENERGY, 0);
		this.dataTracker.startTracking(SPIRIT_LEVEL, 0);
		this.dataTracker.startTracking(MINOR_BOTTLENECK, false);
	}

	@Override
	public void spirit_leveling$setDataMaxEnergy(int amount) {
		this.dataTracker.set(MAX_SPIRIT_ENERGY, amount);
	}

	@Override
	public void spirit_leveling$setDataCurrentEnergy(int amount) {
		this.dataTracker.set(CURRENT_SPIRIT_ENERGY, amount);
	}

	@Override
	public void spirit_leveling$setDataSpiritLevel(int amount) {
		this.dataTracker.set(SPIRIT_LEVEL, amount);
	}

	@Override
	public void spirit_leveling$setDataMinorBottleneck(boolean bool) {
		this.dataTracker.set(MINOR_BOTTLENECK, bool);
	}

	@Override
	public int spirit_leveling$getDataMaxEnergy() {
		return this.dataTracker.get(MAX_SPIRIT_ENERGY);
	}

	@Override
	public int spirit_leveling$getDataCurrentEnergy() {
		return this.dataTracker.get(CURRENT_SPIRIT_ENERGY);
	}

	@Override
	public int spirit_leveling$getDataSpiritLevel() {
		return this.dataTracker.get(SPIRIT_LEVEL);
	}

	@Override
	public boolean spirit_leveling$getDataMinorBottleneck() {
		return this.dataTracker.get(MINOR_BOTTLENECK);
	}
}