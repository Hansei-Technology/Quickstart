package SIRIUS;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import SIRIUS.intake.mechanisms.ColorSensor;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;


//O autonomie ce puncteaza un specimen preload si se parcheaza in observation zone



@Config
@Autonomous
public class RightTruba extends LinearOpMode {

    //Robotul
    RobotSystems robot;

    //Componenta ce misca sasiul
    Follower follower;

    //Traiectoriile
    Path preloadPath;
    Path goToPickup1;
    Path drop1;
    Path goToPickup2;
    Path drop2;
    Path goToPickup3;
    Path parkingPath;


    //State-urile autonomiei
    public enum States{
        START,
        MOVING,
        SCORING1,
        GOTOPICKUP1,
        PICKUP1,
        DROP1,
        GOTOPICKUP2,
        PICKUP2,
        DROP2,
        GOTOPICKUP3,
        PARKING,
        END
    }
    States CS = States.START;   //CS = Current State
    States NS = States.START;   //NS = Next State


    //Coordonatele
    //Fiecare traiectorie are un punct x y de start si un punct x y de final
    //De asemenea, o traiectorie are si heading

    //https://pedro-path-generator.vercel.app/ --> generator de traiectorii

    public static double startX = 0, startY = 0, startH = 0; //Punctul de start al robotului
    public static double preloadX = 35, preloadY = 0, preloadH = 0; //Punctul unde robotul va pune specimenul

    public static double pickup1X = 11.5, pickup1Y = -52, pickup1H = 315;

    public static double drop1X = 12, drop1Y = -46, drop1H = -120;
    public static double pikcup2X = 8.2, pickup2Y = -58.2, pickup2H = 315;
    public static double drop2X = 12, drop2Y = -55, drop2H = -120;
    public static double pickup3X = 12, pickup3Y = -55, pickup3H = -100;


    //Timers
    ElapsedTime timer;
    public static double timeToClipSpecimen = 500; //milisecunde
    public static double timeToOpenClaw = 120;
    public static double timeToExtend = 200;
    public static double timeToSuckIn = 2500;
    public static double timeToSpit = 1250;
    public static double timeToFix = 500;


    //Speeds
    public static double maxSpeed = 1;
    public static double mediumSpeed = 0.5;
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

        goToPickup1 = new Path(
                new BezierCurve(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(pickup1X, pickup1Y, Point.CARTESIAN)
                )
        );
        goToPickup1.setLinearHeadingInterpolation(startH, Math.toRadians(pickup1H));

        drop1 = new Path(
                new BezierCurve(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(drop1X, drop1Y, Point.CARTESIAN)
                )
        );
        drop1.setLinearHeadingInterpolation(startH, Math.toRadians(drop1H));

        goToPickup2 = new Path(
                new BezierCurve(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(pikcup2X, pickup2Y, Point.CARTESIAN)
                )
        );
        goToPickup2.setLinearHeadingInterpolation(startH, Math.toRadians(pickup2H));

        drop2 = new Path(
                new BezierCurve(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(drop2X, drop2Y, Point.CARTESIAN)
                )
        );
        drop2.setLinearHeadingInterpolation(startH, Math.toRadians(drop2H));

        goToPickup3 = new Path(
                new BezierCurve(
                        new Point(startX, startY, Point.CARTESIAN),
                        new Point(pickup3X, pickup3Y, Point.CARTESIAN)
                )
        );
        goToPickup3.setLinearHeadingInterpolation(startH, Math.toRadians(pickup3H));

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

            switch (CS){

                case START:
                    robot.outtake.goToSpecimenScore();
                    robot.outtake.lift.goToHighChamber();
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(preloadPath);
                    CS = States.MOVING;
                    NS = States.SCORING1;
                    break;

                case MOVING:
                    //Verifica daca robotul a ajuns la finalul traiectoriei
                    if(!follower.isBusy()){
                        CS = NS; //Daca a ajuns, trece la urmatorul state
                        timer.reset(); //reseteaza timer-ul(cronometrul) pentru urmatorul state

                    }
                    break;

                case SCORING1:
                    if(timer.milliseconds() > timeToClipSpecimen){
                        robot.outtake.claw.open();
                        timer.reset();
                        CS = States.MOVING;
                        NS = States.GOTOPICKUP1;
                    }
                    break;

                case GOTOPICKUP1:
                    robot.outtake.goToCollectSpecimen();
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(goToPickup1);
                    CS = States.MOVING;
                    NS = States.PICKUP1;
                    break;

                case PICKUP1:
                    if (timer.milliseconds() > timeToFix) {
                        robot.intake.extendo.extend();
                        if (timer.milliseconds() > timeToExtend) {
                            robot.intake.goToCollect();
                            if (robot.intake.color.getColor() != ColorSensor.Colors.UNKNOWN || timer.milliseconds() > timeToSuckIn) {
                                robot.intake.active.stop();
                                robot.intake.joint.goToGround();
                                timer.reset();
                                CS = States.MOVING;
                                NS = States.DROP1;

                            }
                        }
                    }
                    break;

                    case DROP1:
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(drop1);
                    if (timer.milliseconds() > timeToFix) {
                        if (timer.milliseconds() > timeToSpit) {
                            robot.intake.active.outtake();
                            CS = States.MOVING;
                            NS = States.GOTOPICKUP2;
                        }
                    }
                    break;

                case GOTOPICKUP2:
                    follower.setMaxPower(mediumSpeed);
                    follower.followPath(goToPickup2);
                    if (timer.milliseconds() > timeToFix){
                        CS = States.MOVING;
                        NS = States.PICKUP2;
                    }
                    break;


                case PICKUP2:
                        robot.intake.goToCollect();
                        if (robot.intake.color.getColor() != ColorSensor.Colors.UNKNOWN || timer.milliseconds() > timeToSuckIn){
                            robot.intake.active.stop();
                            robot.intake.joint.goToGround();
                            CS = States.MOVING;
                            NS = States.DROP2;
                        }
                        break;
                case DROP2:
                    follower.setMaxPower(maxSpeed);
                    follower.followPath(drop1);
                    if (timer.milliseconds() > timeToFix) {
                        if (timer.milliseconds() > timeToSpit) {
                            robot.intake.active.outtake();
                            CS = States.MOVING;
                            NS = States.GOTOPICKUP3;
                        }
                    }
                        break;

                case GOTOPICKUP3:
                            follower.setMaxPower(mediumSpeed);
                            follower.followPath(goToPickup3);
                            if (timer.milliseconds() > timeToFix){
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
