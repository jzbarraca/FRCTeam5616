package org.usfirst.frc.team5610.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;

public class Shooter {
	
	boolean shooter_state = false;
	
	private Talon leftMotor = new Talon(4);
	private Talon rightMotor = new Talon(5);
	
	private SpeedControllerGroup shooterSpeedControllerGroup
	= new SpeedControllerGroup(leftMotor, rightMotor);
	
	
	public void initialize() {
		rightMotor.setInverted(true);
		shooterSpeedControllerGroup.set(1.0);
		this.setShooter_state(true);
	}
	
	public boolean isShooter_state() {
		return shooter_state;
	}

	public void setShooter_state(boolean shooter_state) {
		this.shooter_state = shooter_state;
	}
	

}
