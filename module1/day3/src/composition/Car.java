package composition;

public class Car {
    private Engine engine;
    private Ac Ac;
    private MusicSystem musicSystem;
    public Engine getEngine() {
        return engine;
    }

    public void getDetails(){
        engine = engine;
        Ac = Ac;
        musicSystem = musicSystem;
    }
}
