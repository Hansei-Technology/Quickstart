package SIRIUS.outtake.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import SIRIUS.intake.IntakeMap;
import SIRIUS.outtake.OuttakeMap;

public class OuttakeTouchSensor {
    TouchSensor touchSensor;

    public OuttakeTouchSensor(HardwareMap hardwareMap){
        touchSensor = hardwareMap.get(TouchSensor.class, OuttakeMap.touchSensor);
    }

    public boolean isTouch(){
        return touchSensor.isPressed();
    }
    public double getValue(){
        return touchSensor.getValue();
    }
}
