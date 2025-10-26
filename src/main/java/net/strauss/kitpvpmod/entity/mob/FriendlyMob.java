package net.strauss.kitpvpmod.entity.mob;

import javax.annotation.Nullable;
import java.util.UUID;

public interface FriendlyMob {
    @Nullable
    UUID getOwner();
}
