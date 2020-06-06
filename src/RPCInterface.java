
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author khubaibumer
 */
public class RPCInterface {

    private final String REQ = "req,";
    private final String USR = "user,";
    private final String OK = "resp,ok,";
    private final String ADDUSR = "db," + "add," + USR;
    private final String DELUSR = "db," + "del," + USR;
    private final String UPDATEUSR = "db," + "update," + USR;

    private static RPCInterface instance = null;

    public static RPCInterface getInstance() {
        if (instance == null) {
            instance = new RPCInterface();
        }

        return instance;
    }

    public enum KEYTYPE {
        PASSWD,
        MODE,
        INFORMATION
    };

    private int mapUser(String mode) {

        switch (mode) {
            case "Admin":
                return SessionInfo.ADMIN;
            case "Merchant":
                return SessionInfo.MERCHAT;
            default:
                return SessionInfo.USER;
        }
    }

    protected boolean addUser(String user, String pass, String mode, String info) {

        boolean ret = false;
        String req = ADDUSR + user + "," + pass + "," + this.mapUser(mode) + "," + info;
        try {
            NetworkUtils.getInstance().sendToServer(req);
            String response = NetworkUtils.getInstance().recvFromServer();
            if (response.contains(OK)) {
                ret = true;
            } else if (response.contains("resp,fail,reason,")) {
                String reason = (response.split(","))[3];
                JOptionPane.showMessageDialog(new JFrame(), reason, "Failed to Add User",
                        JOptionPane.ERROR_MESSAGE);
                ret = false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getLocalizedMessage(), "Fatal Exception",
                    JOptionPane.ERROR_MESSAGE);
        }

        return ret;
    }

    protected boolean deleteUserInfo(String user, String pass) {

        boolean ret = false;
        String req = DELUSR + user + "," + pass + "\n";
        try {
            NetworkUtils.getInstance().sendToServer(req);
            String response = NetworkUtils.getInstance().recvFromServer();
            if (response.contains(OK)) {
                ret = true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getLocalizedMessage(), "Fatal Exception",
                    JOptionPane.ERROR_MESSAGE);
        }

        return ret;
    }

    protected boolean updateUserInfo(String user, String key, KEYTYPE keyType) {

        boolean ret = false;
        String req = UPDATEUSR;
        switch (keyType) {
            case PASSWD: {
                req += KEYTYPE.PASSWD + "," + key;
            }
            break;

            case MODE: {
                req += KEYTYPE.MODE + "," + key;
            }
            break;

            case INFORMATION: {
                req += KEYTYPE.INFORMATION + "," + key;
            }
            break;

            default:
                return false;
        }

        try {
            NetworkUtils.getInstance().sendToServer(req);
            String response = NetworkUtils.getInstance().recvFromServer();
            if (response.contains(OK)) {
                ret = true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getLocalizedMessage(), "Fatal Exception",
                    JOptionPane.ERROR_MESSAGE);
        }
        return ret;
    }

}
