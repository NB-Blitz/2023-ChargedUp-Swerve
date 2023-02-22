package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.*;

public class ManipulatorStateSubsystem extends SubsystemBase {
    private final CANSparkMax shoulderMotor;
    private final CANSparkMax telescopeMotor;
    private final TalonSRX wristMotor;

    private final RelativeEncoder shoulderEncoder;
    private final RelativeEncoder telescopeEncoder;

    private double targetShoulderAngle;
    private double targetTelescopeLength;
    private double targetWristAngle;
    private boolean coneMode;

    public ManipulatorStateSubsystem() {
        ShuffleboardTab tab = Shuffleboard.getTab("Manipulator");

        this.shoulderMotor = new CANSparkMax(SHOULDER_MOTOR_ID, MotorType.kBrushless);
        this.telescopeMotor = new CANSparkMax(TELESCOPE_MOTOR_ID, MotorType.kBrushless);
        this.wristMotor = new TalonSRX(WRIST_MOTOR_ID);
        this.shoulderEncoder = this.shoulderMotor.getEncoder();
        this.telescopeEncoder = this.telescopeMotor.getEncoder();
        this.coneMode = true;

        tab.addDouble("Shoulder Position", () -> getShoulderPos());
        tab.addDouble("Telescope Position", () -> getTelescopePos());
        tab.addDouble("Wrist Position", () -> getWristPos());
    }

    public void setHome() { //starting position
        targetShoulderAngle = CONST_SHOULDER_HOME;

    }

    public void setFloor() { // floor level

    }

    public void setTwo() { // 2nd level

    }

    public void setThree() { // third level

    }

    public void setPlayerArea() { // the human player place

    }

    public void setModeCone(){
        coneMode = true;
    }
    public void setModeCube(){
        coneMode = false;
    }

    private void setTelescopePos(double change) {
        //telescope motor target angle = change
    }

    private void changeTelescopePos(double change) {
        //telescope motor target angle = change + getTelescopePos()
    }

    @Override
    public void periodic() {
        setTelescopePos();
    }

    private double getShoulderPos() {
        return shoulderEncoder.getPosition();
    }

    private double getTelescopePos() {
        return telescopeEncoder.getPosition();
    }

    private double getWristPos() {
        return wristMotor.getSelectedSensorPosition();
    }
}
