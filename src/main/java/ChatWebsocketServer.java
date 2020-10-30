import hibernate.User;
import netscape.javascript.JSObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MariaDBDialect;

import org.json.*;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.net.InetSocketAddress;
import java.util.*;

public class ChatWebsocketServer extends WebSocketServer {

    private static int TCP_PORT = 4444;
    private HashMap<Long, WebSocket> userConnections;
    private HashMap<WebSocket, Long> userIds;
    private List<User> loggedUsers;

    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String LOGIN_ERROR_MSG = "Wrong username or password.";
    private final int LOGIN_ERROR_CODE = 4000;
    private final int USED_NAME_ERROR_CODE = 4001;
    private final String ACTION = "action";


    public ChatWebsocketServer() {
        super(new InetSocketAddress(TCP_PORT));
        userConnections = new HashMap<Long, WebSocket>();
        userIds = new HashMap<WebSocket, Long>();
        loggedUsers = new ArrayList<User>();
    }

    @Override
    public void onOpen(WebSocket socket, ClientHandshake handshake) {
        Map<String, String> urlParams = UrlParser(handshake.getResourceDescriptor());


        String userName = urlParams.get(USERNAME);
        String passWord = urlParams.get(PASSWORD);

        if(urlParams.get(ACTION).equals("/login")){
            User user = DatabaseManager.getInstance().getUser(userName, passWord);
            if(user != null){
                userConnections.put(user.getId(), socket);
                userIds.put(socket, user.getId());
                loggedUsers.add(user);
                socket.send(MessageManager.getSuccessMessage(user.getId()));
            } else {
                socket.send(MessageManager.getErrorMessage(LOGIN_ERROR_CODE));
                socket.close(LOGIN_ERROR_CODE, LOGIN_ERROR_MSG);
            }
        } else if (urlParams.get(ACTION).equals("/register")){
            boolean isRegistered = DatabaseManager.getInstance().createUser(userName, passWord);
            User user = DatabaseManager.getInstance().getUser(userName, passWord);

            if(isRegistered == true) {
                userConnections.put(user.getId(), socket);
                userIds.put(socket, user.getId());
                loggedUsers.add(user);
                socket.send(MessageManager.getSuccessMessage(DatabaseManager.getInstance().getUserId(userName)));
            } else {
                socket.send(MessageManager.getErrorMessage(USED_NAME_ERROR_CODE));
            }
        } else if(urlParams.get(ACTION).equals("/logout")) {

        }

        System.out.println("New connection from " + socket.getRemoteSocketAddress().getAddress().getHostAddress());


    }

    @Override
    public void onClose(WebSocket socket, int code, String reason, boolean remote) {
        userConnections.remove(socket);
        System.out.println("Closed connection to " + socket.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket socket, String message) {
        try {
            JSONObject response = new JSONObject(message);

            if(response.getString("type").equals("command")) {
                if(response.getJSONObject("info").getString("commandName").equals("getOnlineUsers")){
                    socket.send(MessageManager.getAllLoggedUsersMessage(loggedUsers, response.getJSONObject("info").getString("commandUniqueVal"))) ;
                }

            } else if(response.getString("type").equals("message")) {

                long receiverId =  response.getJSONObject("info").getLong("receiver");
                WebSocket receiverSocket = userConnections.get(receiverId);
                receiverSocket.send(MessageManager.getClientMessage(userIds.get(socket), response.getJSONObject("info").getString("message")));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket socket, Exception ex) {
        //ex.printStackTrace();
        if (socket != null) {
            userConnections.remove(socket);
            // do some thing if required
        }
        System.out.println("ERROR from " + socket.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onStart() {

    }

    private Map<String, String> UrlParser(String url){
       String[] queryArray = url.split("\\?");
       String query = queryArray[1];
       String[] params = query.split("&");
       Map<String, String> map = new HashMap<String, String>();
       map.put(ACTION, queryArray[0]);

       for (String param : params)
       {
           String name = param.split("=")[0];
           String value = param.split("=")[1];
           map.put(name, value);
       }

       return map;
    }


}