import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String [] args){

        Tunnel leftTunnel=new Tunnel(1,"Первый");//создаем левый тонель
        Tunnel rightTunnel=new Tunnel(1,"Второй");//создаем правый тонель

        ExecutorService es = Executors.newFixedThreadPool(10);//создаем Executor на 10 потоков

        for(int i=0;i<10;i++){ //создаем 10 поездов-потоков и запускаем их
            Train train=new Train(i,leftTunnel,rightTunnel);
            es.execute(train);
        }
        es.shutdown();//завершить потоки после их отработки
    }
}
