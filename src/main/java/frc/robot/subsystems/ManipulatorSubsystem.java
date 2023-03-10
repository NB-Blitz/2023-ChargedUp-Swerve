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

public class ManipulatorSubsystem extends SubsystemBase {
    private final CANSparkMax shoulderMotor;
    private final CANSparkMax telescopeMotor;
    private final TalonSRX wristMotor;

    private final RelativeEncoder shoulderEncoder;
    private final RelativeEncoder telescopeEncoder;
    private final CANCoder wristEncoder;

    private final ColorSensorV3 shoulderSensor;
    private final SparkMaxLimitSwitch telescopeSwitch;

    private boolean presetMode = false;

    private double shoulderSpeed;
    private double telescopeSpeed;
    private double wristSpeed;

    private double targetShoulderAngle;
    private double targetTelescopeLength;
    private double targetWristAngle;

    public ManipulatorSubsystem() {
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
    }

    public void move(double shoulderSpeed, double telescopeSpeed, double wristSpeed) {
        if (shoulderSpeed != 0 || telescopeSpeed != 0 /*|| wristSpeed != 0*/) {
            presetMode = false;
        }
        this.shoulderSpeed = shoulderSpeed;
        this.telescopeSpeed = telescopeSpeed;
        this.wristSpeed = wristSpeed;
    }

    public void setHome() { // Starting position
        presetMode = true;
        targetShoulderAngle = SHOULDER_HOME;
        targetTelescopeLength = TELESCOPE_HOME;
    }

    public void setFloor() { // Floor level
        presetMode = true;
        targetShoulderAngle = SHOULDER_FLOOR;
        targetTelescopeLength = TELESCOPE_FLOOR;
    }

    public void setTwo() { // 2nd level
        presetMode = true;
        targetShoulderAngle = SHOULDER_LVL2;
        targetTelescopeLength = TELESCOPE_LVL2;
    }

    public void setThree() { // 3rd level
        presetMode = true;
        targetShoulderAngle = SHOULDER_LVL3;
        targetTelescopeLength = TELESCOPE_LVL3;
    }

    public void setPlayerShelf() { // Human player shelf
        presetMode = true;
        targetShoulderAngle = SHOULDER_PLAYER;
        targetTelescopeLength = TELESCOPE_PLAYER;
    }

    @Override
    public void periodic() {
        if (isShoulderHome()) {
            shoulderEncoder.setPosition(0);
        }
        if (telescopeSwitch.isPressed()) {
            telescopeEncoder.setPosition(0);
        }

        if (!presetMode) {
            // Shoulder Manual Control
            if ((isShoulderHome() && shoulderSpeed <= 0) ||
                (getShoulderAngle() >= MAX_SHOULDER_ANGLE && shoulderSpeed >= 0) ||
                (getShoulderAngle() <= SHOULDER_FLOOR + SHOULDER_ANGLE_ERROR && shoulderSpeed < 0 && !telescopeSwitch.isPressed())) {
                shoulderMotor.set(0);
            } else {
                shoulderMotor.set(shoulderSpeed * SHOULDER_SPEED_FAST_MULT);
            }

            // Telescope Manual Control
            if ((getTelescopePos() >= MAX_TELESCOPE_ENCODER_VALUE && telescopeSpeed > 0) ||
                (getShoulderAngle() < SHOULDER_FLOOR - SHOULDER_ANGLE_ERROR && telescopeSpeed > 0)) {
                telescopeMotor.set(0);
            } else if ((getTelescopePos() < 20 && telescopeSpeed < 0) ||
                       (getTelescopePos() > MAX_TELESCOPE_ENCODER_VALUE - 20 && telescopeSpeed > 0)) {
                telescopeMotor.set(telescopeSpeed * TELESCOPE_SPEED_SLOW_MULT);
            } else {
                telescopeMotor.set(telescopeSpeed * TELESCOPE_SPEED_FAST_MULT);
            }
        } else {
            // Use limit switches to go to home position instead of encoder values
            if (targetShoulderAngle == 0 && targetTelescopeLength == 0) {
                if (isShoulderHome() || !telescopeSwitch.isPressed()) {
                    shoulderMotor.set(0);
                } else if (getShoulderAngle() < 40) {
                    shoulderMotor.set(-SHOULDER_SPEED_SLOW_MULT);
                } else {
                    shoulderMotor.set(-SHOULDER_SPEED_FAST_MULT);
                }
    
                if (telescopeSwitch.isPressed()) {
                    telescopeMotor.set(0);
                } else if (getTelescopePos() < 20) {
                    telescopeMotor.set(-TELESCOPE_SPEED_SLOW_MULT);
                } else {
                    telescopeMotor.set(-TELESCOPE_SPEED_FAST_MULT);
                }
            } else {
                // Shoulder Preset Control
                if (getShoulderAngle() > targetShoulderAngle + SHOULDER_ANGLE_ERROR) {
                    if (getShoulderAngle() - targetShoulderAngle < 10) {
                        shoulderMotor.set(-SHOULDER_SPEED_SLOW_MULT);
                    } else {
                        shoulderMotor.set(-SHOULDER_SPEED_FAST_MULT);
                    }
                } else if (getShoulderAngle() < targetShoulderAngle - SHOULDER_ANGLE_ERROR) {
                    if (targetShoulderAngle - getShoulderAngle() < 10) {
                        shoulderMotor.set(SHOULDER_SPEED_SLOW_MULT);
                    } else {
                        shoulderMotor.set(SHOULDER_SPEED_FAST_MULT);
                    }
                } else {
                    shoulderMotor.set(0);
                }

                // Telescope Preset Control
                if (getShoulderAngle() < SHOULDER_FLOOR - SHOULDER_ANGLE_ERROR) {
                    telescopeMotor.set(0);
                } else if (getTelescopePos() > targetTelescopeLength + TELESCOPE_ERROR) {
                    if (getTelescopePos() - targetTelescopeLength < 20) {
                        telescopeMotor.set(-TELESCOPE_SPEED_SLOW_MULT);
                    } else {
                        telescopeMotor.set(-TELESCOPE_SPEED_FAST_MULT);
                    }
                } else if (getTelescopePos() < targetTelescopeLength - TELESCOPE_ERROR) {
                    if (targetTelescopeLength - getTelescopePos() < 20) {
                        telescopeMotor.set(TELESCOPE_SPEED_SLOW_MULT);
                    } else {
                        telescopeMotor.set(TELESCOPE_SPEED_FAST_MULT);
                    }
                } else {
                    telescopeMotor.set(0);
                }
            }
        }

        // Wrist Control
        if (presetMode && targetShoulderAngle == 0) {
            targetWristAngle = 0;
        } else if (getShoulderAngle() >= SHOULDER_FLOOR - SHOULDER_ANGLE_ERROR) {
            targetWristAngle = 90 + getShoulderAngle();
        } else {
            targetWristAngle = 0;
        }

        if (targetWristAngle == 0) {
            if (getWristAngle() > targetWristAngle + WRIST_ANGLE_ERROR && getWristAngle() < 360 - WRIST_ANGLE_ERROR) {
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
            if (getWristAngle() > targetWristAngle + WRIST_ANGLE_ERROR) {
                if (getWristAngle() > 300) {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_SLOW_MULT);
                } else if (getWristAngle() - targetWristAngle > 10) {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_FAST_MULT);
                } else {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_SLOW_MULT);
                }
            } else if (getWristAngle() < targetWristAngle - WRIST_ANGLE_ERROR) {
                if (targetWristAngle - getWristAngle() > 10) {
                    wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_FAST_MULT);
                } else { 
                    wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_SLOW_MULT);
                }
            } else {
                wristMotor.set(TalonSRXControlMode.PercentOutput, 0);
            }
        }
        //wristMotor.set(TalonSRXControlMode.PercentOutput, wristSpeed * WRIST_SPEED_FAST_MULT);
    }

    private double getShoulderAngle() {
        return shoulderEncoder.getPosition() * SHOULDER_ANGLE_SCALE;
    }

    private boolean isShoulderHome() {
        return shoulderSensor.getRawColor().red > 1100;
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
