package cc.funkemunky.bungee.listeners;

import cc.funkemunky.bungee.AtlasBungee;
import cc.funkemunky.bungee.data.user.User;
import cc.funkemunky.bungee.utils.Color;
import cc.funkemunky.bungee.utils.asm.Init;
import lombok.val;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

@Init
public class AtlasMsgListener implements Listener {

    public AtlasMsgListener() {
        ProxyServer.getInstance().registerChannel(AtlasBungee.INSTANCE.outChannel);
        ProxyServer.getInstance().registerChannel(AtlasBungee.INSTANCE.inChannel);
    }

    @EventHandler
    public void onEvent(PluginMessageEvent event) {
        System.out.println("Received msg: " + event.getTag());
        if(event.getTag().equalsIgnoreCase("atlas:out")) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(event.getData());
                ObjectInputStream inputStream = new ObjectInputStream(bis);

                System.out.println(event.getData().length);
                String type = (String)inputStream.readObject();
                System.out.println("type: " + type);

                switch(type) {
                    case "sendObjects": {
                        String serverField = (String)inputStream.readObject();

                        if(!serverField.contains("_")) {
                            return;
                        }
                        String server = serverField.split("_")[0];

                        if(server.equalsIgnoreCase("all")) {
                            for (ServerInfo info : ProxyServer.getInstance().getServers().values()) {
                                info.sendData("atlas:in", event.getData());
                            }
                        } else {
                            Optional<ServerInfo> infoOptional = ProxyServer.getInstance().getServers().values()
                                    .stream().filter(val -> val.getName().equalsIgnoreCase(server)).findFirst();

                            infoOptional.ifPresent(serverInfo -> serverInfo.sendData("atlas:in", event.getData(), true));
                        }

                        break;
                    }
                    case "broadcastMsg": {
                        String message = (String) inputStream.readObject();
                        String permission = (String) inputStream.readObject();

                        if(permission.equals("")) {
                            BungeeCord.getInstance().broadcast((new TextComponent(Color.translate(message))));
                        } else {
                            BungeeCord.getInstance().getPlayers().stream()
                                    .filter(player -> player.hasPermission(permission))
                                    .forEach(player -> player.sendMessage(new TextComponent(Color.translate(message))));
                        }
                        break;
                    }
                    /*case "ping": {
                        String name = inputStream.readUTF();
                        long time = inputStream.readLong();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream output = new ObjectOutputStream(baos);

                        output.writeUTF("ping");
                        output.writeUTF("ALL");
                        output.writeLong(time);
                        output.writeLong(System.currentTimeMillis() - time);

                        if(name.equalsIgnoreCase("ALL")) {
                            ProxyServer.getInstance().getServers()
                                    .forEach((sname, info) -> info.sendData("atlas:in", baos.toByteArray()));
                        } else {
                            ServerInfo field;

                            if((field = BungeeCord.getInstance().getServerInfo(name)) != null) {
                                field.sendData("atlas:in", baos.toByteArray());
                            }
                        }
                        break;
                    }*/
                    case "version": {

                        UUID uuid = (UUID) inputStream.readObject();

                        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                        ObjectOutputStream dataOut = new ObjectOutputStream(bOut);

                        dataOut.writeUTF("version");

                        User user = User.getUser(uuid);

                        dataOut.writeBoolean(true);
                        dataOut.writeInt(user.version);
                        dataOut.writeObject(uuid);

                        ProxyServer.getInstance().getServers()
                                .forEach((name, info) -> info.sendData("atlas:in", bOut.toByteArray(), true));
                        break;
                    }
                    case "mods": {
                        UUID uuid = (UUID) inputStream.readObject();
                        User user = User.getUser(uuid);

                        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                        ObjectOutputStream dataOut = new ObjectOutputStream(bOut);

                        dataOut.writeUTF("mods");
                        dataOut.writeObject(uuid);
                        dataOut.writeObject(user.modData != null ? user.modData.getModsMap() : "");

                        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(uuid);

                        if(player != null) {
                            System.out.println("getting mods of " + player.getName());
                        }
                        if(user.modData != null) {
                            System.out.println(uuid.toString() + " mods: ");
                            user.modData.getModsMap().forEach((key, value) -> {
                                System.out.println("- " + key + "::" + value);
                            });
                        } else System.out.println(uuid.toString() + " mods null.");

                        BungeeCord.getInstance().getServers()
                                .forEach((name, info) -> {
                                    info.sendData(AtlasBungee.INSTANCE.outChannel, bOut.toByteArray(), true);
                                });
                        break;
                    }
                    case "commandBungee": {
                        String command = (String) inputStream.readObject();

                        BungeeCord.getInstance().getPluginManager()
                                .dispatchCommand(BungeeCord.getInstance().getConsole(), command);
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
