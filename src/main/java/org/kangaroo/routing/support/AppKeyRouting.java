//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.routing.support;

import java.util.HashSet;
import java.util.Set;
import org.kangaroo.common.ThreadContextHolder;

public class AppKeyRouting implements TableRouting {
    private boolean popularize = false;
    private Set<String> specifiedKey = new HashSet();

    public AppKeyRouting() {
    }

    public String determineTable(String originalName) {
        originalName = originalName.replace("`", "");
        String appKey = ThreadContextHolder.get();
        String tag = appKey.substring(appKey.length() - 2);
        if (this.popularize && !this.specifiedKey.contains(tag)) {
            return appKey + "_" + originalName;
        } else {
            return !this.popularize && this.specifiedKey.contains(tag) ? appKey + "_" + originalName : originalName;
        }
    }

    public void setPopularize(boolean popularize) {
        this.popularize = popularize;
    }

    public void setSpecifiedKey(Set<String> specifiedKey) {
        this.specifiedKey = specifiedKey;
    }
}
