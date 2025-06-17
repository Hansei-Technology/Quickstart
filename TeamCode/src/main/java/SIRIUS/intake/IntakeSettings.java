package SIRIUS.intake;

import com.acmerobotics.dashboard.config.Config;

@Config
public abstract class IntakeSettings {
        //Active
        public static double intakeSpeed = 1;
        public static double outtakeSpeed = -0.6;


        //Joint
        public static double jointTransferPos = 0.15;
        public static double jointGroundPos = 0.4;
        public static double jointCollectPos = 0.64;

        //Extendo
        public static double extendoExtendPos = 0.56;
        public static double extendoTransferPos = 0.34;
        public static double extendoRetractPos = 0.36;
}
