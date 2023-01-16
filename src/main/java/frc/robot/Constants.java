package frc.robot;

public class Constants {
    public static final int DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR = 4; // CAN ID
    public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR = 3; // CAN ID
    public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER = 9; // CAN ID

    public static final int DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR = 2; // CAN ID
    public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR = 1; // CAN ID
    public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_ENCODER = 10; // CAN ID

    public static final int DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR = 8; // CAN ID
    public static final int DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR = 7; // CAN ID
    public static final int DRIVETRAIN_BACK_LEFT_ANGLE_ENCODER = 11; // CAN ID

    public static final int DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR = 5; // CAN ID
    public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR = 6; // CAN ID
    public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_ENCODER = 12; // CAN ID

    // The left-to-right distance between the drivetrain wheels, measured from center to center.
    public static final double TRACKWIDTH = 51;
    // The front-to-back distance between the drivetrain wheels, measured from center to center.
    public static final double WHEELBASE = 51;

    public static final double FRONT_LEFT_ANGLE_OFFSET = 0.0;
    public static final double FRONT_RIGHT_ANGLE_OFFSET = 0.0;
    public static final double BACK_LEFT_ANGLE_OFFSET = 0.0;
    public static final double BACK_RIGHT_ANGLE_OFFSET = 0.0;

    public static final double P = 0.5;
    public static final double I = 0.0;
    public static final double D = 0.0001;

    public static final double CAN_UPDATE_RATE = 50.0;
}
