import java.util.*;

public class FCAI extends AbstractSchedulingAlgorithm {
    FCAI(ArrayList<Process> processes, int contextTime, int agingTime) {
        super(processes, contextTime, agingTime);
    }

    @Override
    public void calcAnswer(ArrayList<Process> processes, int contextTime, int agingTime) {
//        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        Map<Process, Boolean> processed = initializeProcessedMap(); // Track processed status

        int currentTime = 0;
        int done = 0;
        PriorityQueue<Process> FCAIPQ = new PriorityQueue<>(Comparator.comparingInt(p -> p.fcaiFactor));
        List<Process> queue = new ArrayList<>();

        double v1 = 0.0, v2 = 0.0;
        List<Double> v = addEligibleProcesses(currentTime, processed, v1, v2, queue, FCAIPQ);
        v1 = v.get(0) / 10;
        v2 = v.get(1) / 10;

        // In the beginning, pick the highest factor since they all have arrival time 0
        Process currentProcess = FCAIPQ.poll();
        queue.remove(currentProcess);

        while (done < pqArrivalTime.size()) {
            // Run current Process for 40% of quantum
            if (done == pqArrivalTime.size() - 1) {
                currentTime += currentProcess.remainingTime;
                done++;
                answer.add(currentProcess);
                for (int i = 0; i < currentProcess.remainingTime; i++) {
                    ganttChart.add(currentProcess);
                }
                finish(currentProcess, currentTime, FCAIPQ, processed, queue, v1, v2, contextTime, done, currentProcess.remainingTime);
                break;
            }
            int executedTime = (int) Math.ceil(0.4 * currentProcess.quantum);
            if (executedTime >= currentProcess.remainingTime) {
                // Finish current process
                currentTime += currentProcess.remainingTime;
                done++;
                finish(currentProcess, currentTime, FCAIPQ, processed, queue, v1, v2, contextTime, done, currentProcess.remainingTime);

                // Ensure queue is not empty before accessing the first element
                if (!queue.isEmpty()) {
                    currentProcess = queue.get(0);
                    FCAIPQ.remove(currentProcess);
                    queue.remove(currentProcess);
                }
                continue;
            }
            // Execute the process for less than quantum time
            currentTime += executedTime;
            currentProcess.remainingTime -= executedTime;
//            currentProcess.calculateFCAIFactor(v1, v2);
            System.out.println("Quantum executed for process: " + currentProcess.name
                    + " for " + executedTime + " units of time. Remaining burst time: " + currentProcess.remainingTime);

            check_and_add(currentTime, FCAIPQ, processed, queue, v1, v2);

            int unusedQuantum = currentProcess.quantum - executedTime;
            Process highestFcai = FCAIPQ.peek();
            int numberOfExecutions = executedTime;
            while (highestFcai == null || (highestFcai != null && highestFcai.fcaiFactor >= currentProcess.fcaiFactor && unusedQuantum > 0 && currentProcess.remainingTime > 0)) {
                numberOfExecutions++;
                currentTime++;
                unusedQuantum--;
                currentProcess.remainingTime--;
                System.out.println("Quantum executed for process: " + currentProcess.name
                        + " for 1 unit of time. Remaining burst time: " + currentProcess.remainingTime);
//                currentProcess.calculateFCAIFactor(v1, v2);
                check_and_add(currentTime, FCAIPQ, processed, queue, v1, v2);

                highestFcai = FCAIPQ.peek();
            }
            answer.add(currentProcess);
            for (int i = 0; i < numberOfExecutions; i++) {
                ganttChart.add(currentProcess);
            }
            if (done != processes.size()) {
                for (int i = 0; i < contextTime; i++) {
                    ganttChart.add(new Process());
                }
            }
            if (currentProcess.remainingTime == 0) {
                // Process done
                done++;
                finish(currentProcess, currentTime, FCAIPQ, processed, queue, v1, v2, contextTime, done, numberOfExecutions);

                // Ensure queue is not empty before accessing the first element
                if (!queue.isEmpty()) {
                    currentProcess = queue.get(0);
                    FCAIPQ.remove(currentProcess);
                    queue.remove(currentProcess);
                }
                continue;
            }
            // still not done, handle quantum update
            else {
                currentProcess.calculateFCAIFactor(v1, v2);
                currentProcess.updateQuantum(unusedQuantum);
                FCAIPQ.add(currentProcess);
                queue.add(currentProcess);

                if (unusedQuantum == 0) { // pick the next
                    currentProcess = queue.get(0);
                    FCAIPQ.remove(currentProcess);
                    queue.remove(currentProcess);

                } else { // pick the best factor
                    Process tmp = FCAIPQ.poll();
                    queue.remove(tmp);
                    currentProcess = tmp;
                }
                currentTime += contextTime;
            }
        }
    }

    @Override
    protected void intializeReadyQueue() {

    }


    private Map<Process, Boolean> initializeProcessedMap() {
        Map<Process, Boolean> processed = new HashMap<>();
        for (Process p : pqArrivalTime) {
            processed.put(p, false);
        }
        return processed;
    }

    private List<Double> addEligibleProcesses(int currentTime, Map<Process, Boolean> processed, double v1, double v2,
                                              List<Process> queue, PriorityQueue<Process> FCAIPQ) {
        for (Process p : pqArrivalTime) {
            if (p.arrivalTime <= currentTime && !processed.get(p)) {
//                p.calculateFCAIFactor(v1, v2); // mynf34 n7sb el fcai factor hena le an el v1,v2 lesa mt7sb4
                processed.put(p, true);
                queue.add(p);
                FCAIPQ.add(p);
                System.out.println("Process " + p.name + " added to queue at time " + p.arrivalTime);
            }
            v1 = Math.max(v1, p.arrivalTime);
            v2 = Math.max(v2, p.burstTime);
        }
        for (Process p : pqArrivalTime) {
            if (p.arrivalTime <= currentTime && processed.get(p)) {
                p.calculateFCAIFactor(v1 / 10, v2 / 10); // hena mashy
            }
        }

        return Arrays.asList(v1, v2);
    }

    private void finish(Process currentProcess, int currentTime, PriorityQueue<Process> FCAIPQ, Map<Process, Boolean> processed, List<Process> queue, double v1, double v2, int contex, int done, int numberOfExecutions) {
        currentProcess.turnAroundTime = currentTime - currentProcess.arrivalTime;
        currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;
        currentProcess.completionTime = currentTime;
        currentTime += contex;




        System.out.println("Executed process: " + currentProcess.name
                + ", Waiting Time: " + currentProcess.waitingTime + ", Turnaround Time: " + currentProcess.turnAroundTime);
//        break;
        // during that time we may get new processes on the way
        check_and_add(currentTime, FCAIPQ, processed, queue, v1, v2);

    }


    private void check_and_add(int currentTime, PriorityQueue<Process> FCAIPQ, Map<Process, Boolean> processed,
                               List<Process> queue, double v1, double v2) {
        for (Process p : pqArrivalTime) {
            if (p.arrivalTime <= currentTime && !FCAIPQ.contains(p) && p.remainingTime > 0 && !processed.get(p)) {
                p.calculateFCAIFactor(v1, v2);
                processed.put(p, true); // Mark as processed
                queue.add(p);
                FCAIPQ.add(p);
            }
        }
    }
}
