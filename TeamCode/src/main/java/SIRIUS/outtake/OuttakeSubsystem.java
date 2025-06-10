package SIRIUS.outtake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import SIRIUS.RobotSystems;
import SIRIUS.outtake.mechanisms.OuttakeBar;
import SIRIUS.outtake.mechanisms.OuttakeJoint;
import SIRIUS.outtake.mechanisms.OuttakeLift;
import SIRIUS.outtake.mechanisms.OuttakeClaw;
import SIRIUS.outtake.mechanisms.OuttakeTouchSensor;

public class OuttakeSubsystem {
    public OuttakeClaw claw;
    public OuttakeJoint joint;
    public OuttakeBar barLeft;
    public OuttakeLift lift;

    public OuttakeTouchSensor touchSensor;


    public OuttakeSubsystem(HardwareMap hardwareMap) {
        claw = new OuttakeClaw(hardwareMap);
        joint = new OuttakeJoint(hardwareMap);
        barLeft = new OuttakeBar(hardwareMap);
        lift = new OuttakeLift(hardwareMap);
        touchSensor = new OuttakeTouchSensor(hardwareMap);
    }

    public void goToSampleScore() {
        joint.goToSampleScore();
        barLeft.goToSampleScore();
    }

    public void goToSpecimenScore() {
        claw.close();
        lift.goToHighChamber();
        joint.goToSpecimenScore();
        barLeft.goToSpecimenScore();
    }

    public void goToTransfer() {
        joint.goToTransfer();
        barLeft.goToTransfer();
        lift.goToTransfer();
        claw.open();
    }

    public void goToPreTransfer() {
        joint.goToTransfer();
        barLeft.goToPreTransfer();
        lift.goToTransfer();
        claw.open();
    }

    public void goToGround() {
        joint.goToTransfer();
        barLeft.goToTransfer();
        lift.goToGround();
    }
    public void initAutoTruba(){
        joint.goToSampleScore();
        barLeft.goToSampleScore();
        claw.close();
    }

    public void init(){
        goToPreTransfer();
    }

    public void goToCollectSpecimen(){
        lift.goToGround();
            joint.goToCollectSpecimen();
            barLeft.goToCollectSpecimen();
            claw.open();

    }

    public void openClaw() {
        claw.open();
    }

    public void closeClaw() {
        claw.close();
    }



}
