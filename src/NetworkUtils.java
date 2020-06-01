
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sun.rmi.runtime.Log;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author khubaibumer
 */
public class NetworkUtils {

    private static NetworkUtils instance = null;
    private SSLSocketFactory factory;
    private SSLSocket socket;
    private SSLContext sc;
    private static BufferedReader in;
    private static PrintWriter out;

    public NetworkUtils() {
        factory = null;
        socket = null;
        sc = null;
        in = null;
        out = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (GeneralSecurityException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getLocalizedMessage(), "Fatal Exception",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static NetworkUtils getInstance() {
        if (instance == null) {
            instance = new NetworkUtils();
        }

        return instance;
    }

    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

    public void closeServer() throws IOException {

        in.close();
        out.close();
        instance = new NetworkUtils();
    }

    public boolean sendToServer(String msg) throws IOException {
        if (!msg.isEmpty() && out != null) {
            if (!out.checkError()) {
                out.println(msg);
                out.println();
                out.flush();
                return true;
            }
        }
        return false;
    }

    public boolean validateUser(String input) {
        if (input.equals(SessionInfo.getSession().pass)) {
            return true;
        }
        return false;
    }

    public String recvFromServer() throws IOException {
        if (in != null) {
            return in.readLine();
        } else {
            return "";
        }
    }

    public boolean connectServer(SessionInfo _session) {

        boolean kicked = false;
        String input = "";
        try {
            factory = sc.getSocketFactory();

            SessionInfo session = SessionInfo.getSession();

            socket
                    = (SSLSocket) factory.createSocket(session.ip, session.port);
            socket.startHandshake();

            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())));

            /* read response */
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            String inputLine;
//            inputLine = in.readLine();
            inputLine = this.recvFromServer();

            if (inputLine.contains("Enter Credentials:")) {
                String msg = session.id + "," + session.pass;
                this.sendToServer(msg);
//                out.println(msg);
//                out.println();
//                out.flush();
            } else {
                kicked = true;
            }
//            inputLine = in.readLine();
            inputLine = this.recvFromServer();
            input += inputLine;
            if (inputLine.contains("Welcome")) {
                kicked = false;
            } else if (inputLine.contains("kicked")) {
                kicked = true;
            }

            /*
             * Make sure there were no surprises
             */
            if (out.checkError()) {
                System.out.println(
                        "SSLSocketClient: java.io.PrintWriter error");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getLocalizedMessage(), "Fatal Exception",
                    JOptionPane.ERROR_MESSAGE);
            return false;

        }

//        JOptionPane optionPane = new NarrowOptionPane();
//        optionPane.setMessage(input);
//        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
//        JDialog dialog = optionPane.createDialog(null, "Credentaial Validation");
//        dialog.setVisible(true);
        return !kicked;
    }
}

class NarrowOptionPane extends JOptionPane {

    NarrowOptionPane() {
    }

    public int getMaxCharactersPerLineCount() {
        return 100;
    }
}
