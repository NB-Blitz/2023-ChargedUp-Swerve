package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ManipulatorStateSubsystem;

/*
 * wrist angle facing down = -90 degrees when shoulder down DEFAULT
 * wrist angle facing forward = 0 degrees when shoulder down
 * shoulder angle facing down = 0 DEFAULT
 * change wrist angle by shoulder angle x-1
 * lt = close grip
 * rt = open grip
 * ? = home position
 * ? = floor position
 * ? = position two
 * ? = position three
 * ? = player station position
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
        m_manipulatorStateSubsystem.setHome();
    }
}
