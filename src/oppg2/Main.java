package oppg2;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Main {
    // made by truls
    public static void main(String[] args) {
        final String[] CHEFS = { "Anne", "Erik", "Knut" };
        final String[] SERVERS = { "Mia", "Per" };

        Tray t = new Tray(4);

        for (String name : CHEFS) {
            new Chef(name, t).start();
        }

        for (String name : SERVERS) {
            new Server(name, t).start();
        }
    }

}

class Burger {

    private int id;

    public Burger(int id) {
        this.id = id;
    }

    public String toString() {
        return String.format("◖%d◗", this.id);
    }

}

class Tray {

    private Queue<Burger> burgers;
    private int capacity;
    private int total;

    public Tray(int capacity) {
        this.burgers = new LinkedList<Burger>();
        this.capacity = capacity;
        this.total = 0;
    }

    public synchronized void add(String name) {
        while (this.burgers.size() == this.capacity) {
            System.out.println(name + " has a burger ready but the tray is full, waiting...");
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        this.burgers.add(new Burger(this.total));
        System.out.println(name + " added a burger | Tray: " + this.toString());
        this.total++;
        notifyAll();
    }

    public synchronized Burger take(String name) {
        while (this.burgers.isEmpty()) {
            System.out.println(name + " wants to take a burger, but the tray is empty, waiting ...");
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        Burger b = this.burgers.remove();
        System.out.println(name + " took a burger | Tray: " + this.toString());
        notifyAll();
        return b;
    }

    public String toString() {
        return this.burgers.toString();
    }

}

class Chef extends Thread {

    private Tray tray;
    private Random randy;

    public Chef(String name, Tray tray) {
        super(name);
        this.tray = tray;
        this.randy = new Random(LocalDateTime.now().getNano());
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                sleep((randy.nextInt(5) + 2) * 1000);
            } catch (InterruptedException e) {
            }
            tray.add(this.getName());
        }
    }

}

class Server extends Thread {

    private Tray tray;
    private Random randy;

    public Server(String name, Tray tray) {
        super(name);
        this.tray = tray;
        this.randy = new Random(LocalDateTime.now().getNano());
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                sleep((randy.nextInt(5) + 2) * 1000);
            } catch (InterruptedException e) {
            }
            tray.take(this.getName());
        }
    }

}
