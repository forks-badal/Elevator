package com.webmihir;

import java.util.concurrent.TimeUnit;


public class ElevatorConcrete extends Elevator {
  private final int _id;
  private final FloorChooserStrategy _floorChooser;
  private final ElevatorStatus _status = new ElevatorStatus(0, ElevatorStatus.State.STOPPED);

  ElevatorConcrete(int id, FloorChooserStrategy floorChooser) {
    _id = id;
    _floorChooser = floorChooser;
  }

  @Override
  public ElevatorStatus getStatus() {
    return _status;
  }

  @Override
  public void setMaintenance() {
    log("In Maintenance Mode.");
    _status.setState(ElevatorStatus.State.MAINTENANCE);
    _floorChooser.notify(_status);
  }

  @Override
  public void setBroken() {
    log("In Broken Mode.");
    _status.setState(ElevatorStatus.State.BROKEN);
    _floorChooser.notify(_status);
  }

  @Override
  public void resetElevator() {
    log("In Stopped Mode.");
    _status.setState(ElevatorStatus.State.STOPPED);
    _floorChooser.notify(_status);
  }

  @Override
  public void gotoFloor(int floor) {
    _floorChooser.addFloor(floor);
    log("Adding Floor: " + floor);
  }

  @Override
  public int getId() {
    return _id;
  }

  @Override
  public int costToGoTo(int floor) {
    return _floorChooser.getCost(floor);
  }

  private void moveTo(int floor) {
    if (_status.getFloor() < floor) {
      _status.setState(ElevatorStatus.State.GOING_UP);
    } else if (_status.getFloor() > floor) {
      _status.setState(ElevatorStatus.State.GOING_DOWN);
    } else {
      _status.setState(ElevatorStatus.State.STOPPED);
    }

    _status.setFloor(floor);
    _floorChooser.notify(_status);
    log("Moved to floor: " + floor + ", State = " + _status.getState().name());
  }

  private void log(String s) {
    System.out.println("Elevator " + _id + ": " + s);
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used
   * to create a thread, starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may
   * take any action whatsoever.
   *
   * @see     Thread#run()
   */
  @Override
  public void run() {
    log("Starting.");
    while (true) {
      if (!_floorChooser.hasMore()) {
        try {
          TimeUnit.MILLISECONDS.sleep(250);
          continue;
        } catch (InterruptedException e) {
          //...
        }
      }

      int nextFloor = _floorChooser.getNextFloor();
      moveTo(nextFloor);
      if (!_floorChooser.hasMore()) {
        _status.setState(ElevatorStatus.State.STOPPED);
        _floorChooser.notify(_status);
      }
    }
  }
}
