package ra.enterwind.lapaskumala.activities.modulchat;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

class SendNotification {
    public SendNotification(String message, String new_message, Object notificationKey) {
        try {
            JSONObject notificationContent = new JSONObject(
                    "{'contents':{'en':'" +  message + "'},"+
                            "'include_player_ids':['" + notificationKey + "']," +
                            "'headings':{'en': '" +"Pesan Dari" + new_message + "'}}");
            OneSignal.postNotification(notificationContent, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
