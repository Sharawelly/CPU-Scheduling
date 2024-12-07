public class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int priorityNumber;
    int completionTime;
    int turnAroundTime;
    int waitingTime;
    int remainingTime;
    int quantum;
    int fcaiFactor;

    Process(){
        this.name = "Idle";
        this.color = "white";
        this.arrivalTime = this.burstTime = this.priorityNumber = this.completionTime = this.turnAroundTime = this.waitingTime = 0;
    }

    public Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber, int completionTime, int turnAroundTime, int waitingTime) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.completionTime = completionTime;
        this.turnAroundTime = turnAroundTime;
        this.waitingTime = waitingTime;
    }

    Process(String name, int arrivalTime, int burstTime, int priority, int initialQuantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priority;
        this.remainingTime = burstTime;
        this.quantum = initialQuantum;
    }

    Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber, int quantum){
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.quantum = quantum;
        this.remainingTime = burstTime;
        this.completionTime = this.turnAroundTime = this.waitingTime = 0;
    }

    public void calculateFCAIFactor(double V1, double V2) {
        fcaiFactor=(10 - priorityNumber) + (int)Math.ceil(arrivalTime / V1) + (int)Math.ceil(remainingTime / V2);
    }

    // Update quantum (for when a process is preempted or completes its quantum)
    public void updateQuantum(int unusedQuantum) {
        if (unusedQuantum > 0) {
            quantum += unusedQuantum; // Add unused quantum if preempted
        } else {
            quantum += 2; // Add 2 if quantum is fully used
        }
    }
}
