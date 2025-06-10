package pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = 0.0029518052629020266;
        ThreeWheelConstants.strafeTicksToInches = 0.004641863316160653;
        ThreeWheelConstants.turnTicksToInches = 0.002939410948160748;
        ThreeWheelConstants.leftY = 2.835;
        ThreeWheelConstants.rightY = -2.835;
        ThreeWheelConstants.strafeX = -3.74;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "rearLeft";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "rearRight";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "frontRight";
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.rightEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
    }
}




