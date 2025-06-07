package com.rain.spiritleveling.mixin;

import com.rain.spiritleveling.util.ISpiritEnergyPlayer;
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
public abstract class ClientDataTrackerMixin extends LivingEntity implements ISpiritEnergyPlayer {
	// keys for current and max spirit energy tracking
	@Unique
	private static final TrackedData<Integer> MAX_SPIRIT_ENERGY = DataTracker.registerData(ClientDataTrackerMixin.class, TrackedDataHandlerRegistry.INTEGER);
	@Unique
	private static final TrackedData<Integer> CURRENT_SPIRIT_ENERGY = DataTracker.registerData(ClientDataTrackerMixin.class, TrackedDataHandlerRegistry.INTEGER);
	@Unique
	private static final TrackedData<Integer> SPIRIT_LEVEL = DataTracker.registerData(ClientDataTrackerMixin.class, TrackedDataHandlerRegistry.INTEGER);

	protected ClientDataTrackerMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("TAIL"), method = "initDataTracker")
	private void initCustomDataTrackers(CallbackInfo info) {
		this.dataTracker.startTracking(MAX_SPIRIT_ENERGY, 0);
		this.dataTracker.startTracking(CURRENT_SPIRIT_ENERGY, 0);
		this.dataTracker.startTracking(SPIRIT_LEVEL, 0);
	}

	@Override
	public void spirit_leveling$setMaxData(int amount) {
		this.dataTracker.set(MAX_SPIRIT_ENERGY, amount);
	}

	public void spirit_leveling$setCurrentData(int amount) {
		this.dataTracker.set(CURRENT_SPIRIT_ENERGY, amount);
	}

	public void spirit_leveling$setLevelData(int amount) {
		this.dataTracker.set(SPIRIT_LEVEL, amount);
	}

	public int spirit_leveling$getMaxData() {
		return this.dataTracker.get(MAX_SPIRIT_ENERGY);
	}

	public int spirit_leveling$getCurrentData() {
		return this.dataTracker.get(CURRENT_SPIRIT_ENERGY);
	}

	public int spirit_leveling$getLevelData() {
		return this.dataTracker.get(SPIRIT_LEVEL);
	}
}