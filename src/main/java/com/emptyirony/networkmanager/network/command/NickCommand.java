package com.emptyirony.networkmanager.network.command;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.util.bookutils.NMSBookBuilder;
import com.emptyirony.networkmanager.util.bookutils.NMSBookUtils;
import com.emptyirony.networkmanager.util.signutils.SignGUI;
import com.mojang.authlib.GameProfile;
import com.qrakn.honcho.command.CommandMeta;
import lombok.SneakyThrows;
import lombok.val;
import me.neznamy.tab.api.TABAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import strafe.games.core.profile.Profile;
import strafe.games.core.util.CC;

import java.lang.reflect.Field;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 14:39
 * 4
 */
@CommandMeta(label = "nick", async = true)
public class NickCommand {
    public void execute(Player player) {
        if (NetworkManager.getInstance().getServerType() != 0) {
            player.sendMessage(CC.translate("&c您只能在大厅使用该指令！"));
            return;
        }
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());

        if (data.isNick()) {
            nick(player, data.getName());
            data.setNick(false);
            return;
        }
        TextComponent option = new TextComponent(CC.translate("&0&n&0➤ 我同意并且遵守服务器守则\n" +
                "      &n开始设置我的昵称"));
        option.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick accept"));
        option.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&f点击进行下一步"))}));
        NMSBookUtils.open(player, NMSBookBuilder.create(CC.translate("&e昵称系统"), new TextComponent(CC.translate("&0Nick系统允许你更改不同的名字\n" +
                "       使其他玩家看不到你的\n" +
                "\n" +
                "      即使其他玩家看不到你的名字，但是所有服务器规则仍然适用于你，我们仍然能查询到你的真实ID进行惩罚\n&c&l目前该指令正在测试中，可能有不稳定性！如有Bug倾向我们反馈！")), option));


    }

    public void execute(Player player, String text1) {
        TextComponent option1 = new TextComponent(CC.translate("\n&0➤ &a[士]"));
        option1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick accept 1"));
        option1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&f点击进行下一步"))}));
        TextComponent option2 = new TextComponent(CC.translate("\n&0➤ &a[大夫]"));
        option2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick accept 2"));
        option2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&f点击进行下一步"))}));
        TextComponent option3 = new TextComponent(CC.translate("\n&0➤ &b[上卿]"));
        option3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick accept 3"));
        option3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&f点击进行下一步"))}));
        NMSBookUtils.open(player, NMSBookBuilder.create(CC.translate("&f选择会员等级"),
                new TextComponent(CC.translate("&0选择一个会员等级! 首先，选择一个 &l等级 &0将会适用于你的昵称.")), new TextComponent(option1, option2, option3)));
    }

    public void execute(Player player, String text1, String text2) {
        val profile = Profile.getByUuid(player.getUniqueId());
        if (text1.equals("accept")) {
            TextComponent option1 = new TextComponent(CC.translate("\n&0➤ &0我的原始皮肤"));
            option1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + text2 + " " + player.getName()));
            option1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&f点击进行下一步"))}));
//            TextComponent option3 = new TextComponent(CC.translate("\n&0➤ &0随机皮肤"));
//            String skinName = Core.RANDOM_NAMES_LIST.get(new Random().nextInt(Core.RANDOM_NAMES_LIST.size()));
//            option3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + text2 + " " + skinName));
//            option3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&f点击进行下一步"))}));
            TextComponent option2 = new TextComponent(CC.translate("\n&0➤ &0史蒂夫/爱丽丝"));
            option2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + text2 + " Steve"));
            option2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&f点击进行下一步"))}));
//            TextComponent option4 = new TextComponent(CC.translate("\n&0➤ &0继续使用" + (profile.getUsedNickedSkin())));
//            option4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + text2 + " " + profile.getUsedNickedSkin()));
//            option4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&f点击进行下一步"))}));


            NMSBookUtils.open(player, NMSBookBuilder.create(CC.translate("&f皮肤"), new TextComponent(CC.translate("&0很好! 现在, 选择一个在你使用昵称时的 &l皮肤 &0?")), option1, option2));
        }
    }

    public void execute(Player player, String text1, String text2, String text3) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        TextComponent option1 = new TextComponent(CC.translate("\n&0➤ &0输入自定义名字"));
        option1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + text2 + " " + text3 + " ENTERNAME"));
        option1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&ff点击进行下一步"))}));
        TextComponent option2 = new TextComponent(CC.translate("\n&0➤ &0使用随机名字"));
        option2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + text2 + " " + text3 + " RANDOM"));
        option2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&ff点击进行下一步"))}));
        if (data.getNickedName() != null) {
            TextComponent option3 = new TextComponent(CC.translate("\n&0➤ &0继续使用" + data.getNickedName()));
            option3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + text2 + " " + text3 + " " + data.getNickedName()));
            option3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&ff点击进行下一步"))}));
        }
        NMSBookUtils.open(player, NMSBookBuilder.create(CC.translate("&f选择名字"), new TextComponent(CC.translate("&0OK, 现在你需要选择一个 &0&l名字 &0去使用!")), player.hasPermission("core.yt") ? option1 : new TextComponent(""), option2));

    }

    public void execute(Player player, String text1, String typeRank, String typeSkin, String typeName) {
        if (typeName.equals("ENTERNAME")) {
            SignGUI.open(player, "", "~~~~~~~~~~~~~~", "在这里", "输入你的名字", (event) -> {
                player.chat("/nick " + text1 + " " + typeRank + " " + typeSkin + " " + event.getLines()[0]);
            });
            return;
        }
        Profile targetProfile = null;
        try {
            targetProfile = Profile.getByUsername(typeName);
        } catch (Exception ignored) {
        }

        if (targetProfile != null && targetProfile.getFirstSeen() != 0) {
            player.sendMessage(CC.translate("&c该名称已被使用！请重新填写名称！"));
            return;
        }
//        AtomicBoolean canNick = new AtomicBoolean(true);
//        new Global().load(global->{
//            global.getUsedName().forEach(usedName->{
//                if (usedName.equalsIgnoreCase(typeName)){
//                    canNick.set(false);
//                }
//            });
//        });
//        if (!canNick.get()){
//            player.sendMessage(CC.translate("&c该名称已被使用！请重新填写名称！"));
//            return;
//        }
        if (typeName.equals("RANDOM")) {
            String name = "NoBody";
            TextComponent option1 = new TextComponent(CC.translate("\n&0➤ &a确认"));
            option1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + typeRank + " " + typeSkin + " " + name));
            option1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&ff点击进行下一步"))}));
            TextComponent option2 = new TextComponent(CC.translate("\n&0➤ &c换一个"));
            option2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + typeRank + " " + typeSkin + " RANDOM"));
            option2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&ff点击进行下一步"))}));
            TextComponent option3 = new TextComponent(CC.translate("\n&0➤ &c自定义名字"));
            option3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick " + text1 + " " + typeRank + " " + typeSkin + " ENTERNAME"));
            option3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&ff点击进行下一步"))}));
            NMSBookUtils.open(player, NMSBookBuilder.create(CC.translate("&f选择名字"), new TextComponent(CC.translate("&0我们为您挑选了一个名字\n&0&l" + name + "\n")), option1, option3, player.hasPermission("core.yt") ? option3 : new TextComponent("")));
            return;
        }


        String prefix;
        switch (typeRank) {
            case "1":
                prefix = "&7[&8蠹虫&7]&8";
                break;
            case "2":
                prefix = "&7[&2僵尸&7]&2";
                break;
            case "3":
                prefix = "&7[&5末影人&7]&5";
                break;
            case "4":
                prefix = "&7[&6末影龙&7]&6";
                break;
            default:
                prefix = "&7";
        }

        String name;
        name = typeName;
        nick(player, name);
        player.sendMessage(CC.translate("&a成功！你现在的昵称是" + name));
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        data.setNick(true);
        data.setNickedName(name);
        data.save(false);
    }


    @SneakyThrows
    private void nick(Player player, String id) {
        Field name = GameProfile.class.getDeclaredField("name");
        CraftPlayer ePlayer = ((CraftPlayer) player);
        name.setAccessible(true);
        name.set(ePlayer.getProfile(), id);
        TABAPI.setCustomTabNameTemporarily(player.getUniqueId(), player.getDisplayName());

        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(player.getEntityId());
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(player)) {
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(destroy);
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(playerInfo);
                playerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(spawn);
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(playerInfo);
            }
        }
    }
}
