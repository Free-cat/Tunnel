import java.util.concurrent.TimeUnit;

public class Train implements Runnable {

    private final static int CRITICAL_TIME=2000;//критическое время ожидания основоного тоннеля
    private int number;//номер поезда
    private int side;//сторона тоннеля на которой находится поезд вначале: 1 или 2
    private Tunnel tunnel1;//основной тоннель для поезда
    private Tunnel tunnel2;//резервный тоннель

    public Train(int number,Tunnel leftTunnel, Tunnel rightTunnel) {
        this.number = number;
        this.side=(int)Math.floor(Math.random()*1.999+1);//сторону генерируем случайно
        if(side==1){ //в зависимости от стороны с которой подъехал поезд, задаем основной и резервный тоннели
            this.tunnel1=leftTunnel;
            this.tunnel2=rightTunnel;
        }else{
            this.tunnel1=rightTunnel;
            this.tunnel2=leftTunnel;
        }
    }

    @Override
    public void run() {

        int time=(int)(Math.random()*1000+1);//генерируем время нахождения поезда в тонели

        System.out.println("Поезд №"+number+" подъexaл к тоннелю со стороны "+side);
        try {

            boolean result= tunnel1.tryAcquire(CRITICAL_TIME, TimeUnit.MILLISECONDS);//ждем тоннель не больше CRITICAL_TIME
            if(result) {//если дождались
                System.out.println("Поезд №" + number + " зашел в " + tunnel1.getName() + " тоннель со стороны " + side);
                System.out.println("В " + tunnel1.getName() + " тоннель в очереди " + tunnel1.getQueueLength() + " поездов");
                Thread.sleep(time);//поезд в тоннеле
                System.out.println("Поезд №" + number + " вышел из " + tunnel1.getName() + " тоннель через " + time + "мс");
                tunnel1.release();//освобождаем тоннель
            } else if(tunnel2.getQueueLength()==0){//если не дождались и очередь резервного пуста, переходим в резервный
                System.out.println("Поезд №"+number+" перешел к резервному тоннелю");
                tunnel2.acquire();
                System.out.println("Поезд №"+number+" зашел в "+tunnel2.getName()+" тоннель со стороны "+side);
                Thread.sleep(time);//поезд в тоннеле
                System.out.println("Поезд №"+number+" вышел из "+tunnel2.getName()+" тоннель через "+ time +"мс");
                tunnel2.release();//освобождаем тоннель
            } else{//если очередь резервного не пуста, ждем дальше
                tunnel1.acquire();//занимаем тоннель
                System.out.println("Поезд №" + number + " зашел в " + tunnel1.getName() + " тоннель со стороны " + side);
                System.out.println("В " + tunnel1.getName() + " тоннель в очереди " + tunnel1.getQueueLength() + " поездов");
                Thread.sleep(time);//поезд в тоннеле
                System.out.println("Поезд №" + number + " вышел из " + tunnel1.getName() + " тоннель через " + time + "мс");
                tunnel1.release();//освобождаем тоннель
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
