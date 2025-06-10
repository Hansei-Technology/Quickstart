package SIRIUS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class ChassisController {


    DcMotorEx leftFrontMotor;
    DcMotorEx rightFrontMotor;
    DcMotorEx leftRearMotor;
    DcMotorEx rightRearMotor;

    public static double speed = 1;
    public static double rotSpeed = 1;

    public ChassisController(HardwareMap hardwareMap) {

        leftFrontMotor = hardwareMap.get(DcMotorEx.class, "frontLeft");
        rightFrontMotor = hardwareMap.get(DcMotorEx.class, "frontRight");
        leftRearMotor = hardwareMap.get(DcMotorEx.class, "rearLeft");
        rightRearMotor = hardwareMap.get(DcMotorEx.class, "rearRight");

        leftFrontMotor.setDirection(DcMotorEx.Direction.REVERSE);
        leftRearMotor.setDirection(DcMotorEx.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightRearMotor.setDirection(DcMotorEx.Direction.FORWARD);

        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void updateMovement(Gamepad g){

        leftFrontMotor.setPower((-g.left_stick_y + g.left_stick_x*1.09 + g.right_stick_x * rotSpeed) * speed);
        rightFrontMotor.setPower((-g.left_stick_y - g.left_stick_x*1.09 - g.right_stick_x * rotSpeed) * speed);
        leftRearMotor.setPower((-g.left_stick_y - g.left_stick_x*1.09 + g.right_stick_x * rotSpeed) * speed);
        rightRearMotor.setPower((-g.left_stick_y + g.left_stick_x*1.09 - g.right_stick_x * rotSpeed) * speed);

    }
}
