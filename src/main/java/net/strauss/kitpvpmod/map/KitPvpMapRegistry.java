package net.strauss.kitpvpmod.map;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.strauss.kitpvpmod.dto.PlayerPosition;

import java.util.*;

public class KitPvpMapRegistry {

    private final Map<String, KitPvpMap> mapRegistry = new HashMap<>();
    private final Random random = new Random();


    public KitPvpMapRegistry() {

        registerMap(new KitPvpMap(
                "farm",
                new Vec3(-491, 4, -89),   // firstPlace
                new Vec3(-436, 5, -88),  // secondPlace
                new Vec3(-462, 4, -63),  // thirdPlace
                new Vec3(-463, 5, -115)  // fourthPlace
        ));
        registerMap(new KitPvpMap(
                "metro",
                new Vec3(-226, 4, 32),   // firstPlace
                new Vec3(-225, 4, 71),  // secondPlace
                new Vec3(-199, 4, 49),  // thirdPlace
                new Vec3(-254, 4, 49)  // fourthPlace
        ));
        registerMap(new KitPvpMap(
                "halloween",
                new Vec3(-451, 4, 33),   // firstPlace
                new Vec3(-450, 4, 96),  // secondPlace
                new Vec3(-481, 4, 63),  // thirdPlace
                new Vec3(-416, 4, 65)  // fourthPlace
        ));

    }

    public void registerMap(KitPvpMap map) {
        mapRegistry.put(map.mapTag, map);
    }
    public List<PlayerPosition> getMapByTag(String mapTag, List<Player> players) {

        if (mapRegistry.isEmpty()) {
            throw new IllegalStateException("Нет зарегистрированных карт!");
        }

        KitPvpMap map = mapRegistry.get(mapTag);

        return map.getPlayersPosition(players);
    }
    public List<PlayerPosition> getRandomMap(List<Player> players) {
        if (mapRegistry.isEmpty()) {
            throw new IllegalStateException("Нет зарегистрированных карт!");
        }

        List<String> keys = new ArrayList<>(mapRegistry.keySet());
        String randomKey = keys.get(random.nextInt(keys.size()));

        KitPvpMap map = mapRegistry.get(randomKey);

        return map.getPlayersPosition(players);
    }
}
