// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.commands.GripCommand;
import frc.robot.commands.ManipulatorStateCommand;
import frc.robot.subsystems.GripSubsystem;
import frc.robot.subsystems.ManipulatorStateSubsystem;
import edu.wpi.first.wpilibj.XboxController;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private final DrivetrainSubsystem m_drivetrainSubsystem = new DrivetrainSubsystem();
    private final ManipulatorStateSubsystem m_manipulatorStateSubsystem = new ManipulatorStateSubsystem();
    private final GripSubsystem m_gripSubsystem = new GripSubsystem();
    private final Joystick m_joystick = new Joystick(0);
    private final XboxController m_controller = new XboxController(1);
    
    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Set up the default command for the drivetrain.
        // The controls are for field-oriented driving

        configureButtonBindings();
        
        m_drivetrainSubsystem.setDefaultCommand(new DefaultDriveCommand(
            m_drivetrainSubsystem,
            () -> -modifyAxis(m_joystick.getY()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(m_joystick.getX()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(m_joystick.getTwist() * 0.6) * DrivetrainSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
        ));
        
        m_manipulatorStateSubsystem.setDefaultCommand(new ManipulatorStateCommand(
            m_manipulatorStateSubsystem,
            () -> m_controller.getLeftY(),
            () -> deadband(m_controller.getLeftY(), 0.2),
            () -> translateBumpers(m_controller.getLeftBumper(), m_controller.getRightBumper()),
            () -> deadband(m_controller.getRawAxis(4), 0.2)
        ));

        m_gripSubsystem.setDefaultCommand(new GripCommand(
            m_gripSubsystem,
            () -> translateTriggers(m_controller.getLeftTriggerAxis(), m_controller.getRightTriggerAxis())));
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        // Button 11 on the joystick zeros the gyroscope
        new JoystickButton(m_joystick, 11)
            .onTrue(new InstantCommand(() -> m_drivetrainSubsystem.zeroGyroscope()));
        
        // Holding button 12 on the joystick slows drive speed to 40%
        new JoystickButton(m_joystick, 12)
            .onTrue(new InstantCommand(() -> m_drivetrainSubsystem.setDrivePercent(0.25)))
            .onFalse(new InstantCommand(() -> m_drivetrainSubsystem.setDrivePercent(0.6)));
        
        // A sets manipulator to home position
        new JoystickButton(m_controller, 1)
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setHome()));

        // X sets manipulator to floor position
        new JoystickButton(m_controller, 3)
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setFloor()));

        // Y sets manipulator to lower scoring position
        new JoystickButton(m_controller, 4)
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setTwo()));

        // B sets manipulator to higher scoring position
        new JoystickButton(m_controller, 2)
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setThree()));

        /*// Right joystick button sets manipulator to player station position
        new JoystickButton(m_controller, 10)
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setPlayerArea()));*/
        
        /*// Left bumper sets manipulator to cone mode
        new JoystickButton(m_controller, 5)
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setModeCone()));

        // Right bumper sets manipulator to cube mode
        new JoystickButton(m_controller, 6)
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setModeCube()));*/
    }

    public void sendManipulatorHome() {
        m_manipulatorStateSubsystem.setHome();
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return new InstantCommand();
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

    private static double modifyAxis(double value) {
        // Deadband
        value = deadband(value, 0.05);

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
