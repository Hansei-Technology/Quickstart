package SIRIUS.outtake.mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import SIRIUS.outtake.OuttakeMap;
import SIRIUS.outtake.OuttakeSettings;

public class OuttakeBar {
    Servo left, right;

    public OuttakeBar(HardwareMap hardwareMap) {
        left = hardwareMap.get(Servo.class, OuttakeMap.barLeft);
        right = hardwareMap.get(Servo.class, OuttakeMap.barRight);

        setPosition(OuttakeSettings.barTransfer);
    }

    public void setPosition(double position) {
        left.setPosition(position);
        right.setPosition(position);
    }


    public void goToSampleScore() {
        setPosition(OuttakeSettings.barSampleScore);
    }

    public void goToSpecimenScore() {
        setPosition(OuttakeSettings.barSpecimenScore);
    }

    public void goToTransfer() {
        setPosition(OuttakeSettings.barTransfer);
    }

    public void goToPreTransfer() {
        setPosition(OuttakeSettings.barPreTransfer);
    }
    public void goToCollectSpecimen() {
        setPosition(OuttakeSettings.barSpecimenCollect);
    }
}
