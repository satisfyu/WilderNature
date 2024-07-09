package net.satisfy.wildernature.event;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<Runnable> runnableList = new ArrayList<>();

    public void subscribe(Runnable r) {
        runnableList.add((r));
    }

    public void invoke() {
        runnableList.forEach(Runnable::run);
    }
}
