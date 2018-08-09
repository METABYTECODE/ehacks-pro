/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehacks.mod.modulesystem.classes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import ehacks.api.module.Mod;
import ehacks.api.module.ModStatus;
import ehacks.mod.gui.reeszrbteam.YouAlwaysWinClickGui;
import ehacks.mod.wrapper.Events;
import ehacks.mod.wrapper.ModuleCategories;
import ehacks.mod.wrapper.Wrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;

/**
 *
 * @author radioegor146
 */
public class NoLimitDamage extends Mod {

    public NoLimitDamage() {
        super(ModuleCategories.EHACKS);
    }

    @Override
    public String getName() {
        return "NoLimitDamage";
    }

    public boolean lastPressed = false;
    
    @Override
    public void onMouse(MouseEvent event) {
        if (Wrapper.INSTANCE.mc().objectMouseOver.entityHit != null && event.button == 0 && !lastPressed)
        {
            int playerId = Wrapper.INSTANCE.player().getEntityId();
            int entityId = Wrapper.INSTANCE.mc().objectMouseOver.entityHit.getEntityId();
            int dimensionId = Wrapper.INSTANCE.player().dimension;
            ByteBuf buf = Unpooled.buffer(0);
            buf.writeByte(0);
            buf.writeInt(entityId);
            buf.writeInt(playerId);
            buf.writeInt(dimensionId);
            buf.writeFloat(Float.MAX_VALUE);
            buf.writeBoolean(false);
            C17PacketCustomPayload packet = new C17PacketCustomPayload("taintedmagic", buf);
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
            lastPressed = true;
        }
        if (lastPressed && event.button == 0 && !event.buttonstate)
            lastPressed = false;
    }
    
    @Override
    public ModStatus getModStatus() {
        try {
            Class.forName("taintedmagic.common.network.PacketKatanaAttack");
            return ModStatus.WORKING;
        } catch (Exception e) {
            return ModStatus.NOTWORKING;
        }
    }
    
    @Override
    public void onEnableMod() {
        try {
            Class.forName("taintedmagic.common.network.PacketKatanaAttack");
        }
        catch (Exception ex) {
            this.off();
            YouAlwaysWinClickGui.log("[NoLimitDamage] Not working");
        }
    }
    
    @Override
    public String getModName() {
        return "Tainted Magic";
    }
}