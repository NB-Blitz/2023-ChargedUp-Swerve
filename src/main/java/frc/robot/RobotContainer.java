// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.commands.GripCommand;
import frc.robot.commands.ManipulatorCommand;
import frc.robot.subsystems.GripSubsystem;
import frc.robot.subsystems.ManipulatorSubsystem;
import edu.wpi.first.wpilibj.XboxController;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
    private final ManipulatorSubsystem manipulatorSubsystem = new ManipulatorSubsystem();
    private final GripSubsystem gripSubsystem = new GripSubsystem();
    private final Joystick joystick = new Joystick(0);
    private final XboxController controller = new XboxController(1);
    
    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Set up the default command for the drivetrain.
        // The controls are for field-oriented driving

        configureButtonBindings();
        
        drivetrainSubsystem.setDefaultCommand(new DefaultDriveCommand(
            drivetrainSubsystem,
            () -> -modifyAxis(joystick.getY(), 0.05) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(joystick.getX(), 0.05) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(joystick.getTwist(), 0.05) * DrivetrainSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
            () -> joystick.getRawButton(12)
        ));
        
        manipulatorSubsystem.setDefaultCommand(new ManipulatorCommand(
            manipulatorSubsystem,
            () -> -modifyAxis(controller.getLeftY(), 0.2),
            () -> translateBumpers(controller.getLeftBumper(), controller.getRightBumper()),
            () -> -modifyAxis(controller.getRawAxis(5), 0.2)
        ));

        gripSubsystem.setDefaultCommand(new GripCommand(
            gripSubsystem,
            () -> translateTriggers(controller.getLeftTriggerAxis(), controller.getRightTriggerAxis())));
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        // Button 11 on the joystick zeros the gyroscope controller button 8 is human player
        new JoystickButton(joystick, 11)
            .onTrue(new InstantCommand(() -> drivetrainSubsystem.zeroGyroscope()));
        
        // A sets manipulator to home position
        new JoystickButton(controller, 1)
            .onTrue(new InstantCommand(() -> manipulatorSubsystem.setHome()));

        // X sets manipulator to floor position
        new JoystickButton(controller, 3)
            .onTrue(new InstantCommand(() -> manipulatorSubsystem.setFloor()));

        // Y sets manipulator to lower scoring position
        new JoystickButton(controller, 4)
            .onTrue(new InstantCommand(() -> manipulatorSubsystem.setTwo()));

        // B sets manipulator to higher scoring position
        new JoystickButton(controller, 2)
            .onTrue(new InstantCommand(() -> manipulatorSubsystem.setThree()));

        // Right joystick button sets manipulator to player station position
        new JoystickButton(controller, 8)
            .onTrue(new InstantCommand(() -> manipulatorSubsystem.setPlayerShelf()));
    }

    public void sendManipulatorHome() {
        manipulatorSubsystem.setHome();
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return new AutonomousCommand(drivetrainSubsystem, manipulatorSubsystem, gripSubsystem);
    }

    private static double deadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }

    private static double modifyAxis(double value, double deadband) {
        // Deadband
        value = deadband(value, deadband);

        // Square the axis
        value = Math.copySign(value * value, value);

        return value;
    }

    private static double translateTriggers(double left, double right) {
        return -left + right;
    }

    private static double translateBumpers(boolean left, boolean right) {
        double result = 0.0;
        if (left) result--;
        if (right) result++;
        return result;
    }
}
