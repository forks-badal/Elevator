package com.webmihir;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;


public class FloorChooserConcrete implements FloorChooserStrategy {
  private PriorityQueue<Integer> _upQueue = new PriorityQueue<>();
  private PriorityQueue<Integer> _downQueue = new PriorityQueue<>(Comparator.reverseOrder());
  private int _minFloor;
  private int _maxFloor;
  private ElevatorStatus _status;

  private final int COST_PER_STOP = 10;
  private final int COST_PER_VISIT = 1;

  public FloorChooserConcrete(int minFloor, int maxFloor, ElevatorStatus initialStatus) {
    _minFloor = minFloor;
    _maxFloor = maxFloor;
    _status = initialStatus;
  }

  @Override
  public int getNextFloor() {
      if (_status.getState() == ElevatorStatus.State.GOING_UP && !_upQueue.isEmpty()) {
        return _upQueue.poll();
      }
      else if (_status.getState() == ElevatorStatus.State.GOING_DOWN && !_downQueue.isEmpty()) {
        return _downQueue.poll();
      }
      else if (!_downQueue.isEmpty()) {
        return _downQueue.poll();
      } else if (!_upQueue.isEmpty()) {
        return _upQueue.poll();
      }

      return _minFloor;
  }

  @Override
  public int getCost(int floor) {
    if (floor < _minFloor || floor > _maxFloor) {
      return Integer.MAX_VALUE;
    }
    if (_status.getState() == ElevatorStatus.State.BROKEN || _status.getState() == ElevatorStatus.State.MAINTENANCE) {
      return Integer.MAX_VALUE;
    }

    if (_status.getFloor() == floor) {
      return 0;
    }

    //Calculate actual cost
    int numStops = getNumStops(floor);
    int numVisits = getNumVisits(floor);
    return (numStops * COST_PER_STOP) + (numVisits * COST_PER_VISIT);
  }

  private int getNumStops(int floor) {
    //TODO: Get actual number of stops between current floor and target floor
    return 0;
  }

  private int getNumVisits(int floor) {
    int currentFloor = _status.getFloor();
    ElevatorStatus.State currentState = _status.getState();
    int absDistance = Math.abs(currentFloor - floor);

    //If elevator is stooped or travelling in the same direction as floor, return the difference
    if (currentState == ElevatorStatus.State.STOPPED ||
        (currentFloor < floor && currentState == ElevatorStatus.State.GOING_UP) ||
        (currentFloor > floor && currentState == ElevatorStatus.State.GOING_DOWN)) {
      return absDistance;
    }

    //If elevator is going down and request is at a higher floor
    //Elevator needs to travel to floor 0 first, then to the target floor
    if (currentFloor < floor && currentState == ElevatorStatus.State.GOING_DOWN) {
      return currentFloor + floor;
    }

    //If elevator is going up and request is at a lower floor
    //Elevator needs to travel to max floor first, then to the target floor
    return (_maxFloor - currentFloor) + (_maxFloor - floor);
  }

  @Override
  public void addFloor(int floor) {
    Queue<Integer> targetQueue = _upQueue;

    if (_status.getFloor() > floor) {
      targetQueue = _downQueue;
    }

    targetQueue.offer(floor);
  }

  @Override
  public boolean hasMore() {
    return !_upQueue.isEmpty() || !_downQueue.isEmpty();
  }

  @Override
  public void notify(ElevatorStatus status) {
    _status = status;

    //If you reached the top floor and there is still work remaining in upqueue
    //Move it to the down queue
    if (status.getFloor() >= _maxFloor) {
      while (!_upQueue.isEmpty()) {
        _downQueue.offer(_upQueue.poll());
      }
    }

    //If you reached the bottom floor and there is still work remaining in down queue
    //Move it to the up queue
    if (status.getFloor() <= _minFloor) {
      while (!_downQueue.isEmpty()) {
        _upQueue.offer(_downQueue.poll());
      }
    }
  }
}
