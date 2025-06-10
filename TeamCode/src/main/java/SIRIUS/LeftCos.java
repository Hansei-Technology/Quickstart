package SIRIUS;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    Path collect1Path;


    //State-urile autonomiei
    public enum States{
        START,
        MOVING,
        SCORING1,
        PICKUP1,
        PICKUP2,
        PICKUP3,
        SCORING2,
        SCORING3,
        SCORING4,
        FINISH_SCORING,
        PARKING,
        END
    }
    States CS = States.START;   //CS = Current State
    States NS = States.START;   //NS = Next State


    //Coordonatele
    //Fiecare traiectorie are un punct x y de start si un punct x y de final
    //De asemenea, o traiectorie are si heading

    //https://pedro-path-generator.vercel.app/ --> generator de traiectorii

    public static double startX = 0, startY = 0, startH = 0.0; //Punctul de start al robotului
    public static double preloadX = 5, preloadY = 19, preloadH = -0.6; //Punctul unde robotul va pune specimenul
    public static double pickUp1X = 5.6, pickUp1Y = 18.3, pickUp1H = 0.0;
    public static double pickUp2X = 0.0, pickup2Y = 0.0, pickUp2H = 0.0;
    public static double parkingX = 136, parkingY = 82, parkingH = 270; //Punctul de parcare al robotului
    public static double parkSafeX = 136, parkSafeY = 124;  //Un safe point; acesta este folosit pentru a genera o traiectorie curbata
    //pentru a intelege mai bine cum functioneaza safe points, intra pe generatorul de traiectorii si apasa pe plusul verde din dreapta unei linii de traiectorie
    //daca te joci putin cu el intelegi repede cum functioneaza



    //Timers
    ElapsedTime timer;
    public static double timeToClipSpecimen = 500; //milisecunde
    public static double timeToOpenClaw = 120;
    public static double timeToCollectSample = 2000;
    public static double timeToCollectSpecimen = 2000;


    //Speeds
    public static double maxSpeed = 1;
    public static double mediumSpeed = 0.75;
    public static double slowSpeed = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new RobotSystems(hardwareMap);
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(new Pose(startX, startY, startH));
        timer = new ElapsedTime();


        //Generarea traiectoriilor

        preloadPath = new Path(
                new BezierLine(
                        new Point(startX, startY, Point.CARTESIAN),    // Punctul de start
                        new Point(preloadX, preloadY, Point.CARTESIAN) // Punctul de final
                )
        );
        preloadPath.setLinearHeadingInterpolation(startH, preloadH); // Seteaza un heading constant pe intreaga traiectorie (in acest caz 0 grade)

        collect1Path = new Path(
                new BezierLine(
                        new Point(preloadX, preloadY, Point.CARTESIAN), // Punctul de start
                        new Point(pickUp1X, pickUp1Y, Point.CARTESIAN) // Punctul de final
                )
        );
        collect1Path.setLinearHeadingInterpolation(Math.toRadians(preloadH), Math.toRadians(pickUp1H));  //Schimba heading-ul pe parcursul traiectoriei(pleaca cu un heading de 0 grade si ajunge la 270)

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

                    robot.outtake.claw.close();
                    robot.intake.extendo.goToTransfer();
                    robot.intake.joint.goToGround();
                    robot.outtake.goToSampleScore();
                    robot.outtake.lift.goToHighBasket();
                    follower.setMaxPower(maxSpeed); //Seteaza viteza maxima a robotului (are valori intre 0 si 1)
                    follower.followPath(preloadPath, false);  //Urmeaza traiectoria de preload
                    CS = States.MOVING;
                    NS = States.END;
                    break;

                case MOVING:
                    //Verifica daca robotul a ajuns la finalul traiectoriei
                    if(!follower.isBusy()){
                        CS = NS; //Daca a ajuns, trece la urmatorul state
                        timer.reset(); //reseteaza timer-ul(cronometrul) pentru urmatorul state
                    }
                    break;

                case SCORING1:
                    robot.outtake.claw.open();
                    if (timer.milliseconds() > timeToClipSpecimen){
                        robot.intake.goToCollect();
                        robot.outtake.goToPreTransfer();
                        robot.intake.extendo.extend();
                        follower.setMaxPower(maxSpeed); //Seteaza viteza maxima a robotului (are valori intre 0 si 1)
                        follower.followPath(collect1Path);
                        CS = States.MOVING;
                        NS = States.PICKUP1;
                    }
                    break;

                case PICKUP1:
                    if(robot.intake.color.getColor() != ColorSensor.Colors.UNKNOWN) {
                        robot.outtake.goToPreTransfer();
                        robot.startTransfer();
                        robot.outtake.goToSampleScore();
                        CS = States.MOVING;
                        NS = States.END;
                    }

                    break;
                case SCORING2:
                    robot.outtake.claw.open();
                    if (timer.milliseconds() > timeToClipSpecimen){
                        robot.intake.goToCollect();
                        robot.outtake.goToPreTransfer();
                        robot.intake.extendo.extend();
                        follower.setMaxPower(maxSpeed); //Seteaza viteza maxima a robotului (are valori intre 0 si 1)
                        follower.followPath(collect1Path, false);
                        CS = States.MOVING;
                        NS = States.END;
                    }

                case END:
                    //Aici este ultimul state al autonomiei unde robotul nu face nimic
                    break;

            }

        }



    }
}
