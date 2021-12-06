import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private String name;
    private String address;

    private Client(String name) {
        this.name = name;
    }
    public static void main(String[] args) {

        // Получение заглушки на RMI-объект
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            AdditionInterface stub = (AdditionInterface)
            registry.lookup("Addition");

            // Вызов метода удаленного объекта
            String response = String.valueOf(stub.add(10, 20));
            System.out.println("response: " + response);
            Thread.sleep(5*1000);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}