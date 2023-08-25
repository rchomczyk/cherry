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

import static java.awt.Color.BLACK;
import static java.time.Duration.ofSeconds;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Exclude;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import java.awt.Color;
import java.time.Duration;

@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class LayerConfig extends OkaeriConfig {

  @Exclude
  public static final String LAYER_CONFIG_FILE_NAME = "layer.yml";

  public String markerClickText = "Owner: {username}";

  public String markerHoverText = "{name}";

  public ColorSettings fillSettings = new ColorSettings();

  public ColorSettings strokeSettings = new ColorSettings();

  public Duration markerUpdateInterval = ofSeconds(30);

  @Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
  public static class ColorSettings extends OkaeriConfig {

    public boolean whetherIsVisible = true;

    public Color color = BLACK;

    public int weight = 1;

    public double opacity = 0.7;
  }
}
