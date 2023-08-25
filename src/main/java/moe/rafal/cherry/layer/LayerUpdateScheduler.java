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

import static java.util.Optional.ofNullable;
import static moe.rafal.cherry.icon.IconController.PLOT_ICON_KEY;
import static moe.rafal.cherry.icon.IconController.PLOT_ICON_SIZE;
import static org.bukkit.Bukkit.getWorld;
import static xyz.jpenilla.squaremap.api.marker.Marker.icon;
import static xyz.jpenilla.squaremap.api.marker.Marker.rectangle;

import java.util.UUID;
import moe.rafal.cherry.plot.PlotController;
import org.bukkit.Location;
import org.bukkit.World;
import pl.minecodes.plots.api.plot.PlotApi;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import xyz.jpenilla.squaremap.api.marker.Rectangle;

public class LayerUpdateScheduler implements Runnable {

  private static final String CHERRY_MARKER_KEY_ID_PREFIX = "cherry_%s_plot_%d";
  private static final String CHERRY_MARKER_KEY_ICON_SUFFIX = "_icon";
  private static final String PLOT_NAME_VARIABLE_NAME = "{name}";
  private static final String PLOT_OWNER_USERNAME_VARIABLE_NAME = "{username}";
  private final UUID worldUniqueId;
  private final LayerConfig layerConfig;
  private final PlotController plotController;
  private final SimpleLayerProvider plotLayer;

  public LayerUpdateScheduler(
      UUID worldUniqueId, LayerConfig layerConfig, PlotController plotController,
      SimpleLayerProvider plotLayer) {
    this.worldUniqueId = worldUniqueId;
    this.layerConfig = layerConfig;
    this.plotController = plotController;
    this.plotLayer = plotLayer;
  }

  @Override
  public void run() {
    plotLayer.clearMarkers();
    for (final PlotApi plot : plotController.getPlotsByWorld(worldUniqueId)) {
      plotLayer.addMarker(
          Key.of(CHERRY_MARKER_KEY_ID_PREFIX.formatted(worldUniqueId, plot.getId()) + CHERRY_MARKER_KEY_ICON_SUFFIX),
          getStylizedIconMarker(plot));
      plotLayer.addMarker(
          Key.of(CHERRY_MARKER_KEY_ID_PREFIX.formatted(worldUniqueId, plot.getId())),
          getStylizedPlotMarker(plot));
    }
  }

  private Marker getStylizedPlotMarker(PlotApi plot) {
    return getRegionMarkerWithStyling(
        getRegionMarker(
            plot,
            ofNullable(getWorld(worldUniqueId))
                .orElseThrow(() -> new LayerRenderingException(
                    "Could not get world with specified %s unique id.".formatted(worldUniqueId))),
            plot.getRegion().getSize()));
  }

  private Marker getStylizedIconMarker(PlotApi plot) {
    return getIconMarkerWithStyling(
        getIconMarker(plot),
        plot,
        plotController.getUsernameOfPlotOwner(plot).join());
  }

  private Icon getIconMarker(PlotApi plot) {
    return icon(
        Point.of(
            plot.getCenter().getX(),
            plot.getCenter().getZ()),
        PLOT_ICON_KEY, PLOT_ICON_SIZE);
  }

  private Icon getIconMarkerWithStyling(
      Icon baseMarker, PlotApi plot, String usernameOfPlotOwner) {
    baseMarker.markerOptions(MarkerOptions.builder()
        .clickTooltip(layerConfig.markerClickText
            .replace(PLOT_OWNER_USERNAME_VARIABLE_NAME, usernameOfPlotOwner))
        .hoverTooltip(layerConfig.markerHoverText
            .replace(PLOT_NAME_VARIABLE_NAME, plot.getName()))
        .build());
    return baseMarker;
  }


  private Rectangle getRegionMarker(PlotApi plot, World world, double radius) {
    final double radiusOffset = 1D;

    final Location plotUpperCorner = new Location(
        world, plot.getRegion().getX() + radius, world.getMaxHeight(),
        plot.getRegion().getZ() + radius);
    final Location plotLowerCorner = new Location(
        world, plot.getRegion().getX() - radius, world.getMinHeight(),
        plot.getRegion().getZ() - radius);

    return rectangle(
        Point.of(
            plotLowerCorner.getX(),
            plotLowerCorner.getZ()),
        Point.of(
            plotUpperCorner.getX() + radiusOffset,
            plotUpperCorner.getZ() + radiusOffset));
  }

  private Rectangle getRegionMarkerWithStyling(Rectangle baseMarker) {
    baseMarker.markerOptions(MarkerOptions.builder()
        .fill(layerConfig.fillSettings.whetherIsVisible)
        .fillColor(layerConfig.fillSettings.color)
        .fillOpacity(layerConfig.fillSettings.opacity)
        .stroke(layerConfig.strokeSettings.whetherIsVisible)
        .strokeColor(layerConfig.strokeSettings.color)
        .strokeWeight(layerConfig.strokeSettings.weight)
        .strokeOpacity(layerConfig.strokeSettings.opacity));
    return baseMarker;
  }
}
