package flearn.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class BrowserLauncher {

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        String url = "http://localhost:" + port + contextPath + "/login";

        System.out.println("[BrowserLauncher] Opening browser: " + url);

        try {
            if (Desktop.isDesktopSupported()
                    && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                return;
            }

            openBrowserByCommand(url);

        } catch (Exception e) {
            System.out.println("[BrowserLauncher] Không thể tự mở browser.");
            System.out.println("[BrowserLauncher] Hãy mở thủ công: " + url);
            e.printStackTrace();
        }
    }

    private void openBrowserByCommand(String url) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            Runtime.getRuntime().exec(new String[]{
                    "rundll32",
                    "url.dll,FileProtocolHandler",
                    url
            });
        } else if (os.contains("mac")) {
            Runtime.getRuntime().exec(new String[]{
                    "open",
                    url
            });
        } else {
            Runtime.getRuntime().exec(new String[]{
                    "xdg-open",
                    url
            });
        }
    }
}
