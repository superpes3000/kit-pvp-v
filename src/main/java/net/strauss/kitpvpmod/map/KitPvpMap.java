package net.strauss.kitpvpmod.map;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.strauss.kitpvpmod.dto.PlayerPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitPvpMap {
    protected Vec3 firstPlacePosition;
    protected Vec3 secondPlacePosition;
    protected Vec3 thirdPlacePosition;
    protected Vec3 fourthPlacePosition;
    protected String mapTag;

    protected KitPvpMap(String mapTag, Vec3 firstPlacePosition, Vec3 secondPlacePosition,
                                Vec3 thirdPlacePosition, Vec3 fourthPlacePosition) {
        this.firstPlacePosition = firstPlacePosition;
        this.secondPlacePosition = secondPlacePosition;
        this.thirdPlacePosition = thirdPlacePosition;
        this.fourthPlacePosition = fourthPlacePosition;
        this.mapTag = mapTag;
    }

    public List<PlayerPosition> getPlayersPosition(List<Player> players) {
        Collections.shuffle(players); // перемешиваем игроков
        List<PlayerPosition> playerPoses = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Vec3 position;

            // Назначаем позицию в зависимости от индекса
            switch (i) {
                case 0 -> position = firstPlacePosition;
                case 1 -> position = secondPlacePosition;
                case 2 -> position = thirdPlacePosition;
                case 3 -> position = fourthPlacePosition;
                default -> {
                    // Если игроков больше 4, просто используем firstPlacePosition или можно добавить логику
                    position = firstPlacePosition;
                }
            }

            playerPoses.add(new PlayerPosition(player, position));
        }

        return playerPoses;
    }


}
