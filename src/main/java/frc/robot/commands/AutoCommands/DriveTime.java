package frc.robot.commands.AutoCommands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;

public class DriveTime extends CommandBase {
    private double x;
    private double y;
    private double rotation;
    private double time;
    private Timer timer;
    private DrivetrainSubsystem drivetrainSubsystem;

    public DriveTime(double x, double y, double rotation, double time, DrivetrainSubsystem drivetrainSubsystem) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.time = time;
        this.timer = new Timer();
        this.drivetrainSubsystem = drivetrainSubsystem;

        addRequirements(drivetrainSubsystem);

        ShuffleboardTab tab = Shuffleboard.getTab("Autonomous");
        tab.addDouble("Timer", () -> timer.get());
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        //drivetrainSubsystem.drive(ChassisSpeeds.fromFieldRelativeSpeeds(x * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND, y * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND, rotation * DrivetrainSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND, drivetrainSubsystem.getGyroscopeRotation()));
    }

    @Override
    public boolean isFinished() {
        return timer.get() >= time;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.drive(new ChassisSpeeds(0, 0, 0));
        timer.stop();
    }
}
