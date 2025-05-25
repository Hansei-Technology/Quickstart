package SIRIUS.outtake.mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import SIRIUS.outtake.OuttakeMap;
import SIRIUS.outtake.OuttakeSettings;

public class OuttakeClaw {
    Servo claw;
    public Boolean isOpen = false;

    public OuttakeClaw(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, OuttakeMap.claw);

        claw.setPosition(OuttakeSettings.clawOpen);
        isOpen = false;
    }

    public void open() {
        claw.setPosition(OuttakeSettings.clawOpen);
        isOpen = false;
    }


    public void close() {
        claw.setPosition(OuttakeSettings.clawClose);
        isOpen = true;
    }

}
