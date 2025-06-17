package SIRIUS;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptAprilTag;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.ftdi.eeprom.FT_EE_232A_Ctrl;

import java.sql.Time;

import SIRIUS.intake.mechanisms.ColorSensor;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;


//O autonomie ce puncteaza un specimen preload si se parcheaza in observation zone


@Config
@Autonomous
public class LeftCos extends LinearOpMode {

    //Robotul
    RobotSystems robot;

    //Componenta ce misca sasiul
    Follower follower;

    //Traiectoriile
    Path preloadPath;
    Path pickupPath1;
    Path scorePath2;
    Path pickup2Path;
    Path scorePath3;
    Path pickupPath3;
    Path scorePath4;


    //State-urile autonomiei
    public enum States{
        START,
        DROP1,
        GOTOPICKUP1,
        PICKUP1,
        GOTOSCORE2,
        SCORE2,
        GOTOPICKUP2,
        PICKUP2,
        GOTOSCORE3,
        SCORE3,
        GOTOPICKUP3,
        PICKUP3,
        GOTOSCORE4,
        SCORE4,
        MOVING,
        END,
        TRANSFER
    }
    States CS = States.START;   //CS = Current State
    States NS = States.START;   //NS = Next State


    //Coordonatele
    //Fiecare traiectorie are un punct x y de start si un punct x y de final
    //De asemenea, o traiectorie are si heading

    //https://pedro-path-generator.vercel.app/ --> generator de traiectorii

    public static double startX = 0, startY = 0, startH = 0.0; //Punctul de start al robotului
    public static double preloadX = 7, preloadY = 25, preloadH = 315; //Punctul unde robotul va pune specimenul

    public static double pickup1X = 15.8, pickup1y = 39, pickup1H = 338;
    public static double score2X = 6, score2Y = 25, score2H = 315;
    public static double pickup2X = 9.4, pickup2Y = 36.8, pickup2H = 1;
    public static double score3X = 3, score3Y = 28, score3H = 315;
    public static double pickup3X = 10.3, pickup3Y = 29, pickup3H = 37;

    public static double score4X = 2, score4Y = 31, score4H = 315;
    //Timers
    ElapsedTime timer;
    public static double timeToDropSample = 250;
    public static double timeToExtend = 400;
    public static double timeToFix = 500;
    public static double timeToTransfer1 = 1000; //milisecunde
    public static double timeToTransfer2 = 2500;

    public static double timeToLift1 = 2000;
    public static double timeToLift2 = 2500;

    public static double timeToSuckIn = 2500;


    //Speeds
    public static double maxSpeed = 1.0;
    public static double mediumSpeed = 0.8;
    public static double slowSpeed = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot = new RobotSystems(hardwareMap);
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(new Pose(startX, startY, Math.toRadians(startH)));
        timer = new ElapsedTime();


        //Generarea traiectoriilor

        preloadPath = new Path(
                new BezierLine(
                        new Point(startX, startY, Point.CARTESIAN),    // Punctul de start
                        new Point(preloadX, preloadY, Point.CARTESIAN) // Punctul de final
                )
        );
        preloadPath.setLinearHeadingInterpolation(startH, Math.toRadians(preloadH));

        pickupPath1 = new Path(
                new BezierLine(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(pickup1X, pickup1y, Point.CARTESIAN)
                )
        );
        pickupPath1.setLinearHeadingInterpolation(startH, Math.toRadians(pickup1H));

        scorePath2 = new Path(
                new BezierLine(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(score2X, score2Y, Point.CARTESIAN)
                )
        );
        scorePath2.setLinearHeadingInterpolation(startH, Math.toRadians(score2H));

        pickup2Path = new Path(
                new BezierLine(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(pickup2X, pickup2Y, Point.CARTESIAN)
                )
        );
        pickup2Path.setLinearHeadingInterpolation(startH, Math.toRadians(pickup2H));

        scorePath3 = new Path(
                new BezierLine(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(score3X, score3Y, Point.CARTESIAN)
                )
        );
        scorePath3.setLinearHeadingInterpolation(startH, Math.toRadians(score3H));
        pickupPath3 = new Path(
                new BezierLine(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(pickup3X, pickup3Y, Point.CARTESIAN)
                )
        );
        pickupPath3.setLinearHeadingInterpolation(startH, Math.toRadians(pickup3H));

        scorePath4 = new Path(
                new BezierLine(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(score4X, score4Y, Point.CARTESIAN)
                )
        );
        scorePath4.setLinearHeadingInterpolation(startH, Math.toRadians(score4H));

        robot.intake.extendo.retract();
        robot.intake.joint.goToGround();
        robot.outtake.barLeft.goToPreTransfer();


        while (opModeInInit()){
            if (timer.milliseconds() > 3000){
                robot.outtake.claw.close();
            }
        }

        while (opModeIsActive()){

            robot.update();
            follower.update();

//            follower.telemetryDebug(telemetry);
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("h", Math.toRadians(follower.getPose().getHeading()));
            telemetry.update();

            switch (CS){

                case START:

                    robot.outtake.claw.close();
                    robot.intake.extendo.retract();
                    robot.intake.joint.goToGround();
                    robot.outtake.goToSampleScore();
                    robot.outtake.lift.goToHighBasket();
                    follower.setMaxPower(maxSpeed); //Seteaza viteza maxima a robotului (are valori intre 0 si 1)
                    follower.followPath(preloadPath);  //Urmeaza traiectoria de preload
                    CS = States.MOVING;
                    NS = States.DROP1;
                    break;

                case MOVING:
                    //Verifica daca robotul a ajuns la finalul traiectoriei
                    if(!follower.isBusy()){
                        CS = NS; //Daca a ajuns, trece la urmatorul state
                        timer.reset(); //reseteaza timer-ul(cronometrul) pentru urmatorul state
                    }
                    break;

                case TRANSFER:
                    if(timer.milliseconds() > timeToTransfer1) {
                        robot.outtake.lift.goToHighBasket();
                        if (timer.milliseconds() > timeToLift1) {
                            robot.outtake.goToSampleScore();
                        }

                    }
                    if(timer.milliseconds() > timeToLift2) {
                        CS = NS;
                        timer.reset();
                    }
                    break;

                    case DROP1:
                        robot.outtake.claw.open();
                        if (timer.milliseconds() > timeToDropSample){
                            robot.outtake.barLeft.goToPreTransfer();
                            robot.outtake.joint.goToTransfer();
                            robot.outtake.lift.goToGround();
                            CS = States.MOVING;
                            NS = States.GOTOPICKUP1;
                        }
                    break;

                case GOTOPICKUP1:
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(pickupPath1);
                    if (timer.milliseconds()> timeToFix) {
                        CS = States.MOVING;
                        NS = States.PICKUP1;
                    }
                    break;

                case PICKUP1:
                    robot.intake.extendo.extend();
                    if (timer.milliseconds() > timeToExtend) {
                        robot.intake.goToCollect();
                    }
                    if (robot.intake.color.getColor() != ColorSensor.Colors.UNKNOWN || timer.milliseconds() > timeToSuckIn){
                        robot.startTransfer();
                        if (timer.milliseconds()> timeToTransfer1) {
                            CS = States.MOVING;
                            NS = States.GOTOSCORE2;
                        }
                    }
                    break;

                case GOTOSCORE2:
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(scorePath2);
                    CS = States.TRANSFER;
                    NS = States.SCORE2;
                    break;


                case SCORE2:
                    robot.outtake.claw.open();
                    if (timer.milliseconds() > timeToDropSample){
                        robot.outtake.barLeft.goToPreTransfer();
                        robot.outtake.joint.goToTransfer();
                        robot.outtake.lift.goToGround();
                        CS = States.MOVING;
                        NS = States.GOTOPICKUP2;
                    }
                    break;
                case GOTOPICKUP2:
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(pickup2Path);
                    if (timer.milliseconds()> timeToFix) {
                        CS = States.MOVING;
                        NS = States.PICKUP2;
                    }
                    break;

                case PICKUP2:
                    robot.intake.extendo.extend();
                    if (timer.milliseconds() > timeToExtend) {
                        robot.intake.goToCollect();
                    }
                    if (robot.intake.color.getColor() != ColorSensor.Colors.UNKNOWN || timer.milliseconds() > timeToSuckIn){
                        robot.startTransfer();
                        if (timer.milliseconds()> timeToTransfer1) {
                            CS = States.MOVING;
                            NS = States.GOTOSCORE3;
                        }
                    }
                    break;

                case GOTOSCORE3:
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(scorePath3);
                    CS = States.TRANSFER;
                    NS = States.SCORE3;
                    break;

                case SCORE3:
                    robot.outtake.claw.open();
                    if (timer.milliseconds() > timeToDropSample){
                        robot.outtake.barLeft.goToPreTransfer();
                        robot.outtake.joint.goToTransfer();
                        robot.outtake.lift.goToGround();
                        CS = States.MOVING;
                        NS = States.GOTOPICKUP3;
                    }
                    break;
                case GOTOPICKUP3:
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(pickupPath3);
                    if (timer.milliseconds()> timeToFix) {
                        CS = States.MOVING;
                        NS = States.PICKUP3;
                    }
                    break;
                case PICKUP3:
                    robot.intake.extendo.extend();
                    if (timer.milliseconds() > timeToExtend) {
                        robot.intake.goToCollect();
                    }
                    if (robot.intake.color.getColor() != ColorSensor.Colors.UNKNOWN || timer.milliseconds() > timeToSuckIn){
                        robot.startTransfer();
                        if (timer.milliseconds()> timeToTransfer1) {
                            CS = States.MOVING;
                            NS = States.GOTOSCORE4;
                        }
                    }
                    break;
                case GOTOSCORE4:
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(scorePath4);
                    CS = States.TRANSFER;
                    NS = States.SCORE4;
                    break;
                case SCORE4:
                    robot.outtake.claw.open();
                    if (timer.milliseconds() > timeToDropSample){
                        robot.outtake.barLeft.goToPreTransfer();
                        robot.outtake.joint.goToTransfer();
                        robot.outtake.lift.goToGround();
                        CS = States.MOVING;
                        NS = States.END;
                    }
                    break;


                case END:
                    //Aici este ultimul state al autonomiei unde robotul nu face nimic
                    break;

            }

        }



    }
}
