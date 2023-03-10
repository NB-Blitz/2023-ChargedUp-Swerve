package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoCommands.DriveTime;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.GripSubsystem;
import frc.robot.subsystems.ManipulatorSubsystem;

public class Autonomous extends SequentialCommandGroup {
    public Autonomous(DrivetrainSubsystem drivetrainSubsystem,
                             ManipulatorSubsystem manipulatorSubsystem,
                             GripSubsystem gripSubsystem) {
        addRequirements(drivetrainSubsystem, manipulatorSubsystem, gripSubsystem);
        addCommands(new DriveTime(0.5, 2, drivetrainSubsystem));
    }
}
