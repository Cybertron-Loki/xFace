package ming.test.xface.job;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

//@Component
//@ConfigurationProperties(prefix = "demo")
@Data
public class Demo implements CommandLineRunner {

    //    @Value("${demo.lst}")
    private List<String> lst;

    @Override
    public void run(String... args) throws Exception {
        String osName = System.getProperty("os.name").toLowerCase();
        String prefix = "";
        if (osName.contains("windows")) prefix += "cmd /c ";
        Process exec = null;
        for (String item : lst) {
            exec = Runtime.getRuntime().exec(prefix + item);
            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println(exec.waitFor());
            reader.close();
        }

    }
}
