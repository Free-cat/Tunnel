import java.util.concurrent.Semaphore;

public class Tunnel extends Semaphore {

    private String name;

    public Tunnel(int permits,String name) {
        super(permits);
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
