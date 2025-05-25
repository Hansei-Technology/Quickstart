package SIRIUS.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;

import SIRIUS.intake.mechanisms.Active;
import SIRIUS.intake.mechanisms.Extendo;
import SIRIUS.intake.mechanisms.Joint;

public class IntakeSubsystem {

    public Joint joint;
    public Extendo extendo;
    public Active active;

    public IntakeSubsystem(HardwareMap hardwareMap){
        joint = new Joint(hardwareMap);
        extendo = new Extendo(hardwareMap);
        active = new Active(hardwareMap);
    }

    public void goToTransfer(){
        joint.goToTransfer();
        extendo.goToTransfer();
        active.stop();
    }

    public void init(){
        joint.goToTransfer();
        extendo.retract();
    }

    public void goToGround() {
        joint.goToGround();
        active.stop();
    }

    public void goToCollect() {
        joint.goToCollect();
        active.intake();
    }

}
