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

package moe.rafal.cherry.icon;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public class IconController {

  private static final String CHERRY_ICON_KEY_ID_PREFIX = "cherry_icon_%s";
  private static final String PLOT_ICON_ID = "plot";
  private static final String PLOT_ICON_RESOURCE_PATH = "icons/plot.png";
  public static final Key PLOT_ICON_KEY = Key.of(CHERRY_ICON_KEY_ID_PREFIX.formatted(PLOT_ICON_ID));
  public static final int PLOT_ICON_SIZE = 16;
  private final Plugin plugin;

  public IconController(Plugin plugin) {
    this.plugin = plugin;
    this.plugin.saveResource(PLOT_ICON_RESOURCE_PATH, false);
  }

  public void registerCherryIcons() {
    registerCherryIcon(PLOT_ICON_KEY, PLOT_ICON_RESOURCE_PATH);
  }

  private void registerCherryIcon(Key iconKey, String iconResourcePath) {
    try (final InputStream iconStream = requireNonNull(plugin.getResource(iconResourcePath))) {
      SquaremapProvider.get().iconRegistry().register(iconKey, ImageIO.read(iconStream));
    } catch (IOException exception) {
      throw new IconRegistrationException(
          "Could not get cherry icon from resources, because of unexpected exception.",
          exception);
    }
  }
}
