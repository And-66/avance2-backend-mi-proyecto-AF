/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.remote.protocol;

import java.io.Serializable;

/**
 *
 * @author XPC
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    public Operacion op;
    public Object payload;
}

