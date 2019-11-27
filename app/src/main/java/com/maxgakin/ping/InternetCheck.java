package com.maxgakin.ping;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

class InternetCheck extends AsyncTask<Void,Void,String> {

    private Consumer mConsumer;
    public interface Consumer { void accept(String internet); }

    public InternetCheck(Consumer consumer) { mConsumer = consumer; execute(); }

    @Override protected String doInBackground(Void... voids) { try {
        Socket sock = new Socket();
        sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
        sock.close();
        return sock.getLocalAddress().toString();
    } catch (IOException e) { return ""; } }

    @Override protected void onPostExecute(String internet) { mConsumer.accept(internet); }
}
