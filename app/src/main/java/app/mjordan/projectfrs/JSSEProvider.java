package app.mjordan.projectfrs;

import java.security.AccessController;
import java.security.Provider;

/**
 * This class is used along with GMailSender
 * I don't know for what purpose
 * Not my code, but code from Internet
 * I used it because the PHP Restful API was taking too long to send email
 * and Volley was returning null.
 */
class JSSEProvider extends Provider {

    JSSEProvider() {
        super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
        AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
            public Void run() {
                put("SSLContext.TLS",
                        "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
                put("Alg.Alias.SSLContext.TLSv1", "TLS");
                put("KeyManagerFactory.X509",
                        "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
                put("TrustManagerFactory.X509",
                        "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
                return null;
            }
        });
    }

}
