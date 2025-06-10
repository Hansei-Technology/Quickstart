package SIRIUS.outtake.mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import SIRIUS.UtilClasses.PIDController;
import SIRIUS.outtake.OuttakeMap;
import SIRIUS.outtake.OuttakeSettings;

public class OuttakeLift {
    DcMotorEx motor1, motor2;
    PIDController pid;
    public int target = 0, currentPos;
    public OuttakeLift(HardwareMap hardwareMap) {
        motor1 = hardwareMap.get(DcMotorEx.class, OuttakeMap.motor1);
        motor2 = hardwareMap.get(DcMotorEx.class, OuttakeMap.motor2);

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1.setDirection(DcMotor.Direction.REVERSE);

        pid = new PIDController(OuttakeSettings.p, OuttakeSettings.i, OuttakeSettings.d);
        pid.maxOutput = 1;
    }

    public void setPower(double power) {
        motor1.setPower(power);
        motor2.setPower(power);
    }

    public void setTargetPosition(int position) {
        target = position;
        pid.targetValue = target;
    }

    public int getCurrentPosition() {
        return motor1.getCurrentPosition();
    }

    public void goToTransfer() {
        setTargetPosition(OuttakeSettings.liftTransfer);
        pid.maxOutput = 0.6;
    }

    public void goToGround() {
        setTargetPosition(OuttakeSettings.liftGround);
        pid.maxOutput = 0.6;
    }

    public void goToHighBasket() {
        setTargetPosition(OuttakeSettings.liftHighBasket);
        pid.maxOutput = 1;
    }

    public void goToLowBasket() {
        setTargetPosition(OuttakeSettings.liftLowBasket);
    }

    public void goToHighChamber() {
        setTargetPosition(OuttakeSettings.liftHighChamber);
        pid.maxOutput = 1;
    }

    public void goToLowChamber() {
        setTargetPosition(OuttakeSettings.liftLowChamber);
    }

    public void update() {
        currentPos = motor1.getCurrentPosition();
        setPower(pid.update(currentPos));

        pid.p = OuttakeSettings.p;
        pid.i = OuttakeSettings.i;
        pid.d = OuttakeSettings.d;
    }
}
