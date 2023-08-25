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

package moe.rafal.cherry;

import static java.util.Optional.ofNullable;
import static moe.rafal.cherry.layer.LayerConfig.LAYER_CONFIG_FILE_NAME;

import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import moe.rafal.cherry.config.ConfigFactory;
import moe.rafal.cherry.config.SerdesCherry;
import moe.rafal.cherry.icon.IconController;
import moe.rafal.cherry.layer.LayerConfig;
import moe.rafal.cherry.layer.LayerController;
import moe.rafal.cherry.plot.PlotController;
import moe.rafal.endorfy.facade.EndorfyFacade;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pl.minecodes.plots.api.plot.PlotServiceApi;

public class CherryBukkitPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    ConfigFactory configFactory = new ConfigFactory(getDataFolder().toPath(),
        YamlBukkitConfigurer::new);
    LayerConfig layerConfig = configFactory.produceConfig(LayerConfig.class, LAYER_CONFIG_FILE_NAME,
        new SerdesCherry(), new SerdesCommons());

    IconController iconController = new IconController(this);
    iconController.registerCherryIcons();

    LayerController layerController = new LayerController(this, getServer(), layerConfig,
        new PlotController(
            getRegisteredServiceOrThrow(PlotServiceApi.class),
            getRegisteredServiceOrThrow(EndorfyFacade.class)));
    layerController.registerMapLayers();
  }

  private <T> T getRegisteredServiceOrThrow(Class<T> serviceClass) {
    return ofNullable(getServer().getServicesManager().getRegistration(serviceClass))
        .map(RegisteredServiceProvider::getProvider)
        .orElseThrow(() -> new CherryInitializationException(
            "Could not initialize cherry, because of missing service registration."));
  }
}
