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

import static moe.rafal.cherry.config.StringToColorTransformerUtils.mutateColorIntoHex;
import static moe.rafal.cherry.config.StringToColorTransformerUtils.mutateHexIntoColor;

import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import java.awt.Color;
import org.jetbrains.annotations.NotNull;

public class StringToColorTransformer extends BidirectionalTransformer<String, Color> {

  @Override
  public GenericsPair<String, Color> getPair() {
    return genericsPair(String.class, Color.class);
  }

  @Override
  public Color leftToRight(@NotNull String data, @NotNull SerdesContext serdesContext) {
    return mutateHexIntoColor(data);
  }

  @Override
  public String rightToLeft(@NotNull Color data, @NotNull SerdesContext serdesContext) {
    return mutateColorIntoHex(data);
  }
}
