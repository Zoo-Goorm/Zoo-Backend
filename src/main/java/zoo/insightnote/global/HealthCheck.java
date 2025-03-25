package zoo.insightnote.global;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("/actuator/ip")
    public Map<String, String> getServerIp() throws UnknownHostException {
        Map<String, String> result = new HashMap<>();
        InetAddress localHost = InetAddress.getLocalHost();

        result.put("hostName", localHost.getHostName());
        result.put("hostAddress", localHost.getHostAddress());

        return result;
    }
}
