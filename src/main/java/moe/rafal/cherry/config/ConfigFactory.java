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

package moe.rafal.cherry.config;

import static eu.okaeri.configs.ConfigManager.create;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.configurer.Configurer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import java.nio.file.Path;
import java.util.function.Supplier;

public class ConfigFactory {

  private final Path dataPath;
  private final Supplier<Configurer> configurer;

  public ConfigFactory(Path dataPath, Supplier<Configurer> configurer) {
    this.dataPath = dataPath;
    this.configurer = configurer;
  }

  public <T extends OkaeriConfig> T produceConfig(
      Class<T> configClass, String configFileName, OkaeriSerdesPack... serdesPacks) {
    return produceConfig(configClass, dataPath.resolve(configFileName), serdesPacks);
  }

  public <T extends OkaeriConfig> T produceConfig(
      Class<T> configClass, Path configFilePath, OkaeriSerdesPack... serdesPacks) {
    return create(configClass, initializer -> initializer
        .withConfigurer(configurer.get(), serdesPacks)
        .withBindFile(configFilePath)
        .saveDefaults()
        .load(true));
  }
}
