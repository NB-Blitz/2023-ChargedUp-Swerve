// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    /*************** Drivetrain ***************/
    // Left-to-right distance between the drivetrain wheels
    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 18.5;

    // Front-to-back distance between the drivetrain wheels
    public static final double DRIVETRAIN_WHEELBASE_METERS = 19.5;

    public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 2;
    public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 1;
    public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 16;
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(292.7);

    public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 4;
    public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 3;
    public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 15;
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(239.7);

    public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 5;
    public static final int BACK_LEFT_MODULE_STEER_MOTOR = 6;
    public static final int BACK_LEFT_MODULE_STEER_ENCODER = 14;
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(210.1);

    public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 8;
    public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 7;
    public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 13;
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(25.1);


    /*************** Shoulder ***************/
    public static final int SHOULDER_MOTOR_ID = 9;

    public static final double SHOULDER_SPEED_FAST_MULT = 0.9;
    public static final double SHOULDER_SPEED_SLOW_MULT = 0.3;
    public static final double SHOULDER_ANGLE_SCALE = 11;
    public static final double MAX_SHOULDER_ANGLE = 110;


    /*************** Telescope ***************/
    public static final int TELESCOPE_MOTOR_ID = 10;

    public static final double TELESCOPE_SPEED_FAST_MULT = 0.8;
    public static final double TELESCOPE_SPEED_SLOW_MULT = 0.3;
    public static final double MAX_TELESCOPE_ENCODER_VALUE = 180.0;


    /*************** Wrist ***************/
    public static final int WRIST_MOTOR_ID = 11;
    public static final int WRIST_ENCODER_ID = 17;

    public static final double WRIST_SPEED_FAST_MULT = 1.0;
    public static final double WRIST_SPEED_SLOW_MULT = 0.25;
    public static final double WRIST_ENCODER_OFFSET = 234.9;


    /*************** Grip ***************/
    public static final int GRIP_MOTOR_ID = 12;
    
    public static final double GRIP_SPEED_MULTIPLIER = 1.0;


    /*************** Manipulator Positions ***************/
    public static final double SHOULDER_HOME = 0;
    public static final double TELESCOPE_HOME = 0;

    public static final double SHOULDER_FLOOR = 26.16;
    public static final double TELESCOPE_FLOOR = 65.69;

    public static final double SHOULDER_LVL2 = 89;
    public static final double TELESCOPE_LVL2 = 0;

    public static final double SHOULDER_LVL3 = 103.40;
    public static final double TELESCOPE_LVL3 = 180.0;

    public static final double SHOULDER_PLAYER = 89;
    public static final double TELESCOPE_PLAYER = 0;

    public static final double SHOULDER_ANGLE_ERROR = 1;
    public static final double WRIST_ANGLE_ERROR = 2;
    public static final double TELESCOPE_ERROR = 5;
}

