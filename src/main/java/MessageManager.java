import hibernate.User;
import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MessageManager {


    private static HashMap<Integer, String> errCodes = new HashMap<Intege   r, String>() {{
        put(4000, "Wrong username or password");
        put(4001, "Username is already used.");
    }};


    public MessageManager(){
        errCodes.put(4000, "Wrong username or password");
        errCodes.put(4001, "Username is already used.");
    }

    public static String getClientMessage (long sender, String userMessage){
        JSONObject response = new JSONObject();
        String message  = null;
        try {
            response.put("sender", sender);
            response.put("userMessage", userMessage);
            message = getMessage("ClientMessage", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

    private static String getMessage(String type, JSONObject response) throws JSONException {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("response", response);

        return message.toString();
    }

    public static String getErrorMessage(int errorCode) {
        JSONObject response = new JSONObject();
        String message = null;
        try {
            response.put("errorCode", errorCode);
            response.put("message", errCodes.get(errorCode));
            message =  getMessage("Error", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

    public static String getSuccessMessage(long userId){
        JSONObject response = new JSONObject();
        String message = null;
        try {
            response.put("message", "Successful");
            message =  getMessage("Success", response);
        } catch (JSONException e) {  
            e.printStackTrace();
        }

        return message;

    }

    public static String getAllLoggedUsersMessage(List<User> loggedUsers, String uniqueVal){
        JSONObject response = new JSONObject();
        JSONArray users = new JSONArray();
        String message = null;
        int i = 0;

        try{
            for (User u : loggedUsers) {
                JSONObject user = new JSONObject();

                user.put("id", u.getId());
                user.put("userName", u.getUserName());
                users.put(user);
            }
            response.put("commandUniqueVal", uniqueVal);
            response.put("users", users);

            message = getMessage("command", response);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;

    }

}
