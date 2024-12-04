import java.util.ArrayList;

public interface SchedulingAlgorithms {
    public ArrayList<Process> processesExecutionOrder();
    public void calcWaitingTime();
    public void calcTurnAroundTime();
    default public double calcAvgWaitingTime(ArrayList<Process> processes){
        Integer counter = 0;
        for (int i = 0; i < processes.size(); i++){
            counter += processes.get(i).waitingTime;
        }
        return (double) counter / processes.size();
    }
    default public double calcAvgTurnAroundTime(ArrayList<Process> processes){
        Integer counter = 0;
        for (int i = 0; i < processes.size(); i++){
            counter += processes.get(i).turnAroundTime;
        }
        return (double) counter / processes.size();
    }



}
