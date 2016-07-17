package com.srgood.dbot.hooks;

import com.srgood.dbot.hooks.events.*;

public abstract class PlayerListenerAdapter implements PlayerEventListener
{

    public void onPlay(PlayEvent event) {}
    public void onPause(PauseEvent event) {}
    public void onResume(ResumeEvent event) {}
    public void onStop(StopEvent event) {}
    public void onSkip(SkipEvent event) {}
    public void onFinish(FinishEvent event) {}
    public void onRepeat(RepeatEvent event) {}
    public void onReload(ReloadEvent event) {}
    public void onNext(NextEvent event) {}

    @Override
    public void onEvent(PlayerEvent event)
    {
        if (event instanceof PlayEvent)
            onPlay((PlayEvent) event);
        else if (event instanceof PauseEvent)
            onPause((PauseEvent) event);
        else if (event instanceof ResumeEvent)
            onResume((ResumeEvent) event);
        else if (event instanceof StopEvent)
            onStop((StopEvent) event);
        else if (event instanceof SkipEvent)
            onSkip((SkipEvent) event);
        else if (event instanceof FinishEvent)
            onFinish((FinishEvent) event);
        else if (event instanceof RepeatEvent)
            onRepeat((RepeatEvent) event);
        else if (event instanceof ReloadEvent)
            onReload((ReloadEvent) event);
        else if(event instanceof NextEvent)
            onNext((NextEvent) event);
    }
}