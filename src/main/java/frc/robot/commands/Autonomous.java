package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoCommands.DriveTime;
import frc.robot.commands.AutoCommands.ManipulatorPreset;
import frc.robot.commands.AutoCommands.MoveGrip;
import frc.robot.commands.AutoCommands.RotateToAngle;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.GripSubsystem;
import frc.robot.subsystems.ManipulatorSubsystem;

public class Autonomous extends SequentialCommandGroup {

    public Autonomous(String option,
                      DrivetrainSubsystem drivetrainSubsystem,
                      ManipulatorSubsystem manipulatorSubsystem,
                      GripSubsystem gripSubsystem) {
        addRequirements(drivetrainSubsystem, manipulatorSubsystem, gripSubsystem);

        switch(option) {
            case "move":
                // Drive forward out of community
                System.out.println("Moving");
                addCommands(new DriveTime(0.5, 0, 0, 1.9, drivetrainSubsystem));
            case "scoreL":
                // Place cube on top shelf, strafe left, leave community
                addCommands(
                    new ManipulatorPreset("high", manipulatorSubsystem),
                    new DriveTime(0.2, 0, 0, 1, drivetrainSubsystem),
                    new MoveGrip(1, 0.5, gripSubsystem),
                    new DriveTime(-0.2, 0, 0, 0.5, drivetrainSubsystem),
                    new ManipulatorPreset("home", manipulatorSubsystem),
                    new DriveTime(0, 0.3, 0, 0.5, drivetrainSubsystem), //on left +y on right -y looking to the drivers
                    new DriveTime(-0.5, 0, 0, 2, drivetrainSubsystem),
                    new RotateToAngle(180, drivetrainSubsystem)
                );
            case "scoreR":
                // Place cube on top shelf, strafe right, leave community
                addCommands(
                    new ManipulatorPreset("high", manipulatorSubsystem),
                    new DriveTime(0.2, 0, 0, 1, drivetrainSubsystem),
                    new MoveGrip(1, 0.5, gripSubsystem),
                    new DriveTime(-0.2, 0, 0, 0.5, drivetrainSubsystem),
                    new ManipulatorPreset("home", manipulatorSubsystem),
                    new DriveTime(0, -0.3, 0, 0.5, drivetrainSubsystem), //on left +y on right -y looking to the drivers
                    new DriveTime(-0.5, 0, 0, 2, drivetrainSubsystem),
                    new RotateToAngle(180, drivetrainSubsystem)
                );
            case "nothing":
                // Do nothing
        }
    }
}
