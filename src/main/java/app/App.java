package app;
import app.controllers.*;
import advance.Server;
public class App {
    public static void main(String[] args){
        Server app = new Server(9000);
        app.addController("/", new MainController());
        app.listen();
    }
}