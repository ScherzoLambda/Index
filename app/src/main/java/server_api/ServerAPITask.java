package server_api;

import static server_api.ServerAPI.makeRequest;
import static server_api.ServerAPI.makeRequestLogin;

import android.os.AsyncTask;
import android.util.Log;

import mongo_api.MongoAPI;
import mongo_api.MongoAPIListener;

public class ServerAPITask extends AsyncTask<Void, Void, String> {
    private String body;
    private String action;
    private ServerAPIListener listener;

    /**
     * A variavel action pode assumir os valores:
     *  'login'
     *  'create'
     *  'find'
     * **/
    public ServerAPITask(String action, String body, ServerAPIListener listener) {
        this.body = body;
        this.listener = listener;
        this.action = action;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            if(action.equals("create")){return makeRequest(body);}
            if(action.equals("login")){
                Log.d("Action | :", action);
                return makeRequestLogin(body);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onResult(result);
        }
    }

}
