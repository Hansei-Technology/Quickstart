package SIRIUS.outtake.mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import SIRIUS.outtake.OuttakeMap;
import SIRIUS.outtake.OuttakeSettings;

public class OuttakeJoint {
    Servo joint;

    public OuttakeJoint(HardwareMap hardwareMap) {
        joint = hardwareMap.get(Servo.class, OuttakeMap.joint);

        joint.setPosition(OuttakeSettings.jointTransfer);
    }

    public void goToSampleScore() {
        joint.setPosition(OuttakeSettings.jointSampleScore);
    }

    public void goToSpecimenScore() {
        joint.setPosition(OuttakeSettings.jointSpecimenScore);
    }

    public void goToTransfer() {
        joint.setPosition(OuttakeSettings.jointTransfer);
    }
    public void goToCollectSpecimen() {
        joint.setPosition(OuttakeSettings.jointCollectSpecimen);
    }
}
