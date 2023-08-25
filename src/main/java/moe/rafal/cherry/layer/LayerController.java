/*
 *     Copyright (C) 2023 cherry
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package moe.rafal.cherry.layer;

import static java.time.Duration.ZERO;
import static moe.rafal.cherry.CherryUtils.getTicksOf;
import static xyz.jpenilla.squaremap.api.BukkitAdapter.bukkitWorld;

import java.util.UUID;
import moe.rafal.cherry.plot.PlotController;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public class LayerController {

  private static final String CHERRY_LAYER_LABEL = "cherry";
  private static final String CHERRY_LABEL_KEY_ID_PREFIX = "cherry_%s";

  private final Plugin plugin;
  private final Server server;
  private final LayerConfig layerConfig;
  private final PlotController plotController;

  public LayerController(
      Plugin plugin, Server server, LayerConfig layerConfig, PlotController plotController) {
    this.plugin = plugin;
    this.server = server;
    this.layerConfig = layerConfig;
    this.plotController = plotController;
  }

  public void registerMapLayers() {
    SquaremapProvider.get().mapWorlds().forEach(this::registerMapLayer);
  }

  public void registerMapLayer(MapWorld mapWorld) {
    final UUID worldUniqueId = bukkitWorld(mapWorld).getUID();
    final SimpleLayerProvider worldLayerProvider = getMapLayerProvider();
    mapWorld.layerRegistry()
        .register(
            Key.of(CHERRY_LABEL_KEY_ID_PREFIX.formatted(worldUniqueId)),
            worldLayerProvider);
    server.getScheduler().runTaskTimerAsynchronously(plugin,
        new LayerUpdateScheduler(worldUniqueId, layerConfig, plotController, worldLayerProvider),
        getTicksOf(ZERO),
        getTicksOf(layerConfig.markerUpdateInterval));
  }

  public SimpleLayerProvider getMapLayerProvider() {
    return SimpleLayerProvider.builder(CHERRY_LAYER_LABEL).build();
  }
}
