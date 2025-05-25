package SIRIUS.intake.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import SIRIUS.intake.IntakeMap;
import SIRIUS.intake.IntakeSettings;

public class Extendo {

    Servo left, right;

    public Extendo(HardwareMap hardwareMap){
        left = hardwareMap.get(Servo.class, IntakeMap.extendoLeft);
        right = hardwareMap.get(Servo.class, IntakeMap.extendoRight);
    }

    public void extend(){
        left.setPosition(IntakeSettings.extendoExtendPos);
        right.setPosition(IntakeSettings.extendoExtendPos);
    }

    public void goToTransfer(){
        left.setPosition(IntakeSettings.extendoTransferPos);
        right.setPosition(IntakeSettings.extendoTransferPos);
    }

    public void retract(){
        left.setPosition(IntakeSettings.extendoRetractPos);
        right.setPosition(IntakeSettings.extendoRetractPos);
    }
}
