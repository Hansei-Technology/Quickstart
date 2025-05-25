package SIRIUS.intake.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import SIRIUS.intake.IntakeMap;
import SIRIUS.intake.IntakeSettings;

public class Joint {

    Servo left, right;

    public Joint(HardwareMap hardwareMap){
        left = hardwareMap.get(Servo.class, IntakeMap.jointLeft);
        right = hardwareMap.get(Servo.class, IntakeMap.jointRight);
    }

    public void goToTransfer(){
        left.setPosition(IntakeSettings.jointTransferPos);
        right.setPosition(IntakeSettings.jointTransferPos);
    }

    public void goToCollect(){
        left.setPosition(IntakeSettings.jointCollectPos);
        right.setPosition(IntakeSettings.jointCollectPos);
    }

    public void goToGround(){
        left.setPosition(IntakeSettings.jointGroundPos);
        right.setPosition(IntakeSettings.jointGroundPos);
    }
}
