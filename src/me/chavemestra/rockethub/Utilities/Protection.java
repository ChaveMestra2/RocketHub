/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.chavemestra.rockethub.Utilities;

import static me.chavemestra.rockethub.RocketHub.lobby;
import static me.chavemestra.rockethub.RocketHub.pvp;
import static me.chavemestra.rockethub.Utilities.Chat.f;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 *
 * @author GabrielBatistella
 */
public class Protection implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                Player p = (Player) e.getEntity();
                p.teleport(lobby);
            }
            if (e.getCause() == DamageCause.SUFFOCATION || e.getCause() == DamageCause.FALL
                    || e.getCause() == DamageCause.STARVATION
                    || e.getCause() == DamageCause.DROWNING
                    || e.getCause() == DamageCause.LAVA) {
              e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (!pvp.emPvp(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        e.getEntity().remove();
    }
    @EventHandler
    public void comando(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().hasPermission("rocket.staff")) {
            return;
        }
        Player p = e.getPlayer();
        if (!e.getMessage().startsWith("/tell")
                && !e.getMessage().startsWith("/r ")
                && !e.getMessage().startsWith("/skin ")
                && !e.getMessage().startsWith("/cash")
                && !e.getMessage().startsWith("/youtuber")
                && !e.getMessage().startsWith("/yt")) {
            e.getPlayer().sendMessage(f("&eTem alguma dúvida? Digite &a/duvida&e!"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick2(InventoryClickEvent e) {
        if (e.getWhoClicked().isOp()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}
