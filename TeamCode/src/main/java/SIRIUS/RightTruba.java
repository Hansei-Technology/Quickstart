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
    Path parkingPath;


    //State-urile autonomiei
    public enum States{
        START,
        MOVING,
        SCORING1,
        PICKUP1,
        PICKUP2,
        PICKUP3,
        WALLGENERALPOINT,
        SCORING2,
        SCORING3,
        SCORING4,
        SCORING5,
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

    public static double startX = 0, startY = 0, startH = 0; //Punctul de start al robotului
    public static double preloadX = 33, preloadY = 0, preloadH = 0; //Punctul unde robotul va pune specimenul
    public static double parkingX = 136, parkingY = 82, parkingH = 270; //Punctul de parcare al robotului
    public static double parkSafeX = 136, parkSafeY = 124;  //Un safe point; acesta este folosit pentru a genera o traiectorie curbata
    //pentru a intelege mai bine cum functioneaza safe points, intra pe generatorul de traiectorii si apasa pe plusul verde din dreapta unei linii de traiectorie
    //daca te joci putin cu el intelegi repede cum functioneaza



    //Timers
    ElapsedTime timer;
    public static double timeToClipSpecimen = 1000; //milisecunde
    public static double timeToOpenClaw = 120;


    //Speeds
    public static double maxSpeed = 1;
    public static double mediumSpeed = 0.75;
    public static double slowSpeed = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
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
        preloadPath.setConstantHeadingInterpolation(Math.toRadians(preloadH)); // Seteaza un heading constant pe intreaga traiectorie (in acest caz 0 grade)

        parkingPath = new Path(
                new BezierCurve(
                        new Point(preloadX, preloadY, Point.CARTESIAN), // Punctul de start
                        new Point(parkSafeX, parkSafeY, Point.CARTESIAN), //Punctul spre care robotul se va indrepta pe parcursul traiectoriei
                        new Point(parkingX, parkingY, Point.CARTESIAN) // Punctul de final
                )
        );
        parkingPath.setLinearHeadingInterpolation(Math.toRadians(preloadH), Math.toRadians(parkingH));  //Schimba heading-ul pe parcursul traiectoriei(pleaca cu un heading de 0 grade si ajunge la 270)

        waitForStart();
        robot.outtake.initAutoTruba();
        

        while (opModeIsActive()){

            robot.update();
            follower.update();

            switch (CS){

                case START:
                    robot.outtake.goToSpecimenScore();
                    robot.outtake.claw.close();
                    robot.outtake.lift.goToHighChamber();
                    follower.setMaxPower(mediumSpeed); //Seteaza viteza maxima a robotului (are valori intre 0 si 1)
                    follower.followPath(preloadPath);  //Urmeaza traiectoria de preload
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
                    //Teoretic cand robotul intra in acest state el deja a clipsat specimenul pe high chamber
                    //Aici robotul va astepta 1 secunda pentru a fi sigur ca specimenul este clipsat
                    if(timer.milliseconds() > timeToClipSpecimen){
                        robot.outtake.claw.open();
                        timer.reset();
                        CS = States.FINISH_SCORING;
                    }
                    break;

                case FINISH_SCORING:
                    if(timer.milliseconds() > timeToOpenClaw){
                        //Odata ce suntem siguri ca clestele s-a deschis, robotul va urma traiectoria de parcare
                        follower.setMaxPower(slowSpeed);
                        follower.followPath(parkingPath);
                        CS = States.PARKING;
                    }
                    break;

                case PARKING:
                    //Daca robotul a ajuns la jumatatea traiectoriei, atunci sa retraga sistemul de outtake si sa mareasca viteza
                    if(follower.getCurrentTValue() > 0.5){
                        robot.outtake.goToCollectSpecimen();
                        robot.outtake.lift.goToGround();
                        follower.setMaxPower(maxSpeed);
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
