package frc.robot.commands.AutoCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GripSubsystem;

public class MoveGrip extends CommandBase {
    private int dir;
    private double time;
    private Timer timer;
    private GripSubsystem gripSubsystem;
    
    public MoveGrip(int dir, double time, GripSubsystem gripSubsystem) {
        this.dir = dir;
        this.time = time;
        this.timer = new Timer();
        this.gripSubsystem = gripSubsystem;
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        gripSubsystem.move(dir);
    }

    @Override
    public boolean isFinished() {
        return timer.get() >= time;
    }

    @Override
    public void end(boolean interrupted) {
        gripSubsystem.move(0);
        timer.stop();
    }
}
