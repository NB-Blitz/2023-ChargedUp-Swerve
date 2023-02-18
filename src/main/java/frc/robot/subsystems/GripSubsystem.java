package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class GripSubsystem extends SubsystemBase {
    private final CANSparkMax gripMotor;
    private final RelativeEncoder gripEncoder;

    private double gripSpeed;

    public GripSubsystem() {
        ShuffleboardTab tab = Shuffleboard.getTab("Manipulator");

        this.gripMotor = new CANSparkMax(GRIP_MOTOR_ID, MotorType.kBrushless);
        this.gripEncoder = this.gripMotor.getEncoder();

        tab.addDouble("Grip Position", () -> getGripPos());
    }

    public void move(double gripSpeed) {
        this.gripSpeed = gripSpeed;
    }

    @Override
    public void periodic() {
        if (gripSpeed > 0 && getGripPos() < MAX_GRIP_ENCODER_VALUE) {
            gripMotor.set(gripSpeed * GRIP_SPEED_MULTIPLIER);
        } else if (gripSpeed < 0 && getGripPos() > MIN_GRIP_ENCODER_VALUE) {
            gripMotor.set(gripSpeed * GRIP_SPEED_MULTIPLIER);
        } else {
            gripMotor.set(0);
        }
    }

    private double getGripPos() {
        return this.gripEncoder.getPosition();
    }
}
