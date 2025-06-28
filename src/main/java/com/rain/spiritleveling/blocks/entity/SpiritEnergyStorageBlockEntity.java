package com.rain.spiritleveling.blocks.entity;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.util.CachedValue;
import com.rain.spiritleveling.util.UniqueQueue;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SpiritEnergyStorageBlockEntity extends BlockEntity {
    private int maxEnergy;
    private int currentEnergy = 0;
    private final CachedValue<Integer> cachedConnectedMaxEnergy;
    private final CachedValue<Integer> cachedConnectedCurrentEnergy;

    public SpiritEnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int maxEnergy) {
        super(type, pos, state);
        this.maxEnergy = maxEnergy;
        this.cachedConnectedCurrentEnergy = new CachedValue<>(0);
        this.cachedConnectedMaxEnergy = new CachedValue<>(maxEnergy);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("meditation_inventory.maxSpiritEnergy", maxEnergy);
        nbt.putInt("meditation_inventory.spiritEnergy", currentEnergy);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        maxEnergy = nbt.getInt("meditation_inventory.maxSpiritEnergy");
        currentEnergy = nbt.getInt("meditation_inventory.spiritEnergy");
    }

    public int getConnectedMaxEnergy() {
        if (cachedConnectedMaxEnergy.isValid())
            return cachedConnectedMaxEnergy.getValue();

        int max = 0;
        for (SpiritEnergyStorageBlockEntity e : getAllConnected()) {
            max += e.getMaxEnergy();
        }
        SpiritLeveling.LOGGER.info("{}", max);

        cachedConnectedMaxEnergy.setValue(max);
        return max;
    }

    public int getConnectedCurrentEnergy() {
        if (cachedConnectedCurrentEnergy.isValid())
            return cachedConnectedCurrentEnergy.getValue();

        int current = 0;
        for (SpiritEnergyStorageBlockEntity e : getAllConnected()) {
            current += e.getCurrentEnergy();
        }
        SpiritLeveling.LOGGER.info("{}", current);

        cachedConnectedCurrentEnergy.setValue(current);
        return current;
    }

    /// always returns the actual amount that was added to the multiblock
    public int addCurrentEnergy(int amount) {
        int rest = amount;
        for (SpiritEnergyStorageBlockEntity e : getAllConnected((en) -> en.currentEnergy < en.maxEnergy)) {
            if (rest <= e.getMaxEnergy() - e.getCurrentEnergy()) {
                e.setCurrentEnergy(e.getCurrentEnergy() + rest);
                rest = 0;
                break;
            }

            rest -= e.getMaxEnergy() - e.getCurrentEnergy();
            e.setCurrentEnergy(e.getMaxEnergy());
        }

        cachedConnectedCurrentEnergy.markStale();
        cachedConnectedMaxEnergy.markStale();

        return amount - rest;
    }

    /// always returns the actual amount that was removed from the multiblock
    public int removeCurrentEnergy(int amount) {
        int rest = amount;
        for (SpiritEnergyStorageBlockEntity e : getAllConnected((en) -> en.currentEnergy != 0)) {
            if (rest <= e.getCurrentEnergy()) {
                e.setCurrentEnergy(e.getCurrentEnergy() - rest);
                rest = 0;
                break;
            }

            rest -= e.getCurrentEnergy();
            e.setCurrentEnergy(0);
        }

        cachedConnectedCurrentEnergy.markStale();
        cachedConnectedMaxEnergy.markStale();

        return amount - rest;
    }

    public int getMaxEnergy() {
        return this.maxEnergy;
    }

    public int getCurrentEnergy() {
        return this.currentEnergy;
    }

    public void setCurrentEnergy(int value) {
        this.currentEnergy = value;
    }

    /// HELPER
    /// FUNCTIONS

    /// return all directly connected instances of this BlockEntity
    private Set<SpiritEnergyStorageBlockEntity> getAllConnected(Predicate<SpiritEnergyStorageBlockEntity> condition) {
        Set<SpiritEnergyStorageBlockEntity> visited = new HashSet<>();
        visited.add(this);

        UniqueQueue<SpiritEnergyStorageBlockEntity> queue = new UniqueQueue<>();

        // run through all queued blocks
        while (!queue.isEmpty()) {
            SpiritEnergyStorageBlockEntity entity = queue.dequeue();

            // add neighbors only if element not in visited
            if (visited.add(entity)) {
                queue.enqueueAll(entity.getNeighbors());
            }
        }

        // return only the elements of the visited that pass the condition
        return visited.stream().filter(condition).collect(Collectors.toSet());
    }

    private Set<SpiritEnergyStorageBlockEntity> getAllConnected() {
        return getAllConnected((e) -> true);
    }

    /// returns all neighboring block entities of type SpiritEnergyStorageBlockEntity
    public Collection<SpiritEnergyStorageBlockEntity> getNeighbors() {
        Collection<SpiritEnergyStorageBlockEntity> allNeighbors = new ArrayList<>();

        for (Direction d : Direction.values()) {
            assert this.getWorld() != null;
            BlockEntity neighbor = this.getWorld().getBlockEntity(this.getPos().offset(d));

            // add meditation mat entities only from below
            if (neighbor instanceof MeditationMatEntity && d == Direction.UP) {
                allNeighbors.add(((MeditationMatEntity) neighbor));
                continue;
            }

            if (neighbor instanceof SpiritEnergyStorageBlockEntity) {
                allNeighbors.add(((SpiritEnergyStorageBlockEntity) neighbor));
            }
        }

        return allNeighbors;
    }
}
