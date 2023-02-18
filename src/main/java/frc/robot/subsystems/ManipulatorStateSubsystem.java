package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.*;

public class ManipulatorStateSubsystem extends SubsystemBase {
    private final CANSparkMax shoulderMotor;
    private final CANSparkMax telescopeMotor;
    private final TalonSRX wristMotor;

    private final RelativeEncoder shoulderEncoder;
    private final RelativeEncoder telescopeEncoder;

    public ManipulatorStateSubsystem() {
        this.shoulderMotor = new CANSparkMax(SHOULDER_MOTOR_ID, MotorType.kBrushless);
        this.telescopeMotor = new CANSparkMax(TELESCOPE_MOTOR_ID, MotorType.kBrushless);
        this.wristMotor = new TalonSRX(WRIST_MOTOR_ID);
        this.shoulderEncoder = this.shoulderMotor.getEncoder();
        this.telescopeEncoder = this.telescopeMotor.getEncoder();
    }

    public void setHome() {

    }

    public void setFloor() {

    }

    public void setTwo() {

    }

    public void setThree() {

    }

    public void setPlayerArea() {

    }

    @Override
    public void periodic() {

    }

    @Override
    public void simulationPeriodic() {
        
    }
}