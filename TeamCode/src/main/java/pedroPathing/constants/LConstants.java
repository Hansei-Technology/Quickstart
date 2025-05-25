package pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = 0.0029685000494348393;
        ThreeWheelConstants.strafeTicksToInches = 0.003169952196916562;
        ThreeWheelConstants.turnTicksToInches = 0.009012658434116314;
        ThreeWheelConstants.leftY = 8.6;
        ThreeWheelConstants.rightY = -8.6;
        ThreeWheelConstants.strafeX = 0;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "rearLeft";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "rearRight";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "frontRight";
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.rightEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
    }
}




