package SIRIUS.intake.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import SIRIUS.intake.IntakeMap;
import SIRIUS.intake.IntakeSettings;

public class Active {

    DcMotorEx motor;

    public Active(HardwareMap hardwareMap){
        motor = hardwareMap.get(DcMotorEx.class, IntakeMap.active);
        motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void intake(){
        motor.setPower(IntakeSettings.intakeSpeed);
    }

    public void stop(){
        motor.setPower(0.0);
    }

    public void outtake(){
        motor.setPower(IntakeSettings.outtakeSpeed);
    }

}
