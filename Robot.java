/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5616.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
/*	private Spark leftFrontMotor = new Spark(0);
	private Spark leftBackMotor = new Spark(1);
	private Spark rightFrontMotor = new Spark(2);
	private Spark rightBackMotor = new Spark(3);
	*/
	private Talon leftFrontMotor = new Talon(0);
	private Talon leftBackMotor = new Talon(1);
	private Talon rightFrontMotor = new Talon(2);
	private Talon rightBackMotor = new Talon(3);
	
	private SpeedControllerGroup rightSpeedControllerGroup
	= new SpeedControllerGroup(rightFrontMotor, rightBackMotor);
private SpeedControllerGroup leftSpeedControllerGroup
	= new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
private DifferentialDrive m_robotDrive
	= new DifferentialDrive(leftSpeedControllerGroup, rightSpeedControllerGroup);


	//private DifferentialDrive m_robotDrive
		//	= new DifferentialDrive(new Spark(0), new Spark(1));
	private Joystick m_stick = new Joystick(0);
	//private OurXboxController x_stick = new OurXboxController(0);
	private Timer m_timer = new Timer();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		rightSpeedControllerGroup.setInverted(true);
	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		m_timer.reset();
		m_timer.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		// Drive for 2 seconds
		if (m_timer.get() < 2.0) {
			m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
			//eftSpeedControllerGroup.set(0.3); 
			//rightSpeedControllerGroup.set(0.3);
			//m_robotDrive.tankDrive(0.5, -0.5);  // Pivot Turn 2 secs
		} else {
			m_robotDrive.stopMotor(); // stop robot
		}
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		rightSpeedControllerGroup.setInverted(false);
		m_robotDrive.setDeadband(0.10);
		m_robotDrive.setMaxOutput(0.60);
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		//m_robotDrive.tankDrive(x_stick.getLeftStickY(), x_stick.getRightStickY());
		m_robotDrive.arcadeDrive(x_stick.getLeftStickY()*-1.0, x_stick.getRightStickX());
		/* Driver: Bradly
		 * Left Stick: Accelerate front/back
		 * Right Stick: Rotate on x axis
		 */
		
		if(x_stick.getRawButton(2)) { // B Button
			SmartDashboard.putBoolean("B Button Press: ", true);
		} else {
			SmartDashboard.putBoolean("B Button Press: ", false);
		}
		SmartDashboard.putNumber("Left Y Stick: ", x_stick.getLeftStickY());
		SmartDashboard.putNumber("Right Y Stick: ", x_stick.getRightStickY());		

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		rightSpeedControllerGroup.set(0.3);
		leftSpeedControllerGroup.set(0.3);
		
	}
}
