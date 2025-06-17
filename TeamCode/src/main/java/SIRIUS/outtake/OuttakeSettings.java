package SIRIUS.outtake;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OuttakeSettings {

    public static double barTransfer = 0.33;
    public static double barSampleScore = 0.86;
    public static double barSpecimenScore = 0.4;
    public static double barPreTransfer = 0.49;
    public static double barSpecimenCollect = 1;

    //claw pos
    public static double clawOpen = 0.4;
    public static double clawClose = 0.54;

    //joint pos
    public static double jointTransfer = 0.18;
    public static double jointSampleScore = 0.97;
    public static double jointSpecimenScore = 0.4;
    public static double jointCollectSpecimen = 0.9;

    //lift pos
    public static int liftGround = 0;
    public static int liftTransfer = 0;
    public static int liftHighChamber = 520;
    public static int liftLowChamber = 350;
    public static int liftHighBasket = 1650;
    public static int liftLowBasket = 500;

    public static double liftPower = 1.0;

    public static double p = 0.02;
    public static double i = 0.001;
    public static double d = 0.002;


    public static double forwardHang = 1.0;
    public static double backwardsHang = -1.0;

    public static double stop = 0.0;

    public static double forwardLimit = 200;
    public static double backwardLimit = -200;
}
