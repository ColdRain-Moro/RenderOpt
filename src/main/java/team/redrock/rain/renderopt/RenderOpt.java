package team.redrock.rain.renderopt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("renderopt")
public class RenderOpt {

    public Minecraft mc;

    public RenderOpt() {
        mc = Minecraft.getInstance();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        try {
            if (mc.player != null && !canSee(mc.player, event.getEntity())) {
                event.setCanceled(true);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean canSee(Entity viewer, Entity target) {
        Level level = mc.level;
        if (level == null) return true;
        Vec3 viewerEyePosition = viewer.getEyePosition();
        Vec3 targetPosition = target.getEyePosition();
        // 从 viewer 到 target 的射线上没有方块，说明该生物可见
        return level.clip(new ClipContext(viewerEyePosition, targetPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, viewer)).getType() == HitResult.Type.MISS;
    }
}
