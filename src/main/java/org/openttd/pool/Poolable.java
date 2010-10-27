/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd.pool;

/**
 * Poolable items.
 * Any implementation of the GenericPool is required
 * to specify a 'Value' class which implements this class.
 *
 * This is also where the ID is defined of the Client and Company class.
 *
 * @author nathanael
 */
public abstract class Poolable<T extends Number> {
    public T id;
}
