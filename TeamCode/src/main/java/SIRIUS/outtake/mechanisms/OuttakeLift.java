package SIRIUS.outtake.mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import SIRIUS.outtake.OuttakeMap;
import SIRIUS.outtake.OuttakeSettings;

public class OuttakeLift {
    DcMotorEx motor1, motor2;

    public OuttakeLift(HardwareMap hardwareMap) {
        motor1 = hardwareMap.get(DcMotorEx.class, OuttakeMap.motor1);
        motor2 = hardwareMap.get(DcMotorEx.class, OuttakeMap.motor2);

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor2.setDirection(DcMotor.Direction.REVERSE);
    }

    public void setPower(double power) {
        motor1.setPower(power);
        motor2.setPower(power);
    }

    public void setTargetPosition(int position) {
        motor1.setTargetPosition(position);
        motor2.setTargetPosition(position);

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public int getCurrentPosition() {
        return motor1.getCurrentPosition();
    }

    public void goToTransfer() {
        setTargetPosition(OuttakeSettings.liftTransfer);
        setPower(OuttakeSettings.liftPower);
    }

    public void goToGround() {
        setTargetPosition(OuttakeSettings.liftGround);
        setPower(OuttakeSettings.liftPower);
    }

    public void goToHighBasket() {
        setTargetPosition(OuttakeSettings.liftHighBasket);
        setPower(OuttakeSettings.liftPower);
    }

    public void goToLowBasket() {
        setTargetPosition(OuttakeSettings.liftLowBasket);
        setPower(OuttakeSettings.liftPower);
    }

    public void goToHighChamber() {
        setTargetPosition(OuttakeSettings.liftHighChamber);
        setPower(OuttakeSettings.liftPower);
    }

    public void goToLowChamber() {
        setTargetPosition(OuttakeSettings.liftLowChamber);
        setPower(OuttakeSettings.liftPower);
    }
}
