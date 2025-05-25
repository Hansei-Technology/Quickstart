package SIRIUS.intake;

import com.acmerobotics.dashboard.config.Config;

@Config
public abstract class IntakeSettings {
        //Active
        public static double intakeSpeed = 0.7;
        public static double outtakeSpeed = -0.7;


        //Joint
        public static double jointTransferPos = 0.095;
        public static double jointGroundPos = 0.4;
        public static double jointCollectPos = 0.64;


        //Extendo
        public static double extendoExtendPos = 0.52;
        public static double extendoTransferPos = 0.3;
        public static double extendoRetractPos = 0.29;
}
