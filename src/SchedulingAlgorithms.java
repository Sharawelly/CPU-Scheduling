import java.util.ArrayList;

public interface SchedulingAlgorithms {
    public ArrayList<Process> processesExecutionOrder();

    public ArrayList<Process> ganttChart();
    public void calcWaitingTime();
    public void calcTurnAroundTime();

    public double calcAvgWaitingTime(ArrayList<Process> processes);
    public double calcAvgTurnAroundTime(ArrayList<Process> processes);



}
