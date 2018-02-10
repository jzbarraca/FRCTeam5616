package org.usfirst.frc.team5610.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;


public class Template {
	public enum Direction {UP,DOWN}
	Direction direction;

    private int pwm_leftMotor = 4;
    private int pwm_rightMotor = 5;
    
	private Spark leftMotor = new Spark(pwm_leftMotor);
	private Spark rightMotor = new Spark(pwm_rightMotor);
	SpeedControllerGroup motorGroup = new SpeedControllerGroup(leftMotor,rightMotor);
	
	public Template() {
		// TODO Auto-generated constructor stub
	}
	
	public void turnOn(Direction dir) {
		rightMotor.setInverted(true);
		switch (dir) {
		   case UP:
			   motorGroup.set(1.0);
			   break;
		   case DOWN:
			   motorGroup.set(-1.0);
			   break;
		   default:
			   // Turn off motor
			   motorGroup.set(0.0);
			   break;			   
		}		
	}
	
	public void turnOff() {
		   motorGroup.set(0.0);
	}
	
}
