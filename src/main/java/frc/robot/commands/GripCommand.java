package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GripSubsystem;

public class GripCommand extends CommandBase {
    private final GripSubsystem m_gripSubsystem;

    private final DoubleSupplier m_gripSupplier;
    
    public GripCommand(GripSubsystem gripSubsystem, DoubleSupplier gripSupplier) {
        this.m_gripSubsystem = gripSubsystem;
        this.m_gripSupplier = gripSupplier;

        addRequirements(gripSubsystem);
    }

    @Override
    public void execute() {
        m_gripSubsystem.move(m_gripSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        m_gripSubsystem.move(0);
    }
}
