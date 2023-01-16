package frc.robot;

public class Constants {
    public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR = 2; // CAN
    public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER = 0; // CAN
    public static final int DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR = 1; // CAN

    public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR = 4; // CAN
    public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_ENCODER = 1; // CAN
    public static final int DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR = 3; // CAN

    public static final int DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR = 6; // CAN
    public static final int DRIVETRAIN_BACK_LEFT_ANGLE_ENCODER = 2; // CAN
    public static final int DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR = 5; // CAN

    public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR = 8; // CAN
    public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_ENCODER = 3; // CAN
    public static final int DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR = 7; // CAN

    public static final double TRACKWIDTH = 19.5;
    public static final double WHEELBASE = 23.5;

    public static final double FRONT_LEFT_ANGLE_OFFSET = 0.0;
    public static final double FRONT_RIGHT_ANGLE_OFFSET = 0.0;
    public static final double BACK_LEFT_ANGLE_OFFSET = 0.0;
    public static final double BACK_RIGHT_ANGLE_OFFSET = 0.0;

    public static final double P = 0.5;
    public static final double I = 0.0;
    public static final double D = 0.0001;

    public static final double CAN_UPDATE_RATE = 50.0;
}
