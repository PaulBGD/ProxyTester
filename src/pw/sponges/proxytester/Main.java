package pw.sponges.proxytester;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private Map<String, Integer> proxies;
    private Map<String, Integer> working;

    public Main() {
        this.proxies = new HashMap<>();
        this.working = new HashMap<>();

        this.loadFile("proxies.txt");
        this.testProxies();

        System.out.println("Tested all proxies!");
        this.writeWorking("working.txt");
        System.out.println("Finished! Written working proxies to working.txt");
    }

    public static void main(String[] args) {
	    new Main();
    }

    private void testProxies() {
        for (String ip : proxies.keySet()) {
            int port = proxies.get(ip);
            System.out.println("Testing " + ip + ":" + port + "...");

            boolean result = testProxy(ip, port);
            if (result) {
                System.out.println("works");
                addWorking(ip, port);
            } else System.out.println("broken");
        }
    }

    private void addWorking(String ip, int port) {
        working.put(ip, port);
    }

    private boolean testProxy(String ip, int port) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

        URLConnection connection = null;
        try {
            connection = new URL("http://google.co.uk").openConnection(proxy);
            connection.setConnectTimeout(250);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                return connection.getContent() != null;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void loadFile(String name) {
        File file = new File(name);

        if (!file.exists()) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write("");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Please enter the proxies into the " + name + " file in the same directory as the jar!");
            System.exit(-1);
            return;
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String input;

        try {
            while ((input = in.readLine()) != null) {
                String[] split = input.split(":");
                proxies.put(split[0], Integer.parseInt(split[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeWorking(String name) {
        File file = new File(name);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (String ip : working.keySet()) {
                int port = working.get(ip);
                out.write(ip + ":" + port + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
