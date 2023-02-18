package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class GripSubsystem extends SubsystemBase {
    private final CANSparkMax gripMotor;
    private final RelativeEncoder gripEncoder;

    private double gripSpeed;

    public GripSubsystem() {
        this.gripMotor = new CANSparkMax(GRIP_MOTOR_ID, MotorType.kBrushless);
        this.gripEncoder = this.gripMotor.getEncoder();
    }

    public void move(double gripSpeed) {
        this.gripSpeed = gripSpeed;
    }

    @Override
    public void periodic() {
        if (gripSpeed > 0 && gripEncoder.getPosition() < MAX_GRIP_ENCODER_VALUE) {
            this.gripMotor.set(gripSpeed * GRIP_SPEED_MULTIPLIER);
        } else if (gripSpeed < 0 && gripEncoder.getPosition() > MIN_GRIP_ENCODER_VALUE) {
            this.gripMotor.set(gripSpeed * GRIP_SPEED_MULTIPLIER);
        } else {
            gripMotor.set(0);
        }
    }
}
