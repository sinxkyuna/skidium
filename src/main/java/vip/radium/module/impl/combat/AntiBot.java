package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import vip.radium.event.impl.entity.EntityHealthUpdateEvent;
import vip.radium.event.impl.player.MoveEvent;
import vip.radium.event.impl.world.WorldLoadEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.PlayerUtils;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(label = "Anti Bot", category = ModuleCategory.COMBAT)
public final class AntiBot extends Module {

    private final EnumProperty<Mode> mode = new EnumProperty<>("Mode", Mode.WATCHDOG);
    private final List<Entity> validEntities = new ArrayList<>();
    Minecraft mc = Minecraft.getMinecraft();

    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoad = event -> {
        this.validEntities.clear();
    };

    @EventLink
    public final Listener<MoveEvent> moveEventListener = event -> {
        if(mode.getValue() == Mode.MUSH) {
            mc.theWorld.playerEntities.forEach(player -> {
                if (player.moveForward != 0 || player.moveStrafing != 0) {
                    validEntities.add(player);
                } else {
                    validEntities.remove(player);
                }
            });
        }
    };

    @EventLink
    public final Listener<EntityHealthUpdateEvent> onEntityHealthUpdate = event -> {
        if(mode.getValue() == Mode.WATCHDOG) {
            if (event.getEntity() instanceof EntityOtherPlayerMP)
                this.validEntities.add(event.getEntity());
        }
    };

    public AntiBot() {
        setSuffixListener(mode);
    }

    public boolean isBot(final EntityPlayer player) {
        return this.isEnabled() && !this.validEntities.contains(player) && this.mode.getValue().botCheck.check(player);
    }

    private enum Mode {
        WATCHDOG(PlayerUtils::hasInvalidNetInfo),

        MUSH(PlayerUtils::hasInvalidNetInfo);

        private final CheckPlayer botCheck;

        Mode(CheckPlayer botCheck) {
            this.botCheck = botCheck;
        }
    }

    @FunctionalInterface
    public interface CheckPlayer {
        boolean check(EntityPlayer player);
    }
}
