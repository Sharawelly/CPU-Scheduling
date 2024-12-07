import java.util.ArrayList;
import java.util.PriorityQueue;

public class PriorityScheduling extends AbstractSchedulingAlgorithm{


    PriorityScheduling(ArrayList<Process> processes, int contextTime){
        super(processes, contextTime, 0);

    }

    @Override
    public void intializeReadyQueue() {
        readyQueue = new PriorityQueue<>(
                (p1, p2) -> (p1.priorityNumber == p2.priorityNumber) ? p1.arrivalTime - p2.arrivalTime : p1.priorityNumber - p2.priorityNumber
        ); // Ascending
    }

    @Override
    public void calcAnswer(ArrayList<Process> processes, int contextTime, int agingTime) {
        while (!pqArrivalTime.isEmpty()){
            Process process = pqArrivalTime.poll();
            if (answer.isEmpty())
                process.completionTime = process.arrivalTime + process.burstTime;
            else
                process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime + contextTime;
            answer.add(process);
            int time = process.arrivalTime + process.burstTime;
            while (!pqArrivalTime.isEmpty() && pqArrivalTime.peek().arrivalTime <= time){
                readyQueue.add(pqArrivalTime.poll());
            }
            while (!readyQueue.isEmpty()){
                process = readyQueue.poll();
                process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime + contextTime;
                answer.add(process);
            }
        }

        for (Process process : answer){
            for (int i = 0; i < process.burstTime; i++){
                ganttChart.add(process);
            }
            if (!process.equals(answer.getLast())){
                for (int i = 0; i < contextTime; i++){
                    ganttChart.add(new Process());
                }
            }
        }

    }



}
