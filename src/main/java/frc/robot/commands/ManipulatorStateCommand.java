package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ManipulatorStateSubsystem;

/*
 * grabber angle facing down = -90 degrees when main thing down DEFAULT
 * grabber angle facing forward = 0 degrees when main thing down
 * main angle facing down = 0 DEFAULT
 * change grabber angle by main angle x-1 
 * y = close claw
 * a = open claw
 * lt = shoulder down(dynamic position)
 * rt = shoulder up(dynamic position)
 * lb = telescope in(dynamic position)
 * rb = telescope out(dynamic position)
 */

public class ManipulatorStateCommand extends CommandBase {
    private final ManipulatorStateSubsystem m_manipulatorStateSubsystem;

    public ManipulatorStateCommand(ManipulatorStateSubsystem manipulatorStateSubsystem) {
        this.m_manipulatorStateSubsystem = manipulatorStateSubsystem;

        addRequirements(manipulatorStateSubsystem);
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean interrupted) {

    }
}
