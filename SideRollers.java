package org.usfirst.frc.team5616.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class SideRollers {
	
		private Spark leftFrontMotor = new Spark(0);
		private Spark leftBackMotor = new Spark(1);
		private Spark rightFrontMotor = new Spark (2);
		private Spark rightBackMotor = new Spark (3);
		
		private SpeedControllerGroup rightSpeedControllerGroup
		= new SpeedControllerGroup(rightFrontMotor, rightBackMotor);
		private SpeedControllerGroup leftSpeedControllerGroup
		= new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
	
		//public void SideRollersForward() {
			//three modes: forward to shoot, backward to deliver, and nothing
		//	public JoystickButton aButton = new JoystickButton(this, 1);
	//	}
		
	//	public void SideRollersBackward() {
	//		public JoystickButton bButton = new JoystickButton(this, 2);
	//	}

}
