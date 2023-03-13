package frc.robot.commands.AutoCommands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;

public class RotateToAngle extends CommandBase {
    private int targetAngle;
    private DrivetrainSubsystem drivetrainSubsystem;

    private final int ANGLE_ERROR = 2;
    
    public RotateToAngle(int targetAngle, DrivetrainSubsystem drivetrainSubsystem) {
        this.targetAngle = targetAngle;
        this.drivetrainSubsystem = drivetrainSubsystem;

        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void execute() {
        double currentAngle = drivetrainSubsystem.getGyroscopeRotation().getDegrees();
        int direction;
        if (currentAngle < targetAngle - ANGLE_ERROR) {
            direction = 1;
        } else if (currentAngle > targetAngle + ANGLE_ERROR) {
            direction = -1;
        } else {
            direction = 0;
        }
        drivetrainSubsystem.drive(ChassisSpeeds.fromFieldRelativeSpeeds(
            0,
            0,
            0.2 * direction * DrivetrainSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
            drivetrainSubsystem.getGyroscopeRotation()
        ));
    }

    @Override
    public boolean isFinished() {
        double currentAngle = drivetrainSubsystem.getGyroscopeRotation().getDegrees();
        return currentAngle >= targetAngle - ANGLE_ERROR && currentAngle <= targetAngle + ANGLE_ERROR;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.drive(new ChassisSpeeds(0, 0, 0));
    }
}
