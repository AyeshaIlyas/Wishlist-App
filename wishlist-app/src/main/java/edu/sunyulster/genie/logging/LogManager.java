package edu.sunyulster.genie.logging;
public final class LogManager {
    private static int taskX = 0;
    private static Logger prev;
    public static Logger addLog(String taskType, String taskName, String taskLocation, String details){
        Update();
        Logger newLog = new Logger(taskX,taskType,taskName,taskLocation,details);
        prev = newLog;
        taskX++;
        return newLog;
    }
    public static void Update(){
        if(prev != null && prev.success == false){
            prev.Failure();
            prev = null;
        }
    }
}
