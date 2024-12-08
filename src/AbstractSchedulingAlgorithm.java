import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class AbstractSchedulingAlgorithm implements SchedulingAlgorithms{

    protected PriorityQueue<Process> readyQueue;
    protected ArrayList<Process> answer = new ArrayList<>();
    protected ArrayList<Process> ganttChart = new ArrayList<>();
    protected PriorityQueue<Process> pqArrivalTime = new PriorityQueue<>(Comparator.comparingInt(p -> p.arrivalTime)); // Ascending

    AbstractSchedulingAlgorithm(ArrayList<Process> processes, int contextTime, int agingTime){
        for (int i = 0; i < processes.size(); i++) {
            pqArrivalTime.add(processes.get(i));
        }

        intializeReadyQueue();

        calcAnswer(processes, contextTime, agingTime);
    }

    abstract public void calcAnswer(ArrayList<Process> processes, int contextTime, int agingTime);


    abstract protected void intializeReadyQueue();

    @Override
    public ArrayList<Process> processesExecutionOrder() {
        return answer;
    }

    protected void priorityToZero() {
        Process[] priorityToZero = pqArrivalTime.toArray(new Process[0]);
        pqArrivalTime.clear();
        for (Process p : priorityToZero){
            p.priorityNumber = 0;
            pqArrivalTime.add(p);
        }
    }

    @Override
    public ArrayList<Process> ganttChart() {
        return ganttChart;
    }

    @Override
    public void calcTurnAroundTime() {
        for (int i = 0; i < answer.size(); i++)
            answer.get(i).turnAroundTime = answer.get(i).completionTime - answer.get(i).arrivalTime;
    }

    @Override
    public void calcWaitingTime(){
        calcTurnAroundTime();
        for (int i = 0; i < answer.size(); i++){
            if (!answer.get(i).name.equals("Idle"))
                answer.get(i).waitingTime = answer.get(i).turnAroundTime - answer.get(i).burstTime;
        }
    }
    @Override
    public double calcAvgWaitingTime(ArrayList<Process> processes){
        Integer counter = 0;
        for (int i = 0; i < processes.size(); i++){
            counter += processes.get(i).waitingTime;
        }
        return (double) counter / processes.size();
    }
    @Override
    public double calcAvgTurnAroundTime(ArrayList<Process> processes){
        int counter = 0;
        for (int i = 0; i < processes.size(); i++){
            counter += processes.get(i).turnAroundTime;
        }
        return (double) counter / processes.size();
    }




}
