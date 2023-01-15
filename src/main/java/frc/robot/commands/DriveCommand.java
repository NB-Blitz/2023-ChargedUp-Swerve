package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.subsystems.DrivetrainSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.geometry.Translation2d;

public class DriveCommand extends CommandBase {

    public DriveCommand() {
        addRequirements(DrivetrainSubsystem.getInstance());
    }

    @Override
    public void execute() {
        double forward = -Robot.getOi().getPrimaryJoystick().getRawAxis(1);
        forward = deadzone(forward);
        // Square the forward stick
        forward = Math.copySign(Math.pow(forward, 2.0), forward);

        double strafe = -Robot.getOi().getPrimaryJoystick().getRawAxis(0);
        strafe = deadzone(strafe);
        // Square the strafe stick
        strafe = Math.copySign(Math.pow(strafe, 2.0), strafe);

        double rotation = -Robot.getOi().getPrimaryJoystick().getRawAxis(4);
        rotation = deadzone(rotation);
        // Square the rotation stick
        rotation = Math.copySign(Math.pow(rotation, 2.0), rotation);

        DrivetrainSubsystem.getInstance().drive(new Translation2d(forward, strafe), rotation, true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private double deadzone(double input) {
        if (Math.abs(input) < 0.025) {
            return 0;
        }
        return input;
    }
}
