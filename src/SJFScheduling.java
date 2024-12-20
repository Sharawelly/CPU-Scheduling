import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SJFScheduling extends AbstractSchedulingAlgorithm{


    SJFScheduling(ArrayList<Process> processes, int agingTime){
        super(processes, 0, agingTime);

    }


    @Override
    public void intializeReadyQueue() {
        readyQueue = new PriorityQueue<>(
                (p1, p2) -> (p1.priorityNumber == p2.priorityNumber) ? p1.burstTime - p2.burstTime : p2.priorityNumber - p1.priorityNumber
        ); // Ascending
    }

    @Override
    protected void intializeArraivalQueue() {
        pqArrivalTime = new PriorityQueue<>(
                (p1, p2) -> (p1.arrivalTime == p2.arrivalTime) ? p1.burstTime - p2.burstTime : p1.arrivalTime - p2.arrivalTime
        );
    }

    @Override
    public void calcAnswer(ArrayList<Process> processes, int contextTime, int agingTime) {
        super.priorityToZero();
        int piriorityTime = agingTime;
        while (!pqArrivalTime.isEmpty()){
            Process process = pqArrivalTime.poll();
            if (answer.isEmpty())   
                process.completionTime = process.arrivalTime + process.burstTime;
            else
                process.completionTime = answer.get(answer.size() - 1).completionTime + process.burstTime;
            answer.add(process);
            int time = process.arrivalTime + process.burstTime;
            while (!pqArrivalTime.isEmpty() && pqArrivalTime.peek().arrivalTime <= time){
                readyQueue.add(pqArrivalTime.poll());
            }
            while (!readyQueue.isEmpty()){
                while (!pqArrivalTime.isEmpty() && pqArrivalTime.peek().arrivalTime <= process.completionTime){
                    readyQueue.add(pqArrivalTime.poll());
                }
                if (process.completionTime >= piriorityTime){
                    Process[] pr = readyQueue.toArray(new Process[0]);
                    readyQueue.clear();
                    for (Process p : pr){
                        if (process.completionTime - p.arrivalTime >= agingTime)
                            p.priorityNumber++;
                    }
                    for (Process p : pr)
                        readyQueue.add(p);
                    piriorityTime += agingTime;
                }
                Process process2 = readyQueue.poll();
                process2.completionTime = process.completionTime + process2.burstTime;
                process = process2;
                answer.add(process);
            }

        }
        for (Process process : answer){
            for (int i = 0; i < process.burstTime; i++){
                ganttChart.add(process);
            }
        }
    }


}
