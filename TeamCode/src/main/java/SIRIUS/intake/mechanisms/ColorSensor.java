package SIRIUS.intake.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import SIRIUS.intake.IntakeMap;

public class ColorSensor {

    NormalizedColorSensor colorSensor;
    public ColorSensor(HardwareMap hardwareMap){
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, IntakeMap.colorSensor);
    }

    public float getRed(){
        return colorSensor.getNormalizedColors().red;
    }

    public float getGreen(){
        return colorSensor.getNormalizedColors().green;
    }

    public float getBlue(){
        return colorSensor.getNormalizedColors().blue;
    }

    public enum Colors {
        RED,
        YELLOW,
        BLUE,
        UNKNOWN
    }

    /**
     * Computes the hue angle (0–360°) of an RGB color.
     * If the color is grayscale, returns -1.
     */
    private double hueDeg(double r, double g, double b) {
        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));
        double delta = max - min;

        if (delta == 0.0) return -1.0; // grayscale / unknown

        double hue;
        if (max == r) {
            hue = 60 * ((g - b) / delta);
        } else if (max == g) {
            hue = 60 * ((b - r) / delta + 2);
        } else {
            hue = 60 * ((r - g) / delta + 4);
        }

        if (hue < 0) hue += 360;
        return hue;
    }

    /**
     * Classifies an RGB color into RED, YELLOW, BLUE, or UNKNOWN.
     */
    public Colors getColor() {
        double r = colorSensor.getNormalizedColors().red;
        double g = colorSensor.getNormalizedColors().green;
        double b = colorSensor.getNormalizedColors().blue;
        double h = this.hueDeg(r, g, b);
        if (h < 0) return Colors.UNKNOWN;

        if (h < 30 || h > 330) {
            return Colors.RED;
        } else if (h >= 45 && h <= 80) {
            return Colors.YELLOW;
        } else if (h >= 180 && h <= 300) {
            return Colors.BLUE;
        } else {
            return Colors.UNKNOWN;
        }
    }


}
