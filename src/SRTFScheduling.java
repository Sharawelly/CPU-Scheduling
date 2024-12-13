import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class SRTFScheduling extends AbstractSchedulingAlgorithm {

    SRTFScheduling(ArrayList<Process> processes, int contextTime, int agingTime) {
        super(processes, contextTime, agingTime);

    }

    @Override
    protected void intializeReadyQueue() {
        readyQueue = new PriorityQueue<>(
                (p1, p2) ->
                        (p1.priorityNumber == p2.priorityNumber) ?
                                (p1.burstTime == p2.burstTime)?
                                        p1.arrivalTime - p2.arrivalTime:
                                        p1.burstTime - p2.burstTime :
                                p2.priorityNumber - p1.priorityNumber

        );
    }


    @Override
    public void calcAnswer(ArrayList<Process> processes, int contextTime, int agingTime) {
        super.priorityToZero();
        if (processes.size() <= 1) {
            ganttChart = processes;
            return;
        }

        int currentTime = 0, completionTime = 0, piriorityTime = agingTime;
        Process checkPeek = null;
        while (completionTime != processes.size()) {
            boolean enter = false;
            while (!pqArrivalTime.isEmpty() && pqArrivalTime.peek().arrivalTime <= currentTime) {
                readyQueue.add(pqArrivalTime.poll());
            }

            currentTime++;
            if (readyQueue.isEmpty()) {
                ganttChart.add(new Process());
                continue;
            }
            if (checkPeek != null){
                if (checkPeek != readyQueue.peek()){
                    for (int i = 0; i < contextTime; i++){
                        ganttChart.add(new Process());
                    }
                    currentTime += contextTime;
                }
            }

            Process process = readyQueue.poll();
            process.burstTime--;
            process.completionTime++;

            if (process.burstTime == 0) {
                process.completionTime = (completionTime == 0) ?  currentTime : currentTime + contextTime;
                currentTime += (completionTime == 0) ? 0 : contextTime;
                completionTime++;
                enter = true;
            } else{
                readyQueue.add(process);
            }


            ganttChart.add(process);
            if (enter && completionTime != processes.size()){
                for (int i = 0; i < contextTime; i++){
                    ganttChart.add(new Process());
                }
            }

            if (!readyQueue.isEmpty() && currentTime >= piriorityTime) {
                Process pickUp = process;
                Process[] pr = readyQueue.toArray(new Process[0]);
                readyQueue.clear();
                for (Process p : pr) {
                    if ((currentTime - (p.arrivalTime + p.completionTime)) >= agingTime && checkPeek != p)
                        p.priorityNumber++;
                }
                for (Process p : pr)
                    readyQueue.add(p);
                if (!pickUp.equals(readyQueue.peek())){
                    for (int i = 0; i < contextTime; i++){
                        ganttChart.add(new Process());
                    }
                    currentTime += contextTime;
                }
                piriorityTime += agingTime;

            }
            checkPeek = process;

        }

        int burstTime = 0;
        for (int i = 1; i < ganttChart.size(); i++) {
            burstTime++;
            if (!ganttChart.get(i - 1).name.equals(ganttChart.get(i).name)) {
                Process process = ganttChart.get(i - 1);
                process.burstTime = burstTime;
                answer.add(new Process(
                                process.name,
                                process.color,
                                process.arrivalTime,
                                process.burstTime,
                                process.priorityNumber,
                                process.completionTime,
                                process.turnAroundTime,
                                process.waitingTime
                        )
                );
                burstTime = 0;
            }
        }
        ganttChart.getLast().burstTime = ++burstTime;
        answer.add(ganttChart.getLast());
    }



    @Override
    public void calcWaitingTime() {
        calcTurnAroundTime();
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (Process process : answer){
            if (hashMap.containsKey(process.name)){
                int updatedBurstTime = hashMap.get(process.name) + process.burstTime;
                hashMap.put(process.name, updatedBurstTime);
            }
            else
                hashMap.put(process.name, process.burstTime);
        }
        for (int i = 0; i < answer.size(); i++){
            if (!answer.get(i).name.equals("Idle"))
                answer.get(i).waitingTime = answer.get(i).turnAroundTime - hashMap.get(answer.get(i).name);
        }



    }

}
