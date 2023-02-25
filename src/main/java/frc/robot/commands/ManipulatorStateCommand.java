package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ManipulatorStateSubsystem;
import java.util.function.DoubleSupplier;

/*
 * wrist angle facing down = -90 degrees when shoulder down DEFAULT
 * wrist angle facing forward = 0 degrees when shoulder down
 * shoulder angle facing down = 0 DEFAULT
 * change wrist angle by shoulder angle x-1
 */

public class ManipulatorStateCommand extends CommandBase {
    private final ManipulatorStateSubsystem m_manipulatorStateSubsystem;
    private final DoubleSupplier m_trimSupplier;
    private final DoubleSupplier m_shoulderSupplier;
    private final DoubleSupplier m_telescopeSupplier;
    private final DoubleSupplier m_wristSupplier;

    public ManipulatorStateCommand(ManipulatorStateSubsystem manipulatorStateSubsystem,
                                   DoubleSupplier trimSupplier,
                                   DoubleSupplier shoulderSupplier,
                                   DoubleSupplier telescopeSupplier,
                                   DoubleSupplier wristSupplier) {
        this.m_manipulatorStateSubsystem = manipulatorStateSubsystem;
        this.m_trimSupplier = trimSupplier;
        this.m_shoulderSupplier = shoulderSupplier;
        this.m_telescopeSupplier = telescopeSupplier;
        this.m_wristSupplier = wristSupplier;
        
        addRequirements(manipulatorStateSubsystem);
    }

    @Override
    public void execute() {
        m_manipulatorStateSubsystem.moveTrim(m_trimSupplier.getAsDouble());
        m_manipulatorStateSubsystem.move(
            m_shoulderSupplier.getAsDouble(),
            m_telescopeSupplier.getAsDouble(),
            m_wristSupplier.getAsDouble()
        );
    }

    @Override
    public void end(boolean interrupted) {
        m_manipulatorStateSubsystem.setHome();
    }
}
