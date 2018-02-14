package server;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by dawid on 09.02.18.
 */
@RestController
@EnableAutoConfiguration
public class MaxSat {

    BufferedWriter writer;
    public static Vector<Integer> results;
    static int paramsNumber = 0;

    @RequestMapping(value="/maxsat", method = RequestMethod.POST)
    public Response maxsat(@RequestBody int[][][] clauses) {

        results = new Vector<>();
        paramsNumber = clauses[1][0].length;


        String cnf1 = "c maxsat \n";
        String cnf2 = "c \n";
        String cnfHeader = "";
        String cnfBodyIdealAgent = "";
        String cnfBodyTestedAgent = "";
        String cnfBody = "";

        Response response = new Response();
        String homeDirectory = System.getProperty("user.home");
        Process process;
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        StreamGrobbler streamGrobbler;

        String cnf3 = "p cnf " + clauses[0][0].length + " " + (clauses[0][0].length +  paramsNumber) + "\n";
        cnfHeader = cnf1 + cnf2 + cnf3;

        for (int clause : clauses[0][0]) {
            cnfBodyIdealAgent += clause + " 0\n";
        }

        try {
            for (int[] agentProps  : clauses[1]) {

                cnfBodyTestedAgent = "";

                for (int clause : agentProps) {
                    cnfBodyTestedAgent += clause + " 0\n";
                }

                writer = new BufferedWriter(new FileWriter("cnf"));
                cnfBody = cnfBodyIdealAgent + cnfBodyTestedAgent;
                writer.write(cnfHeader);
                writer.write(cnfBody);
                writer.close();


                if(isWindows) {
                    process = Runtime.getRuntime().exec(String.format("sh maxsatx cnf", homeDirectory));
                } else {
                    process = Runtime.getRuntime().exec(String.format("./maxsatz cnf", homeDirectory));
                }

                streamGrobbler = new StreamGrobbler(process.getInputStream());
                Future f = Executors.newSingleThreadExecutor().submit(streamGrobbler);

                int exitCode = process.waitFor();
                while(!f.isDone()){}
                assert exitCode == 0;
            }

            response.values = this.results;
            response.setType("maxsat");

        } catch(Exception e){
            e.printStackTrace();
            response.error = "Problem with Maxsat";
            response.setType("error");
        }

        return response;
    }

    public static void addResult(int result) {
        results.add(paramsNumber - result);
    }

}
