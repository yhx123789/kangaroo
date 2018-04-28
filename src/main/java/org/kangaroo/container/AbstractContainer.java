package org.kangaroo.container;

import org.kangaroo.common.KangarooConfig;

public abstract class AbstractContainer {
    public AbstractContainer() {
    }

    public abstract void refreshContext(KangarooConfig var1);
}
