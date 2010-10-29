package org.openttd.network;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * Handling of Protocol details
 * @author Nathanael Rebsch
 */
public class Protocol {
    protected int version = -1;
    protected EnumMap<AdminUpdateType,EnumSet<AdminUpdateFrequency>> supportedFrequencies = new EnumMap<AdminUpdateType,EnumSet<AdminUpdateFrequency>>(AdminUpdateType.class);

    /**
     * Add support of an AdminUpdateType to an AdminUpdateFrequency (if the indexes are known as such)
     * @param tIndex
     * @param fIndex
     */
    protected void addSupport (int tIndex, int fIndex)
    {
        if (AdminUpdateType.isValid(tIndex) && AdminUpdateFrequency.isValid(fIndex)) {
            AdminUpdateType type = AdminUpdateType.get(tIndex);
            AdminUpdateFrequency freq = AdminUpdateFrequency.valueOf(fIndex);

            if (!supportedFrequencies.containsKey(type))
                supportedFrequencies.put(type, EnumSet.noneOf(AdminUpdateFrequency.class));

            supportedFrequencies.get(type).add(freq);
        }
    }

    /**
     * @return The version of the current Protocol as announced by the server.
     */
    public int getVersion()
    {
        return this.version;
    }

    /**
     * Check if the server supports the given AdminUpdateFrequency for the given AdminUpdateType
     * @param type The AdminUpdateType to check.
     * @param freq The AdminUpdateFrequency to check.
     * @return
     */
    public boolean isSupported(AdminUpdateType type, AdminUpdateFrequency freq)
    {
        return supportedFrequencies.get(type).contains(freq);
    }
}
