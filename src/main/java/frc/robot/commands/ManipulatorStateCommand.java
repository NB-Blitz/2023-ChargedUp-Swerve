package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ManipulatorStateSubsystem;
import java.util.function.DoubleSupplier;

/*
 * wrist angle facing down = -90 degrees when shoulder down DEFAULT
 * wrist angle facing forward = 0 degrees when shoulder down
 * shoulder angle facing down = 0 DEFAULT
 * change wrist angle by shoulder angle * -1
 */

public class ManipulatorStateCommand extends CommandBase {
    private final ManipulatorStateSubsystem m_manipulatorStateSubsystem;
    private final DoubleSupplier m_shoulderSupplier;
    private final DoubleSupplier m_telescopeSupplier;

    public ManipulatorStateCommand(ManipulatorStateSubsystem manipulatorStateSubsystem,
                                   DoubleSupplier shoulderSupplier,
                                   DoubleSupplier telescopeSupplier) {
        this.m_manipulatorStateSubsystem = manipulatorStateSubsystem;
        this.m_shoulderSupplier = shoulderSupplier;
        this.m_telescopeSupplier = telescopeSupplier;
        
        addRequirements(manipulatorStateSubsystem);
    }

    @Override
    public void execute() {
        m_manipulatorStateSubsystem.move(
            m_shoulderSupplier.getAsDouble(),
            m_telescopeSupplier.getAsDouble()
        );
    }

    @Override
    public void end(boolean interrupted) {
        m_manipulatorStateSubsystem.setHome();
    }
}
