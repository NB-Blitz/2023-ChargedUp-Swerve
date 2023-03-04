package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxLimitSwitch.Type;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.*;

public class ManipulatorStateSubsystem extends SubsystemBase {
    private final CANSparkMax shoulderMotor;
    private final CANSparkMax telescopeMotor;
    private final TalonSRX wristMotor;

    private final RelativeEncoder shoulderEncoder;
    private final RelativeEncoder telescopeEncoder;
    private final CANCoder wristEncoder;

    private final ColorSensorV3 shoulderSensor;
    private final SparkMaxLimitSwitch telescopeSwitch;

    private double shoulderSpeed;
    private double telescopeSpeed;

    private double targetShoulderAngle;
    private double targetTelescopeLength;
    private double targetWristAngle;

    public ManipulatorStateSubsystem() {
        ShuffleboardTab tab = Shuffleboard.getTab("Manipulator");

        shoulderMotor = new CANSparkMax(SHOULDER_MOTOR_ID, MotorType.kBrushless);
        telescopeMotor = new CANSparkMax(TELESCOPE_MOTOR_ID, MotorType.kBrushless);
        wristMotor = new TalonSRX(WRIST_MOTOR_ID);
        shoulderEncoder = shoulderMotor.getEncoder();
        telescopeEncoder = telescopeMotor.getEncoder();
        wristEncoder = new CANCoder(WRIST_ENCODER_ID);
        shoulderSensor = new ColorSensorV3(I2C.Port.kOnboard);
        telescopeSwitch = telescopeMotor.getReverseLimitSwitch(Type.kNormallyOpen);

        tab.addDouble("Shoulder Angle", () -> getShoulderAngle());
        tab.addDouble("Telescope Pos", () -> getTelescopePos());
        tab.addDouble("Wrist Angle", () -> getWristAngle());

        tab.addString("Shoulder Color", () -> shoulderSensor.getColor().toString());
        tab.addBoolean("Telescope Switch", () -> telescopeSwitch.isPressed());

        //tab.addDouble("Wrist Current", () -> wristMotor.getStatorCurrent());
        //tab.addDouble("Shoulder Current", () -> shoulderMotor.getOutputCurrent());
        //tab.addDouble("Telescope Current", () -> telescopeMotor.getOutputCurrent());
    }

    public void move(double shoulderSpeed, double telescopeSpeed) {
        this.shoulderSpeed = shoulderSpeed;
        this.telescopeSpeed = telescopeSpeed;
    }

    public void setHome() { // Starting position
        targetShoulderAngle = SHOULDER_HOME;
        targetTelescopeLength = TELESCOPE_HOME;
        targetWristAngle = WRIST_HOME;
    }

    public void setFloor() { // Floor level
        targetShoulderAngle = SHOULDER_FLOOR;
        targetTelescopeLength = TELESCOPE_FLOOR;
        targetWristAngle = WRIST_FLOOR;
    }

    public void setTwo() { // 2nd level
        targetShoulderAngle = SHOULDER_LVL2;
        targetTelescopeLength = TELESCOPE_LVL2;
        targetWristAngle = WRIST_LVL2;
    }

    public void setThree() { // 3rd level
        targetShoulderAngle = SHOULDER_LVL3;
        targetTelescopeLength = TELESCOPE_LVL3;
        targetWristAngle = WRIST_LVL3;
    }

    public void setPlayerArea() { // Human player shelf
        targetShoulderAngle = SHOULDER_PLAYER;
        targetTelescopeLength = TELESCOPE_PLAYER;
        targetWristAngle = WRIST_PLAYER;
    }

    @Override
    public void periodic() {
        /*// Move shoulder
        if (targetShoulderAngle < getShoulderPos() - ENCODER_ERROR) {
            shoulderMotor.set(-SHOULDER_SPEED_MULTIPLIER);
        } else if (targetShoulderAngle > getShoulderPos() + ENCODER_ERROR) {
            shoulderMotor.set(SHOULDER_SPEED_MULTIPLIER);
        } else {
            shoulderMotor.set(0);
        }

        // Move telescope
        if (targetTelescopeLength < getTelescopePos() - ENCODER_ERROR) {
            telescopeMotor.set(-TELESCOPE_SPEED_MULTIPLIER);
        } else if (targetTelescopeLength > getTelescopePos() + ENCODER_ERROR) {
            telescopeMotor.set(TELESCOPE_SPEED_MULTIPLIER);
        } else {
            telescopeMotor.set(0);
        }

        // Move wrist
        if (targetWristAngle < getWristPos() - ENCODER_ERROR) {
            wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_MULTIPLIER);
        } else if (targetWristAngle > getWristPos() + ENCODER_ERROR) {
            wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_MULTIPLIER);
        } else {
            wristMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }*/

        // Shoulder movement
        if (isShoulderHome()) {
            shoulderEncoder.setPosition(0);
        }
        
        if ((isShoulderHome() && shoulderSpeed <= 0) || (getShoulderAngle() >= MAX_SHOULDER_ANGLE && shoulderSpeed >= 0)) {
            shoulderMotor.set(0);
        } else {
            shoulderMotor.set(shoulderSpeed * SHOULDER_SPEED_MULTIPLIER);
        }

        // Telescope movement
        if (telescopeSwitch.isPressed()) {
            telescopeEncoder.setPosition(0);
        }

        if (telescopeSpeed < 0 && getTelescopePos() < 20) {
            telescopeMotor.set(telescopeSpeed * 0.4);
        } else if (getTelescopePos() >= MAX_TELESCOPE_ENCODER_VALUE && telescopeSpeed >= 0) {
            telescopeMotor.set(0);
        } else {
            telescopeMotor.set(telescopeSpeed * TELESCOPE_SPEED_MULTIPLIER);
        }

        // Wrist movement
        if (getShoulderAngle() > 30) {
            targetWristAngle = 90 + getShoulderAngle();
        } else {
            targetWristAngle = 0;
        }

        if (getWristAngle() < targetWristAngle - ANGLE_ERROR) {
            wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_MULTIPLIER);
        } else if (getWristAngle() > targetWristAngle + ANGLE_ERROR) {
            wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_MULTIPLIER);
        } else {
            wristMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    private double getShoulderAngle() {
        return shoulderEncoder.getPosition() / SHOULDER_ROT_PER_DEG;
    }

    private boolean isShoulderHome() {
        return shoulderSensor.getColor() == Color.kWhite;
    }

    private double getTelescopePos() {
        return telescopeEncoder.getPosition();
    }

    private double getWristAngle() {
        double pos = wristEncoder.getAbsolutePosition() - WRIST_ENCODER_OFFSET;
        if (pos < 0) {
            return 360 - Math.abs(pos);
        }
        return pos;
    }
}
