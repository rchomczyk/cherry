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

package moe.rafal.cherry.plot;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import moe.rafal.endorfy.facade.EndorfyFacade;
import moe.rafal.endorfy.mapping.EndorfyMapping;
import pl.minecodes.plots.api.plot.PlotApi;
import pl.minecodes.plots.api.plot.PlotServiceApi;

public class PlotController {

  private final PlotServiceApi plotService;
  private final EndorfyFacade endorfyFacade;

  public PlotController(PlotServiceApi plotService, EndorfyFacade endorfyFacade) {
    this.plotService = plotService;
    this.endorfyFacade = endorfyFacade;
  }

  public Collection<PlotApi> getPlotsByWorld(UUID worldUniqueId) {
    return plotService.getPlots().stream()
        .filter(plot -> whetherPlotIsLocatedInWorld(plot, worldUniqueId))
        .toList();
  }

  private boolean whetherPlotIsLocatedInWorld(PlotApi plot, UUID worldUniqueId) {
    final UUID plotWorldUniqueId = plot.getCenter().getWorld().getUID();
    return plotWorldUniqueId.equals(worldUniqueId);
  }

  public CompletableFuture<String> getUsernameOfPlotOwner(PlotApi plot) {
    return endorfyFacade.getMappingByUniqueId(plot.getOwner())
        .thenApply(EndorfyMapping::getUsername);
  }
}
