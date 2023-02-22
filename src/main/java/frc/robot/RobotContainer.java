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

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private final DrivetrainSubsystem m_drivetrainSubsystem = new DrivetrainSubsystem();

    private final Joystick m_joystick = new Joystick(0);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Set up the default command for the drivetrain.
        // The controls are for field-oriented driving:
        // Left stick Y axis -> forward and backwards movement
        // Left stick X axis -> left and right movement
        // Right stick X axis -> rotation

        // Configure the button bindings
        configureButtonBindings();

        m_drivetrainSubsystem.setDefaultCommand(new DefaultDriveCommand(
            m_drivetrainSubsystem,
            () -> -modifyAxis(m_joystick.getY()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(m_joystick.getX()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(m_joystick.getTwist()) * DrivetrainSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
        ));
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

        // ? sets manipulator to home position
        new JoystickButton(m_controller, 1) // TODO: Set desired button number on controller
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setHome()));

        // ? sets manipulator to floor position
        new JoystickButton(m_controller, 2) // TODO: Set desired button number on controller
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setFloor()));

        // ? sets manipulator to lower scoring position
        new JoystickButton(m_controller, 3) // TODO: Set desired button number on controller
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setTwo()));

        // ? sets manipulator to higher scoring position
        new JoystickButton(m_controller, 4) // TODO: Set desired button number on controller
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setThree()));

        // ? sets manipulator to player station position
        new JoystickButton(m_controller, 5) // TODO: Set desired button number on controller
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setPlayerArea()));
        
        // sets manipulator to cone mode
        new JoystickButton(m_controller, 5) // this is the left bumper
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setModeCone())); 

        // sets manipulator to cone mode
        new JoystickButton(m_controller, 6) // this is the right bumper
            .onTrue(new InstantCommand(() -> m_manipulatorStateSubsystem.setModeCube())); 
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
        value = deadband(value, 0.20);

        // Square the axis
        value = Math.copySign(value * value, value);

        return value;
    }
}
