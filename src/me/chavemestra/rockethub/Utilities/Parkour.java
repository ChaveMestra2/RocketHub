/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.chavemestra.rockethub.Utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;
import static me.chavemestra.rockethub.RocketHub.dbManager;
import static me.chavemestra.rockethub.RocketHub.itemStock;
import static me.chavemestra.rockethub.RocketHub.lobby;
import static me.chavemestra.rockethub.RocketHub.utilidades;
import static me.chavemestra.rockethub.Utilities.Chat.f;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author GabrielBatistella
 */
public class Parkour implements Listener {

    public static HashMap<UUID, Long> parkour;

    public Parkour() {
        parkour = new HashMap();
    }

    public boolean emParkour(Player p) {
        return parkour.containsKey(p.getUniqueId());
    }

    public boolean colocaParkour(Player p) {
        if (!emParkour(p)) {

            //coloco na lista antes pra evitar bugs sinistros
            parkour.put(p.getUniqueId(), System.currentTimeMillis() / 1000);
            p.getInventory().clear();
            p.getInventory().setItem(8, itemStock.sairModo(true, false));
            p.teleport(new Location(Bukkit.getWorld("Lobby"), -7, 38, 13, -64, -5));

            for (Player playerOn : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(playerOn);
            }
            p.sendMessage(f("&2&l[&aParkour&2&l] &eVoce entrou no Parkour! Faça rápido e entre no ranking!"));

            return true;
        } else {
            p.sendMessage(f("&aTá fazendo o que aqui? Corre, rapaz!! &c[Parkour em progresso]"));
        }
        return false;
    }

    public boolean saiuParkour(Player p) {
        if (emParkour(p)) {
            for (Player playerOn : Bukkit.getOnlinePlayers()) {
                p.showPlayer(playerOn);
            }
            //tira us item tudo
            parkour.remove(p.getUniqueId());
            //tiro da lista dps de todo processo pra evitar bugs cabulosos
            p.sendMessage(f("&2&l[&aParkour&2&l] &eVoce saiu do Parkour!"));
            p.teleport(lobby);
            utilidades.setupJoin(p);
            return true;
        }
        return false;
    }

    public void ganhouParkour(Player p) throws SQLException {
        Long tempoHash = parkour.get(p.getUniqueId());
        DecimalFormat df = new DecimalFormat("0.000");
        ResultSet rs = dbManager.executeQuery("tempoParkour", "UUID", p.getUniqueId().toString());
        if (rs.next()) {
        double tempoDb = rs.getDouble("tempoParkour");
        double tempo = (System.currentTimeMillis() / 1000.0 - tempoHash);
        if (tempoDb == 0) {
           dbManager.executeUpdateValue("tempoParkour", Double.parseDouble(df.format(tempo)), "UUID", p.getUniqueId().toString());
            p.sendMessage(f("&2&l[&aParkour&2&l] &eSeu novo recorde é de &b" + df.format(tempo) + " segundos"));
        } else if (tempoDb > tempo) {
           dbManager.executeUpdateValue("tempoParkour", Double.parseDouble(df.format(tempo)), "UUID", p.getUniqueId().toString());
            p.sendMessage(f("&2&l[&aParkour&2&l] &eSeu novo recorde é de &b" + df.format(tempo) + " segundos"));
        }
        Bukkit.broadcastMessage(f("&2&l[&aParkour&2&l] &eO jogador " + utilidades.getGrupo(p) + "&f" + p.getName() + ""
                + "&e venceu o Parkour em &b" + df.format(tempo) + " segundos"));
        saiuParkour(p);
        }
    }

    @EventHandler
    public void onPlaca(PlayerInteractEvent e) throws SQLException {
        Player p = (Player) e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getState() instanceof Sign) {
                Sign s = (Sign) e.getClickedBlock().getState();
                if (s.getLine(0).contains(f("Parkour"))) {
                    colocaParkour(p);
                }
                if (s.getLine(0).contains(f("FINALIZAR!")) && emParkour(p)) {
                    ganhouParkour(p);
                }
            }
        }
    }

}
