package SIRIUS.outtake;

import com.qualcomm.robotcore.hardware.HardwareMap;

import SIRIUS.outtake.mechanisms.OuttakeBar;
import SIRIUS.outtake.mechanisms.OuttakeJoint;
import SIRIUS.outtake.mechanisms.OuttakeLift;
import SIRIUS.outtake.mechanisms.OuttakeClaw;

public class OuttakeSubsystem {
    public OuttakeClaw claw;
    public OuttakeJoint joint;
    public OuttakeBar barLeft;
    public OuttakeLift lift;

    public OuttakeSubsystem(HardwareMap hardwareMap) {
        claw = new OuttakeClaw(hardwareMap);
        joint = new OuttakeJoint(hardwareMap);
        barLeft = new OuttakeBar(hardwareMap);
        lift = new OuttakeLift(hardwareMap);
    }

    public void goToSampleScore() {
        joint.goToSampleScore();
        barLeft.goToSampleScore();
    }

    public void goToSpecimenScore() {
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

    public void init(){
        goToPreTransfer();
    }

    public void goToCollectSpecimen(){
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
