package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class RenderOverlayEvent extends Event
{
    private final Overlay type;

    public RenderOverlayEvent(Overlay type)
    {
        this.type = type;
    }

    public Overlay type()
    {
        return type;
    }

    public enum Overlay
    {
        FIRE,
        BLOCKS,
        HAND,
        ALL
    }
}