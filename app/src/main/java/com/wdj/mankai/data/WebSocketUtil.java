package com.wdj.mankai.data;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.java_websocket.drafts.Draft;
import java.net.URI;

public class WebSocketUtil extends WebSocketClient {
    private JSONObject obj;


    public WebSocketUtil(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public boolean connectBlocking() throws InterruptedException {
        System.out.println( "opened connection" );
        return super.connectBlocking();

    }

    @Override
    public void onMessage( String message ) {

        try {
            obj = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("dpfjdpfjdpfj");
        }

    }

    @Override
    public void onOpen( ServerHandshake handshake ) {
        System.out.println( "opened connection" );
    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        System.out.println( "closed connection" );
    }

    @Override
    public void onError( Exception ex ) {
        ex.printStackTrace();
        System.out.println("에러에러에러");
    }

    public JSONObject getResult() {
        return this.obj;
    }
}
