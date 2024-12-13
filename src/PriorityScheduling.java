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
    protected void intializeArraivalQueue() {
        pqArrivalTime = new PriorityQueue<>(
                (p1, p2) -> (p1.arrivalTime == p2.arrivalTime) ? p1.priorityNumber - p2.priorityNumber : p1.arrivalTime - p2.arrivalTime
        );
    }

    @Override
    public void calcAnswer(ArrayList<Process> processes, int contextTime, int agingTime) {
        int completionTime = 0;
        Process process;
        while (!pqArrivalTime.isEmpty()){
            Boolean enter = false;
            while (!pqArrivalTime.isEmpty() && completionTime >= pqArrivalTime.peek().arrivalTime) {
                enter = true;
                readyQueue.add(pqArrivalTime.poll());
            }
            process = enter ? readyQueue.poll() : pqArrivalTime.poll();
            int time = process.arrivalTime + process.burstTime;
            if (answer.isEmpty())
                process.completionTime = process.arrivalTime + process.burstTime;
            else
                process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime + contextTime;
            answer.add(process);
            while (!pqArrivalTime.isEmpty() && pqArrivalTime.peek().arrivalTime <= time){
                readyQueue.add(pqArrivalTime.poll());
            }

            while (!readyQueue.isEmpty()){
                process = readyQueue.poll();
                process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime + contextTime;
                answer.add(process);
                while (!pqArrivalTime.isEmpty() && answer.getLast().completionTime >= pqArrivalTime.peek().arrivalTime){
                    readyQueue.add(pqArrivalTime.poll());
                }
            }
            completionTime = answer.getLast().completionTime;
        }

        for (Process p : answer){
            for (int i = 0; i < p.burstTime; i++){
                ganttChart.add(p);
            }
            if (!p.equals(answer.getLast())){
                for (int i = 0; i < contextTime; i++){
                    ganttChart.add(new Process());
                }
            }
        }

    }



}

