package com.webmihir;

import java.util.ArrayList;


public class ElevatorChooserConcrete implements ElevatorChooserStrategy {
  private final ArrayList<Elevator> _elevators;

  public ElevatorChooserConcrete(ArrayList<Elevator> elevators) {
    _elevators = elevators;
  }

  @Override
  public Elevator getBestElevator(int floor) {
    //Find elevator with least cost to go to a floor
    Elevator bestElevator = _elevators.get(0);
    int minCost = bestElevator.costToGoTo(floor);
    for (int i = 1; i < _elevators.size(); i++) {
      Elevator e = _elevators.get(i);
      int cost = e.costToGoTo(floor);
      if (cost <= minCost && e.getStatus().getState() != ElevatorStatus.State.BROKEN && e.getStatus().getState() != ElevatorStatus.State.MAINTENANCE) {
        minCost = cost;
        bestElevator = e;
      }
    }

    return bestElevator;
  }
}
