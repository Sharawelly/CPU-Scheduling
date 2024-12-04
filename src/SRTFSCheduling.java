import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SRTFSCheduling implements SchedulingAlgorithms{
    private Integer agingTime = 5;
    private PriorityQueue<Process> pqArrivalTime = new PriorityQueue<>(Comparator.comparingInt(p -> p.arrivalTime)); // Ascending

    private PriorityQueue<Process> pqPriorityNumber = new PriorityQueue<>(
            (p1, p2) -> p1.priorityNumber.equals(p2.priorityNumber) ? p1.burstTime - p2.burstTime : p2.priorityNumber - p1.priorityNumber
    ); // Ascending

    private ArrayList<Process> answer = new ArrayList<>();

    SRTFSCheduling(ArrayList<Process> processes, Integer contextTime){
        for (int i = 0; i < processes.size(); i++){
            processes.get(i).priorityNumber = 0;
            pqArrivalTime.add(processes.get(i));
        }

        if (processes.size() <= 1){
            answer = processes;
            return;
        }


        Process process = pqArrivalTime.poll();
        Boolean firstTime = true;
        while (!pqArrivalTime.isEmpty()){
            Integer time = pqArrivalTime.peek().arrivalTime;
            Integer completionTime;
            if (firstTime){
                completionTime = process.arrivalTime + process.burstTime;
                firstTime = false;
            }

            else{
                completionTime = process.completionTime + process.burstTime;
            }

            while (!pqArrivalTime.isEmpty() && pqArrivalTime.peek().arrivalTime < completionTime && pqArrivalTime.peek().arrivalTime == time){
                pqPriorityNumber.add(pqArrivalTime.poll());
            }
            if (pqPriorityNumber.isEmpty() || (!pqPriorityNumber.isEmpty() && process.burstTime <= pqPriorityNumber.peek().burstTime)){
                if (answer.isEmpty())
                    process.completionTime = process.arrivalTime + process.burstTime;
                else
                    process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime;
                answer.add(process);
                process = pqArrivalTime.poll();
            }
            else{
                process.burstTime -= (pqPriorityNumber.peek().arrivalTime - process.arrivalTime);
                if (answer.isEmpty())
                    process.completionTime = process.arrivalTime + (pqPriorityNumber.peek().arrivalTime - process.arrivalTime);
                else
                    process.completionTime = answer.get(answer.size() - 1).completionTime + (pqPriorityNumber.peek().arrivalTime - process.arrivalTime);
                answer.add(process);
                pqPriorityNumber.add(process);
                process = pqPriorityNumber.poll();
            }
        }
        while (!pqPriorityNumber.isEmpty()){
            process = pqPriorityNumber.poll();
            process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime;
            answer.add(process);
        }


    }
    @Override
    public ArrayList<Process> processesExecutionOrder() {
        return answer;
    }

    @Override
    public void calcWaitingTime() {

    }

    @Override
    public void calcTurnAroundTime() {

    }
}
