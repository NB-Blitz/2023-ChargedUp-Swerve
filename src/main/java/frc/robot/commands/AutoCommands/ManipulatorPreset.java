package frc.robot.commands.AutoCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ManipulatorSubsystem;

public class ManipulatorPreset extends CommandBase {
    private String preset;
    private ManipulatorSubsystem manipulatorSubsystem;

    public ManipulatorPreset(String preset, ManipulatorSubsystem manipulatorSubsystem) {
        this.preset = preset;
        this.manipulatorSubsystem = manipulatorSubsystem;
        
        addRequirements(manipulatorSubsystem);
    }

    @Override
    public void initialize() {
        switch(preset) {
            case "home":
                manipulatorSubsystem.setHome();
            case "floor":
                manipulatorSubsystem.setFloor();
            case "middle":
                manipulatorSubsystem.setTwo();
            case "high":
                manipulatorSubsystem.setThree();
        }
    }

    @Override
    public boolean isFinished() {
        return manipulatorSubsystem.atTargetPreset();
    }
}
