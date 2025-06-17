package SIRIUS.outtake.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import SIRIUS.RobotSystems;
import SIRIUS.outtake.OuttakeMap;
import SIRIUS.outtake.OuttakeSettings;

public class OuttakeHang {
    DcMotor hang;

    public OuttakeHang(HardwareMap hardwareMap) {
        hang = hardwareMap.get(DcMotor.class, OuttakeMap.hang);
        hang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void setPower(double power){
        hang.setPower(power);
    }




    public void forward(){
        setPower(OuttakeSettings.forwardHang);
    }

    public void reverse(){
        setPower(OuttakeSettings.backwardsHang);
    }

    public void stop(){
        setPower(OuttakeSettings.stop);
    }
}
