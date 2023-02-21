package edu.sunyulster.genie.resources;

import edu.sunyulster.genie.services.TestService;
import jakarta.ws.rs.Path;

@Path("/tests")
public class TestResource{

    TestService testService = new TestService();

   // @GET
   // public List<Test> getTests(){
   //     return testService.getAllTests();
   // }


}