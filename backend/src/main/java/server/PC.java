package server;

import pl.edu.agh.talaga.PairwiseComparisons;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * Created by dawid on 26.11.16.
 */
@RestController
@EnableAutoConfiguration
public class PC {

    PairwiseComparisons pc;

    @RequestMapping(value= "/pc", method = RequestMethod.POST)
    public Response update(@RequestBody AhpMatrix fun){
        pc = new PairwiseComparisons();

        Response response = new Response();

        response.setType("server");

        try {

            response.values = pc.ahp(fun.matrix);
            response.setType("vector");
            System.out.println(Arrays.toString((double[]) response.values));

        } catch(Exception e){
            String error = e.toString();
            String myError;
            System.out.println(error);
            if(error.contains("main.out.pl.edu.agh.talaga.PcMatrixException")){
               String[] temp = error.split(": ");
                myError = temp[1];
                response.error = myError;
            } else {
                response.error = "Bad parameters";
            }
            response.setType("error");
        }
        System.out.println(response);

        return response;
    }

}
