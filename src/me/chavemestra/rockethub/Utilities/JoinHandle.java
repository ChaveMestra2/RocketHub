/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.chavemestra.rockethub.Utilities;

import static me.chavemestra.rockethub.RocketHub.itemStock;
import static me.chavemestra.rockethub.RocketHub.lobby;
import static me.chavemestra.rockethub.RocketHub.plugin;
import static me.chavemestra.rockethub.RocketHub.utilidades;
import static me.chavemestra.rockethub.Utilities.Chat.f;
import static me.chavemestra.rockethub.Utilities.Pvp.pvp;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.inventivetalent.tabapi.TabAPI;

/**
 *
 * @author GabrielBatistella
 */
public class JoinHandle implements Listener {
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        if (pvp.contains(e.getPlayer().getUniqueId())) {
            pvp.remove(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            setHeaderAndFooter(e.getPlayer());
            e.getPlayer().teleport(lobby);
            mensagemJoin(e.getPlayer());
        }, 3);
    }
    
    public void setHeaderAndFooter(Player player) {
        TabAPI.setHeader(player,
                " ",
                "§6§lRocket§e§lMC",
                " ");
        
        TabAPI.setFooter(player,
                " ",
                "   §6§lSITE: §ewww.rocketmc.com.br",
                " ");
    }
    
    public void mensagemJoin(Player p) {
        utilidades.sendCenteredMessage(p, f("&6&l&m---------------------------------------------"));
        utilidades.sendCenteredMessage(p, f("&6&lRocket&e&lMC"));
        utilidades.sendCenteredMessage(p, f(" "));
        utilidades.sendCenteredMessage(p, f("&6&lSITE "));
        utilidades.sendCenteredMessage(p, f("&ewww.rocketmc.com.br "));
        utilidades.sendCenteredMessage(p, f(" "));
        utilidades.sendCenteredMessage(p, f("&6&lDUVIDAS? "));
        utilidades.sendCenteredMessage(p, f("&e/duvida "));
        utilidades.sendCenteredMessage(p, f("&6&l&m---------------------------------------------"));
    }
}
