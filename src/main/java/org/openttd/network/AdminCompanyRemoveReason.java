/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd.network;

/**
 *
 * @author nathanael
 */
public enum AdminCompanyRemoveReason {
    ADMIN_CRR_MANUAL,
    ADMIN_CRR_AUTOCLEAN,
    ADMIN_CRR_BANKRUPT;

    public static AdminCompanyRemoveReason get (int i)
    {
        try {
            return AdminCompanyRemoveReason.values()[i];
        } catch (IndexOutOfBoundsException e) {
            return ADMIN_CRR_MANUAL;
        }
    }
}
