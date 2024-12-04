import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SJFScheduling implements SchedulingAlgorithms{
    private Integer agingTime = 5;
    private ArrayList<Process> answer = new ArrayList<>();

    private PriorityQueue<Process> pqArrivalTime = new PriorityQueue<>(Comparator.comparingInt(p -> p.arrivalTime)); // Ascending
    private PriorityQueue<Process> pqPriorityNumber = new PriorityQueue<>(
            (p1, p2) -> p1.priorityNumber.equals(p2.priorityNumber) ? p1.burstTime - p2.burstTime : p2.priorityNumber - p1.priorityNumber
    ); // Ascending

    SJFScheduling(ArrayList<Process> processes){
        for (int i = 0; i < processes.size(); i++){
            processes.get(i).priorityNumber = 0;
            pqArrivalTime.add(processes.get(i));
        }

        while (!pqArrivalTime.isEmpty()){
            Process process = pqArrivalTime.poll();
            if (answer.isEmpty())
                process.completionTime = process.arrivalTime + process.burstTime;
            else
                process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime;
            answer.add(process);
            Integer time = process.arrivalTime + process.burstTime;
            while (!pqArrivalTime.isEmpty() && pqArrivalTime.peek().arrivalTime <= time){
                pqPriorityNumber.add(pqArrivalTime.poll());
            }
            while (!pqPriorityNumber.isEmpty()){
                while (!pqArrivalTime.isEmpty() && pqArrivalTime.peek().arrivalTime <= process.completionTime){
                    pqPriorityNumber.add(pqArrivalTime.poll());
                }
                if (process.completionTime >= agingTime){
                    Process[] pr = pqPriorityNumber.toArray(new Process[0]);
                    pqPriorityNumber.clear();
                    for (Process p : pr){
                        if (p.arrivalTime + process.completionTime <= agingTime)
                            p.priorityNumber++;
                    }
                    for (Process p : pr)
                        pqPriorityNumber.add(p);
                    agingTime *=2;
                }
                else{
                    Process process2 = pqPriorityNumber.poll();
                    process2.completionTime = process.completionTime + process2.burstTime;
                    process = process2;
                    answer.add(process);
                }
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
