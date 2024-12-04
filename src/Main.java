import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void printAnswer(ArrayList<Process> processes) {
        // Print table header
        System.out.printf("%-15s %-10s %-15s %-12s %-15s %-18s %-18s %-15s\n",
                "Process-Name", "Color", "Arrival-Time", "Burst-Time",
                "Priority", "Completion-Time", "Turnaround-Time", "Waiting-Time");

        // Print a separator line
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");


        for (Process process : processes) {
            System.out.printf("%-15s %-10s %-15d %-12d %-15d %-18d %-18d %-15d\n",
                    process.name, process.color, process.arrivalTime,
                    process.burstTime, process.priorityNumber,
                    process.completionTime, process.turnAroundTime,
                    process.waitingTime);
        }
    }


    public static void main(String[] args) {
        try {
            File inputFile = new File("input.txt");
            Scanner scanner = new Scanner(inputFile);

            String line = "";
            if (scanner.hasNextLine()){
                line = scanner.nextLine();
            }
            String[] partition = line.split("\\s+");
            Integer numberOfProcesses = Integer.parseInt(partition[0]);
            Integer roundRobinTime = Integer.parseInt(partition[1]);
            Integer contextSwitching = Integer.parseInt(partition[2]);

            ArrayList<Process> processes = new ArrayList<>();
            while (numberOfProcesses != 0 && scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] parts = line.split("\\s+");
                processes.add(new Process(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
                numberOfProcesses--;
            }
            int op = 3;
            if (op == 1){
                SchedulingAlgorithms sch = new PriorityScheduling(processes, contextSwitching);
                sch.calcWaitingTime();
                printAnswer(sch.processesExecutionOrder());
                System.out.println("The Average Waiting Time is: " + sch.calcAvgTurnAroundTime(sch.processesExecutionOrder()));
                System.out.println("The Average Turnaround  Time is: " + sch.calcAvgWaitingTime(sch.processesExecutionOrder()));
            } else if (op == 2) {
                SchedulingAlgorithms sch = new SJFScheduling(processes);
                sch.calcWaitingTime();
                printAnswer(sch.processesExecutionOrder());
                System.out.println("The Average Waiting Time is: " + sch.calcAvgTurnAroundTime(sch.processesExecutionOrder()));
                System.out.println("The Average Turnaround  Time is: " + sch.calcAvgWaitingTime(sch.processesExecutionOrder()));
            } else if (op == 3) {
                SchedulingAlgorithms sch = new SRTFSCheduling(processes, contextSwitching);
                sch.calcWaitingTime();
                printAnswer(sch.processesExecutionOrder());
                System.out.println("The Average Waiting Time is: " + sch.calcAvgTurnAroundTime(sch.processesExecutionOrder()));
                System.out.println("The Average Turnaround  Time is: " + sch.calcAvgWaitingTime(sch.processesExecutionOrder()));
            } else if (op == 4) {

            }

            scanner.close(); // Close the scanner to free resources
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }


    }
}