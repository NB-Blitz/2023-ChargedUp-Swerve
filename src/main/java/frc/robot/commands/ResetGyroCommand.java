package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ResetGyroCommand extends CommandBase {
    private final AHRS m_navx;

    public ResetGyroCommand(AHRS navX) {
        m_navx = navX;
    }
    
    @Override
    public void execute() {
        m_navx.zeroYaw();
    }
}
