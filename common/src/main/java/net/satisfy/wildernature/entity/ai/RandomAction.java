package net.satisfy.wildernature.entity.ai;

public interface RandomAction {
    boolean isInterruptable();

    void onStop();

    void onStart();

    boolean isPossible();

    int duration();

    float chance();
}
