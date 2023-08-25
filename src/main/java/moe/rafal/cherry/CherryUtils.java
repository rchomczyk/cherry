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

import java.time.Duration;

public final class CherryUtils {

  private static final long TICKS_PER_SECOND = 20;
  private static final long MILLISECONDS_PER_SECOND = 1000;
  private static final long MILLISECONDS_PER_TICK = MILLISECONDS_PER_SECOND / TICKS_PER_SECOND;

  private CherryUtils() {

  }

  public static long getTicksOf(Duration period) {
    return period.toMillis() / MILLISECONDS_PER_TICK;
  }
}
