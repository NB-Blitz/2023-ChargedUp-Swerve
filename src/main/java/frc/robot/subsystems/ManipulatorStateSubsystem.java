package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import static com.revrobotics.SparkMaxLimitSwitch.Type.*;
import static com.revrobotics.SparkMaxRelativeEncoder.Type.*;

import edu.wpi.first.wpilibj.I2C;
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
    private final CANCoder wristEncoder;

    private final ColorSensorV3 shoulderSensor;
    private final SparkMaxLimitSwitch telescopeSwitch;

    private double shoulderSpeed;
    private double telescopeSpeed;
    private double wristSpeed;

    private double targetShoulderAngle;
    private double targetTelescopeLength;
    private double targetWristAngle;

    public ManipulatorStateSubsystem() {
        ShuffleboardTab tab = Shuffleboard.getTab("Manipulator");

        shoulderMotor = new CANSparkMax(SHOULDER_MOTOR_ID, MotorType.kBrushless);
        telescopeMotor = new CANSparkMax(TELESCOPE_MOTOR_ID, MotorType.kBrushless);
        wristMotor = new TalonSRX(WRIST_MOTOR_ID);
        shoulderEncoder = shoulderMotor.getEncoder(kHallSensor, 42);
        telescopeEncoder = telescopeMotor.getEncoder(kHallSensor, 42);
        wristEncoder = new CANCoder(WRIST_ENCODER_ID);
        shoulderSensor = new ColorSensorV3(I2C.Port.kOnboard);
        telescopeSwitch = telescopeMotor.getReverseLimitSwitch(kNormallyClosed);

        wristMotor.setInverted(true);

        tab.addDouble("Shoulder Angle", () -> getShoulderAngle());
        tab.addDouble("Telescope Pos", () -> getTelescopePos());
        tab.addDouble("Wrist Angle", () -> getWristAngle());

        tab.addInteger("Shoulder Red", () -> shoulderSensor.getRawColor().red);
        tab.addBoolean("Telescope Switch", () -> telescopeSwitch.isPressed());

        //tab.addDouble("Wrist Current", () -> wristMotor.getStatorCurrent());
        //tab.addDouble("Shoulder Current", () -> shoulderMotor.getOutputCurrent());
        //tab.addDouble("Telescope Current", () -> telescopeMotor.getOutputCurrent());
    }

    public void move(double shoulderSpeed, double telescopeSpeed, double wristSpeed) {
        this.shoulderSpeed = shoulderSpeed;
        this.telescopeSpeed = telescopeSpeed;
        this.wristSpeed = wristSpeed;
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
        
        if ((isShoulderHome() && shoulderSpeed <= 0) ||
            (getShoulderAngle() >= MAX_SHOULDER_ANGLE && shoulderSpeed >= 0) //||
            /*(getShoulderAngle() <= SHOULDER_FLOOR && shoulderSpeed < 0 && !telescopeSwitch.isPressed())*/) {
            shoulderMotor.set(0);
        } else {
            shoulderMotor.set(shoulderSpeed * SHOULDER_SPEED_MULTIPLIER);
        }

        // Telescope movement
        if (telescopeSwitch.isPressed()) {
            telescopeEncoder.setPosition(0);
        }

        if ((getTelescopePos() >= MAX_TELESCOPE_ENCODER_VALUE && telescopeSpeed > 0) //||
            /*(getShoulderAngle() < SHOULDER_FLOOR && telescopeSpeed > 0)*/) {
            telescopeMotor.set(0);
        } else if (getTelescopePos() < 20 && telescopeSpeed < 0) {
            telescopeMotor.set(telescopeSpeed * 0.4);
        } else {
            telescopeMotor.set(telescopeSpeed * TELESCOPE_SPEED_MULTIPLIER);
        }

        // Wrist movement
        if (getShoulderAngle() >= SHOULDER_FLOOR) {
            targetWristAngle = 90 + getShoulderAngle();
        } else {
            targetWristAngle = 0;
        }

        if (targetWristAngle == 0) {
            if (getWristAngle() > targetWristAngle + ANGLE_ERROR && getWristAngle() < 360 - ANGLE_ERROR) {
                if (getWristAngle() > 300) {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_SLOW_MULT);
                } else if (getWristAngle() > 10) {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_FAST_MULT);
                } else {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_SLOW_MULT);
                }
            } else {
                wristMotor.set(TalonSRXControlMode.PercentOutput, 0);
            }
        } else {
            if (getWristAngle() > targetWristAngle + ANGLE_ERROR) {
                if (getWristAngle() > 300) {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_SLOW_MULT);
                } else if (getWristAngle() - targetWristAngle > 10) {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_FAST_MULT);
                } else {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_SLOW_MULT);
                }
            } else if (getWristAngle() < targetWristAngle - ANGLE_ERROR) {
                if (targetWristAngle - getWristAngle() > 10) {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_FAST_MULT);
                } else { 
                    wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_SLOW_MULT);
                }
            } else {
                wristMotor.set(TalonSRXControlMode.PercentOutput, 0);
            }
        }
        // wristMotor.set(TalonSRXControlMode.PercentOutput, wristSpeed * WRIST_SPEED_MULTIPLIER);
    }

    private double getShoulderAngle() {
        return shoulderEncoder.getPosition() * SHOULDER_ANGLE_SCALE;
    }

    private boolean isShoulderHome() {
        return shoulderSensor.getRawColor().red > 1000;
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
