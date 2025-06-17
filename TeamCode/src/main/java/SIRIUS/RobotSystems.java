package SIRIUS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import SIRIUS.UtilClasses.ServoSmoothing;
import SIRIUS.intake.IntakeSubsystem;
import SIRIUS.outtake.OuttakeSubsystem;

@Config
public class RobotSystems {
    public OuttakeSubsystem outtake;
    public IntakeSubsystem intake;
    public ChassisController chassisController;

    public ElapsedTime transferTimer = new ElapsedTime();
    public ElapsedTime clawTimer = new ElapsedTime();
    public ElapsedTime collectSpecimenTimer = new ElapsedTime();
    public Boolean specimenWasScored = false;
    public ElapsedTime goGroundTimer = new ElapsedTime();


    public static int retractingSlidesTime = 500; // Time to retract slides in milliseconds
    public static int outtakeMovingTime = 200; // Time to move outtake in milliseconds
    public static int closingClawTime = 200; // Time to close claw in milliseconds

    public RobotSystems(HardwareMap hardwareMap) {
        outtake = new OuttakeSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        chassisController = new ChassisController(hardwareMap);
    }

    public void init() {
        outtake.init();
        intake.init();
    }

    public void updateChassisMovement(Gamepad g){
        chassisController.updateMovement(g);
    }

    public enum collectSpecimenState {
        IDLE,
        GETTING_IN_POSITION,
        WAITING_FOR_BUTTON,
        CLOSING_CLAW,
    }
    collectSpecimenState collectSpecimenCS = collectSpecimenState.IDLE;
    public boolean buttonPressed = false;

    public void updateCollectSpecimen(){
        switch (collectSpecimenCS){
            case IDLE:
                break;

            case GETTING_IN_POSITION:
                outtake.goToCollectSpecimen();
                liftCS = liftState.SPECIMEN;
                if(outtake.lift.getCurrentPosition() > 100) {
                    outtake.lift.goToGround();
                }
                collectSpecimenCS = collectSpecimenState.WAITING_FOR_BUTTON;
                break;

            case WAITING_FOR_BUTTON:
                if(buttonPressed){
                    outtake.claw.close();
                    collectSpecimenCS = collectSpecimenState.CLOSING_CLAW;
                    collectSpecimenTimer.reset();
                    buttonPressed = false;
                }
                break;

            case CLOSING_CLAW:
                if(collectSpecimenTimer.milliseconds() > 160) {
                    outtake.lift.goToHighChamber();
                    liftCS = liftState.SPECIMEN;
                    outtake.goToSpecimenScore();
                    collectSpecimenCS = collectSpecimenState.IDLE;
                }
                break;
        }
    }

    public void toggleScoreSpecimen(){
        collectSpecimenCS = collectSpecimenState.GETTING_IN_POSITION;
    }

    public enum extendoState {
        EXTENDED,
        RETRACTED
    }
    extendoState extendoCS = extendoState.RETRACTED;

    public enum activeState {
        UP,
        DOWN,
        TRANSFER,
        SPITTING
    }
    activeState activeCS = activeState.TRANSFER;

    public enum transferStates{
        IDLE,
        RETRACTING_SLIDES,
        OUTTAKE_MOVING,
        CLOSING_CLAW

    }
    transferStates transferCS = transferStates.IDLE;

    public enum liftState {
        GROUND,
        SAMPLE,
        SPECIMEN,
        TRANSFER
    }
    liftState liftCS = liftState.GROUND;



    public void toggleExtendo() {
        if(extendoCS == extendoState.EXTENDED) {
            intake.extendo.retract();
            intake.joint.goToTransfer();
            intake.active.stop();
            extendoCS = extendoState.RETRACTED;
        } else if(extendoCS == extendoState.RETRACTED) {
            intake.extendo.extend();
            extendoCS = extendoState.EXTENDED;
        }
    }

    public void toggleIntake() {
        if (activeCS == activeState.DOWN) {
            intake.goToGround();
            activeCS = activeState.UP;
        } else {
            intake.goToCollect();
            activeCS = activeState.DOWN;
        }
    }

    public void startTransfer() {
        transferCS = transferStates.RETRACTING_SLIDES;
        intake.goToTransfer();
        intake.active.intake();
        intake.extendo.goToTransfer();
        transferTimer.reset();
        outtake.claw.open();
        outtake.goToPreTransfer();
        extendoCS = extendoState.RETRACTED;
    }


    public void updateTransfer() {
        switch (transferCS) {
            case IDLE:
                break;
            case RETRACTING_SLIDES:
                if(transferTimer.milliseconds() > retractingSlidesTime) {
                    outtake.goToTransfer();
                    transferCS = transferStates.OUTTAKE_MOVING;
                    transferTimer.reset();
                }
                break;
            case OUTTAKE_MOVING:
                if(transferTimer.milliseconds() > outtakeMovingTime) {
                        outtake.closeClaw();
                        transferCS = transferStates.CLOSING_CLAW;
                        transferTimer.reset();
                        intake.active.stop();
                        transferCS = transferStates.IDLE;
                }
                break;
            case CLOSING_CLAW:
                if(transferTimer.milliseconds() > closingClawTime) {
                    transferCS = transferStates.IDLE;
                }
                break;
        }
    }

    public void update() {

        if(!outtake.claw.isOpen ) {
            clawTimer.reset();
        }

        updateTransfer();
        updateCollectSpecimen();
        outtake.lift.update();

        if (outtake.lift.currentPos > 200){
        if(outtake.claw.isOpen && clawTimer.milliseconds() > 350) {
                if(liftCS == liftState.SAMPLE){
                outtake.goToPreTransfer();
            } else if(liftCS == liftState.SPECIMEN){
                    outtake.lift.goToGround();
                    if (clawTimer.milliseconds() > 380) {
                        outtake.joint.goToCollectSpecimen();
                        outtake.barLeft.goToCollectSpecimen();
                        outtake.claw.open();
                    }
                    collectSpecimenCS = collectSpecimenState.GETTING_IN_POSITION;
                }
        }


    }}
}
