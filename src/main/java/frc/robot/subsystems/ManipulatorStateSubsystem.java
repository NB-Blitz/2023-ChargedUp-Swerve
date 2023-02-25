package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.CANCoder;
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
    private final CANCoder wristEncoder;

    private double shoulderSpeed;
    private double telescopeSpeed;
    private double wristSpeed;

    private double targetShoulderAngle;
    private double targetTelescopeLength;
    private double targetWristAngle;
    private String currentLevel;
    private boolean coneMode;
    private double trim;

    public ManipulatorStateSubsystem() {
        ShuffleboardTab tab = Shuffleboard.getTab("Manipulator");

        this.shoulderMotor = new CANSparkMax(SHOULDER_MOTOR_ID, MotorType.kBrushless);
        this.telescopeMotor = new CANSparkMax(TELESCOPE_MOTOR_ID, MotorType.kBrushless);
        this.wristMotor = new TalonSRX(WRIST_MOTOR_ID);
        this.shoulderEncoder = this.shoulderMotor.getEncoder();
        this.telescopeEncoder = this.telescopeMotor.getEncoder();
        this.wristEncoder = new CANCoder(WRIST_ENCODER_ID);
        this.currentLevel = "home";
        this.coneMode = true;
        this.trim = 0;

        tab.addDouble("Shoulder Pos", () -> getShoulderPos());
        tab.addDouble("Telescope Pos", () -> getTelescopePos());
        tab.addDouble("Wrist Pos", () -> getWristPos());
        tab.addDouble("Trim", () -> getTrim());

        //tab.addDouble("Wrist Current", () -> wristMotor.getStatorCurrent());
        //tab.addDouble("Shoulder Current", () -> shoulderMotor.getOutputCurrent());
        //tab.addDouble("Telescope Current", () -> telescopeMotor.getOutputCurrent());
    }

    public void move(double shoulderSpeed, double telescopeSpeed, double wristSpeed) {
        this.shoulderSpeed = shoulderSpeed;
        this.telescopeSpeed = telescopeSpeed;
        this.wristSpeed = wristSpeed;
    }

    public void moveTrim(double trimJoystick) {
        if (trimJoystick < -0.5) {
            trimUp();
        } else if (trimJoystick > 0.5) {
            trimDown();
        }
    }

    public void setHome() { // Starting position
        targetShoulderAngle = SHOULDER_HOME;
        targetTelescopeLength = TELESCOPE_HOME;
        targetWristAngle = WRIST_HOME;
        currentLevel = "home";
    }

    public void setFloor() { // Floor level
        targetShoulderAngle = SHOULDER_FLOOR;
        targetTelescopeLength = TELESCOPE_FLOOR;
        targetWristAngle = WRIST_FLOOR;
        currentLevel = "floor";
    }

    public void setTwo() { // 2nd level
        if (coneMode == false) {
            targetShoulderAngle = SHOULDER_LVL2_CUBE;
            targetTelescopeLength = TELESCOPE_LVL2_CUBE;
            targetWristAngle = WRIST_LVL2_CUBE;
        } else if (coneMode == true) {
            targetShoulderAngle = SHOULDER_LVL2_CONE;
            targetTelescopeLength = TELESCOPE_LVL2_CONE;
            targetWristAngle = WRIST_LVL2_CONE;
        }
        currentLevel = "2nd";
    }

    public void setThree() { // 3rd level
        if (coneMode == true) {
            targetShoulderAngle = SHOULDER_LVL3_CONE;
            targetTelescopeLength = TELESCOPE_LVL3_CONE;
            targetWristAngle = WRIST_LVL3_CONE;
        } else if (coneMode == false) {
            targetShoulderAngle = SHOULDER_LVL3_CUBE;
            targetTelescopeLength = TELESCOPE_LVL3_CUBE;
            targetWristAngle = WRIST_LVL3_CUBE;
        }
        currentLevel = "3rd";
    }

    public void setPlayerArea() { // Human player shelf
        targetShoulderAngle = SHOULDER_PLAYER;
        targetTelescopeLength = TELESCOPE_PLAYER;
        targetWristAngle = WRIST_PLAYER;
        currentLevel = "player";
    }

    public void trimUp(){
        trim += TRIM_STEP;
    }
    public void trimDown(){
        trim -= TRIM_STEP;
    }

    public void setModeCone() {
        coneMode = true;
        if (currentLevel == "2nd") {
            setTwo();
        } else if (currentLevel == "3rd") {
            setThree();
        }
    }
    public void setModeCube() {
        coneMode = false;
        if (currentLevel == "2nd") {
            setTwo();
        } else if (currentLevel == "3rd") {
            setThree();
        }
    }

    @Override
    public void periodic() {
        /*// Move shoulder
        if (targetShoulderAngle < getShoulderPos()) {
            shoulderMotor.set(-SHOULDER_SPEED_MULTIPLIER);
        } else if (targetShoulderAngle > getShoulderPos()) {
            shoulderMotor.set(SHOULDER_SPEED_MULTIPLIER);
        } else {
            shoulderMotor.set(0);
        }

        // Move telescope
        if (targetTelescopeLength < getTelescopePos()) {
            telescopeMotor.set(-TELESCOPE_SPEED_MULTIPLIER);
        } else if (targetTelescopeLength > getTelescopePos()) {
            telescopeMotor.set(TELESCOPE_SPEED_MULTIPLIER);
        } else {
            telescopeMotor.set(0);
        }

        // Move wrist
        if (targetWristAngle < getWristPos()) {
            wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_MULTIPLIER);
        } else if (targetWristAngle > getWristPos()) {
            wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_MULTIPLIER);
        } else {
            wristMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }*/

        if (shoulderMotor.getOutputCurrent() < 50) {
            shoulderMotor.set(shoulderSpeed * SHOULDER_SPEED_MULTIPLIER);
        } else {
            shoulderMotor.set(0);
        }

        if (telescopeMotor.getOutputCurrent() < 50) {
            telescopeMotor.set(telescopeSpeed * TELESCOPE_SPEED_MULTIPLIER);
        } else {
            telescopeMotor.set(0);
        }

        if (wristMotor.getStatorCurrent() < 30) {
            wristMotor.set(TalonSRXControlMode.PercentOutput, wristSpeed * WRIST_SPEED_MULTIPLIER);
        } else {
            wristMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    private double getShoulderPos() {
        return shoulderEncoder.getPosition() + trim;
    }

    private double getTelescopePos() {
        return telescopeEncoder.getPosition();
    }

    private double getWristPos() {
        return wristEncoder.getAbsolutePosition() - WRIST_ENCODER_OFFSET;
    }

    private double getTrim() {
        return trim;
    }
}
