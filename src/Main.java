import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {



    private static ProcessPanel getProcessPanel(ArrayList<Process> ganttChar) {
        int totalWidth = 50;
        int height = 200;
        int processHeight = 50;
        int padding = 10;


        for (Process process : ganttChar) {
            totalWidth += process.burstTime * 5 + padding;
        }

        ProcessPanel processPanel = new ProcessPanel(ganttChar);
        processPanel.setPreferredSize(new Dimension(totalWidth + 50, processHeight + height));
        return processPanel;
    }

    public static void printAnswer(ArrayList<Process> processes, ArrayList<Process> ganttChar) {
        // Print table header
        System.out.printf("%-15s %-10s %-15s %-12s %-15s %-18s %-18s %-15s\n",
                "Process-Name", "Color", "Arrival-Time", "Burst-Time",
                "Priority", "Completion-Time", "Turnaround-Time", "Waiting-Time");


        System.out.println("--------------------------------------------------------------------------------------------------------------------------");


        for (Process process : processes) {
            System.out.printf("%-15s %-10s %-15d %-12d %-15d %-18d %-18d %-15d\n",
                    process.name, process.color, process.arrivalTime,
                    process.burstTime, process.priorityNumber,
                    process.completionTime, process.turnAroundTime,
                    process.waitingTime);
        }


        ProcessPanel processPanel = getProcessPanel(ganttChar);

        // Create and display the GUI
        JFrame frame = new JFrame("Process Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(Color.BLACK);

        // Add the panel to a JScrollPane for better usability
        JScrollPane scrollPane = new JScrollPane(processPanel);
        frame.add(scrollPane);

        // Use pack() to adjust the frame size dynamically
        frame.pack();


        // Make the frame visible
        frame.setVisible(true);
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
                processes.add(new Process(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5])));
                numberOfProcesses--;
            }


            int agingTime = 10;
            String op = "4";
            SchedulingAlgorithms sch = null;
            if (processes.size() != 0){
                switch (op) {
                    case "1":
                        sch = new PriorityScheduling(processes, contextSwitching);
                        break;
                    case "2":
                        sch = new SJFScheduling(processes, agingTime);
                        break;
                    case "3":
                        sch = new SRTFScheduling(processes, contextSwitching, agingTime);
                        break;
                    case "4":
                        sch = new FCAI(processes, contextSwitching, agingTime);
                        break;
                    default:
                        sch = null; // Handle invalid options (optional)
                        System.out.println("Invalid option. Please select a valid scheduling algorithm.");
                }

                if (sch != null) {
                    sch.calcWaitingTime();
                    printAnswer(sch.processesExecutionOrder(), sch.ganttChart());
                    System.out.println("The Average Waiting Time is: " + sch.calcAvgTurnAroundTime(sch.processesExecutionOrder()));
                    System.out.println("The Average Turnaround Time is: " + sch.calcAvgWaitingTime(sch.processesExecutionOrder()));
                }
            }

            scanner.close(); // Close the scanner to free resources
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

    }




}