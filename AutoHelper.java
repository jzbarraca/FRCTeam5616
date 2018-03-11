package org.usfirst.frc.team5610.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoHelper {

	public enum StartPosition {LEFT,RIGHT,CENTER}
    private Timer main_timer = new Timer();
	private double mark_time = 0;
	private int sequence = 0;
	
	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	private DifferentialDrive driveTrain;        // Passed in constructor
	private double leftSpeed, rightSpeed = 0.4;  // Tune for motor speed
	private double leftValue;
	private double rightValue;

	public AutoHelper(DifferentialDrive robotDriveTrain) {
		driveTrain = robotDriveTrain;
	}

	public void TestDrive() {
		initialize(0);
		//Drive at 30% power for 5 sec
		
	}

	public void reset() {
		sequence = 0;
		main_timer.reset();
		main_timer.start();
	}
	
	public void initialize(int i) {
		if(sequence == i) {
			sequence++;
			main_timer.reset();
			main_timer.start();
		}		
	}

	public void initialize() {
		// Must be first method call for a sequence of autonomous moves
		mark_time = 0;
			main_timer.reset();
			main_timer.start();
	}
	
	public boolean autoDrive(double speed, double howLong) {
		SmartDashboard.putNumber("Current Time:", main_timer.get());
		SmartDashboard.putNumber("Current Time:", getMarkTime());
		boolean udone = false;
		if(getMarkTime() == 0) {
			//mark start of state
			setMarkTime(main_timer.get());
		}
		if(main_timer.get() < (mark_time + howLong)) {
			// Continue Driving
			driveTrain.tankDrive(speed, -speed); // Replace with Gyro Drive
    	} else {
			// State completed
			driveTrain.tankDrive(0, 0);
			setMarkTime(0);
			udone = true;
		}
		return udone;
	}
	
	public void autoDrive(int state, double speed, double howLong) {
		SmartDashboard.putNumber("Seq:", getState());
		SmartDashboard.putNumber("Mark:", mark_time);
		SmartDashboard.putNumber("Current:", main_timer.get());
		
		if(sequence == state) {  // Your turn to run
			if(mark_time == 0) {
				//mark start of state
				mark_time = main_timer.get();
			}
    		if(main_timer.get() < (mark_time + howLong)) {
				// Continue Driving
				driveTrain.tankDrive(speed, -speed);
	    	} else {
				// State completed
				driveTrain.tankDrive(0, 0);
				mark_time = 0;
				sequence++;
			}
		}	
	} // end drive
	
	public boolean autoRightTurnTime() {
		double howLong = 1;
		return autoTurnTime( 0.6, 1, howLong);
	}
	public boolean autoLeftTurnTime() {
		double howLong = 1;
		return autoTurnTime( 0.6, -1, howLong);
	}
	
	public boolean autoTurnTime(double speed, int direction, double howLong) {
		// Direction 1 is right
		// Direction -1 is left
		boolean udone = false;
			if(getMarkTime() == 0) {
				//mark start of state
				setMarkTime(main_timer.get());
			}
			if(getCurrentTime() < (getMarkTime() + howLong)) {
				driveTrain.tankDrive(speed * direction, speed * direction);
			} else {
				// State completed
				driveTrain.tankDrive(0, 0);
				setMarkTime(0);
				udone = true;
			}		
		return udone;
	}

	public void wait(int state, double howLong) {
		if(sequence == state) {  // Your turn to run
			if(getMarkTime() == 0) {
				//mark start of state
				setMarkTime(main_timer.get());
			}
			if(getCurrentTime() < (getMarkTime() + howLong)) {
				// Just wait
				driveTrain.tankDrive(0, 0);
			} else {
				// State completed
				setMarkTime(0);
				sequence++;
			}		
		}
	}
	
	public int getState() {
		return sequence;
	}

	private void setMarkTime(double time) {
		mark_time = time;
	}
	
	private double getMarkTime() {
		return mark_time;
	}
	
	private double getCurrentTime() {
		return main_timer.get();
	}
	
    public void autoResetTurn() {
    	// Reset heading of gyro
    	rightValue = 0.5;
		mark_time = 0;
    	gyro.reset();
    }

	public boolean autoGyroTurn(double degreesToTurn) {
        //double leftBound = 0.97;
        //double upperBound = 1.03;
        double leftBound = 0.9;
        double upperBound = 1.1;
        double speed = 0.7;
        boolean ltest = (gyro.getAngle() > (degreesToTurn * leftBound));
		boolean utest = (gyro.getAngle() < (degreesToTurn * upperBound));
		boolean udone = ltest && utest;
			SmartDashboard.putNumber("Current Heading:", gyro.getAngle());
			SmartDashboard.putNumber("Current Speed:", speed);
			SmartDashboard.putNumber("Lower:", degreesToTurn * leftBound);
			SmartDashboard.putNumber("Upper:", degreesToTurn * upperBound);
			
			SmartDashboard.putBoolean("Lower:",ltest);
			SmartDashboard.putBoolean("Upper:",utest);
			SmartDashboard.putBoolean("Well:",udone);
	
		if(!udone) {
		if (gyro.getAngle() > (degreesToTurn * leftBound)) {
			//m_robotDrive.tankDrive(leftValue, rightValue);
			driveTrain.arcadeDrive(0.0, rightValue);
		} 
		else if (gyro.getAngle() < (degreesToTurn * upperBound)){
			//m_robotDrive.tankDrive(-leftValue, -rightValue);
			driveTrain.arcadeDrive(0.0, -rightValue);

		}
		else if(udone) {
			//if (gyro.getAngle() > (degreesToTurn * leftBound) && gyro.getAngle() < (degreesToTurn * upperBound)) { 
				//done = true;
				driveTrain.tankDrive(0.0, 0.0);
				//udone = true;
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
		return udone;
	}

	public void turn2heading(int state, double degreesToTurn) {
        //double leftBound = 0.97;
        //double upperBound = 1.03;
        double leftBound = 0.9;
        double upperBound = 1.1;
        double speed = 0.7;
			SmartDashboard.putNumber("Current Heading:", gyro.getAngle());
			SmartDashboard.putNumber("Current Speed:", speed);
			SmartDashboard.putNumber("Lower:", degreesToTurn * leftBound);
			SmartDashboard.putNumber("Upper:", degreesToTurn * upperBound);
			boolean ltest = (gyro.getAngle() > (degreesToTurn * leftBound));
			boolean utest = (gyro.getAngle() < (degreesToTurn * upperBound));
			boolean udone = ltest && utest;
			SmartDashboard.putBoolean("Lower:",ltest);
			SmartDashboard.putBoolean("Upper:",utest);
			SmartDashboard.putBoolean("Well:",udone);
		if(sequence == state) {  // Your turn to run
	
		if(!udone) {
		if (gyro.getAngle() > (degreesToTurn * leftBound)) {
			//m_robotDrive.tankDrive(leftValue, rightValue);
			driveTrain.arcadeDrive(0.0, rightValue);
		} 
		else if (gyro.getAngle() < (degreesToTurn * upperBound)){
			//m_robotDrive.tankDrive(-leftValue, -rightValue);
			driveTrain.arcadeDrive(0.0, -rightValue);

		}
		else if(udone) {
			//if (gyro.getAngle() > (degreesToTurn * leftBound) && gyro.getAngle() < (degreesToTurn * upperBound)) { 
				//done = true;
				sequence++;
				driveTrain.tankDrive(0.0, 0.0);

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
		}// end state
	}
	
	public void shootCube() {
		// Turn on shooter
		// wait 2seconds
		// Turn on lower chamber to advance cube to shooter
	}
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
/*	public void autoSwitchL(int state) {
		switch(state) {
		case 1: // initialize
			initialize();
			state++;
			break;
		case 2: //Drive Forward (might need to reset clock before thsi)
			// Drive forward until time expires
			if(autoDrive(0.5, 3)) {  // Drive 3 seconds at 50% power
				state ++;  // Goto next state when completed
			}
			break;
		case 3: // Start right turn (reset header)
			autoResetTurn();
			state ++;
			break;
		case 4: // turn Right
			if(autoRightTurnTime()) {
			//if(autoHelper.autoTurnTime(0.5, 1, 4)) {
			//if(autoHelper.autoGyroTurn(90)) {  //Turn right 90 
				state++;  // Goto next state when turn is completed
			}
			break;
		case 5:
			initialize();  // reset drive clock for drive
			state++;
			break;
		case 6: // Forward
			if(autoDrive(0.6, 3)) {  // Drive 3 seconds at 50% power
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

	} //end case
  }
*/	
/*	public void autoStart(int state, StartPosition pos,double forward_time1,double forward_time2) {
		boolean finished = false;
		switch(state) {
		case 1: // initialize
			initialize();
			state++;
			break;
		case 2: //Drive leg one Forward 
			// Drive forward until time expires
			if(autoDrive(0.5, forward_time1)) {  // Drive x seconds at 50% power
				state ++;  // Goto next state when completed
			}
			break;
		case 3: // Start turn (reset header)
			autoResetTurn();
			state ++;
			break;
		case 4: // If start position is LEFT, then turn right else turn left
			// Center start position has different logic
			if(pos.LEFT == pos) {
				// Right Turn
				finished = autoRightTurnTime();
				//finished = autoGyroTurn(90);
			}
			if(pos.RIGHT == pos) {
				// Left Turn
				finished = autoLeftTurnTime();
			}
			if(finished) {
				state++;  // Goto next state when turn is completed
				finished = false;
			}
			break;
		case 5:
			initialize();  // reset drive clock for drive
			state++;
			break;
		case 6: // Forward
			if(autoDrive(0.5, forward_time2)) {  // Drive y seconds at 50% power
				state ++;  // Goto next state when completed
			}
			break;
		case 7: // Start turn (reset header)
			autoResetTurn();
			state ++;
			break;
		case 8: // If start position is LEFT, then turn right else turn left
			// Center start position has different logic
			if(pos.LEFT == pos) {
				// Right Turn
				finished = autoRightTurnTime();
				//finished = autoGyroTurn(90);
			}
			if(pos.RIGHT == pos) {
				// Left Turn
				finished = autoLeftTurnTime();
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
*/
}
