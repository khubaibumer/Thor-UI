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

    private final String ADD = "ADDREQ,";
    private final String DEL = "DELREQ,";
    private final String OK = "RESP,OK,200";
    private final String UPDATE = "UPDATEREQ,";
    
    private static RPCInterface instance = null;
    
    public static RPCInterface getInstance() {
        if(instance == null)
            instance = new RPCInterface();
        
        return instance;
    }

    public enum KEYTYPE {
        PASSWD,
        MODE,
        INFORMATION
    };

    protected boolean addUser(String user, String pass, String mode, String info) {

        boolean ret = false;
        String req = ADD + user + "," + pass + "," + mode + "," + info;
        try {
            for(int i = 0; i < 20; i++)
                NetworkUtils.getInstance().sendToServer(req);
            String response = NetworkUtils.getInstance().recvFromServer();
            if (response.equals(OK + "\n")) {
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    protected boolean deleteUserInfo(String user, String pass) {

        boolean ret = false;
        String req = DEL + user + "," + pass + "\n";
        try {
            NetworkUtils.getInstance().sendToServer(req);
            String response = NetworkUtils.getInstance().recvFromServer();
            if (response.equals(OK + "\n")) {
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    protected boolean updateUserInfo(String user, String key, KEYTYPE keyType) {

        boolean ret = false;
        String req = UPDATE;
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
        };

        try {
            NetworkUtils.getInstance().sendToServer(req);
            String response = NetworkUtils.getInstance().recvFromServer();
            if (response.equals(OK + "\n")) {
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;

    }

}
