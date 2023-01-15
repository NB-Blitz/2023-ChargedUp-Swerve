package frc.robot.common.math;

public interface Interpolable<T> {
    T interpolate(T other, double t);
}