package frc.robot.commands.AutoCommands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;

public class DriveTime extends CommandBase {
    private double speed;
    private double time;
    private Timer timer;
    private DrivetrainSubsystem drivetrainSubsystem;

    public DriveTime(double speed, double time, DrivetrainSubsystem drivetrainSubsystem) {
        this.speed = speed;
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
        drivetrainSubsystem.drive(ChassisSpeeds.fromFieldRelativeSpeeds(speed, 0, 0, drivetrainSubsystem.getGyroscopeRotation()));
    }

    @Override
    public boolean isFinished() {
        return timer.get() > time;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.drive(new ChassisSpeeds(0, 0, 0));
        timer.stop();
    }
}
