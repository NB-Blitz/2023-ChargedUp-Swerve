package frc.robot.subsystems;

import static frc.robot.Constants.CONST_SHOULDER_FLOOR;
import static frc.robot.Constants.CONST_SHOULDER_HOME;
import static frc.robot.Constants.CONST_SHOULDER_LVL2_CONE;
import static frc.robot.Constants.CONST_SHOULDER_LVL2_CUBE;
import static frc.robot.Constants.CONST_SHOULDER_LVL3_CONE;
import static frc.robot.Constants.CONST_SHOULDER_LVL3_CUBE;
import static frc.robot.Constants.CONST_SHOULDER_PLAYER;
import static frc.robot.Constants.CONST_TELESCOPE_FLOOR;
import static frc.robot.Constants.CONST_TELESCOPE_HOME;
import static frc.robot.Constants.CONST_TELESCOPE_LVL2_CONE;
import static frc.robot.Constants.CONST_TELESCOPE_LVL2_CUBE;
import static frc.robot.Constants.CONST_TELESCOPE_LVL3_CONE;
import static frc.robot.Constants.CONST_TELESCOPE_LVL3_CUBE;
import static frc.robot.Constants.CONST_TELESCOPE_PLAYER;
import static frc.robot.Constants.CONST_WRIST_FLOOR;
import static frc.robot.Constants.CONST_WRIST_HOME;
import static frc.robot.Constants.CONST_WRIST_LVL2_CONE;
import static frc.robot.Constants.CONST_WRIST_LVL2_CUBE;
import static frc.robot.Constants.CONST_WRIST_LVL3_CONE;
import static frc.robot.Constants.CONST_WRIST_LVL3_CUBE;
import static frc.robot.Constants.CONST_WRIST_PLAYER;
import static frc.robot.Constants.SHOULDER_MOTOR_ID;
import static frc.robot.Constants.TELESCOPE_MOTOR_ID;
import static frc.robot.Constants.WRIST_MOTOR_ID;
import static frc.robot.Constants.WRIST_SPEED_MULTIPLIER;
import static frc.robot.Constants.SHOULDER_SPEED_MULTIPLIER;
import static frc.robot.Constants.TELESCOPE_SPEED_MULTIPLIER;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
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
    private double trim;

    public ManipulatorStateSubsystem() {
        ShuffleboardTab tab = Shuffleboard.getTab("Manipulator");

        this.shoulderMotor = new CANSparkMax(SHOULDER_MOTOR_ID, MotorType.kBrushless);
        this.telescopeMotor = new CANSparkMax(TELESCOPE_MOTOR_ID, MotorType.kBrushless);
        this.wristMotor = new TalonSRX(WRIST_MOTOR_ID);
        this.shoulderEncoder = this.shoulderMotor.getEncoder();
        this.telescopeEncoder = this.telescopeMotor.getEncoder();
        this.coneMode = true;
        this.trim = 0;

        tab.addDouble("Shoulder Position", () -> getShoulderPos());
        tab.addDouble("Telescope Position", () -> getTelescopePos());
        tab.addDouble("Wrist Position", () -> getWristPos());
        tab.addDouble("Trim", () -> getTrim());
    }

    public void setHome() { //starting position
        targetShoulderAngle = CONST_SHOULDER_HOME;
        targetTelescopeLength = CONST_TELESCOPE_HOME;
        targetWristAngle = CONST_WRIST_HOME;
    }

    public void setFloor() { // floor level
        targetShoulderAngle = CONST_SHOULDER_FLOOR;
        targetTelescopeLength = CONST_TELESCOPE_FLOOR;
        targetWristAngle = CONST_WRIST_FLOOR;
    }

    public void setTwo() { // 2nd level

        if (coneMode == false) {
        targetShoulderAngle = CONST_SHOULDER_LVL2_CUBE;
        targetTelescopeLength = CONST_TELESCOPE_LVL2_CUBE;
        targetWristAngle = CONST_WRIST_LVL2_CUBE;
        }
        if (coneMode == true) {
        targetShoulderAngle = CONST_SHOULDER_LVL2_CONE;
        targetTelescopeLength = CONST_TELESCOPE_LVL2_CONE;
        targetWristAngle = CONST_WRIST_LVL2_CONE;
        }
    }

    public void setThree() { // third level

        if (coneMode == true) {
        targetShoulderAngle = CONST_SHOULDER_LVL3_CONE;
        targetTelescopeLength = CONST_TELESCOPE_LVL3_CONE;
        targetWristAngle = CONST_WRIST_LVL3_CONE;
        }

        if (coneMode == false) {
        targetShoulderAngle = CONST_SHOULDER_LVL3_CUBE;
        targetTelescopeLength = CONST_TELESCOPE_LVL3_CUBE;
        targetWristAngle = CONST_WRIST_LVL3_CUBE;
        }
    }

    public void setPlayerArea() { // the human player place
        targetShoulderAngle = CONST_SHOULDER_PLAYER;
        targetTelescopeLength = CONST_TELESCOPE_PLAYER;
        targetWristAngle = CONST_WRIST_PLAYER;
    }

    public void trimUp(){
        trim += TRIM_STEP;
    }
    public void trimDown(){
        trim -= TRIM_STEP;
    }

    public void setModeCone(){
        coneMode = true;
    }
    public void setModeCube(){
        coneMode = false;
    }

    
    public void periodic(double trimJoystick) {
        //setTelescopePos();
        if(trimJoystick < -0.5)
        {
            trimUp();
        }
        else if(trimJoystick > 0.5)
        {
            trimDown();
        }
        if (targetShoulderAngle < getShoulderPos()){
            shoulderMotor.set(-SHOULDER_SPEED_MULTIPLIER);
        }
        else if (targetShoulderAngle > getShoulderPos()){
            shoulderMotor.set(SHOULDER_SPEED_MULTIPLIER);
        }
        else{
            shoulderMotor.set(0);
        }
        if (targetTelescopeLength < getTelescopePos()){
            telescopeMotor.set(-TELESCOPE_SPEED_MULTIPLIER);
        }
        else if (targetTelescopeLength > getTelescopePos()){
            telescopeMotor.set(TELESCOPE_SPEED_MULTIPLIER);
        }
        else{
            telescopeMotor.set(0);
        }
        if (targetWristAngle < getWristPos()){
            wristMotor.set(TalonSRXControlMode.PercentOutput, -WRIST_SPEED_MULTIPLIER);
        }
        else if (targetWristAngle > getWristPos()){
            wristMotor.set(TalonSRXControlMode.PercentOutput, WRIST_SPEED_MULTIPLIER);
        }
        else{
            wristMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }
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

    private double getTrim() {
        return trim;
    }
}
