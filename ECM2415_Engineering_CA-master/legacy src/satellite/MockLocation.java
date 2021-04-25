package satellite;

public class MockLocation implements Runnable{

    private String latitude, longitude, time, velocity, direction;
    private final Object lock = new Object();

    public MockLocation() {
        this.latitude = "";
        this.longitude = "";
        this.time = "0";
        this.velocity = "";
        this.direction = "";
    }

    public String[] getData() {
        synchronized (lock) {
            return new String[]{this.latitude, this.longitude, this.direction, this.velocity, this.time};
        }
    }

    public void openPort(String portName){

    }

    @Override
    public void run(){
        //Simulates a 6 minute journey walking 0.3km
        //NOTE: Data might not be currently be fully realistic, the purpose is to only simulate real data in a way that will replicate the full location object
        try {
            //Thread.sleep(1000); //Will add in in further sprint to simulate waiting to connect
            double lat = 50.729694; //50.733372
            double lon = -3.549088; //-3.524982
            double time = 120000.00;
            int i = 0;
            while (lat < 50.750817) {
                synchronized (lock) {
                    //Simulates temporary loss of signal
                    if (i % 27 == 0){
                        this.latitude = "";
                        this.longitude = "";
                    }else {
                        this.latitude = String.format("%.4f", lat);
                        this.longitude = String.format("%.4f", lon);
                    }
                    this.time = String.valueOf(time);
                    lat += 0.000051;
                    lon -= 0.000335;
                    time += 5;
                    i++;
                }
                Thread.sleep(5000);
            }
        }catch (Exception e){
            System.out.println();
        }
    }

}
