package zoo.insightnote.global.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class ServerInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            Map<String, Object> serverInfo = new HashMap<>();
            serverInfo.put("hostName", ip.getHostName());  // 서버 호스트 이름
            serverInfo.put("hostAddress", ip.getHostAddress());  // 서버 IP 주소
            builder.withDetail("server", serverInfo);
        } catch (UnknownHostException e) {
            builder.withDetail("server", "Unknown Host");
        }
    }
}