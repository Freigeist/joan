/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd.enums;

/**
 *
 * @author Nathanael Rebsch
 */
public enum AdminCompanyRemoveReason implements Reversible<Integer>
{
    ADMIN_CRR_MANUAL     (0),
    ADMIN_CRR_AUTOCLEAN  (1),
    ADMIN_CRR_BANKRUPT   (2);

    private Integer value;
    private static final ReverseLookup<Integer, AdminCompanyRemoveReason> lookup = new ReverseLookup<Integer, AdminCompanyRemoveReason>(AdminCompanyRemoveReason.class);

    AdminCompanyRemoveReason (int i)
    {
        value = i;
    }

    public static boolean isValid (int i)
    {
        return valueOf(i) != null;
    }

    @Override
    public Integer getValue ()
    {
        return value;
    }

    public static AdminCompanyRemoveReason valueOf (int i)
    {
        return lookup.get(i);
    }
}
