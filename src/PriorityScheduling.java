import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityScheduling implements SchedulingAlgorithms{

    private ArrayList<Process> answer = new ArrayList<>();
    private PriorityQueue<Process> pqPriorityNumber = new PriorityQueue<>(
            (p1, p2) -> p1.priorityNumber.equals(p2.priorityNumber) ? p1.arrivalTime - p2.arrivalTime : p1.priorityNumber - p2.priorityNumber
    ); // Ascending
    private PriorityQueue<Process> pqArrivalTime = new PriorityQueue<>(Comparator.comparingInt(p -> p.arrivalTime)); // Ascending

    PriorityScheduling(ArrayList<Process> processes, Integer contextTime){
        for (int i = 0; i < processes.size(); i++){
            pqArrivalTime.add(processes.get(i));
        }

        while (!pqArrivalTime.isEmpty()){
            Process process = pqArrivalTime.poll();
            if (answer.isEmpty())
                process.completionTime = process.arrivalTime + process.burstTime + contextTime;
            else
                process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime + contextTime;
            answer.add(process);
            Integer time = process.arrivalTime + process.burstTime;
            while (!pqArrivalTime.isEmpty() && pqArrivalTime.peek().arrivalTime <= time){
                pqPriorityNumber.add(pqArrivalTime.poll());
            }
            while (!pqPriorityNumber.isEmpty()){
                process = pqPriorityNumber.poll();
                process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime + contextTime;
                answer.add(process);
            }
        }

    }
    @Override
    public ArrayList<Process> processesExecutionOrder() {
        return answer;
    }

    @Override
    public void calcWaitingTime() {
        calcTurnAroundTime();
        for (int i = 0; i < answer.size(); i++)
            answer.get(i).waitingTime = answer.get(i).turnAroundTime - answer.get(i).burstTime;

    }

    @Override
    public void calcTurnAroundTime() {
        for (int i = 0; i < answer.size(); i++)
            answer.get(i).turnAroundTime = answer.get(i).completionTime - answer.get(i).arrivalTime;
    }

}
