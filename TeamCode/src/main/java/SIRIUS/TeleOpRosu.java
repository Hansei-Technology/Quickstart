package SIRIUS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import SIRIUS.UtilClasses.StickyGamepad;
import SIRIUS.intake.mechanisms.ColorSensor;

@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "|> TELEOP_ROSU <|")
public class TeleOpRosu extends LinearOpMode {

    RobotSystems robot;
    StickyGamepad stickyGamepad;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new RobotSystems(hardwareMap);
        stickyGamepad = new StickyGamepad(gamepad1, this);

        robot.init();

        waitForStart();

        while (opModeIsActive()){
            stickyGamepad.update();
            robot.update();
            robot.updateChassisMovement(gamepad1);

            if(stickyGamepad.right_bumper){
                if(robot.outtake.lift.getCurrentPosition() > 150){
                    robot.outtake.openClaw();
                }
                else {
                    robot.toggleIntake();
                }
            }

            if(stickyGamepad.dpad_up){
                robot.toggleScoreSpecimen();
            }

            if(stickyGamepad.left_bumper){
                robot.toggleExtendo();

            }

            if(stickyGamepad.x){
                robot.startTransfer();
            }

            if(stickyGamepad.b) {
                if(robot.activeCS == RobotSystems.activeState.TRANSFER || robot.activeCS == RobotSystems.activeState.UP) {
                    robot.intake.active.outtake();
                    robot.intake.joint.goToGround();
                    robot.activeCS = RobotSystems.activeState.SPITTING;
                } else if(robot.activeCS == RobotSystems.activeState.DOWN){
                    robot.intake.active.stop();
                    robot.intake.joint.goToTransfer();
                    robot.activeCS = RobotSystems.activeState.TRANSFER;
                }
            }

            if(gamepad1.right_trigger > 0.2){
                if(robot.outtake.claw.isOpen && robot.collectSpecimenCS == RobotSystems.collectSpecimenState.WAITING_FOR_BUTTON) {
                    robot.buttonPressed = true;
                    robot.liftCS = RobotSystems.liftState.SPECIMEN;


                } else {
                    robot.outtake.lift.goToHighBasket();
                    robot.outtake.goToSampleScore();
                    robot.liftCS = RobotSystems.liftState.SAMPLE;
                }

            }

            if(gamepad1.y) {
                robot.outtake.lift.goToHighBasket();
                robot.liftCS = RobotSystems.liftState.SAMPLE;
            }

            if(gamepad1.left_trigger > 0.2){
                robot.outtake.lift.goToHighChamber();
                robot.liftCS = RobotSystems.liftState.SPECIMEN;
            }

            if(gamepad1.a){
                robot.outtake.lift.goToGround();
                robot.liftCS = RobotSystems.liftState.GROUND;
            }

            if ((robot.intake.color.getColor() == ColorSensor.Colors.RED ||
                    robot.intake.color.getColor() == ColorSensor.Colors.YELLOW) &&
                    robot.activeCS == RobotSystems.activeState.DOWN){
                robot.intake.joint.goToTransfer();
                robot.activeCS = RobotSystems.activeState.UP;
            }
            else if (robot.activeCS == RobotSystems.activeState.DOWN && robot.intake.color.getColor() == ColorSensor.Colors.BLUE){
                robot.intake.active.outtake();
                robot.activeCS = RobotSystems.activeState.SPITTING;
            } else if (robot.activeCS == RobotSystems.activeState.SPITTING && robot.intake.color.getColor() == ColorSensor.Colors.UNKNOWN){
                robot.intake.active.intake();
                robot.activeCS = RobotSystems.activeState.DOWN;
            } else if (robot.activeCS == RobotSystems.activeState.SPITTING && robot.intake.color.getColor() != ColorSensor.Colors.UNKNOWN){
                robot.intake.active.outtake();
                robot.activeCS = RobotSystems.activeState.SPITTING;
            }


            telemetry.addData("red", robot.intake.color.getRed());
            telemetry.addData("green", robot.intake.color.getGreen());
            telemetry.addData("blue", robot.intake.color.getBlue() );
            telemetry.addData("color", robot.intake.color.getColor());
            telemetry.update();
        }
    }
}