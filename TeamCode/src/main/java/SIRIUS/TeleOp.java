package SIRIUS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import SIRIUS.UtilClasses.StickyGamepad;

@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "|> TELEOP <|")
public class TeleOp extends LinearOpMode {

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
                robot.collectSpecimen();
            }

            if(stickyGamepad.left_bumper){
                robot.toggleExtendo();

            }

            if(stickyGamepad.x){
                robot.startTransfer();
            }

            if(stickyGamepad.b) {
                if(robot.activeCS == RobotSystems.activeState.SPITTING) {
                    robot.intake.active.stop();
                    robot.intake.goToTransfer();
                    robot.activeCS = RobotSystems.activeState.TRANSFER;
                } else {
                    robot.intake.goToGround();
                    robot.intake.active.outtake();
                    robot.activeCS = RobotSystems.activeState.SPITTING;
                }
            }

            if(gamepad1.right_trigger > 0.2){
                if(robot.outtake.claw.isOpen && robot.collectSpecimenCS == RobotSystems.collectSpecimenState.WAITING_FOR_BUTTON) {
                    robot.buttonPressed = true;
                } else {
                    robot.outtake.lift.goToHighBasket();
                    robot.liftCS = RobotSystems.liftState.SAMPLE;
                }

            }

            if(gamepad1.left_trigger > 0.2){
                robot.outtake.lift.goToHighChamber();
                robot.liftCS = RobotSystems.liftState.SPECIMEN;
            }

            if(gamepad1.a){
                robot.outtake.lift.goToGround();
                robot.liftCS = RobotSystems.liftState.GROUND;
            }
        }
    }
}
