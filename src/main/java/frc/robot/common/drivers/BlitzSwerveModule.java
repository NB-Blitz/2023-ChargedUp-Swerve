package frc.robot.common.drivers;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.Notifier;
import frc.robot.common.control.PidConstants;
import frc.robot.common.control.PidController;
import frc.robot.common.math.Vector2;

public class BlitzSwerveModule extends SwerveModule {
    /**
     * The default drive encoder rotations per unit.
     */
    public static final double DEFAULT_DRIVE_ROTATIONS_PER_UNIT = (1.0 / (4.0 * Math.PI)) * (60.0 / 15.0) * (18.0 / 26.0) * (42.0 / 14.0);

    private static final PidConstants ANGLE_CONSTANTS = new PidConstants(0.5, 0.0, 0.0001);

    private static final double CAN_UPDATE_RATE = 50.0;

    private final double angleOffset;

    private CANSparkMax steeringMotor;
    private CANCoder angleEncoder;
    private CANSparkMax driveMotor;
    private RelativeEncoder driveEncoder;

    private final Object canLock = new Object();
    private double driveDistance = 0.0;
    private double drivePercentOutput = 0.0;
    private double driveVelocity = 0.0;
    private double driveCurrent = 0.0;

    private double driveEncoderRotationsPerUnit = DEFAULT_DRIVE_ROTATIONS_PER_UNIT;

    /**
     * All CAN operations are done in a separate thread to reduce latency on the control thread
     */
    private Notifier canUpdateNotifier = new Notifier(() -> {
        double driveRotations = driveEncoder.getPosition();
        synchronized (canLock) {
            driveDistance = driveRotations * (1.0 / driveEncoderRotationsPerUnit);
        }

        double driveRpm = driveEncoder.getVelocity();
        synchronized (canLock) {
            driveVelocity = driveRpm * (1.0 / 60.0) * (1.0 / driveEncoderRotationsPerUnit);
        }

        double localDriveCurrent = driveMotor.getOutputCurrent();
        synchronized (canLock) {
            driveCurrent = localDriveCurrent;
        }

        double localDrivePercentOutput;
        synchronized (canLock) {
            localDrivePercentOutput = drivePercentOutput;
        }
        driveMotor.set(localDrivePercentOutput);
    });

    private PidController angleController = new PidController(ANGLE_CONSTANTS);

    /**
     * @param modulePosition The module's offset from the center of the robot's center of rotation
     * @param angleOffset    An angle in radians that is used to offset the angle encoder
     * @param angleMotor     The motor that controls the module's angle
     * @param driveMotor     The motor that drives the module's wheel
     * @param angleEncoder   The analog input for the angle encoder
     */
    public BlitzSwerveModule(Vector2 modulePosition, double angleOffset,
                           CANSparkMax angleMotor, CANSparkMax driveMotor, CANCoder angleEncoder) {
        super(modulePosition);
        this.angleOffset = angleOffset;
        this.steeringMotor = angleMotor;
        this.angleEncoder = angleEncoder;
        this.driveMotor = driveMotor;
        this.driveEncoder = driveMotor.getEncoder();

        driveMotor.setSmartCurrentLimit(60);

        angleController.setInputRange(0.0, 2.0 * Math.PI);
        angleController.setContinuous(true);
        angleController.setOutputRange(-0.5, 0.5);

        canUpdateNotifier.startPeriodic(1.0 / CAN_UPDATE_RATE);
    }
    
    @Override
    protected double readAngle() {
        return angleEncoder.getAbsolutePosition() + angleOffset;
    }

    @Override
    protected double readDistance() {
        synchronized (canLock) {
            return driveDistance;
        }
    }

    protected double readVelocity() {
        synchronized (canLock) {
            return driveVelocity;
        }
    }

    protected double readDriveCurrent() {
        double localDriveCurrent;
        synchronized (canLock) {
            localDriveCurrent = driveCurrent;
        }

        return localDriveCurrent;
    }

    @Override
    public double getCurrentVelocity() {
        return readVelocity();
    }

    @Override
    public double getDriveCurrent() {
        return readDriveCurrent();
    }

    @Override
    protected void setTargetAngle(double angle) {
        angleController.setSetpoint(angle);
    }

    @Override
    protected void setDriveOutput(double output) {
        synchronized (canLock) {
            this.drivePercentOutput = output;
        }
    }

    @Override
    public void updateState(double dt) {
        super.updateState(dt);

        steeringMotor.set(angleController.calculate(getCurrentAngle(), dt));
    }

    public void setDriveEncoderRotationsPerUnit(double driveEncoderRotationsPerUnit) {
        synchronized (canLock) {
            this.driveEncoderRotationsPerUnit = driveEncoderRotationsPerUnit;
        }
    }
}