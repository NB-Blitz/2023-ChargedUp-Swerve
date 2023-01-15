package frc.robot.common.drivers;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.common.math.Rotation2;

public class NavX {
    private final AHRS navX;
	private Rotation2 adjustmentAngle = Rotation2.ZERO;
	private boolean inverted;

    public NavX(SPI.Port port) {
        this(port, (byte) 200);
    }

    public NavX(SPI.Port port, byte updateRate) {
        navX = new AHRS(port, updateRate);
    }

	public void calibrate() {
        navX.reset();
    }

	public final Rotation2 getAdjustmentAngle() {
		return adjustmentAngle;
	}

	public void setAdjustmentAngle(Rotation2 adjustmentAngle) {
		this.adjustmentAngle = adjustmentAngle;
	}

	public final boolean isInverted() {
		return inverted;
	}

	public final void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public Rotation2 getUnadjustedAngle() {
        return Rotation2.fromRadians(getAxis(Axis.YAW));
    }

	public double getUnadjustedRate() {
        return Math.toRadians(navX.getRate());
    }

	public final Rotation2 getAngle() {
		Rotation2 angle = getUnadjustedAngle().rotateBy(adjustmentAngle.inverse());

		if (inverted) {
			return angle.inverse();
		}

		return angle;
	}

	public final double getRate() {
		double rate = getUnadjustedRate();

		if (inverted) {
			return -rate;
		}

		return rate;
	}

    public double getAxis(Axis axis) {
        switch (axis) {
            case PITCH:
                return Math.toRadians(navX.getPitch());
            case ROLL:
                return Math.toRadians(navX.getRoll());
            case YAW:
                return Math.toRadians(navX.getYaw());
            default:
                return 0.0;
        }
    }

    public enum Axis {
        PITCH,
        ROLL,
        YAW
    }
}