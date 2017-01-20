package com.webmihir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {

  public static void main(String[] args) throws InterruptedException {
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

    int numElevators = getNumElevators(buffer);
    final int min;
    final int max;
    int tmpMin, tmpMax;
    min = getMinFloor(buffer);
    max = getMaxFloor(buffer, min);

    ArrayList<Elevator> arr = IntStream.range(1, numElevators + 1)
        .mapToObj(n -> Elevator.createElevator(n - 1, min, max, new ElevatorStatus(0, ElevatorStatus.State.STOPPED)))
        .collect(Collectors.toCollection(ArrayList::new));
    ElevatorController controller = ElevatorController.getInstance(new ElevatorChooserConcrete(arr), arr);
    TimeUnit.SECONDS.sleep(1);
    runPrompts(buffer, controller, arr);
  }

  static private int getNumElevators(BufferedReader buffer) {
    while (true) {
      int numElevators = 0;
      System.out.print("How many elevators does this system have? ");
      try {
        numElevators = Integer.parseInt(buffer.readLine());
        return numElevators;
      } catch (IOException e) {
        System.out.println("Invalid Input");
      }
    }
  }

  static private int getMinFloor(BufferedReader buffer) {
    //TODO: Prompt user for input
    return 0;
  }

  static private int getMaxFloor(BufferedReader buffer, int min) {
    //TODO: Prompt user for input
    return 100;
  }

  static private void runPrompts(BufferedReader buffer, ElevatorController controller, ArrayList<Elevator> elevators) {
    while (true) {
      int n = getUserInput(buffer);
      switch (n) {
        case 1: //call any elevator
          controller.callElevator(getFloor(buffer));
          break;

        case 2: //Call specific elevator to floor
          controller.callElevator(getFloor(buffer), getElevator(buffer));
          break;

        case 3: //Put elevator in maintenance mode
          controller.setMaintenanceMode(getElevator(buffer));
          break;

        case 4: //Put elevator in broken mode
          controller.setBroken(getElevator(buffer));
          break;

        case 5: //Reset elevator state
          controller.resetElevator(getElevator(buffer));
          break;

        case 6: //Stop program
          return;
      }
    }
  }

  static private void printPrompts() {
    System.out.println("\n***** MAIN MENU *****");
    System.out.println("Chose an option: ");
    System.out.println("1) Call any elevator to a specified floor.");
    System.out.println("2) Go to a floor from a specific elevator");
    System.out.println("3) Put an elevator in maintenance mode");
    System.out.println("4) Put an elevator in broken mode");
    System.out.println("5) Reset an elevator's state");
    System.out.println("6) Quit");
  }

  static private int getUserInput(BufferedReader buffer) {
    printPrompts();
    while (true) {
      try {
        System.out.print("Enter Your Choice: ");
        int n = Integer.parseInt(buffer.readLine());
        if (n >= 1 && n <= 6) return n;
        System.out.println("Invalid Input.");
      } catch (Exception e) {
        System.out.println("Invalid Input.");
      }
    }
  }

  static private int getFloor(BufferedReader buffer) {
    while (true) {
      try {
        System.out.print("\tEnter desired floor: ");
        int floor = Integer.parseInt(buffer.readLine());
        return floor;
      } catch (Exception e) {
        System.out.println("Invalid Input.");
      }
    }
  }

  static private int getElevator(BufferedReader buffer) {
    while (true) {
      try {
        System.out.print("\tEnter desired elevator: ");
        int elevator = Integer.parseInt(buffer.readLine());
        return elevator;
      } catch(Exception e) {
        System.out.println("Invalid Input.");
      }
    }
  }
}
