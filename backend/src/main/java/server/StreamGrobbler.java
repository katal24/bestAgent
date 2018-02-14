package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * Created by dawid on 09.02.18.
 */
public class StreamGrobbler implements Runnable {

    private InputStream inputSream;

    private Consumer<String> consumerMaxSatOutput = (line) -> {
        System.out.println(line);

        if(line.startsWith("c Optimal Solution")){
            MaxSat.addResult(Integer.valueOf(line.substring(line.indexOf("=")+2)));
        }
    };

    public StreamGrobbler(InputStream inputSream) {
        this.inputSream = inputSream;
    }


    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputSream)).lines().forEach(consumerMaxSatOutput);
    }
}
