/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author khubaibumer
 */
public class SessionInfo {

    public String ip;
    public int port;
    public String id;
    public String pass;
    
    private static SessionInfo session;

    SessionInfo() {
        this.ip = "";
        this.port = -1;
        this.id = "";
        this.pass = "";
    }

    public SessionInfo(String ip, int port, String id, String pass) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.pass = pass;
    }
    
    public void setSession(SessionInfo _session) {
    
        this.session = _session;
    }
    
    public static SessionInfo getSession() {
        return session;
    }
    
}
