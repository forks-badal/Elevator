package com.webmihir;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ElevatorController {
  private final ElevatorChooserStrategy _elevatorChooser;
  private final ArrayList<Elevator> _elevators;
  private static ElevatorController INSTANCE;
  private static final Object _lockObj = new Object();
  private static ExecutorService _executor;

  private ElevatorController(ElevatorChooserStrategy elevatorChooser, ArrayList<Elevator> elevators) {
    _elevatorChooser = elevatorChooser;
    _elevators = elevators;
  }

  public static ElevatorController getInstance(ElevatorChooserStrategy elevatorChooser, ArrayList<Elevator> elevators) {
    if (INSTANCE != null) return INSTANCE;

    synchronized (_lockObj) {
      if (INSTANCE == null) {
        INSTANCE = new ElevatorController(elevatorChooser, elevators);
        _executor = Executors.newFixedThreadPool(elevators.size());
        for(Elevator e : elevators) {
          _executor.submit(e);
        }
      }
    }

    return INSTANCE;
  }

  public void callElevator(int floor) {
    Elevator bestElevator = _elevatorChooser.getBestElevator(floor);
    callElevator(floor, bestElevator);
  }

  private void callElevator(int floor, Elevator elevator) {
    elevator.gotoFloor(floor);
  }

  public void callElevator(int floor, int elevator) {
    Elevator e = getElevator(elevator);
    if (e == null) return;
    callElevator(floor, e);
  }

  private Elevator getElevator(int elevator) {
    if (elevator < 0 || elevator >= _elevators.size()) return null;
    return _elevators.get(elevator);
  }

  public void setMaintenanceMode(int elevator) {
    Elevator e = getElevator(elevator);
    if (e == null) return;
    e.setMaintenance();
  }

  public void setBroken(int elevator) {
    Elevator e = getElevator(elevator);
    if (e == null) return;
    e.setBroken();
  }

  public void resetElevator(int elevator) {
    Elevator e = getElevator(elevator);
    if (e == null) return;
    e.resetElevator();
  }
}
