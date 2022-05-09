/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.message;
import utils.DataSource;
import utils.Statics;

/**
 *
 * @author khoualdi koussay
 */
public class message_service {
         private ConnectionRequest request;

    private boolean responseResult;
    public ArrayList<message> messages;
     public message_service() {
        request = DataSource.getInstance().getRequest();
    }
       public ArrayList<message> getmessage(int id_r) {
        String url = Statics.BASE_URL + "message_mobile/"+id_r;

        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                messages = parseMes(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return messages;
    }
         public ArrayList<message> add_message(int id_r,int id_s,String mes) {
        String url = Statics.BASE_URL + "add_message_mobile/"+id_r+"/"+id_s+"/"+mes;

        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                messages = parseMes(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return messages;
    }
         public ArrayList<message> delete_message(int id_r,int idm) {
        String url = Statics.BASE_URL + "supp_message_mobile/"+id_r+"/"+idm;

        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                messages = parseMes(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return messages;
    }
        public ArrayList<message> parseMes(String jsonText) {
        
        try {
            messages = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) 
            {           
            String created_at = obj.get("created_at").toString(); 
             
            String message = obj.get("message").toString();     
          
            
            int id = (int)Float.parseFloat(obj.get("id").toString());  
            
            messages.add(new message(id, message, created_at));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return messages;
    }
}
