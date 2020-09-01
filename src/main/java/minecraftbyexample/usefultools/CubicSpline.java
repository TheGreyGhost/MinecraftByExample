package minecraftbyexample.usefultools;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import net.minecraft.util.math.MathHelper;

import java.util.List;

/**
 * Performs spline interpolation given a set of control points.
 *
 * Cubic splines are used to smoothly interpolate between points on a curve
 * They are great for animation
 * For more information on cubic splines, see wikipedia cubic splines
 * https://en.wikipedia.org/wiki/Spline_interpolation
 *
 * Typical usage:
 * 1) Create an array of points that you want to interpolate [t,x], where x is a function of t; for example
 *    a graph of x position over time.  Must be in ascending order of t.
 * 2) call createCubicSpline to create the spline for those points [t,x]
 * 3) call interpolate to calculate the interpolated x for any given t
 * 4) call interpolateFirstDerivative to calculate the first derivative of the interpolated x for any given t
 *
 */
public class CubicSpline {

  /**
   * Creates a monotone cubic spline from a given set of control points.
   *
   * The spline is guaranteed to pass through each control point exactly. Moreover, assuming the control points are
   * monotonic then the interpolated values will also be monotonic.
   *
   * This function uses the Fritsch-Carlson method for computing the spline parameters.
   * http://en.wikipedia.org/wiki/Monotone_cubic_interpolation
   *
   * @param t
   *            The t values of the control points, strictly increasing.
   * @param x
   *            The t values corresponding to the given values of t
   * @return
   *
   * @throws IllegalArgumentException
   *             if the t or t arrays are null, have different lengths or have fewer than 2 values.
   */
  public static CubicSpline createCubicSpline(List<Float> t, List<Float> x) {
    if (t == null || x == null || t.size() != x.size() || t.size() < 2) {
      throw new IllegalArgumentException("There must be at least two control "
              + "points and the arrays must be of equal length.");
    }

    final int n = t.size();
    float[] d = new float[n - 1]; // could optimize this out
    float[] m = new float[n];

    // Compute slopes of secant lines between successive points.
    for (int i = 0; i < n - 1; i++) {
      float h = t.get(i + 1) - t.get(i);
      if (h <= 0f) {
        throw new IllegalArgumentException("The control points must all "
                + "have strictly increasing t values.");
      }
      d[i] = (x.get(i + 1) - x.get(i)) / h;
    }

    // Initialize the tangents as the average of the secants.
    m[0] = d[0];
    for (int i = 1; i < n - 1; i++) {
      m[i] = (d[i - 1] + d[i]) * 0.5f;
    }
    m[n - 1] = d[n - 2];

    // Update the tangents to preserve monotonicity.
    for (int i = 0; i < n - 1; i++) {
      if (d[i] == 0f) { // successive Y values are equal
        m[i] = 0f;
        m[i + 1] = 0f;
      } else {
        float a = m[i] / d[i];
        float b = m[i + 1] / d[i];
        float h = (float) Math.hypot(a, b);
        if (h > 9f) {
          float u = 3f / h;
          m[i] = u * a * d[i];
          m[i + 1] = u * b * d[i];
        }
      }
    }
    return new CubicSpline(t, x, m);
  }

  /**
   * Interpolates the value of X = f(t) for given t. Clamps t to the domain of the spline.
   *
   * @param t The t value.
   * @return The interpolated X = f(t) value.
   */
  public float interpolate(float t) {
    // Handle the boundary cases.
    final int n = mT.size();
    if (Float.isNaN(t)) {
      return t;
    }
    if (t <= mT.get(0)) {
      return mX.get(0);
    }
    if (t >= mT.get(n - 1)) {
      return mX.get(n - 1);
    }

    // Find the index 'i' of the last point with smaller t.
    // We know this will be within the spline due to the boundary tests.
    int i = 0;
    while (t >= mT.get(i + 1)) {
      i += 1;
      if (t == mT.get(i)) {
        return mX.get(i);
      }
    }

    // Perform cubic Hermite spline interpolation.
    float h = mT.get(i + 1) - mT.get(i);
    float u = (t - mT.get(i)) / h;
    return (mX.get(i) * (1 + 2 * u) + h * mM[i] * u) * (1 - u) * (1 - u)
            + (mX.get(i + 1) * (3 - 2 * u) + h * mM[i + 1] * (u - 1)) * u * u;
  }

  /**
   * Interpolates the value of t = f'(t) for given t, i.e. the first derivative. Clamps t to the domain of the spline.
   *
   * @param t The t value.
   * @return The interpolated t = f'(t) value.
   */
  public float interpolateFirstDerivative(float t) {
    // Handle the boundary cases.
    final int n = mT.size();
    if (Float.isNaN(t)) {
      return t;
    }
    t = MathHelper.clamp(t, mT.get(0), mT.get(n-1));

    // Find the index 'i' of the last point with smaller X.
    // We know this will be within the spline due to the boundary tests.
    int i = 0;
    while (t > mT.get(i + 1)) {
      i += 1;
    }

    // Perform cubic Hermite spline interpolation.
    float h = mT.get(i + 1) - mT.get(i);
    float u = (t - mT.get(i)) / h;
    return ( 3*h*mM[i + 1] - 6*mX.get(i+1) + 3*h*mM[i] + 6*mX.get(i)) * u * u +
           (-2*h*mM[i + 1] + 6*mX.get(i+1) - 4*h*mM[i] - 6*mX.get(i)) * u +
            h*mM[i];
  }

  // For debugging.
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    final int n = mT.size();
    str.append("[");
    for (int i = 0; i < n; i++) {
      if (i != 0) {
        str.append(", ");
      }
      str.append("(").append(mT.get(i));
      str.append(", ").append(mX.get(i));
      str.append(": ").append(mM[i]).append(")");
    }
    str.append("]");
    return str.toString();
  }

  private final List<Float> mT;
  private final List<Float> mX;
  private final float[] mM;

  private CubicSpline(List<Float> t, List<Float> y, float[] m) {
    mT = t;
    mX = y;
    mM = m;
  }
}

