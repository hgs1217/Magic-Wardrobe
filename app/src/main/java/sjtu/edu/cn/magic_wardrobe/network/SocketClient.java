package sjtu.edu.cn.magic_wardrobe.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by HgS_1217_ on 2017/11/15.
 */

public class SocketClient {

    private static Socket client;

    public SocketClient(String site, int port) {
        try {
            client = new Socket(site, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMsg(String msg) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());
            out.println(msg);
            out.flush();
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void closeSocket() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
