package net.strauss.kitpvpmod.dto;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PlayerPosition {
    public PlayerPosition(Player player, Vec3 position) {
        this.Player = player;
        this.Position = position;
    }
    public Player Player;
    public Vec3 Position;
}
