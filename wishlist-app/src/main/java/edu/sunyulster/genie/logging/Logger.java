package edu.sunyulster.genie.logging;

import javax.management.openmbean.SimpleType;
import javax.swing.text.DefaultTextUI;

public class Logger {
    private int taskID;
    private String taskType;
    private String taskName;
    private String taskLocation;
    private String details;

    public boolean success = false;

    public Logger(int taskID, String taskType, String taskName, String taskLocation, String details){
        this.taskID = taskID;
        this.taskType = taskType;
        this.taskName = taskName;
        this.taskLocation = taskLocation;
        this.details = details;

        Start();
    }

    public void Start(){
        System.out.println("-=TASK " + "["+taskID+"] " + "STARTED=-");
        System.out.printf("Type : " + taskType + " " + taskName +
        "%nLocation : "+taskLocation+
        "%nDetails : "+details);
    }
    public void Success(){
        System.out.println("-=TASK " + "["+taskID+"] " + "SUCCESS=-");
        success = true;
    }
    public void Failure(){
        System.out.println("-=TASK " + "["+taskID+"] " + "FAILURE=-");
    }
}
