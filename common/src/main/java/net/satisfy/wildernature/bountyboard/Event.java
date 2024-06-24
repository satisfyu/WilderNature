package net.satisfy.wildernature.bountyboard;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private List<Runnable> runnableList = new ArrayList<>();
    public void subscribe(Runnable r){
        runnableList.add((r));
    }
    public void invoke(){
        runnableList.forEach((r)->{
            r.run();
        });
    }
}
