/*
 * Main FRC PowerUp Code
 */
package org.usfirst.frc.team5610.robot;

import org.usfirst.frc.team5610.robot.AutoHelper.StartPosition;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

//import org.usfirst.frc5616.RobotTest0001.RobotMap;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
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
	//
	SendableChooser<Integer> chooser;
    //
	double speed = 1.0;
	double leftValue = 1.0;
	double rightValue = 1.0;
	/*
	 * Robot Subsystems
	 */
	Shooter shooter = new Shooter();
	AutoHelper autoHelper;
	String fmsData;
	private Integer autoSelectedInt;
	private String autoSelected;
	

	//private DifferentialDrive m_robotDrive
	//= new DifferentialDrive(new Spark(0), new Spark(1));
   //private Joystick m_stick = new Joystick(0);
   private OurXboxController x_stick = new OurXboxController(0);
	private Timer m_timer = new Timer();
	//
	public ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	private double rightStick;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//rightSpeedControllerGroup.setInverted(true);
		CameraServer.getInstance().startAutomaticCapture();
		autoHelper = new AutoHelper(m_robotDrive);
		fmsData = DriverStation.getInstance().getGameSpecificMessage();
		//
		chooser = new SendableChooser<Integer>();
		chooser.addDefault("leftSwitch", 1);
		chooser.addObject("leftScale", 2);
		chooser.addObject("---------", 3);
		chooser.addObject("rightSwitch", 4);
		chooser.addObject("rightScale", 5);
		chooser.addObject("---------", 6);
		chooser.addObject("centerSwitch", 7);
		chooser.addObject("---------", 8);
		chooser.addObject("centerExchange", 9);
		chooser.addObject("straightForward", 10);
		SmartDashboard.putData("Autonomous Selector", chooser);
		//

		gyro.calibrate();
		gyro.reset();
	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		rightSpeedControllerGroup.setInverted(true);
		leftSpeedControllerGroup.setInverted(false);

		autoSelectedInt = chooser.getSelected();
		System.out.println("Auto selected: " + autoSelected);
		//autoSelectedInt = chooser.getSelected();
		//System.out.println("Auto selected: " + autoSelected);
		m_timer.reset();
		m_timer.start();
		//gyro.calibrate();
		gyro.reset();
		state = 1;
		fmsData = "Left";
	}
	int state = 1;

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {		
		// Drive for 2 seconds
		//autoHelper.initialize(0);
		//autoHelper.autoDrive(1, 0.6, 3);
		//autoHelper.turnTime(2, 0.6, 1, 3);
		//turn2heading(90);
		//autoHelper.autoDrive(2, -0.6, 2);

		SmartDashboard.putNumber("State:", state);
		SmartDashboard.putNumber("Selected:", autoSelectedInt);
		try {
			SmartDashboard.putString("Data:", fmsData.toString());
		}
		catch (Exception e) {
			SmartDashboard.putString("Data:", "Left (null)");
			fmsData = "Left";
		}

		//Use the following to fine tune forward time
		// 140 inches to front of switch
		// 196 inches to back side of switch
		// 261 inches to front of platform
		// 228 inches is where we turn
		double forawrdTimeLeg1 = 2.5;
		// 132 inches to center line
		// 66 inches to first switch (might be the side)
		// 198 inches to second switch
		double forawrdTimeLeg2near = 1.0;
		double forawrdTimeLeg2far  = 3.0;
		// 13 feet/sec, 19 feet to turn point, 1.5 seconds to turn point
		// 8 feet/sec, 19 feet to turn point, 2.4 seconds to turn point
		//// 6 feet to switch, 1 sec
		//// 17 feet to switch, 3 sec

		switch (autoSelectedInt) {
		case 1:
			// leftSwitch
			if (fmsData.charAt(0) == 'L') {
				// Start on Left side
				// Shoot from far side of switch
				//testCase();
				// Start Left, 3 secs, right, 1 sec, right, shoot
				autoStart(AutoHelper.StartPosition.LEFT,forawrdTimeLeg1,forawrdTimeLeg2near);  
			} else {
				// Start Left, 3 secs, right, 5 sec, right, shoot
				autoStart(AutoHelper.StartPosition.LEFT,forawrdTimeLeg1,forawrdTimeLeg2far);  
			}
			break;
		case 2:
			// RightSwitch
			if (fmsData.charAt(0) == 'L') {
				// Start on Right side
				// Shoot from far side of switch
				// Start Right, 3 secs, right, 1 sec, right, shoot
				autoStart(AutoHelper.StartPosition.RIGHT,forawrdTimeLeg1,forawrdTimeLeg2far);  
			} else {
				// Start Left, 3 secs, right, 5 sec, right, shoot
				autoStart(AutoHelper.StartPosition.RIGHT,forawrdTimeLeg1,forawrdTimeLeg2near);  
			}
			break;
		case 3:
			// CenterSwitch
			// If L, turn Left 45, forward to front of switch, shoot
			// If R, turn right 45, forward, shoot
			if (fmsData.charAt(0) == 'L') {
				autoStart(AutoHelper.StartPosition.CENTER,1.0,0.0);  
			} else {
				// Start Left, 3 secs, right, 5 sec, right, shoot
				autoStart(AutoHelper.StartPosition.CENTER,1.0,0.0);  
			}
			break;
		default:
			// Robot can't do the Scale position
			// Default is just to drive over line
		}
		
		
/*		switch(state) {
		case 1: // initialize
			autoHelper.initialize();
			state++;
			break;
		case 2: //Drive Forward (might need to reset clock before thsi)
			// Drive forward until time expires
			if(autoHelper.autoDrive(0.5, 3)) {  // Drive 3 seconds at 50% power
				state ++;  // Goto next state when completed
			}
			break;
		case 3: // Start right turn (reset header)
			autoHelper.autoResetTurn();
			state ++;
			break;
		case 4: // turn Right
			if(autoHelper.autoRightTurnTime()) {
			//if(autoHelper.autoTurnTime(0.5, 1, 4)) {
			//if(autoHelper.autoGyroTurn(90)) {  //Turn right 90 
				state++;  // Goto next state when turn is completed
			}
			break;
		case 5:
			autoHelper.initialize();  // reset drive clock for drive
			state++;
			break;
		case 6: // Forward
			if(autoHelper.autoDrive(0.6, 3)) {  // Drive 3 seconds at 50% power
				state ++;  // Goto next state when completed
			}
			break;
		case 7: // Shoot
			//shooter.turnOn(Shooter.Direction.UP);
			state ++;
			break;
		case 8: // wait 2 seconds
			state ++;
			break;
		case 9: // Advance cube from chamber to shooter
			//chamber.turnOn(Chamber..Direction.UP);
			state ++;
			break;
		default: // Stop all motors
			//shooter.turnOff();
			//chamber.turnOff();
	
		} // end switch
*/	}

	
	@Override
	public void disabledPeriodic() {
		// TODO Auto-generated method stub
		super.disabledPeriodic();
		autoHelper.reset();
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		rightSpeedControllerGroup.setInverted(true);
		leftSpeedControllerGroup.setInverted(true);

		m_robotDrive.setDeadband(-0.5);
		m_robotDrive.setMaxOutput(-0.5);
		//gyro.calibrate();
		gyro.reset();
		
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		SmartDashboard.putNumber("Current Heading:", gyro.getAngle());
		SmartDashboard.putNumber("Current Rate:", gyro.getRate());
		SmartDashboard.putBoolean("Gyro Connected:", gyro.isConnected());
		
		rightStick = x_stick.getRightStickX();
		if( Math.abs(rightStick)< 0.10 && Math.abs(x_stick.getLeftStickY())> 0.10) {
			// Use gyro to fix heading
			// if rate of change is positive, turn the other way
			rightStick = gyro.getRate()/100;
		}
		else {
			rightStick = x_stick.getRightStickX();
		}
		m_robotDrive.arcadeDrive(x_stick.getLeftStickY()*-1.0, rightStick);
		
		//m_robotDrive.arcadeDrive(x_stick.getLeftStickY()*-1.0, x_stick.getRightStickX());
		/* Driver: Bradly
		 * Left Stick: Accelerate front/back
		 * Right Stick: Rotate on -x axis
		 */
		
		if(x_stick.getRawButton(2)) { // B Button
			SmartDashboard.putBoolean("B Button Press: ", true);
			//this.turn2heading(45);
		} else {
			SmartDashboard.putBoolean("B Button Press: ", false);
		}
		SmartDashboard.putNumber("Left Y Stick: ", x_stick.getLeftStickY());
		SmartDashboard.putNumber("Right Y Stick: ", x_stick.getRightStickY());		
	}
	
	
// TEST =================================================================================
	@Override
    public void testInit() {
		 autoHelper.autoResetTurn();
		 autoHelper.initialize(); // Set drive time
		 //
			rightSpeedControllerGroup.setInverted(false);
			m_robotDrive.setDeadband(-0.5);
			m_robotDrive.setMaxOutput(-0.5);
	}
	
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
//		 rightSpeedControllerGroup.set(0.3); 
//		 leftSpeedControllerGroup.set(0.3);
//		 autoHelper.autoTurn(45);
		autoHelper.initialize();
		if(autoHelper.autoRightTurnTime()) { }


	}
	
	public void turn2heading(double degreesToTurn) {
        //double leftBound = 0.97;
        //double upperBound = 1.03;
        double leftBound = 0.9;
        double upperBound = 1.1;
        speed = 0.3;
			boolean ltest = (gyro.getAngle() > (degreesToTurn * leftBound));
			boolean utest = (gyro.getAngle() < (degreesToTurn * upperBound));
			boolean udone = ltest && utest;
//			SmartDashboard.putNumber("Current Heading:", gyro.getAngle());
//			SmartDashboard.putNumber("Current Speed:", speed);
//			SmartDashboard.putNumber("Lower:", degreesToTurn * leftBound);
//			SmartDashboard.putNumber("Upper:", degreesToTurn * upperBound);
//			SmartDashboard.putBoolean("Lower:",ltest);
//			SmartDashboard.putBoolean("Upper:",utest);
//			SmartDashboard.putBoolean("Well:",udone);
			
		if(!udone) {
		if (gyro.getAngle() > (degreesToTurn * leftBound)) {
			//m_robotDrive.tankDrive(leftValue, rightValue);
			m_robotDrive.arcadeDrive(0.0, rightValue);
		} 
		else if (gyro.getAngle() < (degreesToTurn * upperBound)){
			//m_robotDrive.tankDrive(-leftValue, -rightValue);
			m_robotDrive.arcadeDrive(0.0, -rightValue);

		}
		else if(udone) {
			//if (gyro.getAngle() > (degreesToTurn * leftBound) && gyro.getAngle() < (degreesToTurn * upperBound)) { 
				udone = true;
				m_robotDrive.tankDrive(0.0, 0.0);

			} else {
				// Slow down turn
				speed -= 0.035;
				if (speed < 0.3) {
					speed = 0.3;
				}
				leftValue = -speed;
				rightValue =speed;
			}
		} //done		
	}
	
	
	/*
	 * Auto Code for Game
	 * If you start on the LEFT side of switch 
	 * Team Color is LEFT side
	 * -- Throw cube in from left side of switch
	 * ---- forward, turn right, forward, shoot
	 * -- Throw from back side of switch
	 * ---- forward, turn right, forward, turn right, shoot
	 * Team color is RIGHT side
	 * -- Just go pass line
	 * -- Throw cube from back side right switch
	 * ---- forward, turn right, forward, turn right, shoot
	 * 
	 * If you start on the RIGHT side of switch 
	 * -- Throw cube in from right side of switch
	 * ---- forward, turn left, forward, shoot
	 * -- Throw from back side of switch
	 * ---- forward, turn left, forward, turn left, shoot
	 * 
	 * If you start in the CENTER
	 * -- Color is LEFT, turn -45, forward, shoot
	 * -- Color is RIGHT, turn 45, forward, shoot
	 * 
	 * Start(start position[LEFT,CENTER,RIGHT],Forward Time 1,Forward Time 2)
	 * Start(LEFT,3,1) LEFT Switch
	 * Start(LEFT,3,5) RIGHT Switch
	 * Start(RIGHT,3,5) LEFT Switch
	 * Start(RIGHT,3,1) RIGHT Switch
	 */
	public void autoStart(StartPosition pos,double forward_time1,double forward_time2) {
		boolean finished = false;
		switch(state) {
		case 1: // initialize
			autoHelper.initialize();
			state++;
			break;
		case 2: //Drive leg one Forward 
			// Drive forward until time expires
			if(autoHelper.autoDrive(0.5, forward_time1)) {  // Drive x seconds at 50% power
				state ++;  // Goto next state when completed
			}
			break;
		case 3: // Start turn (reset header)
			autoHelper.autoResetTurn();
			state ++;
			break;
		case 4: // If start position is LEFT, then turn right else turn left
			// Center start position has different logic
			if(pos.LEFT == pos) {
				// Right Turn
				finished = autoHelper.autoRightTurnTime();
				//finished = autoGyroTurn(90);
			}
			if(pos.RIGHT == pos) {
				// Left Turn
				finished = autoHelper.autoLeftTurnTime();
			}
			if(finished) {
				state++;  // Goto next state when turn is completed
				finished = false;
			}
			break;
		case 5:
			autoHelper.initialize();  // reset drive clock for drive
			state++;
			break;
		case 6: // Forward
			if(autoHelper.autoDrive(0.5, forward_time2)) {  // Drive y seconds at 50% power
				state ++;  // Goto next state when completed
			}
			break;
		case 7: // Start turn (reset header)
			autoHelper.autoResetTurn();
			state ++;
			break;
		case 8: // If start position is LEFT, then turn right else turn left
			// Center start position has different logic
			if(pos.LEFT == pos) {
				// Right Turn
				finished = autoHelper.autoRightTurnTime();
				//finished = autoGyroTurn(90);
			}
			if(pos.RIGHT == pos) {
				// Left Turn
				finished = autoHelper.autoLeftTurnTime();
			}
			if(finished) {
				state++;  // Goto next state when turn is completed
				finished = false;
			}
			break;
		case 9: // Shoot
			//shooter.turnOn(Shooter.Direction.UP);
			state ++;
			break;
		case 10: // wait 2 seconds
			state ++;
			break;
		case 11: // Advance cube from chamber to shooter
			//chamber.turnOn(Chamber.Direction.UP);
			state ++;
			break;
		default: // Stop all motors
			//shooter.turnOff();
			//chamber.turnOff();
	} //end case		
	} // end method

	
	public void testCase() {
		switch(state) {
		case 1: // initialize
			autoHelper.initialize();
			state++;
			break;
		case 2: //Drive Forward (might need to reset clock before thsi)
			// Drive forward until time expires
			if(autoHelper.autoDrive(0.5, 3)) {  // Drive 3 seconds at 50% power
				state ++;  // Goto next state when completed
			}
			break;
		case 3: // Start right turn (reset header)
			autoHelper.autoResetTurn();
			state ++;
			break;
		case 4: // turn Right
			//if(autoHelper.autoRightTurnTime()) {
			//if(autoHelper.autoTurnTime(0.5, 1, 2)) {  //This turns 180 degrees
			if(autoHelper.autoTurnTime(0.5, 1, 1)) {  //This turns 180 degrees
			//if(autoHelper.autoGyroTurn(90)) {  //Turn right 90 
				state++;  // Goto next state when turn is completed
			}
			break;
		case 5:
			autoHelper.initialize();  // reset drive clock for drive
			state++;
			break;
		case 6: // Forward
			if(autoHelper.autoDrive(0.6, 1)) {  // Drive 3 seconds at 50% power
				state ++;  // Goto next state when completed
			}
			break;
		case 7: // Shoot
			//shooter.turnOn(Shooter.Direction.UP);
			state ++;
			break;
		case 8: // wait 2 seconds
			state ++;
			break;
		case 9: // Advance cube from chamber to shooter
			//chamber.turnOn(Chamber..Direction.UP);
			state ++;
			break;
		default: // Stop all motors
			//shooter.turnOff();
			//chamber.turnOff();
		}

	}
}
