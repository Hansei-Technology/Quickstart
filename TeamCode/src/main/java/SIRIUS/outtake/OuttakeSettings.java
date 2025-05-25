package SIRIUS.outtake;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OuttakeSettings {

    public static double barTransfer = 0.35;
    public static double barSampleScore = 0.79;
    public static double barSpecimenScore = 0.4;
    public static double barPreTransfer = 0.42;
    public static double barSpecimenCollect = 1;

    //claw pos
    public static double clawOpen = 0.29;
    public static double clawClose = 0.51;

    //joint pos
    public static double jointTransfer = 0.08;
    public static double jointSampleScore = 0.82;
    public static double jointSpecimenScore = 0.4;
    public static double jointCollectSpecimen = 0.7;

    //lift pos
    public static int liftGround = 0;
    public static int liftTransfer = 0;
    public static int liftHighChamber = 600;
    public static int liftLowChamber = 350;
    public static int liftHighBasket = 1000;
    public static int liftLowBasket = 500;

    public static double liftPower = 1.0;
}
