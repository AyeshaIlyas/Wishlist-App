package edu.sunyulster.genie.resources;

import edu.sunyulster.genie.service.TestService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;
import edu.sunyulster.genie.model.Test;

@Path("/tests")
public class TestResource{

    TestService testService = new TestService();

   // @GET
   // public List<Test> getTests(){
   //     return testService.getAllTests();
   // }


}