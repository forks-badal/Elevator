package com.webmihir;

public class ElevatorStatus {
  public enum State {
    GOING_UP,
    GOING_DOWN,
    STOPPED,
    MAINTENANCE,
    BROKEN
  };

  private int currentFloor;
  private State currentState;

  public int getFloor() {
    return currentFloor;
  }

  public State getState() {
    return currentState;
  }

  public ElevatorStatus(int floor, State state) {
    this.currentFloor = floor;
    this.currentState = state;
  }

  public void setFloor(int floor) {
    this.currentFloor = floor;
  }

  public void setState(State state) {
    this.currentState = state;
  }
}
