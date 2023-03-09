package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.GripSubsystem;
import frc.robot.subsystems.ManipulatorSubsystem;

public class AutonomousCommand extends CommandBase {
    private final DrivetrainSubsystem drivetrainSubsystem;
    private final ManipulatorSubsystem manipulatorSubsystem;
    private final GripSubsystem gripSubsystem;

    private int step = 1;

    public AutonomousCommand(DrivetrainSubsystem drivetrainSubsystem,
                             ManipulatorSubsystem manipulatorSubsystem,
                             GripSubsystem gripSubsystem) {
        this.drivetrainSubsystem = drivetrainSubsystem;
        this.manipulatorSubsystem = manipulatorSubsystem;
        this.gripSubsystem = gripSubsystem;

        addRequirements(drivetrainSubsystem, manipulatorSubsystem, gripSubsystem);
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
        if (false) {
            step = 2;
        }
    }

    private void dropPiece() {
        if (false) {
            step = 3;
        }
    }

    private void leaveCommunity() {
        if (false) {
            step = -1;
        }
    }

    @Override
    public void end(boolean interrupted) {

    }
}
