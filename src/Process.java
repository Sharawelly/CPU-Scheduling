public class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int priorityNumber;
    int completionTime;
    int turnAroundTime;
    int waitingTime;

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


    Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber){
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.completionTime = this.turnAroundTime = this.waitingTime = 0;
    }
}
