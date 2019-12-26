import org.java_websocket.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MessageManager {


    private static HashMap<Integer, String> errCodes = new HashMap<Integer, String>() {{
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
        message.put("Type", type);
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


}
