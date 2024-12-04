public class Process {
    String name;
    String color;
    Integer arrivalTime;
    Integer burstTime;
    Integer priorityNumber;
    Integer completionTime;
    Integer turnAroundTime;
    Integer waitingTime;

    Process(String name, String color, Integer arrivalTime, Integer burstTime, Integer priorityNumber){
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.completionTime = this.turnAroundTime = this.waitingTime = 0;
    }
}
