package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;

import java.util.function.DoubleSupplier;
import java.util.function.BooleanSupplier;

public class DefaultDriveCommand extends CommandBase {
    private final DrivetrainSubsystem drivetrainSubsystem;

    private final DoubleSupplier translationXSupplier;
    private final DoubleSupplier translationYSupplier;
    private final DoubleSupplier rotationSupplier;
    private final BooleanSupplier slowSupplier;

    public DefaultDriveCommand(DrivetrainSubsystem drivetrainSubsystem,
                               DoubleSupplier translationXSupplier,
                               DoubleSupplier translationYSupplier,
                               DoubleSupplier rotationSupplier,
                               BooleanSupplier slowSupplier) {
        this.drivetrainSubsystem = drivetrainSubsystem;
        this.translationXSupplier = translationXSupplier;
        this.translationYSupplier = translationYSupplier;
        this.rotationSupplier = rotationSupplier;
        this.slowSupplier = slowSupplier;

        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void execute() {
        double rot = rotationSupplier.getAsDouble();

        if (slowSupplier.getAsBoolean()) {
            drivetrainSubsystem.setDrivePercent(0.5);
            rot *= 0.3;
        } else {
            drivetrainSubsystem.setDrivePercent(0.8);
            rot *= 0.4;
        }
        
        // You can use `new ChassisSpeeds(...)` for robot-oriented movement instead of field-oriented movement
        drivetrainSubsystem.drive(
            /*new ChassisSpeeds(
                translationXSupplier.getAsDouble(),
                translationYSupplier.getAsDouble(),
                rotationSupplier.getAsDouble()
            )*/
            ChassisSpeeds.fromFieldRelativeSpeeds(
               translationXSupplier.getAsDouble(),
               translationYSupplier.getAsDouble(),
               rot,
               drivetrainSubsystem.getGyroscopeRotation()
            )
        );
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.drive(new ChassisSpeeds(0.0, 0.0, 0.0));
    }
}
