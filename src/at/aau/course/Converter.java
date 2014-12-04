package at.aau.course;

import java.awt.Color;

public class Converter {

    public static double divFactor;

    public static int colorToGrayScale(Color c) {
        return (int) ((c.getRed() * 0.299) + (c.getGreen() * 0.587) + (c
                .getBlue() * 0.114));
    }

    public static double[] RGBToLab(int R, int G, int B) {

        double[] values = new double[3];

        // normalization of a red, a green and a blue color
        double rLinear = (double) R / 255.0;
        double gLinear = (double) G / 255.0;
        double bLinear = (double) B / 255.0;

        // convert to a sRGB form
        double r = (rLinear > 0.04045) ? Math.pow((rLinear + 0.055) / (1.055), 2.2) : (rLinear / 12.92);
        double g = (rLinear > 0.04045) ? Math.pow((gLinear + 0.055) / (1.055), 2.2) : (rLinear / 12.92);
        double b = (rLinear > 0.04045) ? Math.pow((bLinear + 0.055) / (1.055), 2.2) : (rLinear / 12.92);

        // convert to CIEXYZ
        double x, y, z;
        x = r * 0.4124 + g * 0.3576 + b * 0.1805;
        y = r * 0.2126 + g * 0.7152 + b * 0.0722;
        z = r * 0.0193 + g * 0.1192 + b * 0.9505;

        // CIEXYZ D65 = new CIEXYZ(0.9505, 1.0, 1.0980)
        // C_L
        values[0] = 116.0 * Fxyz(y / 1.0) - 16;
        values[1] = 116.0 * Fxyz(y / 1.0) - 16;
        values[2] = 116.0 * Fxyz(y / 1.0) - 16;

        return values;
    }

    private static double Fxyz(double t) {
        return ((t > 0.008856) ? Math.pow(t, (1.0 / 3.0)) : (7.787 * t + 16.0 / 116.0));
    }

    public static double[] RGBtoHSV(int r, int g, int b) {
        double min, max, delta;
        double h, s, v;
        double result[] = new double[3];

        // max
        max = r;
        if (max < g) {
            max = g;
        }
        if (max < b) {
            max = b;
        }
        v = max;

        // min
        min = r;
        if (min > g) {
            min = g;
        }
        if (min > b) {
            min = b;
        }

        // min
        delta = max - min;
        if (max != 0) {
            s = delta / max;		// s
        } else {
            // r = g = b = 0		// s = 0, v is undefined
            s = 0;
            h = -1.0;
            result[0] = h;
            result[1] = s;
            result[2] = v;
            return result;
        }
        if (r == max) {
            h = (g - b) / delta;		// between yellow & magenta
        } else if (g == max) {
            h = 2 + (b - r) / delta;	// between cyan & yellow
        } else {
            h = 4 + (r - g) / delta;	// between magenta & cyan
        }
        h *= 60;				// degrees
        if (h < 0) {
            h += 360;
        }
        result[0] = h;
        result[1] = s;
        result[2] = v;
        return result;
    }
}
