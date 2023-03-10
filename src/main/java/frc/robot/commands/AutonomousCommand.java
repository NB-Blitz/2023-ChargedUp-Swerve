package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.GripSubsystem;
import frc.robot.subsystems.ManipulatorSubsystem;

public class AutonomousCommand extends CommandBase {
    private final DrivetrainSubsystem drivetrainSubsystem;
    private final ManipulatorSubsystem manipulatorSubsystem;
    private final GripSubsystem gripSubsystem;

    private Timer timer;

    private int step;
    private boolean skip = true;

    public AutonomousCommand(Timer timer,
                             DrivetrainSubsystem drivetrainSubsystem,
                             ManipulatorSubsystem manipulatorSubsystem,
                             GripSubsystem gripSubsystem) {
        this.timer = timer;
        this.drivetrainSubsystem = drivetrainSubsystem;
        this.manipulatorSubsystem = manipulatorSubsystem;
        this.gripSubsystem = gripSubsystem;

        addRequirements(drivetrainSubsystem, manipulatorSubsystem, gripSubsystem);

        step = 1;

        ShuffleboardTab tab = Shuffleboard.getTab("Autonomous");
        tab.addInteger("Step", () -> step);
        tab.addDouble("Time", () -> timer.get());
    }

    @Override
    public void execute() {
        switch(step) {
            case 1:
                moveForward();
            case 2:
                dropPiece();
            case 3:
                leaveCommunity();
        }
    }

    private void moveForward() {
        drivetrainSubsystem.drive(ChassisSpeeds.fromFieldRelativeSpeeds(0.5, 0, 0, drivetrainSubsystem.getGyroscopeRotation()));

        if (timer.get() > 2) {
            drivetrainSubsystem.drive(new ChassisSpeeds(0, 0, 0));
            step = 2;
        }
    }

    private void dropPiece() {
        if (skip) {
            step = 3;
        }
    }

    private void leaveCommunity() {
        if (skip) {
            step = -1;
        }
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.drive(new ChassisSpeeds(0, 0, 0));
    }
}
